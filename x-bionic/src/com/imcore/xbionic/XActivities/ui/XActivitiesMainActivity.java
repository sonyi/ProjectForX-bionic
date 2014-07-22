package com.imcore.xbionic.XActivities.ui;

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
import com.imcore.xbionic.DefinedListView.XListView;
import com.imcore.xbionic.DefinedListView.XListView.IXListViewListener;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.imagework.ImageWork;
import com.imcore.xbionic.model.XActivityMain;
import com.imcore.xbionic.util.JsonUtil;

public class XActivitiesMainActivity extends Activity implements
		OnClickListener {
	private XListView mListView;
	private ImageView mBack;
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xactivities_main);
		mXActivityArray = new ArrayList<XActivityMain>();
		mListView = (XListView) findViewById(R.id.lv_xactivities_layout);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(listViewListener);
		mBack = (ImageView) findViewById(R.id.iv_xactivities_back);
		mBack.setOnClickListener(this);
		getXActivityListInfo(0, 10);
		mDialog = ProgressDialog.show(this, " ", "正在获取数据,请稍后... ", true);

	}

	private IXListViewListener listViewListener = new IXListViewListener() {

		@Override
		public void onLoadMore() {
			if (mXActivityArray.size() == mTotal) {
				mListView.noLoadMore();
			} else {
				int offset = mXActivityArray.size();
				int fetchSize = mXActivityArray.size() + 10;
				if (fetchSize > mTotal) {
					fetchSize = mTotal;
				}
				getXActivityListInfo(offset, fetchSize);
			}
		}
	};

	private int mTotal = 0;

	private void getXActivityListInfo(int offset, int fetchSize) {
		String url = Constant.HOST + "/search/keyword.do?type=2&offset="
				+ offset + "&fetchSize=" + fetchSize;

		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						String total = JsonUtil.getJsonValueByKey(response,
								"total");
						mTotal = Integer.parseInt(total);
						String data = JsonUtil.getJsonValueByKey(response,
								"data");
						// Log.i("sign", mTotal + "------" + data);
						onResponseForXActivityList(data);

						if (mDialog != null) {
							mDialog.cancel();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						if (mDialog != null) {
							mDialog.cancel();
						}
					}
				});

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	ArrayList<XActivityMain> mXActivityArray;

	private void onResponseForXActivityList(String response) {
		ArrayList<XActivityMain> data = (ArrayList<XActivityMain>) JsonUtil
				.toObjectList(response, XActivityMain.class);

		mXActivityArray.addAll(data);
		// Log.i("sign", mXActivityArray.toString());
		if (mXActivityArray.size() == data.size()) {
			mListViewAdapter = new ListViewAdapter();
			mListView.setAdapter(mListViewAdapter);
		} else {
			mListViewAdapter.notifyDataSetChanged();
			mListView.stopLoadMore();
		}
	}

	private ListViewAdapter mListViewAdapter;

	private class ListViewAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mXActivityArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mXActivityArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.view_xactivities_main, null);
				vh = new ViewHolder();
				vh.title = (TextView) view
						.findViewById(R.id.tv_xactivities_title);
				vh.date = (TextView) view
						.findViewById(R.id.tv_xactivities_date);
				vh.img = (ImageView) view.findViewById(R.id.iv_xactivities_img);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			vh.title.setText(mXActivityArray.get(position).title);
			String sd = mXActivityArray.get(position).beginTime;
			String startDate = sd.substring(0, sd.indexOf("T"));
			String ed = mXActivityArray.get(position).endTime;
			String endDate = ed.substring(0, ed.indexOf("T"));
			vh.date.setText(startDate + "-" + endDate);
			String url = Constant.IMAGE_ADDRESS
					+ mXActivityArray.get(position).titleImageUrl + ".jpg";
			view.setOnClickListener(new ListViewOnClickListener(position));
			
			vh.img.setTag(url);
			Bitmap bitmap = ImageWork.getImageWork().setImgBitmap(vh.img, url);
			if (bitmap == null) {
				vh.img.setImageResource(R.drawable.ic_product_img);
			}else{
				vh.img.setImageBitmap(bitmap);
			}

			return view;
		}

		class ViewHolder {
			TextView title;
			TextView date;
			ImageView img;
		}

	};

	private class ListViewOnClickListener implements OnClickListener {
		private int position;

		ListViewOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(XActivitiesMainActivity.this,
					XActivitiesDetailsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("xactivities", mXActivityArray.get(position));
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_product_img, R.drawable.ic_product_img);

		loader.get(url, listener, 400, 400);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.iv_xactivities_back) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}
