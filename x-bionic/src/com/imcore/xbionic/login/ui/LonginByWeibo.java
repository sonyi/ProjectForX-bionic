package com.imcore.xbionic.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.imcore.xbionic.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class LonginByWeibo extends Activity{
    private WeiboAuth mWeiboAuth;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo);
		
		 // 创建微博实例
        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        
		
		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  mWeiboAuth.anthorize(new AuthListener());
				  
//				  mSsoHandler = new SsoHandler(Weibo.this, mWeiboAuth);
//	                mSsoHandler.authorize(new AuthListener());
			}
		});
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
	
	 class AuthListener implements WeiboAuthListener {
	        
	        @Override
	        public void onComplete(Bundle values) {
	            // 从 Bundle 中解析 Token
	            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	            if (mAccessToken.isSessionValid()) {
	                // 显示 Token
	            	Log.i("msg", mAccessToken.toString());
	               
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
	            
	        }
	    }

}
