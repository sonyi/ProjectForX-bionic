package com.imcore.xbionic.product.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

public class FragmentProductDetailTech extends Fragment{
	private View view;
	private ListView mListView;
	private long productDetailId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_product_detail_tech, null);
		mListView = (ListView) view.findViewById(R.id.lv_pro_det_tech);

		productDetailId = getArguments().getLong(Const.PRODUCT_DETAIL_FRAGMENT_KEY);
		//ToastUtil.showToast(getActivity(), productDetailId + "");
		
		
		getProductDetailTech();
		
		return view;
	}
	
	
	private BaseAdapter techAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if(view == null){
				view = LayoutInflater.from(getActivity()).inflate(R.layout.view_product_detail_tech, null);
				vh = new ViewHolder();
				vh.title = (TextView) view.findViewById(R.id.tv_pro_det_tech_title);
				vh.img = (ImageView) view.findViewById(R.id.iv_pro_det_tech_img);
				view.setTag(vh);
			}else{
				vh = (ViewHolder) view.getTag();
			}
			vh.title.setText(mTitle.get(position));
			String url = Constant.IMAGE_ADDRESS + mImgUrl.get(position) + "_M.jpg";
			setImag(vh.img, url);
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return mTitle.get(position);
		}
		
		@Override
		public int getCount() {
			return mTitle.size();
		}
		
		class ViewHolder{
			ImageView img;
			TextView title;
		}
	};
	
	
	
	
	private void getProductDetailTech() {
		mImgUrl = new ArrayList<String>();
		mTitle = new ArrayList<String>();
		String url = Constant.HOST + "/product/labs/list.do?id=" + productDetailId;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//Log.i("sign", response);
						onResponseForProductList(response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());

					}
				});

		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(request);
	}

	ArrayList<String> mImgUrl;
	ArrayList<String> mTitle;
	private void onResponseForProductList(String response) {
		
		ArrayList<String> arr = (ArrayList<String>) JsonUtil.toJsonStrList(response);
		
		for (String json : arr) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				mTitle.add(jsonObject.getString("title"));
				mImgUrl.add(jsonObject.getString("imageUrl"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		mListView.setAdapter(techAdapter);
	}
	
	
	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getActivity().getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_product_img, R.drawable.ic_product_img);
		loader.get(url, listener, 400, 400);
	}
	

}
