package com.imcore.xbionic.home.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.ToastUtil;

public class HomeActivityLogin extends FragmentActivity {
	public DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	// private View mDrawerUserDetail;
	private String[] mNaviItemText;
	private int[] mNaviItemIcon;
	private final static String NAVI_ITEM_TEXT = "item_text";
	private final static String NAVI_ITEM_ICOM = "item_icom";
	private Fragment mFragmentHost;
	private Fragment mFragmentUser;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_login);

		initDrawerLayout();// 侧拉菜单
		initFragment(); // 主页面
	}

	private void initFragment() {
		mFragmentHost = new HomeFragmentHost();
		mFragmentUser = new HomeDrawerUser();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.home_activity_login_fragment, mFragmentHost);
		ft.add(R.id.drawer_fragment_user, mFragmentUser);
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
		for(int i = 0; i < mNaviItemText.length; i++){
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
			ToastUtil.showToast(HomeActivityLogin.this, "item:" + arg2);
			mDrawerLayout.closeDrawers();
			

		}
	};

}
