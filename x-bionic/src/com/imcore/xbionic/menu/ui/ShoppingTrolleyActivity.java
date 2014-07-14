package com.imcore.xbionic.menu.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityLogin;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.login.ui.XLoginActivity;
import com.imcore.xbionic.util.JsonUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

public class ShoppingTrolleyActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_trolley);
		doLogin();
	}
	
	private void doLogin() {
		final String userId = "2341";
		

		
		String url = Constant.HOST + "/shoppingcart/list.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						 Log.i("sign", response);
						//onResponseForLogin(response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.i("sign", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("token", "e138dcdac23a7828fdab7b92eca990f6d223bf539b75b0f761d4026821e2b9ac83c5e0858a2b4ad6169644e2255bbf9d3c832b7f005fa4b2acc9144af388694d");
				params.put("userId", userId);
				Log.i("msg", userId + "----");
				
				return params;
			}
		};

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForLogin(String response) {
		String userAddress = JsonUtil
				.getJsonValueByKey(response, "userAddress");
		String userId = null;
		JSONObject jo;
		try {
			jo = new JSONObject(userAddress);
			userId = jo.getString("userId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String token = JsonUtil.getJsonValueByKey(response, "token");
		String firstName = JsonUtil.getJsonValueByKey(response, "firstName");
		String lastName = JsonUtil.getJsonValueByKey(response, "lastName");
		String userName = lastName + firstName;

		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		Editor editor = sp.edit();// 获取编辑器
		editor.putString("userId", userId);
		editor.putString("token", token);
		editor.putString("userName", userName);
		editor.putBoolean("isLogin", true);
		editor.commit();// 提交修改
		
	

		

	}

}
