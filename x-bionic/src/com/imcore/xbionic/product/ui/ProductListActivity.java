package com.imcore.xbionic.product.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.imagework.ImageWork;
import com.imcore.xbionic.model.ProductList;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;

public class ProductListActivity extends Activity implements OnClickListener {
	private GridView mGridView;
	private ArrayList<ProductList> mProductListGroup;
	private TextView mAsceOrder, mDescOrder;
	private String navId;
	private String subNavId;
	private ImageView mBack;
	private ProgressDialog mDialog;
	private RadioGroup mRadioGroup;
	private int mFilterId = 2;
	private static int ASCE_ORDER = 0;
	private static int DESC_ORDER = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		mGridView = (GridView) findViewById(R.id.gri_product_list_layout);
		mBack = (ImageView) findViewById(R.id.iv_product_list_back);
//		mAsceOrder = (TextView) findViewById(R.id.tv_product_list_asce);
//		mDescOrder = (TextView) findViewById(R.id.tv_product_list_desc);
//		mRadioGroup = (RadioGroup) findViewById(R.id.rg_product_list_radio);
//		mRadioGroup.setOnCheckedChangeListener(mRadioGroupListener);
		mBack.setOnClickListener(this);
//		mAsceOrder.setOnClickListener(this);
//		mDescOrder.setOnClickListener(this);

		Bundle bundle = this.getIntent().getExtras();
		navId = bundle.getString("navId");
		subNavId = bundle.getString("subNavId");
		mProductListGroup = new ArrayList<ProductList>();
		getUrl();

	}

	private void getUrl(int order, int filterId) {
		String url = Constant.HOST + "/category/products.do?navId=" + navId
				+ "&subNavId=" + subNavId + "&offset=0&fetchSize=15&desc="
				+ order + "&filterId=" + filterId;
		getProductListInfo(url);
	}

	private void getUrl() {
		String url = Constant.HOST + "/category/products.do?navId=" + navId
				+ "&subNavId=" + subNavId + "&offset=0&fetchSize=15";
		getProductListInfo(url);
	}

	private void getProductListInfo(String url) {
		mDialog = ProgressDialog.show(ProductListActivity.this, " ",
				"正在获取数据,请稍后... ", true);

		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						String data = JsonUtil.getJsonValueByKey(response,
								"data");
						onResponseForProductList(data);

						mDialog.cancel();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
						mDialog.cancel();
					}
				});
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForProductList(String response) {
		ArrayList<String> arrJsonStr = (ArrayList<String>) JsonUtil
				.toJsonStrList(response);
		if (mProductListGroup != null && mProductListGroup.size() != 0) {
			mProductListGroup.clear();
		}
		for (String json : arrJsonStr) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				ProductList proList = new ProductList();
				proList.id = jsonObject.getLong("id");
				proList.name = jsonObject.getString("name");
				proList.price = jsonObject.getString("price");
				proList.imageUrl = jsonObject.getString("imageUrl");
				mProductListGroup.add(proList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		mGridView.setAdapter(new ProductListAdapter());
	}

	private class ProductListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mProductListGroup.size();
		}

		@Override
		public Object getItem(int position) {
			return mProductListGroup.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.view_product_list,
						null);
				vh = new ViewHolder();
				vh.img = (ImageView) view
						.findViewById(R.id.iv_product_list_img);
				vh.tvName = (TextView) view
						.findViewById(R.id.tv_product_list_dec);
				vh.tvPrice = (TextView) view
						.findViewById(R.id.tv_product_list_price);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}

			vh.tvName.setText(mProductListGroup.get(position).name);
			vh.tvPrice.setText("￥" + mProductListGroup.get(position).price);
			String url = Constant.IMAGE_ADDRESS
					+ mProductListGroup.get(position).imageUrl + "_L.jpg";
			// setImag(vh.img, url);
			vh.img.setTag(url);
			Bitmap bitmap = ImageWork.getImageWork().setImgBitmap(vh.img, url);
			if (bitmap == null) {
				vh.img.setImageResource(R.drawable.ic_product_img);
			} else {
				vh.img.setImageBitmap(bitmap);
			}

			view.setOnClickListener(new ItemOnClickListener(mProductListGroup
					.get(position)));
			return view;
		}

		class ViewHolder {
			ImageView img;
			TextView tvName;
			TextView tvPrice;
		}
	}

	private class ItemOnClickListener implements OnClickListener {
		private ProductList p;

		ItemOnClickListener(ProductList p) {
			this.p = p;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ProductListActivity.this,
					ProductDetailsActivity.class);
			intent.putExtra(Const.PRODUCT_DETAIL_FRAGMENT_KEY, p.id);
			startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_product_list_back) {
			finish();
			return;
		} 
//		else if (v.getId() == R.id.tv_product_list_asce) {
//			getUrl(ASCE_ORDER,mFilterId);
//			return;
//		} else if (v.getId() == R.id.tv_product_list_desc) {
//			getUrl(DESC_ORDER,mFilterId);
//			return;
//		}
	}

	private OnCheckedChangeListener mRadioGroupListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
//			if (checkedId == R.id.rb_product_list_btn1) {
//				mFilterId = 1;
//				Log.i("sign", mFilterId + "");
//				return;
//			} else if (checkedId == R.id.rb_product_list_btn2) {
//				mFilterId = 2;
//				Log.i("sign", mFilterId + "");
//				return;
//			} else if (checkedId == R.id.rb_product_list_btn3) {
//				mFilterId = 3;
//				Log.i("sign", mFilterId + "");
//				return;
//			}
		}
	};

}
