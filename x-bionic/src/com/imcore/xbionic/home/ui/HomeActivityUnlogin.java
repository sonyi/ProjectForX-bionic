package com.imcore.xbionic.home.ui;

import com.imcore.xbionic.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class HomeActivityUnlogin extends FragmentActivity{
	Fragment mFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_unlogin);
		
		initFragment();	//主页面
	}
	
	private void initFragment() {
		mFragment = new HomeFragmentHost();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.home_activity_unlogin_fragment, mFragment);
		ft.commit();
	}

}
