package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.routing.RouteInfo.LayerType;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityLogin;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.login.ui.LoginMainActivity;
import com.imcore.xbionic.login.ui.XLoginActivity;
import com.imcore.xbionic.model.ProductColor;
import com.imcore.xbionic.model.ProductQuantity;
import com.imcore.xbionic.model.ProductSize;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

public class FragmentProductDetailInfo extends Fragment implements
		OnClickListener {
	private View view;
	private TextView mTitleView;
	private TextView mPriceView;
	private int mImgID;
	private ImageView mBuyImg;
	private TextView mAmount;
	private EditText mBuyEt;
	private ArrayList<TextView> mSizeArray;
	private ProductColor mSelectProductColor;
	private ProductSize mSelectProductSize;
	private long productDetailId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_product_detail_info, null);
		productDetailId = getArguments().getLong(
				Const.PRODUCT_DETAIL_FRAGMENT_KEY);

		initWidget();

		getProductDetailInfo();// 获取产品详情
		getProductDetailSize();// 获取产品尺寸
		return view;
	}

	// 初始化控件
	private void initWidget() {
		mTitleView = (TextView) view.findViewById(R.id.tv_pro_det_info_title);
		mPriceView = (TextView) view.findViewById(R.id.tv_pro_det_price);
		mBuyImg = (ImageView) view.findViewById(R.id.iv_pro_det_buy);
		mAmount = (TextView) view.findViewById(R.id.tv_pro_det_amount);
		mBuyEt = (EditText) view.findViewById(R.id.et_pro_det_amount);
		mBuyImg.setOnClickListener(this);

	}

	// 获取产品详情
	private void getProductDetailInfo() {
		mColorArray = new ArrayList<ProductColor>();
		mColorImgArray = new ArrayList<ImageView>();
		String url = Constant.HOST + "/product/get.do?id=" + productDetailId;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if(response != null){
							//Log.i("sign", response);
							String responseData = JsonUtil.getJsonValueByKey(response, "data");
							onResponseForProductList(responseData);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}

	private String mTitle;
	private String mPrice;
	private ArrayList<ProductColor> mColorArray;
	private ArrayList<ImageView> mColorImgArray;

	private void onResponseForProductList(String response) {
		if(getActivity() == null){
			return;
		}
		try {
			JSONObject j = new JSONObject(response);
			mTitle = j.getString("name");
			mPrice = j.getString("price");
			mColorArray = (ArrayList<ProductColor>) JsonUtil.toObjectList(
					j.getString("sysColorList"), ProductColor.class);
			//Log.i("sign", mColorArray.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mTitleView.setText(mTitle);
		mPriceView.setText("￥" + mPrice);
		addColor();// 添加颜色图片控件
	}

	// 动态生成颜色控件
	private void addColor() {
		if(getActivity() == null){
			return;
		}
		LinearLayout insertLayout = (LinearLayout) view
				.findViewById(R.id.rel_pro_det_color);
		mImgID = 0x11;
		for (ProductColor p : mColorArray) {
			ImageView img = new ImageView(getActivity());
			String url = Constant.IMAGE_ADDRESS + p.colorImage + ".jpg";
			setImag(img, url);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					50, 50);
			layoutParams.leftMargin = 20;
			img.setScaleType(ScaleType.FIT_XY);
			img.setId(mImgID++);
			insertLayout.addView(img, layoutParams);
			img.setOnClickListener(colorOnClickListener);
			mColorImgArray.add(img);
			if (mColorArray.indexOf(p) == 0) {
				img.setBackgroundResource(R.drawable.product_detail_info_color_select_background);
				mSelectProductColor = p;
			}
		}
	}

	// 控件添加图片
	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getActivity().getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_product_img, R.drawable.ic_product_img);
		loader.get(url, listener, 400, 400);
	}

	// 获取产品尺寸
	private ArrayList<ProductSize> mSize;

	private void getProductDetailSize() {
		mSize = new ArrayList<ProductSize>();

		String url = Constant.HOST + "/product/size/list.do?id="
				+ productDetailId;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 if(response != null){
							 String responseData = JsonUtil.getJsonValueByKey(response, "data");
							 String jsonSize = JsonUtil.getJsonValueByKey(responseData,
										"sysSizeList");
								mSize = (ArrayList<ProductSize>) JsonUtil.toObjectList(
										jsonSize, ProductSize.class);
								addSize();
						 }
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());

					}
				});

		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(request);
		
	}
	

	// 动态生成尺寸控件
	private int mSizeId;
	private void addSize() {
		if(getActivity() == null){
			return;
		}
		mSizeArray = new ArrayList<TextView>();
		LinearLayout insertLayout = (LinearLayout) view
				.findViewById(R.id.rel_pro_det_size);
		mSizeId = 0x01;
		for (ProductSize p : mSize) {
			TextView tv = new TextView(getActivity());
			int index = p.size.indexOf("（");
			String s ;
			if(index != -1){
				s = p.size.substring(0, index);
			}else{
				s = p.size;
			}
			tv.setText(s);
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					 LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 10;
			tv.setId(mSizeId++);
			insertLayout.addView(tv, layoutParams);
			tv.setOnClickListener(sizeOnClickListener);
			tv.setBackgroundResource(R.drawable.product_detail_info_size_background);
			mSizeArray.add(tv);
		}
	}
	


	// 监听颜色控件
	private OnClickListener colorOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (ImageView img : mColorImgArray) {
				if (v.getId() == img.getId()) {
					img.setBackgroundResource(R.drawable.product_detail_info_color_select_background);
					mSelectProductColor = mColorArray.get(mColorImgArray
							.indexOf(img));
					//Log.i("sign", mSelectProductColor.toString());
					if (mSelectProductColor != null
							&& mSelectProductSize != null) {
						getProductNum(mSelectProductColor.id,
								mSelectProductSize.id);
					}

				} else {
					img.setBackgroundResource(R.drawable.product_detail_info_color_background);
				}
			}
		}
	};

	// 监听尺寸控件
	private OnClickListener sizeOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (TextView tv : mSizeArray) {
				if (v.getId() == tv.getId()) {
					tv.setBackgroundResource(R.drawable.product_detail_info_size_select_background);
					mSelectProductSize = mSize.get(mSizeArray.indexOf(tv));
					if (mSelectProductColor != null
							&& mSelectProductSize != null) {
						getProductNum(mSelectProductColor.id,
								mSelectProductSize.id);
					}
				} else {
					tv.setBackgroundResource(R.drawable.product_detail_info_size_background);
				}
			}
		}
	};

	private String sumBuy;

	@Override
	// 监听购买控件
	public void onClick(View v) {
		if (v.getId() == R.id.iv_pro_det_buy) {
			sumBuy = mBuyEt.getText().toString().trim();

			if (mSelectProductSize == null) {
				new AlertDialog.Builder(getActivity()).setMessage("尺寸还没有选....")
						.create().show();
				return;
			} else if (productQuantity == null || productQuantity.qty == 0) {
				new AlertDialog.Builder(getActivity()).setTitle("太火了，已经卖光了...")
						.create().show();
				return;
			} else if (sumBuy == null || sumBuy.equals("")) {
				new AlertDialog.Builder(getActivity()).setTitle("购买数量还没有选....")
						.create().show();
				return;
			} else if (Integer.parseInt(sumBuy) > productQuantity.qty) {
				new AlertDialog.Builder(getActivity())
						.setTitle("您要的太多，小的没法给了...").create().show();
				return;
			} else {
				submitOrders();
			}

		}
	}

	/**
	 * 下单成功对话框
	 * 
	 * @param context
	 * @return
	 */
	private Dialog showDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("下单成功");
		builder.setPositiveButton("马上结算",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		builder.setNegativeButton("再逛逛", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}

	private String userId;
	private String token;

	private void submitOrders() {
		SharedPreferences sp = getActivity().getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		if (userId.equals("") || token.equals("")) {
			new AlertDialog.Builder(getActivity())
					.setTitle("您还未登陆，请先登陆.....")
					.setPositiveButton("登陆",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent intent = new Intent(getActivity(),
											XLoginActivity.class);
									intent.putExtra(Const.LOGIN_KEY,
											Const.LOGIN_AT_BUY_VALUE);
									startActivity(intent);
								}
							}).create().show();
			return;
		}

		String url = Constant.HOST + "/shoppingcart/update.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 if(response != null){
							 showDialog(getActivity()).show();
						 }
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", userId);
				params.put("token", token);
				params.put("productQuantityId", productQuantity.id + "");
				params.put("qty", sumBuy);
				// Log.i("sign", productQuantity.id + "----" + sumBuy);
				return params;
			}
		};
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}

	// 获取产品库存
	private ProductQuantity productQuantity;

	private void getProductNum(long colorId, long sizeId) {
		//Log.i("sign", colorId + "---" + sizeId);
		String url = Constant.HOST + "/product/quantity/get.do?id="
				+ productDetailId + "&colorId=" + colorId + "&sizeId=" + sizeId;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 if(response != null){
							 String responseData = JsonUtil.getJsonValueByKey(response, "data");
							 productQuantity = JsonUtil.toObject(responseData,
										ProductQuantity.class);
						 }
						if(productQuantity != null){
							mAmount.setText("(库存" + productQuantity.qty + "件)");
							//Log.i("sign", productQuantity.qty + "");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}
}
