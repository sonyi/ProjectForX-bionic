package com.imcore.xbionic.product.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imcore.xbionic.R;

public class FragmentProductDetailInfo extends Fragment{
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.view_product_detail_img, null);
		return view;
	}
	

}
