package com.imcore.xbionic.menu.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductShopping;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

public class ShoppingTrolleyActivity extends Activity implements
		OnClickListener {
	private ListView mList;
	private ImageView mBack, mEdit;
	private TextView mSettle, mTotal;
	private boolean isVisible = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_trolley);
		initWidget();
		getShoppingInfo();
	}

	private void initWidget() {
		mList = (ListView) findViewById(R.id.lv_product_shopping_list);
		mList.setDividerHeight(0);
		mBack = (ImageView) findViewById(R.id.iv_product_shopping_back);
		mSettle = (TextView) findViewById(R.id.tv_pro_shopping_settle);
		mTotal = (TextView) findViewById(R.id.tv_pro_shopping_totals);
		mEdit = (ImageView) findViewById(R.id.iv_product_shopping_edit);

		mEdit.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mSettle.setOnClickListener(this);
	}

	private String userId;
	private String token;

	private void getShoppingInfo() {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/shoppingcart/list.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						
						String responseData = JsonUtil.getJsonValueByKey(
								response, "data");
						onResponseForLogin(responseData);
						// Log.i("sign", responseData);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						error.printStackTrace();
						// Log.i("sign", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", token);
				params.put("userId", userId);
				// Log.i("sign", userId + "----");
				return params;
			}
		};

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private ArrayList<ProductShopping> mShoppingArray;
	private float totalMoney;

	private void onResponseForLogin(String response) {
		mShoppingArray = new ArrayList<ProductShopping>();
		ArrayList<String> arr = (ArrayList<String>) JsonUtil
				.toJsonStrList(response);
		if(totalMoney != 0){
			totalMoney = 0;
		}
		for (String json : arr) {
			ProductShopping ps = new ProductShopping();
			String product = JsonUtil.getJsonValueByKey(json, "product");
			ps.imageUrl = JsonUtil.getJsonValueByKey(product, "imageUrl");
			String sysSize = JsonUtil.getJsonValueByKey(product, "sysSize");
			ps.size = JsonUtil.getJsonValueByKey(sysSize, "size");
			String sysColor = JsonUtil.getJsonValueByKey(product, "sysColor");
			ps.color = JsonUtil.getJsonValueByKey(sysColor, "color");
			ps.price = JsonUtil.getJsonValueByKey(product, "price");
			ps.name = JsonUtil.getJsonValueByKey(product, "name");
			ps.id = JsonUtil.getJsonValueByKey(json, "id");
			ps.productQuantityId = JsonUtil.getJsonValueByKey(json,
					"productQuantityId");
			ps.qty = JsonUtil.getJsonValueByKey(json, "qty");
			mShoppingArray.add(ps);
			totalMoney += Float.parseFloat(ps.price) * Float.parseFloat(ps.qty);
		}
		// Log.i("sign", mShoppingArray.toString());
		mTotal.setText(totalMoney + "");
		mVhArray = new ArrayList<ViewHolder>();
		mList.setAdapter(shoppingAdapter);
	}

	private ArrayList<ViewHolder> mVhArray;
	private BaseAdapter shoppingAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.view_product_shopping, null);
				vh = new ViewHolder();
				
				vh.title = (TextView) view
						.findViewById(R.id.tv_pro_shopping_title);
				vh.color = (TextView) view
						.findViewById(R.id.tv_pro_shopping_color);
				vh.size = (TextView) view
						.findViewById(R.id.tv_pro_shopping_size);
				vh.qty = (TextView) view.findViewById(R.id.tv_pro_shopping_qty);
				vh.add = (ImageView) view
						.findViewById(R.id.ib_pro_shopping_right_btn);
				vh.del = (ImageView) view
						.findViewById(R.id.ib_pro_shopping_left_btn);
				vh.remove = (ImageView) view
						.findViewById(R.id.iv_pro_shopping_remove);
				vh.img = (ImageView) view
						.findViewById(R.id.iv_pro_shopping_img);
				vh.price = (TextView) view
						.findViewById(R.id.tv_pro_shopping_total);
				view.setTag(vh);

			} else {
				vh = (ViewHolder) view.getTag();
			}
			vh.title.setText(mShoppingArray.get(position).name);
			vh.color.setText(mShoppingArray.get(position).color);
			vh.size.setText(mShoppingArray.get(position).size);
			vh.price.setText(mShoppingArray.get(position).price);
			vh.qty.setText(mShoppingArray.get(position).qty);
			vh.id = mShoppingArray.get(position).id;
			vh.productQuantityId = mShoppingArray.get(position).productQuantityId;
			String url = Constant.IMAGE_ADDRESS
					+ mShoppingArray.get(position).imageUrl + "_L.jpg";
			setImag(vh.img, url);

			vh.add.setOnClickListener(new vhImgOnClickListener(vh));
			vh.del.setOnClickListener(new vhImgOnClickListener(vh));
			vh.remove.setOnClickListener(new vhImgOnClickListener(vh));
			if (mVhArray.indexOf(vh) == -1) {
				mVhArray.add(vh);
			}
			return view;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return mShoppingArray.get(position);
		}

		@Override
		public int getCount() {
			return mShoppingArray.size();
		}
	};

	class ViewHolder {
		String id;
		String productQuantityId;
		ImageView img;
		TextView title;
		TextView color;
		TextView size;
		TextView price;
		TextView qty;
		ImageView add;
		ImageView del;
		ImageView remove;
	}

	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_product_img, R.drawable.ic_product_img);
		loader.get(url, listener, 400, 400);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.iv_product_shopping_back) {
			finish();
			return;
		} else if (v.getId() == R.id.tv_pro_shopping_settle) {
			ToastUtil.showToast(this, "提交结算，支付接口木有了");
		} else if (v.getId() == R.id.iv_product_shopping_edit) {
			// ToastUtil.showToast(this, "edit");
			setViewVisible();
		}

	}

	private void setViewVisible() {
		if (mVhArray != null && mVhArray.size() != 0) {
			if (isVisible) {
				for (ViewHolder vh : mVhArray) {
					vh.add.setVisibility(View.GONE);
					vh.del.setVisibility(View.GONE);
					vh.remove.setVisibility(View.GONE);
				}
				isVisible = false;
			} else {
				for (ViewHolder vh : mVhArray) {
					vh.add.setVisibility(View.VISIBLE);
					vh.del.setVisibility(View.VISIBLE);
					vh.remove.setVisibility(View.VISIBLE);
				}
				isVisible = true;
			}
		}
	}

	private class vhImgOnClickListener implements OnClickListener {
		private ViewHolder vh;

		vhImgOnClickListener(ViewHolder vh) {
			this.vh = vh;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == vh.add.getId()) {
				addProduct(vh);
				return;
			} else if (v.getId() == vh.del.getId()) {
				delProduct(vh);
				return;
			} else if (v.getId() == vh.remove.getId()) {
				removeProduct(vh);
				return;
			}
		}

	}

	
	private void addProduct(ViewHolder vh) {
		int sum = Integer.parseInt(vh.qty.getText().toString());
		update(vh, sum + 1,sum);

	}

	private void delProduct(ViewHolder vh) {
		int sum = Integer.parseInt(vh.qty.getText().toString());
		if (sum == 0) {
			return;
		}
		update(vh, sum - 1,sum);
	}

	private void removeProduct(ViewHolder vh) {
		removeDialog(this, vh).show();

	}

	/**
	 * 提示是否删除商品
	 * 
	 * @param context
	 * @return
	 */
	private Dialog removeDialog(Context context, ViewHolder vh) {
		final ViewHolder v = vh;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("确定要删除该商品吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				remove(v);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}

	// 删除商品
	private void remove(ViewHolder vh) {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/shoppingcart/delete.do";
		final ViewHolder v = vh;
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						ToastUtil.showToast(ShoppingTrolleyActivity.this,
								"删除成功");
						getShoppingInfo();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", token);
				params.put("userId", userId);
				params.put("cartId", v.id);
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	// 更新商品数量
	private void update(ViewHolder vh, int newNum,int oldNum) {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/shoppingcart/update.do";
		final ViewHolder v = vh;
		final int nn = newNum;
		final int on = oldNum;
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						v.qty.setText(nn + "");
						float total = Float.parseFloat(mTotal.getText()
								.toString());
						if(nn > on){
							mTotal.setText((total + Float.parseFloat(v.price
								.getText().toString())) + "");
						}else if(nn < on){
							mTotal.setText((total - Float.parseFloat(v.price
									.getText().toString())) + "");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", token);
				params.put("userId", userId);
				params.put("id", v.id);
				params.put("productQuantityId", v.productQuantityId);
				params.put("qty", nn + "");
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}
}
