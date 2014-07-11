package com.imcore.xbionic.product.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;

public class GetInfoFormInternet {
	private Context context;
	private ArrayList<String> groups;
	private ArrayList<String> itemName;
	private ArrayList<String> itemUrl;
	
	GetInfoFormInternet(Context context){
		this.context = context;
	}
	
	/**
	 * 获取主项目格式
	 * @return
	 */
	public ArrayList<String> getGroups(){
		groups = new ArrayList<String>();
		first();
		return groups;
	}
	
	/**
	 * 获取子项目名称
	 * @param code
	 * @return
	 */
	public ArrayList<ArrayList<String>> getItemInfo(String code){
		itemName = new ArrayList<String>();
		itemUrl = new ArrayList<String>();
		second(code);
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		data.add(itemName);
		data.add(itemUrl);
		return data;
	}
	

	private void first() {
		String url = Constant.HOST + "/category/list.do";
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 Log.i("sign", response);
						 responseForFirst(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});
		RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
	}

	private void responseForFirst(String response) {
		
		try {
			JSONArray ja = new JSONArray(response);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject j = ja.getJSONObject(i);
				int code = j.getInt("code");
				groups.add(code + "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.i("sign", groups.toString());
		//second(groups.get(0));
	}
	
	private void second(String code) {
		String url = Constant.HOST + "/category/list.do?navId=" + code;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 Log.i("sign", response);
						 responseForSecond(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});
		RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
	}

	private void responseForSecond(String response) {
		try {
			JSONArray ja = new JSONArray(response);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject j = ja.getJSONObject(i);
				itemName.add(j.getString("name"));
				itemUrl.add(Constant.IMAGE_HOST + j.getString("imageUrl") + "_L.png");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
