package com.imcore.xbionic.home.ui;

import com.imcore.xbionic.R;
import com.imcore.xbionic.login.ui.LoginMainActivity;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.DisplayUtil;
import com.imcore.xbionic.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class HomeFragmentHost extends Fragment implements OnClickListener {
	private View mFragmentView;
	public ImageView mLogoImg, mMenuImg, mSearchImg;
	private ViewPager viewPager;
	private ImageView[] mImageViews;
	private int[] imgIdArray;
	private boolean isLogin;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFragmentView = inflater.inflate(R.layout.home_fragment, null);
		mLogoImg = (ImageView) mFragmentView.findViewById(R.id.iv_home_logo);
		mMenuImg = (ImageView) mFragmentView.findViewById(R.id.iv_home_menu);
		mSearchImg = (ImageView) mFragmentView
				.findViewById(R.id.iv_home_search);

		Bundle bundle = this.getArguments();
		isLogin = bundle.getBoolean(Const.IS_LOGIN_KEY);
		if (isLogin) {
			mMenuImg.setImageResource(R.drawable.ic_home_menu_login);
		} else {
			mMenuImg.setImageResource(R.drawable.ic_user);
		}
		mMenuImg.setOnClickListener(this);
		mLogoImg.setOnClickListener(this);
		mSearchImg.setOnClickListener(this);

		initWidget();

		return mFragmentView;
	}

	// viewPager小于三张时会出错，未做判断
	private void initWidget() {
		viewPager = (ViewPager) mFragmentView
				.findViewById(R.id.drawer_main_body_layout);

		imgIdArray = new int[] { R.drawable.ic_home_activtypage1,
				R.drawable.ic_home_activtypage2,
				R.drawable.ic_home_activtypage3,
				R.drawable.ic_home_activtypage4 };

		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageResource(imgIdArray[i]);
			mImageViews[i] = imageView;
		}

		viewPager.setAdapter(new MyAdapter());
		// viewPager.setOnPageChangeListener(pageViewChangeListener);
		viewPager.setCurrentItem((mImageViews.length) * 200);// 设置当前position

	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mImageViews[position
					% mImageViews.length]);

		}

		@Override
		public Object instantiateItem(View container, int position) {
			View view = null;
			try {
				view = mImageViews[position % mImageViews.length];
				final int p = position;
				view.setOnClickListener(new OnClickListener() {// pager点击监听事件

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ToastUtil.showToast(getActivity(), p
								% mImageViews.length + "");
					}
				});

				((ViewPager) container).addView(view, 0);
			} catch (Exception e) {

			}
			return view;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_home_menu:
			menuClickListener(isLogin);
			break;
		case R.id.iv_home_logo:

			break;
		case R.id.iv_home_search:

			break;
		}

	}
	
	private void menuClickListener(boolean isLogin){
		if(isLogin){
			//ToastUtil.showToast(getActivity(), "login");
		}else {
			//ToastUtil.showToast(getActivity(), "unlogin");
			Intent intent = new Intent(getActivity(),LoginMainActivity.class);
			startActivity(intent);
		}
	}

	/*
	 * private OnPageChangeListener pageViewChangeListener = new
	 * OnPageChangeListener() {
	 * 
	 * @Override public void onPageSelected(int arg0) {
	 * 
	 * }
	 * 
	 * @Override public void onPageScrolled(int arg0, float arg1, int arg2) {
	 * 
	 * }
	 * 
	 * @Override public void onPageScrollStateChanged(int arg0) { // TODO
	 * Auto-generated method stub // setImageBackground(arg0 %
	 * mImageViews.length); } };
	 */

}
