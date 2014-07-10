package com.imcore.xbionic.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;

import com.imcore.xbionic.R;
import com.imcore.xbionic.home.ui.HomeActivityLogin;
import com.imcore.xbionic.product.ui.ProductMainActivity;

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
		boolean isFirstUse = sp.getBoolean("isFirstUse", true);

		Intent intent = null;
//		if (isFirstUse) {
//			intent = new Intent(this, InstructorActivtity.class);
//		} else {
//			intent = new Intent(this, HomeActivityLogin.class);
//		}
		
		//intent = new Intent(this, LoginMainActivity.class);
		intent = new Intent(this, ProductMainActivity.class);
		

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

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
