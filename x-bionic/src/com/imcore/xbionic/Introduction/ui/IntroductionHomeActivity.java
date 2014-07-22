package com.imcore.xbionic.Introduction.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class IntroductionHomeActivity extends Activity implements OnClickListener{
	private ImageView mAware,mPrototype,mBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduction_home);
		mAware = (ImageView) findViewById(R.id.iv_introduction_aware);
		mPrototype = (ImageView) findViewById(R.id.iv_introduction_prototype);
		mBack = (ImageView) findViewById(R.id.iv_introduction_back);
		mBack.setOnClickListener(this);
		mAware.setOnClickListener(this);
		mPrototype.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.iv_introduction_aware){
			Intent intent = new Intent(this,IntroductionDetailActivity.class);
			intent.putExtra("introductionIndex", "aware");
			startActivity(intent);
			
		}else if(v.getId() == R.id.iv_introduction_prototype){
			Intent intent = new Intent(this,IntroductionDetailActivity.class);
			intent.putExtra("introductionIndex", "prototype");
			startActivity(intent);
		}else if(v.getId() == R.id.iv_introduction_back){
			finish();
		}
	}
}
