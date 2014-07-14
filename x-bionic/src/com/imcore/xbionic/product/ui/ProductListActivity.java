package com.imcore.xbionic.product.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityLogin;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.login.ui.XLoginActivity;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListActivity extends Activity {
	private GridView mGridView;
	private int[] mProductListGroup = { 1, 2, 3, 4, 5 };
	private String navId;
	private String subNavId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		mGridView = (GridView) findViewById(R.id.gri_product_list_layout);
		Bundle bundle = this.getIntent().getExtras();
		navId = bundle.getString("navId");
		subNavId = bundle.getString("subNavId");
		// ToastUtil.showToast(this, navId +"------"+ subNavId);
		getProductListInfo();
		mGridView.setAdapter(new ProductListAdapter());

	}

	private void getProductListInfo() {
		String url = Constant.HOST + "/category/products.do?navId=" + navId
				+ "&subNavId=" + subNavId + "&offset=0&fetchSize=10";
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						Log.i("sign", response);
						//onResponseForLogin(response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForLogin(String response) {
		String userAddress = JsonUtil
				.getJsonValueByKey(response, "userAddress");
		String userId = null;
		JSONObject jo;
		try {
			jo = new JSONObject(userAddress);
			userId = jo.getString("userId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String token = JsonUtil.getJsonValueByKey(response, "token");
		String firstName = JsonUtil.getJsonValueByKey(response, "firstName");
		String lastName = JsonUtil.getJsonValueByKey(response, "lastName");
		String userName = lastName + firstName;

		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		Editor editor = sp.edit();// 获取编辑器
		editor.putString("userId", userId);
		editor.putString("token", token);
		editor.putString("userName", userName);
		editor.putBoolean("isLogin", true);
		editor.commit();// 提交修改

		Intent intent = new Intent(this, HomeActivityLogin.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 清除栈内其他activity
		startActivity(intent);

	}

	private class ProductListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mProductListGroup.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mProductListGroup[position];
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
				view = getLayoutInflater().inflate(R.layout.view_product_list,
						null);
				vh = new ViewHolder();
				vh.img = (ImageView) view
						.findViewById(R.id.iv_product_list_img);
				vh.tvDec = (TextView) view
						.findViewById(R.id.tv_product_list_dec);
				vh.tvPrice = (TextView) view
						.findViewById(R.id.tv_product_list_price);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}

			return view;
		}

		class ViewHolder {
			ImageView img;
			TextView tvDec;
			TextView tvPrice;
		}

	}

}
