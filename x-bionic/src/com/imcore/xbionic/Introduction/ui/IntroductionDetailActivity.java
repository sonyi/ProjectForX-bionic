package com.imcore.xbionic.introduction.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class IntroductionDetailActivity extends Activity{
	private ImageView mImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduction_detail);
		mImg = (ImageView) findViewById(R.id.iv_introduction_detail_img);
		
		String index = getIntent().getStringExtra("introductionIndex");
		if(index.equals("aware")){
			mImg.setImageResource(R.drawable.ic_introduction_awareingtext);
		}else if(index.equals("prototype")){
			mImg.setImageResource(R.drawable.ic_introduction_bionicprototypetex);
		}
	}

}
