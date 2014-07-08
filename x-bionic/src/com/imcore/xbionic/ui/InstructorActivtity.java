package com.imcore.xbionic.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityUnlogin;

public class InstructorActivtity extends Activity implements
		OnPageChangeListener {
	private ViewPager viewPager;
	private ImageView[] tips;//装点点的ImageView数组
	private ImageView[] mImageViews;//装ImageView数组
	private int[] imgIdArray;//图片资源id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor);
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		// 载入图片资源ID
		imgIdArray = new int[] { R.drawable.ic_instructor_welcompage1,
				R.drawable.ic_instructor_welcompage2, R.drawable.ic_instructor_welcompage3};

		// 将点点加入到ViewGroup中
		tips = new ImageView[imgIdArray.length];
		for (int i = 0; i < tips.length; i++) {
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(15, 15);
			layout.setMargins(10, 0, 10, 0);
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setLayoutParams(layout);
			
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}

			group.addView(imageView);
			
			
			SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE); //私有数据
			Editor editor = sp.edit();//获取编辑器
			editor.putBoolean("isLoginFirst", false);
			editor.commit();//提交修改
		}

		// 将图片装载到数组中
		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgIdArray[i]);
			mImageViews[i] = imageView;
		}

		// 设置Adapter
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(this);
		

	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mImageViews[position]);

		}

		@Override
		public Object instantiateItem(View container, int position) {
			View view = mImageViews[position];
			
			final int p = position;
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(p == (mImageViews.length -1)){
						Intent intent = new Intent(InstructorActivtity.this,HomeActivityUnlogin.class);
						InstructorActivtity.this.finish();
						startActivity(intent);
						
					}
				}
			});
			
			((ViewPager) container).addView(view, 0);
			return view;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0);
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

}
