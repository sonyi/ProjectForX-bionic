package com.imcore.xbionic.product.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.imcore.xbionic.R;

public class ProductDetailsActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment ImgFragment = new FragmentProductDetailImg();
		ft.add(R.id.fragment_product_detail_img, ImgFragment).addToBackStack(null).commit();
	}



}
