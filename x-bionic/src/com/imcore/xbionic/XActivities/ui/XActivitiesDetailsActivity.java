package com.imcore.xbionic.XActivities.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.XActivityMain;

public class XActivitiesDetailsActivity extends Activity implements OnClickListener{
	private XActivityMain x;
	private ImageView mBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xactivities_detail);
		x = getIntent().getParcelableExtra("xactivities");
		mBack = (ImageView) findViewById(R.id.iv_xactivities_detail_back);
		mBack.setOnClickListener(this);
		//Log.i("sign", x.toString());
		initWidget();
	}

	private void initWidget() {
		TextView title = (TextView) findViewById(R.id.tv_xactivities_title);
		TextView date = (TextView) findViewById(R.id.tv_xactivities_date);
		TextView address = (TextView) findViewById(R.id.tv_xactivities_address);
		TextView organizer = (TextView) findViewById(R.id.tv_xactivities_organizer);
		TextView signUpDeadLine = (TextView) findViewById(R.id.tv_xactivities_signUpDeadLine);
		TextView provinceId = (TextView) findViewById(R.id.tv_xactivities_provinceId);
		TextView content = (TextView) findViewById(R.id.tv_xactivities_content);
		ImageView img = (ImageView) findViewById(R.id.iv_xactivities_img);
		title.setText(x.title);
		String startDate = x.beginTime.substring(0, x.beginTime.indexOf("T"));
		String endDate = x.endTime.substring(0, x.endTime.indexOf("T"));
		date.setText(startDate + "-" + endDate);
		address.setText("地点：" + x.address);
		organizer.setText("发起人：" + x.organizer);
		String deadDate = x.signUpDeadLine.substring(0, x.signUpDeadLine.indexOf("T"));
		signUpDeadLine.setText("报名结束时间：" + deadDate);
		provinceId.setText(x.provinceId + "人参加");
		String s = x.content.replaceAll("<br />", "\r\n");
		String ss = s.replaceAll("<br/>", "\r\n");
		content.setText(ss);
		String url = Constant.IMAGE_ADDRESS + x.titleImageUrl + ".jpg";
		setImag(img, url);
	}

	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_product_img, R.drawable.ic_product_img);
		loader.get(url, listener, 400, 400);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.iv_xactivities_detail_back){
			finish();
		}
	}
}
