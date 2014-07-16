package com.imcore.xbionic.login.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityLogin;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;

public class XLoginActivity extends Activity implements OnClickListener {
	private ImageView mBackImg, mForgetpsw, mSignIn;
	private EditText mUser, mPsw;
	private ProgressDialog mDialog;
	private String loginValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_x);

		mBackImg = (ImageView) findViewById(R.id.iv_login_x_back);
		mForgetpsw = (ImageView) findViewById(R.id.iv_login_x_forgetpsw);
		mSignIn = (ImageView) findViewById(R.id.iv_login_x_signin);
		mUser = (EditText) findViewById(R.id.et_login_x_user);
		mPsw = (EditText) findViewById(R.id.et_login_x_password);
		mBackImg.setOnClickListener(this);
		mForgetpsw.setOnClickListener(this);
		mSignIn.setOnClickListener(this);
		
		loginValue = getIntent().getStringExtra(Const.LOGIN_KEY);
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_login_x_back:
			finish();
			break;
		case R.id.iv_login_x_forgetpsw:

			break;
		case R.id.iv_login_x_signin:
			doLogin();
			break;
		}

	}

	private void doLogin() {
		final String userName = mUser.getText().toString().trim();
		final String password = mPsw.getText().toString().trim();

		mDialog = ProgressDialog.show(XLoginActivity.this, " ",
				"正在登陆中,请稍后... ", true);
		String url = Constant.HOST + "/passport/login.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						// Log.i("sign", response);
						onResponseForLogin(response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.i("sign", error.getMessage());
						mDialog.cancel();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", userName);
				params.put("password", password);
				Log.i("msg", userName + "----" + password);
				// params.put("device", "device");
				params.put("client", "android");

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
		
		mDialog.cancel();

		Intent intent = null;
		if(loginValue.equals(Const.LOGIN_AT_BUY_VALUE)){
			finish();
		}else if(loginValue.equals(Const.LOGIN_AT_OTHER_VALUE)){
			intent = new Intent(this, HomeActivityLogin.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 清除栈内其他activity
			startActivity(intent);
		}
	}
}
