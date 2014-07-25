package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.login.ui.XLoginActivity;
import com.imcore.xbionic.menu.ui.ShoppingTrolleyActivity;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.ToastUtil;

import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class FragmentProductDetailMenu extends Fragment implements OnClickListener{
	Context context = null;
	@SuppressWarnings("deprecation")
	LocalActivityManager manager = null;
	ViewPager pager = null;
	TabHost tabHost = null;
	private TextView mCommentTv,mNewsTv,mAwardTv;
	private TextView mShare,mShopping,mStore;
	private long productDetailId;
	
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private ImageView cursor;// 动画图片
	
	
	private View view;
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_product_detail_menu, null);
		productDetailId = getArguments().getLong(Const.PRODUCT_DETAIL_FRAGMENT_KEY);
		//Log.i("sign", productDetailId + "");
		context = getActivity();
		manager = new LocalActivityManager(getActivity() , true);
		manager.dispatchCreate(savedInstanceState);

		InitImageView();
		initTextView();
		initPagerViewer();
		
		
		return view;
	}
	
	
	/**
	 * 初始化标题
	 */
	private void initTextView() {
		mCommentTv = (TextView) view.findViewById(R.id.tv_product_menu_comment);
		mNewsTv = (TextView) view.findViewById(R.id.tv_product_menu_news);
		mAwardTv = (TextView) view.findViewById(R.id.tv_product_menu_award);
		mCommentTv.setText("产品评价");
		mNewsTv.setText("产品新闻");
		mAwardTv.setText("所获奖项");
		mCommentTv.setOnClickListener(new MyOnClickListener(0));
		mNewsTv.setOnClickListener(new MyOnClickListener(1));
		mAwardTv.setOnClickListener(new MyOnClickListener(2));
		
		mShare = (TextView) view.findViewById(R.id.tv_pro_menu_share);
		mShopping = (TextView) view.findViewById(R.id.tv_pro_menu_shopping);
		mStore = (TextView) view.findViewById(R.id.tv_pro_menu_collect);
		mShare.setOnClickListener(this);
		mShopping.setOnClickListener(this);
		mStore.setOnClickListener(this);
	}
	/**
	 * 初始化PageViewer
	 */
	private void initPagerViewer() {
		pager = (ViewPager) view.findViewById(R.id.view_product_menu_viewpage);
		final ArrayList<View> list = new ArrayList<View>();
		Intent intent = new Intent(context, ActivityProductMenuComment.class);
		intent.putExtra("productDetailId", productDetailId);
		list.add(getView("A", intent));
		Intent intent2 = new Intent(context, ActivityProductMenuNews.class);
		intent2.putExtra("productDetailId", productDetailId);
		list.add(getView("B", intent2));
		Intent intent3 = new Intent(context, ActivitytProductMenuAward.class);
		intent3.putExtra("productDetailId", productDetailId);
		list.add(getView("C", intent3));

		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) view.findViewById(R.id.iv_pro_menu_cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller)
		.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = ((screenW-180) / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	
	/**
	 * 通过activity获取视图
	 * @param id
	 * @param intent
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/**
	 * Pager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter{
		List<View> list =  new ArrayList<View>();
		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);	
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
	}
	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.tv_pro_menu_share){
			
		}else if(v.getId() == R.id.tv_pro_menu_shopping){
			startActivity(new Intent(getActivity(),ShoppingTrolleyActivity.class));
		}else if(v.getId() == R.id.tv_pro_menu_collect){
			addFavorite();
		}
	};
	
	private String userId;
	private String token;
	private void addFavorite() {
		SharedPreferences sp = getActivity().getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		if (userId.equals("") || token.equals("")) {
			new AlertDialog.Builder(getActivity())
					.setTitle("您还未登陆，请先登陆.....")
					.setPositiveButton("登陆",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent intent = new Intent(getActivity(),
											XLoginActivity.class);
									intent.putExtra(Const.LOGIN_KEY,
											Const.LOGIN_AT_BUY_VALUE);
									startActivity(intent);
								}
							}).create().show();
			return;
		}

		String url = Constant.HOST + "/user/favorite/add.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 if(response != null){
							 //Log.i("sign", response);
							ToastUtil.showToast(getActivity(), "收藏成功！");
						 }
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						//Log.i("sign", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", userId);
				params.put("token", token);
				params.put("type", "1");
				params.put("productId", productDetailId + "");
				 //Log.i("sign", productDetailId + "----");
				return params;
			}
		};
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}
}
