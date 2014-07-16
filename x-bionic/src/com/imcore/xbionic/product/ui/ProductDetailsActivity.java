package com.imcore.xbionic.product.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityUnLogin;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.ToastUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class ProductDetailsActivity extends SlidingFragmentActivity implements OnClickListener{
	private long productDetailId;
	private ImageView mBack;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		mBack = (ImageView) findViewById(R.id.iv_product_detail_back);
		mBack.setOnClickListener(this);
		
		Intent intent = getIntent();
		productDetailId = intent.getLongExtra(Const.PRODUCT_DETAIL_FRAGMENT_KEY, 0);
		
		initProductDetailFragment();
		intiMenuFragment();
	}

	// 初始化界面信息
	private void initProductDetailFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment imgFragment = new FragmentProductDetailImg();
		Fragment infoFragment = new FragmentProductDetailInfo();
		Fragment sizeFragment = new FragmentProductDetailSize();
		Fragment techFragment = new FragmentProductDetailTech();
		ft.add(R.id.fragment_product_detail_img, imgFragment);
		ft.add(R.id.fragment_product_detail_info, infoFragment);
		ft.add(R.id.fragment_product_detail_size, sizeFragment);
		ft.add(R.id.fragment_product_detail_tech, techFragment);
		Bundle bundle = new Bundle();
		bundle.putLong(Const.PRODUCT_DETAIL_FRAGMENT_KEY, productDetailId);
		imgFragment.setArguments(bundle);
		infoFragment.setArguments(bundle);
		sizeFragment.setArguments(bundle);
		techFragment.setArguments(bundle);
	
		ft.addToBackStack(null).commit();

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
	
	/** 
	 * 捕捉back 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
	        finish();
	        return true;  
	    }  
	    return super.onKeyDown(keyCode, event);  
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.iv_product_detail_back){
			finish();
		}
	} 
}
