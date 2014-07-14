package com.imcore.xbionic.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imcore.xbionic.R;
import com.imcore.xbionic.login.ui.LoginMainActivity;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.ToastUtil;

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
				view.setOnClickListener(new ViewPagerOnClickListener(position));
				((ViewPager) container).addView(view, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}

	private class ViewPagerOnClickListener implements OnClickListener {
		private int position;

		ViewPagerOnClickListener(int position) {
			this.position = position% mImageViews.length;
		}

		@Override
		public void onClick(View v) {
			ToastUtil.showToast(getActivity(), position+ "");
			switch (position) {
			case 0://产品购买
				Intent intent = new Intent(getActivity(),ProductMainActivity.class);
				startActivity(intent);
				break;
			case 1://x的介绍

				break;
			case 2://达人故事

				break;
			case 3://x的活动

				break;
			}
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

	private void menuClickListener(boolean isLogin) {
		if (isLogin) {
			// ToastUtil.showToast(getActivity(), "login");
			Intent intent = new Intent(Const.OPEN_DRAWERLAYOUT);
			getActivity().sendBroadcast(intent);
			
		} else {
			// ToastUtil.showToast(getActivity(), "unlogin");
			Intent intent = new Intent(getActivity(), LoginMainActivity.class);
			startActivity(intent);
		}
	}

}
