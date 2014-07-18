package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityLogin;
import com.imcore.xbionic.home.ui.HomeActivityUnLogin;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductCatagory;
import com.imcore.xbionic.model.ProductItem;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

public class ProductMainActivity extends Activity implements OnClickListener{
	private ExpandableListView mExpandableListView;
	private ArrayList<ProductCatagory> mProductGroups;
	private HashMap<String, ArrayList<ProductItem>> mProductItems;
	private ImageView mBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_main);
		mExpandableListView = (ExpandableListView) findViewById(R.id.exp_product_main_list);
		mProductItems = new HashMap<String, ArrayList<ProductItem>>();
		mBack = (ImageView) findViewById(R.id.iv_product_main_back);
		mBack.setOnClickListener(this);
		getGroupsInfo();

	}

	private void getGroupsInfo() {
		String url = Constant.HOST + "/category/list.do";
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						onResponseForLogin(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						//Log.i("sign", error.getMessage());
						ToastUtil.showToast(ProductMainActivity.this, "网络加载失败");
						finish();
					}
				});

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForLogin(String response) {

		mProductGroups = (ArrayList<ProductCatagory>) JsonUtil
				.toObjectList(response, ProductCatagory.class);
		if(mProductGroups == null || mProductGroups.size() == 0){
			ToastUtil.showToast(this, "网络加载失败");
			finish();
			return;
		}
		
		for(ProductCatagory p : mProductGroups){
			getItemsInfo(p);
		}
	}
	
	private void getItemsInfo(ProductCatagory p) {
		String url = Constant.HOST + "/category/list.do?navId=" + p.code;
		final ProductCatagory pro = p;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						ArrayList<ProductItem> a = (ArrayList<ProductItem>) JsonUtil.toObjectList(response, ProductItem.class);
						mProductItems.put(mProductGroups.indexOf(pro) + "", a);
						if(mProductItems.size() == mProductGroups.size()){
							initAdapter();
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
	
	private void initAdapter(){
		mExpandableListView.setAdapter(new ExpandableAdapter(this, mProductGroups, mProductItems));
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.iv_product_main_back){
			SharedPreferences sp = getSharedPreferences("loginUser",
					Context.MODE_PRIVATE); // 私有数据
			Intent intent;
			if(sp.getBoolean("isLogin", false)){
				intent = new Intent(this,HomeActivityLogin.class);
			}else{
				intent = new Intent(this,HomeActivityUnLogin.class);
			}
			startActivity(intent);
		}
	}
}
