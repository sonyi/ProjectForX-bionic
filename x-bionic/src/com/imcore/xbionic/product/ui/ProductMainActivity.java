package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductCatagory;
import com.imcore.xbionic.model.ProductItem;
import com.imcore.xbionic.util.JsonUtil;

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
		mBack = (ImageView) findViewById(R.id.iv_product_main_back);
		mBack.setOnClickListener(this);
		mProductItems = new HashMap<String, ArrayList<ProductItem>>();
		getGroups();// 网络加载数据

	}

	private void getGroups() {
		String url = Constant.HOST + "/category/list.do";
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//Log.i("sign", response);
						responseForGroups(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void responseForGroups(String response) {
		mProductGroups = (ArrayList<ProductCatagory>) JsonUtil.toObjectList(
				response, ProductCatagory.class);
		for (ProductCatagory p : mProductGroups) {
			getItems(p);
		}
	}

	private void getItems(ProductCatagory p) {
		String url = Constant.HOST + "/category/list.do?navId=" + p.code;
		final ProductCatagory pro = p;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						ArrayList<ProductItem> arr = (ArrayList<ProductItem>) JsonUtil
								.toObjectList(response, ProductItem.class);
						mProductItems.put(mProductGroups.indexOf(pro) + "", arr);
						// Log.i("sign", mProductGroups.indexOf(pro) + "-------"
						// + arr.toString());
						if (mProductItems.size() == mProductGroups.size()) {
							mExpandableListView
									.setAdapter(new ExpandableAdapter(
											ProductMainActivity.this,
											mProductGroups, mProductItems));// 加载成功后，初始化适配器
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.iv_product_main_back){
			finish();
			return;
		}
	}

}
