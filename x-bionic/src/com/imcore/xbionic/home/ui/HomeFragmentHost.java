package com.imcore.xbionic.home.ui;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.DisplayUtil;
import com.imcore.xbionic.util.ToastUtil;

import android.annotation.SuppressLint;
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
	public ImageView mImgLogo;
	private ViewPager viewPager;
	private ImageView[] mImageViews;
	private int[] imgIdArray;
	private String isLogin;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFragmentView = inflater.inflate(R.layout.home_fragment, null);
		mImgLogo = (ImageView) mFragmentView
				.findViewById(R.id.iv_drawer_main_logo);
		mImgLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		initWidget();

		return mFragmentView;
	}

	//viewPager小于三张时会出错，未做判断
	private void initWidget() {
		viewPager = (ViewPager) mFragmentView
				.findViewById(R.id.drawer_main_body_layout);

		imgIdArray = new int[] { R.drawable.ic_home_activtypage1,
				R.drawable.ic_home_activtypage2, R.drawable.ic_home_activtypage3,
				R.drawable.ic_home_activtypage4 };

		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageResource(imgIdArray[i]);
			mImageViews[i] = imageView;
		}

		viewPager.setAdapter(new MyAdapter());
		//viewPager.setOnPageChangeListener(pageViewChangeListener);
		viewPager.setCurrentItem((mImageViews.length) * 200);//设置当前position

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
			try{
				view = mImageViews[position% mImageViews.length];
				final int p = position;
				view.setOnClickListener(new OnClickListener() {//pager点击监听事件
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ToastUtil.showToast(getActivity(), p% mImageViews.length + "");
					}
				});
				
				((ViewPager) container).addView(view, 0);
			}catch(Exception e){
				
			}
			return view;
		}
	}

/*	private OnPageChangeListener pageViewChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			// setImageBackground(arg0 % mImageViews.length);
		}
	};*/

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_drawer_main_menu:

			break;
		case R.id.iv_drawer_main_logo:

			break;
		case R.id.iv_drawer_main_search:

			break;
		}

	}

}
