package com.imcore.xbionic.product.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.ToastUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class ProductDetailsActivity extends SlidingFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		Intent intent = getIntent();
		long id = intent.getLongExtra("detailId", 0);
		ToastUtil.showToast(this, id + "");
		
		
		initProductDetailFragment();
		intiMenuFragment();
	}

	// 初始化界面信息
	private void initProductDetailFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment ImgFragment = new FragmentProductDetailImg();
		ft.add(R.id.fragment_product_detail_img, ImgFragment)
				.addToBackStack(null).commit();

	}

	private void intiMenuFragment() {
		// 设置滑动菜单视图界面
		setBehindContentView(R.layout.fragment_product_menu);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_product_menu,
						new FragmentProductDetailMenu()).commit();

		// 设置滑动菜单的属性值
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
		getSlidingMenu().setFadeDegree(0.35f);
		getSlidingMenu().setMode(SlidingMenu.RIGHT);
	}
}
