package com.imcore.xbionic.expertstory.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.definedlistView.XListView;
import com.imcore.xbionic.definedlistView.XListView.IXListViewListener;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.imagework.ImageWork;
import com.imcore.xbionic.model.ExperStoryList;
import com.imcore.xbionic.util.JsonUtil;

public class ExpertStoryHomeActivity extends Activity implements
		OnClickListener {
	private XListView mListView;
	private ImageView mBack;
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experstory_home);
		mExperStoryLists = new ArrayList<ExperStoryList>();
		mListView = (XListView) findViewById(R.id.lv_experstory_layout);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(listViewListener);
		mBack = (ImageView) findViewById(R.id.iv_experstory_back);
		mBack.setOnClickListener(this);

		getExperStoryListInfo(0, 10);
		mDialog = ProgressDialog.show(this, " ",
				"正在获取数据,请稍后... ", true);

	}

	private IXListViewListener listViewListener = new IXListViewListener() {

		@Override
		public void onLoadMore() {
			if (mExperStoryLists.size() == mTotal) {
				mListView.noLoadMore();
			} else {
				int offset = mExperStoryLists.size();
				int fetchSize = mExperStoryLists.size() + 10;
				if (fetchSize > mTotal) {
					fetchSize = mTotal;
				}
				getExperStoryListInfo(offset, fetchSize);
			}
		}
	};

	private int mTotal = 0;

	private void getExperStoryListInfo(int offset, int fetchSize) {
		String url = Constant.HOST + "/testteam/list.do?offset=" + offset
				+ "&fetchSize=" + fetchSize;

		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						String total = JsonUtil.getJsonValueByKey(response,
								"total");
						mTotal = Integer.parseInt(total);
						String data = JsonUtil.getJsonValueByKey(response,
								"data");
						// Log.i("sign", data);
						onResponseForExperStoryList(data);
						if(mDialog != null){
							mDialog.cancel();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						if(mDialog != null){
							mDialog.cancel();
						}
					}
				});

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	ArrayList<ExperStoryList> mExperStoryLists;

	private void onResponseForExperStoryList(String response) {
		ArrayList<ExperStoryList> data = (ArrayList<ExperStoryList>) JsonUtil
				.toObjectList(response, ExperStoryList.class);
		// Log.i("sign", mExperStoryLists.toString());
		mExperStoryLists.addAll(data);
		if (mExperStoryLists.size() == data.size()) {
			mListView.setAdapter(mListViewAdapter);
		} else {
			mListViewAdapter.notifyDataSetChanged();
			mListView.stopLoadMore();
		}
	}

	private BaseAdapter mListViewAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.view_experstory_layout, null);
				vh = new ViewHolder();
				vh.img = (ImageView) view.findViewById(R.id.iv_experstory_img);
				vh.title = (TextView) view
						.findViewById(R.id.tv_experstory_title);
				vh.data = (TextView) view.findViewById(R.id.tv_experstory_data);
				vh.dec = (TextView) view
						.findViewById(R.id.tv_experstory_simpleDescrition);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			vh.title.setText(mExperStoryLists.get(position).title);
			String testdata = mExperStoryLists.get(position).testDate;
			String d = testdata.substring(0, testdata.indexOf("T"));
			String[] sp = d.split("/");
			vh.data.setText(sp[2] + "-" + sp[0] + "-" + sp[1]);
			vh.dec.setText(mExperStoryLists.get(position).simpleDescrition);
			String url = Constant.IMAGE_ADDRESS + mExperStoryLists.get(position).phoneUrl + "_N.jpg";
			//setImag(vh.img, url);
			vh.img.setTag(url);
			Bitmap bitmap = ImageWork.getImageWork().setImgBitmap(vh.img, url);
			if (bitmap == null) {
				vh.img.setImageResource(R.drawable.ic_product_img);
			}else{
				vh.img.setImageBitmap(bitmap);
			}
			
			view.setOnClickListener(new listViewOnClickListener(
					mExperStoryLists.get(position).descrition));
			return view;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return mExperStoryLists.get(position);
		}

		@Override
		public int getCount() {
			return mExperStoryLists.size();
		}

		class ViewHolder {
			ImageView img;
			TextView title;
			TextView data;
			TextView dec;
		}
	};

	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_product_img, R.drawable.ic_product_img);
		loader.get(url, listener, 400, 400);
	}

	private class listViewOnClickListener implements OnClickListener {
		String detail;

		listViewOnClickListener(String detail) {
			this.detail = detail;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ExpertStoryHomeActivity.this,
					ExperStoryDtailActivity.class);
			intent.putExtra("experStoryDetail", detail);
			startActivity(intent);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.iv_experstory_back) {
			finish();
		}
	}
}
