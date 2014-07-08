package com.imcore.xbionic.login.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class LoginMainActivity extends Activity implements OnClickListener {
	private ImageView mQQLoginImg, mSinaLoginImg, mXLoginImg, mRegisterImg,
			mHelpImg, mServeImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
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
		case R.id.iv_login_main_qqlogin:

			break;
		case R.id.iv_login_main_sina:

			break;
		case R.id.iv_login_main_x:
			Intent intent = new Intent(this,XLoginActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_login_main_register:

			break;
		case R.id.iv_login_help:

			break;
		case R.id.iv_login_serve:

			break;

		}
	}
}
