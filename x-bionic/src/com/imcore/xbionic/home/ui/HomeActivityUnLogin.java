package com.imcore.xbionic.home.ui;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.Const;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class HomeActivityUnLogin extends FragmentActivity {
	private Fragment mFragmentForUnLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initHomeFragment();

	}

	// 初始化未登录的主界面
	private void initHomeFragment() {
		mFragmentForUnLogin = new HomeFragmentHost();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putBoolean(Const.IS_LOGIN_KEY, Const.UN_LOGIN);
		mFragmentForUnLogin.setArguments(bundle);
		ft.add(R.id.home_activity_unlogin_fragment, mFragmentForUnLogin);
		ft.commit();
	}
}
