package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.DisplayUtil;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

public class FragmentProductDetailSize extends Fragment{
	View view;
	private GridView gridView;
	private LayoutInflater inflater;
	private List<String> dataList = new ArrayList<String>();
	private List<String> dataColumns = new ArrayList<String>();
	
	private long productDetailId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_product_detail_size, null);
		
		productDetailId = getArguments().getLong(Const.PRODUCT_DETAIL_FRAGMENT_KEY);
		//ToastUtil.showToast(getActivity(), productDetailId + "");
		
		getProductDetailSize();
		
		return view;
	}

	private void initGridView() {
		LayoutInflater inflater;
		gridView = (GridView) view.findViewById(R.id.gri_Pro_det_size);
		inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		gridView.setAdapter(new GridViewAdapter());
		
		int sums = dataList.size();
		int columns = dataColumns.size();
		int rows = sums/columns;
		
		int allWidth;
		int allHeight;
		int itemWidth;
		
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int screenWidth = DisplayUtil.getScreenWidth(getActivity());
		int gridWidth = (int) (80 * columns * density);
		if(screenWidth < gridWidth){
			allWidth = gridWidth;
			itemWidth = (int) (80 * density);
		}else{
			allWidth = screenWidth;
			itemWidth = screenWidth/columns;
		}
		int h = DisplayUtil.dip2Px(getActivity(), 36);
		allHeight = rows*h;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				allWidth,allHeight);
		gridView.setLayoutParams(params);
		gridView.setColumnWidth(itemWidth);
		//gridView.setHorizontalSpacing(10);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setNumColumns(columns);
	}
	
	class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			convertView = LayoutInflater.from(getActivity()).inflate(R.layout.view_product_detail_size, null);;
			TextView textView = (TextView) convertView
					.findViewById(R.id.tv_product_detail_size);
			String str = dataList.get(position);
			textView.setText(str);
			return convertView;
		}
	}

	private void getProductDetailSize() {
		imgUrl = new ArrayList<String>();
		String url = Constant.HOST + "/product/size/list.do?id=" + productDetailId;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//Log.i("sign", response);
						 String responseData = JsonUtil.getJsonValueByKey(response, "data");
						onResponseForProductList(responseData);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());

					}
				});
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(request);
	}

	ArrayList<String> imgUrl;
	private void onResponseForProductList(String response) {
		if(getActivity() == null){
			return;
		}
		String jsonSize = JsonUtil.getJsonValueByKey(response, "sizeStandardDetailList");
		ArrayList<String> arrRow = (ArrayList<String>) JsonUtil.toJsonStrList(jsonSize);
		if(!dataList.isEmpty()){
			dataList.clear();
		}
		for (String json : arrRow) {
			try {
				JSONObject j = new JSONObject(json);
				if(!dataColumns.isEmpty()){
					dataColumns.clear();
				}
				if(j.getString("size") != null && !j.getString("size").equals("")){
					dataColumns.add(j.getString("size"));
				}
				if(j.getString("p1") != null && !j.getString("p1").equals("")){
					dataColumns.add(j.getString("p1"));
				}
				if(j.getString("p2") != null && !j.getString("p2").equals("")){
					dataColumns.add(j.getString("p2"));
				}
				if(j.getString("p3") != null && !j.getString("p3").equals("")){
					dataColumns.add(j.getString("p3"));
				}
				if(j.getString("p4") != null && !j.getString("p4").equals("")){
					dataColumns.add(j.getString("p4"));
				}
				if(j.getString("p5") != null && !j.getString("p5").equals("")){
					dataColumns.add(j.getString("p5"));
				}
				if(j.getString("p6") != null && !j.getString("p6").equals("")){
					dataColumns.add(j.getString("p6"));
				}
				
				if(j.getString("p7") != null && !j.getString("p7").equals("")){
					dataColumns.add(j.getString("p7"));
				}
				if(j.getString("p8") != null && !j.getString("p8").equals("")){
					dataColumns.add(j.getString("p8"));
				}
				dataList.addAll(dataColumns);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		initGridView();
	}
	
	
}
