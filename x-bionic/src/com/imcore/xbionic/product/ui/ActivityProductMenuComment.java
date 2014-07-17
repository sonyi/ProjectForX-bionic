package com.imcore.xbionic.product.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductColor;
import com.imcore.xbionic.model.ProductComment;
import com.imcore.xbionic.util.JsonUtil;

public class ActivityProductMenuComment extends Activity {
	private ArrayList<ProductComment> mCommentArray;
	private ListView mCommentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_menu_comment);
		mCommentList = (ListView) findViewById(R.id.lv_pro_comment_list);
		getProductComment();

	}

	// 获取产品详情
	private void getProductComment() {
		String url = Constant.HOST + "/product/comments/list.do?id=267";
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (response != null) {
							// Log.i("sign", response);
							onResponseForProductComment(response);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForProductComment(String response) {
		mCommentArray = (ArrayList<ProductComment>) JsonUtil.toObjectList(
				response, ProductComment.class);
		 Log.i("sign", mCommentArray.toString());
		mCommentList.setAdapter(commentAdapter);
	}

	private BaseAdapter commentAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.view_product_menu_comment, null);
				vh = new ViewHolder();
				vh.comment = (TextView) view
						.findViewById(R.id.tv_pro_comment_comment);
				vh.name = (TextView) view
						.findViewById(R.id.tv_pro_comment_name);
				vh.data = (TextView) view
						.findViewById(R.id.tv_pro_comment_time);
				vh.linearLayout = (LinearLayout) view.findViewById(R.id.lin_pro_comment_start);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			vh.comment.setText(mCommentArray.get(position).comment);
			vh.name.setText(mCommentArray.get(position).lastName
					+ mCommentArray.get(position).firstName);
			String allData = mCommentArray.get(position).commentDate;

			String d = allData.substring(0, allData.indexOf("T"));
			String[] arr = d.split("/");
			vh.data.setText(arr[2] + "-" + arr[1] + "-" + arr[0]);
			
			addStar(mCommentArray.get(position).star,vh.linearLayout);
			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mCommentArray.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCommentArray.size();
		}
	};

	class ViewHolder {
		TextView comment;
		TextView name;
		TextView data;
		LinearLayout linearLayout;
	}
	
	// 动态生成爱心控件
		private void addStar(long star,LinearLayout insertLayout ) {
			
			int len;
			if(star >= 5){
				len = 5;
			}else{
				len = (int) star;
			}
			for (int i = 0; i < 4; i++) {
				ImageView img = new ImageView(this);
				img.setImageResource(R.drawable.ic_pro_comment_love);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						50, 50);
				layoutParams.leftMargin = 10;
				img.setScaleType(ScaleType.FIT_XY);
				insertLayout.addView(img, layoutParams);
			}
		}
}
