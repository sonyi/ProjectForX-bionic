package com.imcore.xbionic.login.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PhoneRegisterCodeActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_register_code);
		findViewById(R.id.iv_pho_reg_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PhoneRegisterCodeActivity.this.finish();
			}
		});
	}

}
