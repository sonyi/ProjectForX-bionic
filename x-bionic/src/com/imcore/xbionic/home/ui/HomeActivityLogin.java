package com.imcore.xbionic.home.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.Const;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeActivityLogin extends SlidingFragmentActivity {
	private Fragment mFragmentForUnLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initHomeFragment();
		intiMenuFragment();

		// 注册打开侧拉菜单监听事件
		IntentFilter filter = new IntentFilter(Const.OPEN_OR_CLOSE_DRAWERLAYOUT);
		registerReceiver(receiverForDrawer, filter);
	}

	private void intiMenuFragment() {
		// 设置滑动菜单视图界面
		setBehindContentView(R.layout.fragment_home_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_home_menu, new HomeFragmentMenu())
				.commit();

		// 设置滑动菜单的属性值
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
		getSlidingMenu().setFadeDegree(0.35f);
		getSlidingMenu().setMode(SlidingMenu.LEFT);
	}

	// 初始化未登录的主界面
	private void initHomeFragment() {
		mFragmentForUnLogin = new HomeFragmentHost();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putBoolean(Const.IS_LOGIN_KEY, Const.LOGIN);
		mFragmentForUnLogin.setArguments(bundle);
		ft.add(R.id.home_activity_unlogin_fragment, mFragmentForUnLogin);
		ft.commit();
	}

	// 接收menu按钮点击事件，打开侧拉菜单
	private BroadcastReceiver receiverForDrawer = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Const.OPEN_OR_CLOSE_DRAWERLAYOUT)) {
				toggle();
			}
		}
	};
	
	
	/** 
	 * 捕捉back 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
	        ExitDialog(HomeActivityLogin.this).show();  
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
	   // builder.setIcon(R.drawable.ic_launcher);  
	  //  builder.setTitle("系统信息");  
	    builder.setMessage("确定要退出程序吗?");  
	    builder.setPositiveButton("确定",  
	            new DialogInterface.OnClickListener() {  
	                public void onClick(DialogInterface dialog, int whichButton) {           
	                   // System.exit(0);
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
	   
	   
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiverForDrawer);
	}
}
