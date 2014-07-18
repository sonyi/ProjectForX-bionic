package com.imcore.xbionic.product.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductAward;
import com.imcore.xbionic.model.ProductAward;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ActivitytProductMenuAward extends Activity{
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_menu_award);
		mListView = (ListView) findViewById(R.id.lv_pro_award_list);
		mListView.setDividerHeight(0);
		getProductAward();
	}
	
private BaseAdapter awardAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder vh = null;
			if(view == null){
				view = getLayoutInflater().inflate(R.layout.view_product_menu_award, null);
				vh = new ViewHolder();
				vh.data = (TextView) view.findViewById(R.id.tv_pro_award_data);
				vh.title = (TextView) view.findViewById(R.id.tv_pro_award_title);
				vh.img = (ImageView) view.findViewById(R.id.iv_pro_award_img);
				view.setTag(vh);
			}else{
				vh = (ViewHolder) view.getTag();
			}
			vh.title.setText(mAwardArray.get(position).title);
			String[] d = mAwardArray.get(position).createDate.split("T");
			vh.data.setText(d[0] + "  " + d[1]);
			String url = Constant.IMAGE_ADDRESS + mAwardArray.get(position).imageUrl + "_S.jpg";
			setImag(vh.img, url);
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
			return mAwardArray.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAwardArray.size();
		}
		
		class ViewHolder{
			ImageView img;
			TextView title;
			TextView data;
		}
	};
	
	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_product_img, R.drawable.ic_product_img);
		loader.get(url, listener, 400, 400);
	}
	
	private void getProductAward() {
		
		String url = Constant.HOST + "/honor/list.do?offset=0&fetchSize=10";
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 if(response != null){
							 //Log.i("sign", response);
							 response(response);
						 }
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						//Log.i("sign", error.getMessage());
					}
				});
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(this).addToRequestQueue(
				request);
	}
	
	private ArrayList<ProductAward> mAwardArray;
	private void response(String response){
		mAwardArray = new ArrayList<ProductAward>();
		ArrayList<String> data = (ArrayList<String>) JsonUtil.toJsonStrList(response);
		for(String s : data){
			try {
				JSONObject j = new JSONObject(s);
				ProductAward p = new ProductAward();
				p.id = j.getLong("id");
				p.title = j.getString("title");
				p.imageUrl = j.getString("imageUrl");
				p.createDate = j.getString("createDate");
				mAwardArray.add(p);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		//Log.i("sign", mAwardArray.toString());
		mListView.setAdapter(awardAdapter);
		
	}
}
