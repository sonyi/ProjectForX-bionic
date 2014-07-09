package com.imcore.xbionic.menu.ui;

import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountResetActivity extends Activity implements OnClickListener{
	private ImageView mBack;
	private TextView mSignOut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_reset);
		mBack = (ImageView) findViewById(R.id.iv_account_reset_back);
		mSignOut = (TextView) findViewById(R.id.tv_account_reset_signout);
		mBack.setOnClickListener(this);
		mSignOut.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_account_reset_back://回退
			finish();
			break;

		case R.id.tv_account_reset_signout://注销账户
			SharedPreferences sp = getSharedPreferences("loginUser",
					Context.MODE_PRIVATE); // 私有数据
			Editor editor = sp.edit();// 获取编辑器
			editor.putBoolean("isLogin", false);
			editor.commit();// 提交修改
			Intent intent = new Intent(this,HomeActivityLogin.class);
			startActivity(intent);
			finish();
			break;

		case R.id.rel_account_reset_baseinfromation://基本资料

			break;

		case R.id.rel_account_reset_address://地址信息

			break;

		case 4://购物车

			break;
		}
	}
}
