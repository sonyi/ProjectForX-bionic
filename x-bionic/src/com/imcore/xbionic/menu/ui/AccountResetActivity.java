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
import com.imcore.xbionic.home.ui.HomeActivityUnLogin;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.util.JsonUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountResetActivity extends Activity implements OnClickListener {
	private ImageView mBack;
	private TextView mSignOut;
	private TextView mLastName, mFirstName, mSex, mEmail, mProvince, mAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_reset);
		mBack = (ImageView) findViewById(R.id.iv_account_reset_back);
		mSignOut = (TextView) findViewById(R.id.tv_account_reset_signout);
		mLastName = (TextView) findViewById(R.id.tv_acc_reset_info_lastname);
		mFirstName = (TextView) findViewById(R.id.tv_acc_reset_info_firstname);
		mSex = (TextView) findViewById(R.id.tv_acc_reset_info_sex);
		mEmail = (TextView) findViewById(R.id.tv_acc_reset_info_email);
		mProvince = (TextView) findViewById(R.id.tv_acc_reset_address_province);
		mAddress = (TextView) findViewById(R.id.tv_acc_reset_address);

		
		
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE); // 私有数据
		if(sp.getBoolean("isChange", true)){
			doLogin();// 网络获取数据
		}
		
		initWidget(sp);

		mBack.setOnClickListener(this);
		mSignOut.setOnClickListener(this);

	}

	private void initWidget(SharedPreferences sp) {
		mLastName.setText(sp.getString("lastName", ""));
		mFirstName.setText(sp.getString("firstName", ""));
		mEmail.setText(sp.getString("email", ""));
		mProvince.setText(sp.getString("address", ""));
		mAddress.setText(sp.getString("address", ""));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_account_reset_back:// 回退
			finish();
			break;

		case R.id.tv_account_reset_signout:// 注销账户
			SharedPreferences sp = getSharedPreferences("loginUser",
					Context.MODE_PRIVATE); // 私有数据
			Editor editor = sp.edit();// 获取编辑器
			editor.putBoolean("isLogin", false);
			editor.putString("userId", "");
			editor.putString("token", "");
			editor.commit();// 提交修改
			Intent intent = new Intent(this, HomeActivityUnLogin.class);
			startActivity(intent);
			finish();
			break;

		case R.id.rel_account_reset_baseinfromation:// 基本资料

			break;

		case R.id.rel_account_reset_address:// 地址信息

			break;

		}
	}

	private void doLogin() {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		String userId = sp.getString("userId", "");
		String token = sp.getString("token", "");
		if (userId.equals("") || token.equals("")) {
			return;
		}

		String url = Constant.HOST + "/user/get.do";
		final String id = userId;
		final String tok = token;
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						Log.i("sign", response);
						onResponseForLogin(response);
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
				params.put("userId", id);
				params.put("token", tok);
				Log.i("msg", id);
				return params;
			}
		};

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForLogin(String response) {
		String userAddress = JsonUtil
				.getJsonValueByKey(response, "userAddress");
		String address = null;
		JSONObject jo;
		try {
			jo = new JSONObject(userAddress);
			address = jo.getString("address");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String firstName = JsonUtil.getJsonValueByKey(response, "firstName");
		String lastName = JsonUtil.getJsonValueByKey(response, "lastName");
		String email = JsonUtil.getJsonValueByKey(response, "email");

		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE); // 私有数据
		Editor editor = sp.edit();// 获取编辑器
		editor.putBoolean("isChange", false);
		editor.putString("address", address);
		editor.putString("firstName", firstName);
		editor.putString("lastName", lastName);
		editor.putString("email", email);
		editor.commit();// 提交修改
		
		initWidget(sp);
	}
}
