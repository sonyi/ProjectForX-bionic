package com.imcore.xbionic.home.ui;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.Const;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

public class HomeActivityUnLogin extends FragmentActivity {
	private Fragment mFragmentForUnLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initHomeFragment();

	}

	// 初始化未登录的主界面
	private void initHomeFragment() {
		mFragmentForUnLogin = new HomeFragmentHost();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putBoolean(Const.IS_LOGIN_KEY, Const.UN_LOGIN);
		mFragmentForUnLogin.setArguments(bundle);
		ft.add(R.id.home_activity_unlogin_fragment, mFragmentForUnLogin);
		ft.commit();
	}
	
	
	/** 
	 * 捕捉back 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
	        ExitDialog(HomeActivityUnLogin.this).show();  
	        return true;  
	    }  
	      
	    return super.onKeyDown(keyCode, event);  
	} 
	
	/** 
	 * 提示退出系统 
	 * @param context 
	 * @return 
	 */  
	   private Dialog ExitDialog(Context context) {  
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);  
	    builder.setIcon(R.drawable.ic_launcher);  
	    builder.setTitle("系统信息");  
	    builder.setMessage("确定要退出程序吗?");  
	    builder.setPositiveButton("确定",  
	            new DialogInterface.OnClickListener() {  
	                public void onClick(DialogInterface dialog, int whichButton) {           
	                	//System.exit(0);
	                	Intent i = new Intent(Intent.ACTION_MAIN);
	                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    i.addCategory(Intent.CATEGORY_HOME);
	                    startActivity(i);
	                }  
	            });  
	    builder.setNegativeButton("取消",  
	            new DialogInterface.OnClickListener() {  
	                public void onClick(DialogInterface dialog, int whichButton) {  
	                }  
	            });  
	    return builder.create();  
	}  
}
