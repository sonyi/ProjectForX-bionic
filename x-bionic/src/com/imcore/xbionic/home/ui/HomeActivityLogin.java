package com.imcore.xbionic.home.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.imcore.xbionic.R;
import com.imcore.xbionic.menu.ui.AccountResetActivity;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.ToastUtil;

public class HomeActivityLogin extends FragmentActivity {
	public DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] mNaviItemText;
	private int[] mNaviItemIcon;
	private final static String NAVI_ITEM_TEXT = "item_text";
	private final static String NAVI_ITEM_ICOM = "item_icom";
	private Fragment mFragmentForLoginHost;
	private Fragment mFragmentForLoginUser;

	private Fragment mFragmentForUnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		boolean isLogin = sp.getBoolean("isLogin", false);

		if (isLogin) {
			setContentView(R.layout.activity_home_login);
			initDrawerLayout();// 侧拉菜单
			initFragmentForLogin(); // 主页面
		} else {
			setContentView(R.layout.activity_home_unlogin);
			initFragmentForUnLogin(); // 主页面
		}
	}

	private void initFragmentForUnLogin() {
		mFragmentForUnLogin = new HomeFragmentHost();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putBoolean(Const.IS_LOGIN_KEY, Const.UN_LOGIN);
		mFragmentForUnLogin.setArguments(bundle);
		ft.add(R.id.home_activity_unlogin_fragment, mFragmentForUnLogin);
		ft.commit();
	}

	private void initFragmentForLogin() {
		mFragmentForLoginHost = new HomeFragmentHost();
		mFragmentForLoginUser = new HomeDrawerUser();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putBoolean(Const.IS_LOGIN_KEY, Const.LOGIN);
		mFragmentForLoginHost.setArguments(bundle);
		ft.add(R.id.home_activity_login_fragment, mFragmentForLoginHost);
		ft.add(R.id.drawer_fragment_user, mFragmentForLoginUser);
		ft.commit();
	}

	private void initDrawerLayout() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mNaviItemText = getResources().getStringArray(
				R.array.drawer_item_array_text);

		mNaviItemIcon = new int[] { R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher };

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mNaviItemText.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put(NAVI_ITEM_TEXT, mNaviItemText[i]);
			item.put(NAVI_ITEM_ICOM, mNaviItemIcon[i]);
			data.add(item);

		}

		String[] from = new String[] { NAVI_ITEM_ICOM, NAVI_ITEM_TEXT };
		int[] to = new int[] { R.id.iv_navi_item_icon, R.id.tv_navi_item_text };
		mDrawerList = (ListView) findViewById(R.id.lv_drawer_list);
		mDrawerList.setAdapter(new SimpleAdapter(this, data,
				R.layout.view_navi_drawer_item, from, to));
		mDrawerList.setOnItemClickListener(drawerListOnItemClickListener);
	}

	private OnItemClickListener drawerListOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			listMenuClickListener(arg2);
			mDrawerLayout.closeDrawers();
		}

	};

	private void listMenuClickListener(int arg2) {
		switch (arg2) {
		case 0://您的订购

			break;

		case 1://账户设置
			//ToastUtil.showToast(HomeActivityLogin.this, "账户设置");
			Intent intent = new Intent(this,AccountResetActivity.class);
			startActivity(intent);
			break;

		case 2://达人社区

			break;

		case 3://部落社区

			break;

		case 4://购物车

			break;

		case 5://订阅信息

			break;

		case 6://分享设置

			break;

		case 7://密码设置

			break;

		case 8://关于我们

			break;
		}
	}
}
