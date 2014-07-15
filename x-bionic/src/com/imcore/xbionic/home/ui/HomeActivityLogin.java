package com.imcore.xbionic.home.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.Const;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeActivityLogin extends SlidingFragmentActivity {
	private Fragment mFragmentForUnLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initHomeFragment();
		intiMenuFragment();

		// 注册打开侧拉菜单监听事件
		IntentFilter filter = new IntentFilter(Const.OPEN_DRAWERLAYOUT);
		registerReceiver(receiverForDrawer, filter);
	}

	private void intiMenuFragment() {
		// 设置滑动菜单视图界面
		setBehindContentView(R.layout.fragment_home_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_home_menu, new HomeFragmentMenu())
				.commit();

		// 设置滑动菜单的属性值
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
		getSlidingMenu().setFadeDegree(0.35f);
		getSlidingMenu().setMode(SlidingMenu.LEFT);
	}

	// 初始化未登录的主界面
	private void initHomeFragment() {
		mFragmentForUnLogin = new HomeFragmentHost();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putBoolean(Const.IS_LOGIN_KEY, Const.LOGIN);
		mFragmentForUnLogin.setArguments(bundle);
		ft.add(R.id.home_activity_unlogin_fragment, mFragmentForUnLogin);
		ft.commit();
	}

	// 接收menu按钮点击事件，打开侧拉菜单
	private BroadcastReceiver receiverForDrawer = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Const.OPEN_DRAWERLAYOUT)) {
				toggle();
			}
		}
	};
}
