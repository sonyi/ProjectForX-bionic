package com.imcore.xbionic.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityLogin;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;

public class LoadingActivity extends Activity {
	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar_loading_animation);

		initIntent();

	}

	private void initIntent() {
		// 判断用户是否是第一次登入
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		boolean isLoginFristsp = sp.getBoolean("isLoginFirst", true);
		Intent intent = null;
		if (isLoginFristsp) {
			intent = new Intent(this, InstructorActivtity.class);
		} else {
			intent = new Intent(this, HomeActivityLogin.class);
		}
		
		

		final Intent it = intent; // 你要转向的Activity
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			int i = 1;
			@Override
			public void run() {
				
				while (i < 100) {
					i++;
					mProgressBar.setProgress(i*2);
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				 LoadingActivity.this.finish();
				 startActivity(it); // 执行
			}
		};
		timer.schedule(task, 0); // 
	}

	private void doLogin() {
		final String userName = "";
		final String password = "";

		String url = Constant.HOST + "/passport/login";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", userName);
				params.put("password", password);

				return params;
			}
		};

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
