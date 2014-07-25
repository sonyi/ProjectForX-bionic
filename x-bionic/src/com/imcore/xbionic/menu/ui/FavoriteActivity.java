package com.imcore.xbionic.menu.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductColor;
import com.imcore.xbionic.model.ProductFavorite;
import com.imcore.xbionic.product.ui.ProductDetailsActivity;
import com.imcore.xbionic.util.JsonUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class FavoriteActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_home);
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
						//Log.i("sign", error.getMessage());
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
	private void onResponseForProductFavorite(String response){
		mFavoriteArray = (ArrayList<ProductFavorite>) JsonUtil.toObjectList(
				response, ProductFavorite.class);
		Log.i("sign", mFavoriteArray.toString());
	}
}
