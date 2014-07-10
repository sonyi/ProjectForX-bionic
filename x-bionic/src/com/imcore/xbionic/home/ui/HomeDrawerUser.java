package com.imcore.xbionic.home.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.login.ui.XLoginActivity;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeDrawerUser extends Fragment implements OnClickListener{
	private View mFragmentView;
	private ImageView mHeadImg,mModifyImg;
	private TextView mUserName;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFragmentView = inflater.inflate(R.layout.view_navi_drawer_userdetail, null);
		mHeadImg = (ImageView) mFragmentView.findViewById(R.id.iv_drawer_user_img);
		mModifyImg = (ImageView) mFragmentView.findViewById(R.id.iv_drawer_user_modify);
		mUserName = (TextView) mFragmentView.findViewById(R.id.tv_drawer_user_name);
		SharedPreferences sp = getActivity().getSharedPreferences("loginUser",Context.MODE_PRIVATE); // 私有数据
		String userName = sp.getString("userName", "用户,你好");
		mUserName.setText(userName + ",你好");
		
		mHeadImg.setOnClickListener(this);
		mModifyImg.setOnClickListener(this);
		
		return mFragmentView;
	}
	
	
	

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.iv_drawer_user_img || v.getId() == R.id.iv_drawer_user_modify){
			ToastUtil.showToast(getActivity(), "修改用户信息");
		}
	}
}
