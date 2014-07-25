package com.imcore.xbionic.login.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.ToastUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;

public class LoginMainActivity extends Activity implements OnClickListener {
	private ImageView mQQLoginImg, mSinaLoginImg, mXLoginImg, mRegisterImg,
			mHelpImg, mServeImg;

	// 微博授权登录
	private WeiboAuth mWeiboAuth;
	private Oauth2AccessToken mAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
		mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);

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
		case R.id.iv_login_main_qqlogin:// 腾讯微博账户登录
			long appid = 801522061;
			String app_secket = "82f97c425278b7dd63fa97106c39120a";
			auth(appid, app_secket);
			break;
		case R.id.iv_login_main_sina:// 微博授权登录
			mWeiboAuth.anthorize(new AuthListener());
			break;
		case R.id.iv_login_main_x:// 部落账户登录
			Intent intent = new Intent(this, XLoginActivity.class);
			intent.putExtra(Const.LOGIN_KEY, Const.LOGIN_AT_OTHER_VALUE);
			startActivity(intent);
			break;
		case R.id.iv_login_main_register:// 新用户注册
			startActivity(new Intent(this, PhoneRegisterCodeActivity.class));
			break;
		case R.id.iv_login_help:

			break;
		case R.id.iv_login_serve:

			break;

		}
	}

	// 新浪微博授权登入正确后返回的回调方法
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				Log.i("msg", mAccessToken.toString());
				Toast.makeText(LoginMainActivity.this,
						"微博成功授权，但该死的部落接口不能用，试试部落账号登录吧....", Toast.LENGTH_LONG)
						.show();
			} else {
				String code = values.getString("code");
				Log.i("msg", code);
			}
		}

		@Override
		public void onCancel() {
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ToastUtil.showToast(LoginMainActivity.this,
					"微博授权登录异常：" + e.getMessage());
		}
	}

	private void auth(long appid, String app_secket) {
		final Context context = this.getApplicationContext();
		AuthHelper.register(this, appid, app_secket, new OnAuthListener() {

			// 如果当前设备没有安装腾讯微博客户端，走这里
			@Override
			public void onWeiBoNotInstalled() {
				AuthHelper.unregister(LoginMainActivity.this);
				Intent i = new Intent(LoginMainActivity.this, Authorize.class);
				startActivity(i);
			}

			// 如果当前设备没安装指定版本的微博客户端，走这里
			@Override
			public void onWeiboVersionMisMatch() {
				AuthHelper.unregister(LoginMainActivity.this);
				Intent i = new Intent(LoginMainActivity.this, Authorize.class);
				startActivity(i);
			}

			// 如果授权失败，走这里
			@Override
			public void onAuthFail(int result, String err) {
				AuthHelper.unregister(LoginMainActivity.this);
			}

			// 授权成功，走这里
			@Override
			public void onAuthPassed(String name, WeiboToken token) {
				Util.saveSharePersistent(context, "ACCESS_TOKEN",
						token.accessToken);
				Util.saveSharePersistent(context, "EXPIRES_IN",
						String.valueOf(token.expiresIn));
				Util.saveSharePersistent(context, "OPEN_ID", token.openID);
				Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
				Util.saveSharePersistent(context, "CLIENT_ID", Util.getConfig()
						.getProperty("APP_KEY"));
				Util.saveSharePersistent(context, "AUTHORIZETIME",
						String.valueOf(System.currentTimeMillis() / 1000l));
				AuthHelper.unregister(LoginMainActivity.this);
			}
		});
		AuthHelper.auth(this, "");
	}
}
