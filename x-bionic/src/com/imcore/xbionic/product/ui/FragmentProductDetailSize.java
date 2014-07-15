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
import com.imcore.xbionic.util.JsonUtil;

public class FragmentProductDetailSize extends Fragment{
	View view;
	private GridView gridView;
	private LayoutInflater inflater;
	private List<String> dataList = new ArrayList<String>();
	ArrayList<String> arrRow;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_product_detail_size, null);
		
		getProductDetailSize();
		
		return view;
	}

	private void initGridView() {
		LayoutInflater inflater;
		gridView = (GridView) view.findViewById(R.id.gri_Pro_det_size);
		
		
		inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		gridView.setAdapter(new GridViewAdapter());
		int size = dataList.size();
		int row = arrRow.size();
		
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int allWidth = (int) (70 * 14 * density);
		int itemWidth = (int) (70 * density);
		int allHeight = row*85;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				allWidth, allHeight);
		gridView.setLayoutParams(params);
		gridView.setColumnWidth(itemWidth);
		//gridView.setHorizontalSpacing(10);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setNumColumns(14);
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
		String url = Constant.HOST + "/product/size/list.do?id=267";
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

	ArrayList<String> imgUrl;

	private void onResponseForProductList(String response) {
		String jsonSize = JsonUtil.getJsonValueByKey(response, "sizeStandardDetailList");
		arrRow = (ArrayList<String>) JsonUtil.toJsonStrList(jsonSize);
		if(!dataList.isEmpty()){
			dataList.clear();
		}
		dataList.add("ID");
		dataList.add("StandardId");
		dataList.add("Size");
		dataList.add("P1");
		dataList.add("P2");
		dataList.add("P3");
		dataList.add("P4");
		dataList.add("P5");
		dataList.add("P6");
		dataList.add("P7");
		dataList.add("P8");
		dataList.add("P9");
		dataList.add("P10");
		dataList.add("P11");
		for (String json : arrRow) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				dataList.add(jsonObject.getString("id") +"");
				dataList.add(jsonObject.getString("sizeStandardId") +"");
				dataList.add(jsonObject.getString("size") +"");
				dataList.add(jsonObject.getString("p1") +"");
				dataList.add(jsonObject.getString("p2") +"");
				dataList.add(jsonObject.getString("p3") +"");
				dataList.add(jsonObject.getString("p4") +"");
				dataList.add(jsonObject.getString("p5") +"");
				dataList.add(jsonObject.getString("p6") +"");
				dataList.add(jsonObject.getString("p7") +"");
				dataList.add(jsonObject.getString("p8") +"");
				dataList.add(jsonObject.getString("p9") +"");
				dataList.add(jsonObject.getString("p10") +"");
				dataList.add(jsonObject.getString("p11") +"");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//Log.i("sign", json);
		}
		initGridView();
	}
	
	
}
