package com.imcore.xbionic.expertstory.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.imcore.xbionic.R;

public class ExperStoryDtailActivity extends Activity implements OnClickListener{
	private WebView mText;
	private ImageView mBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experstory_detail);
		String detail = getIntent().getStringExtra("experStoryDetail");
		//Log.i("sign", detail);
		mText = (WebView) findViewById(R.id.tv_exper_detail_text);
		mText.loadData(detail, "text/html; charset=UTF-8", null);
		mBack = (ImageView) findViewById(R.id.iv_experstory_detail_back);
		mBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.iv_experstory_detail_back){
			finish();
		}
		
	}
}
