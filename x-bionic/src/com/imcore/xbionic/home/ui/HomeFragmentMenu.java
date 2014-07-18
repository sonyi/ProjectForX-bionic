package com.imcore.xbionic.home.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.imcore.xbionic.R;
import com.imcore.xbionic.menu.ui.AccountResetActivity;
import com.imcore.xbionic.util.Const;

public class HomeFragmentMenu extends Fragment {
	View view;
	private String[] mNaviItemText;
	private int[] mNaviItemIcon;
	private ListView mDrawerList;
	private final static String NAVI_ITEM_TEXT = "item_text";
	private final static String NAVI_ITEM_ICOM = "item_icom";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home_menu_detail, null);
		initDrawerLayout();
		initFragmentForLogin();
		return view;
	}

	private void initFragmentForLogin() {
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.add(R.id.drawer_fragment_user, new HomeDrawerUser());
		ft.commit();
	}

	// 初始化侧拉菜单
	private void initDrawerLayout() {

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
		mDrawerList = (ListView) view.findViewById(R.id.lv_drawer_list);
		mDrawerList.setAdapter(new SimpleAdapter(getActivity(), data,
				R.layout.view_navi_drawer_item, from, to));
		mDrawerList.setOnItemClickListener(drawerListOnItemClickListener);
	}

	// 接收menu按钮点击事件，打开侧拉菜单

	// 侧拉菜单选项单击事件
	private OnItemClickListener drawerListOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			listMenuClickListener(arg2);

		}
	};

	private void listMenuClickListener(int arg2) {
		switch (arg2) {
		case 0:// 您的订购

			break;

		case 1:// 账户设置
				// ToastUtil.showToast(HomeActivityLogin.this, "账户设置");
			Intent intent = new Intent(getActivity(),AccountResetActivity.class);
			Intent in = new Intent(Const.OPEN_DRAWERLAYOUT);
			getActivity().sendBroadcast(in);
			startActivity(intent);
			break;

		case 2:// 达人社区

			break;

		case 3:// 部落社区

			break;

		case 4:// 购物车

			break;

		case 5:// 订阅信息

			break;

		case 6:// 分享设置

			break;

		case 7:// 密码设置

			break;

		case 8:// 关于我们

			break;
		}
	}
}
