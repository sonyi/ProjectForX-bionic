package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.imcore.xbionic.login.ui.XLoginActivity;
import com.imcore.xbionic.model.ProductList;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListActivity extends Activity implements OnClickListener{
	private GridView mGridView;
	private ArrayList<ProductList> mProductListGroup;
	private String navId;
	private String subNavId;
	private ImageView mBack;
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		mGridView = (GridView) findViewById(R.id.gri_product_list_layout);
		mBack = (ImageView) findViewById(R.id.iv_product_list_back);
		mBack.setOnClickListener(this);
		
		
		Bundle bundle = this.getIntent().getExtras();
		navId = bundle.getString("navId");
		subNavId = bundle.getString("subNavId");
		mProductListGroup = new ArrayList<ProductList>();
		getProductListInfo();
		

	}

	private void getProductListInfo() {
		String url = Constant.HOST + "/category/products.do?navId=" + navId
				+ "&subNavId=" + subNavId + "&offset=0&fetchSize=10";
		mDialog = ProgressDialog.show(ProductListActivity.this, " ",
				"正在获取数据,请稍后... ", true);
		
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						onResponseForProductList(response);
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
		ArrayList<String> arrJsonStr = (ArrayList<String>) JsonUtil.toJsonStrList(response);
		for(String json : arrJsonStr){
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
			String url = Constant.IMAGE_ADDRESS + mProductListGroup.get(position).imageUrl + "_L.jpg";
			setImag(vh.img, url);
			view.setOnClickListener(new ItemOnClickListener(mProductListGroup.get(position)));
			return view;
		}

		class ViewHolder {
			ImageView img;
			TextView tvName;
			TextView tvPrice;
		}
		
		private void setImag(ImageView image, String url) {
			ImageLoader loader = RequestQueueSingleton.getInstance(
					getApplicationContext()).getImageLoader();
			ImageListener listener = ImageLoader.getImageListener(image,
					R.drawable.ic_product_img, R.drawable.ic_product_img);
			loader.get(url, listener, 400, 400);
		}

	}
	
	private class ItemOnClickListener implements OnClickListener{
		private ProductList p;
		ItemOnClickListener(ProductList p){
			this.p = p;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//ToastUtil.showToast(ProductListActivity.this, p.id + "");
			Intent intent = new Intent(ProductListActivity.this,ProductDetailsActivity.class);
			intent.putExtra("detailId", p.id);
			startActivity(intent);
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.iv_product_list_back){
			finish();
		}
	}

}
