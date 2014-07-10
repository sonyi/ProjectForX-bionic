package com.imcore.xbionic.login.ui;

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
import com.imcore.xbionic.login.ui.LonginByWeibo.AuthListener;
import com.imcore.xbionic.util.ToastUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class LoginMainActivity extends Activity implements OnClickListener {
	private ImageView mQQLoginImg, mSinaLoginImg, mXLoginImg, mRegisterImg,
			mHelpImg, mServeImg;
	
	//微博授权登录
	private WeiboAuth mWeiboAuth;
    private Oauth2AccessToken mAccessToken;
    //private SsoHandler mSsoHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
		mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		
		initWidget();

	}

	private void initWidget() {
		mQQLoginImg = (ImageView) findViewById(R.id.iv_login_main_qqlogin);
		mSinaLoginImg = (ImageView) findViewById(R.id.iv_login_main_sina);
		mXLoginImg = (ImageView) findViewById(R.id.iv_login_main_x);
		mRegisterImg = (ImageView) findViewById(R.id.iv_login_main_register);
		mHelpImg = (ImageView) findViewById(R.id.iv_login_help);
		mServeImg = (ImageView) findViewById(R.id.iv_login_serve);

		mQQLoginImg.setOnClickListener(this);
		mSinaLoginImg.setOnClickListener(this);
		mXLoginImg.setOnClickListener(this);
		mRegisterImg.setOnClickListener(this);
		mHelpImg.setOnClickListener(this);
		mServeImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_login_main_qqlogin://腾讯微博账户登录

			break;
		case R.id.iv_login_main_sina://微博授权登录
			mWeiboAuth.anthorize(new AuthListener());
			break;
		case R.id.iv_login_main_x://部落账户登录
			Intent intent = new Intent(this,XLoginActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_login_main_register://新用户注册

			break;
		case R.id.iv_login_help:

			break;
		case R.id.iv_login_serve:

			break;

		}
	}
	
	//新浪微博授权登入正确后返回的回调方法
	class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
            	Log.i("msg", mAccessToken.toString());
            	String uid = mAccessToken.getToken();
            	
            	doLogin(mAccessToken.toString(),"sina");
               
            } else {
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                String code = values.getString("code");
                Log.i("msg", code);
            }
        }

        @Override
        public void onCancel() {
           
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showToast(LoginMainActivity.this, "微博授权登录异常：" + e.getMessage());
        }
    }
	
	
	private void doLogin(String uid,String type) {
//		final String userName = "";
//		final String password = "";

//		mDialog = ProgressDialog.show(LoginMainActivity.this, " ",
//				"正在登陆中,请稍后... ", true);
		String url = Constant.HOST + "/passport/login.do";
		
		
		final String accountIdEncripted = uid;
		final String accountType = type;
		
		
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						 Log.i("msg", response);
						//onResponseForLogin(response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.i("msg", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("accountType", accountType);
				params.put("accountIdEncripted", accountIdEncripted);
				Log.i("msg", accountIdEncripted + "----" + accountType);
				

				return params;
			}
		};

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}
}
