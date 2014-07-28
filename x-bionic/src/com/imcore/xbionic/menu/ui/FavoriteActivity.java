package com.imcore.xbionic.menu.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.imagework.ImageWork;
import com.imcore.xbionic.model.ProductFavorite;
import com.imcore.xbionic.model.ProductFavoriteDetail;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

public class FavoriteActivity extends Activity implements OnClickListener{
	private ListView mListView;
	private ImageView mBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_home);
		mListView = (ListView) findViewById(R.id.lv_favorite_layout);
		mListView.setDividerHeight(0);
		mBack = (ImageView) findViewById(R.id.iv_favorite_back);
		mBack.setOnClickListener(this);
		getFavorite();
	}
	
	private String userId;
	private String token;
	private void getFavorite() {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/user/favorite/list.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 if(response != null){
							 //Log.i("sign", response);
							 String responseData = JsonUtil.getJsonValueByKey(response, "data");
							onResponseForProductFavorite(responseData);
							 
							 
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
				params.put("type", "1");
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(
				request);
	}
	
	private ArrayList<ProductFavorite> mFavoriteArray;
	ArrayList<String> arrayTemp;
	private void onResponseForProductFavorite(String response){
		mFavoriteArray = (ArrayList<ProductFavorite>) JsonUtil.toObjectList(
				response, ProductFavorite.class);
		arrayTemp = new ArrayList<String>();
		for(ProductFavorite product : mFavoriteArray){
			if(arrayTemp.indexOf(product.productId + "") == -1){
				arrayTemp.add(product.productId + "");
			}
		}
		mDeailArray = new ArrayList<ProductFavoriteDetail>();
		for(String id : arrayTemp){
			getProductInfo(id);
		}
		
		if(mDeailArray.size() == 0){
			mListView.setAdapter(listViewAdapter);
		}
	}
	
	private void getProductInfo(String id) {
		String url = Constant.HOST + "/product/get.do?id=" + id;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 if(response != null){
							String responseData = JsonUtil.getJsonValueByKey(response, "data");
							onResponseForProductInfo(responseData);
							
						 }
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						//Log.i("sign", error.getMessage());
					}
				});
		RequestQueueSingleton.getInstance(this).addToRequestQueue(
				request);
	}
	
	private ArrayList<ProductFavoriteDetail> mDeailArray;
	private void onResponseForProductInfo(String response){
		try {
			JSONObject json = new JSONObject(response);
			ProductFavoriteDetail productDetail = new ProductFavoriteDetail();
			productDetail.id = json.getLong("id");
			productDetail.name = json.getString("name");
			productDetail.price = json.getString("price");
			productDetail.imageUrl = json.getString("imageUrl");
			mDeailArray.add(productDetail);
			//Log.i("sign", productDetail.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(arrayTemp.size() == mDeailArray.size()){
			mListView.setAdapter(listViewAdapter);
		}
	}
	
	
	private BaseAdapter listViewAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = arg1;
			ViewHolder vh = null;
			if(view == null){
				view = getLayoutInflater().inflate(R.layout.view_favorite_activity, null);
				vh = new ViewHolder();
				vh.img = (ImageView) view.findViewById(R.id.iv_favorite_img);
				vh.title = (TextView) view.findViewById(R.id.tv_favorite_title);
				vh.price = (TextView) view.findViewById(R.id.tv_favorite_price);
				vh.date = (TextView) view.findViewById(R.id.tv_favorite_date);
				vh.remove = (TextView) view.findViewById(R.id.tv_favorite_remove);
				view.setTag(vh);
			}else{
				vh = (ViewHolder) view.getTag();
			}
			ProductFavoriteDetail proDetail = mDeailArray.get(arg0);
			ProductFavorite proFavorite = null;
			for(ProductFavorite pro : mFavoriteArray){
				if(pro.productId == proDetail.id){
					proFavorite = pro;
					break;
				}
			}
			vh.title.setText(proDetail.name);
			vh.price.setText("单价：￥" + proDetail.price);
			if(proFavorite != null){
				String[] dateAndTime = proFavorite.addDate.split("T");
				String[] date = dateAndTime[0].split("/");
				vh.date.setText(date[2] + "-" + date[0] + "-" + date[1] + "  " + dateAndTime[1]);
			}
			String url = Constant.IMAGE_ADDRESS + proDetail.imageUrl + "_L.jpg";
			vh.img.setTag(url);
			Bitmap bitmap = ImageWork.getImageWork().setImgBitmap(vh.img, url);
			if (bitmap == null) {
				vh.img.setImageResource(R.drawable.ic_product_img);
			}else{
				vh.img.setImageBitmap(bitmap);
			}
			vh.remove.setOnClickListener(new removeOnClickListener(proFavorite));
			return view;
		}
		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mDeailArray.get(arg0);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDeailArray.size();
		}
		
		class ViewHolder{
			ImageView img;
			TextView title;
			TextView price;
			TextView date;
			TextView remove;
		}
	};
	
	class removeOnClickListener implements OnClickListener {
		private ProductFavorite proFavorite;
		removeOnClickListener(ProductFavorite proFavorite){
			this.proFavorite = proFavorite;
		}
		
		@Override
		public void onClick(View v) {
			for(ProductFavorite favPro : mFavoriteArray){
				if(favPro.productId == proFavorite.productId){
					removeFavoriteProduct(favPro.id);
				}
			}
		}
	};
	
	private void removeFavoriteProduct(long id) {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/user/favorite/delete.do";
		final String removeId = id + "";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//Log.i("sign", response);
						ToastUtil.showToast(FavoriteActivity.this, "删除成功");
						getFavorite();//重新刷新界面
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						//Log.i("sign", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", userId);
				params.put("token", token);
				params.put("id", removeId);
				params.put("type", "1");
				//Log.i("sign", removeId + "");
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(
				request);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.iv_favorite_back){
			finish();
		}
	}
	
}
