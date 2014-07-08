package xmu.software.acbuwa;

import xmu.software.acbuwa.adapter.MenuListAdapter;
import xmu.software.acbuwa.callback.SizeCallBackForMenu;
import xmu.software.acbuwa.ui.MenuHorizontalScrollView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ACBUWAPageActivity extends Activity {
	
	private MenuHorizontalScrollView scrollView;
	private ListView menuList;
	private View acbuwaPage;
	private Button menuBtn;
	private MenuListAdapter menuListAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		this.scrollView = (MenuHorizontalScrollView)findViewById(R.id.scrollView);
		this.menuListAdapter = new MenuListAdapter(this, 0);
		this.menuList = (ListView)findViewById(R.id.menuList);
		this.menuList.setAdapter(menuListAdapter);
		
		this.acbuwaPage = inflater.inflate(R.layout.acbuwa_page, null);
		this.menuBtn = (Button)this.acbuwaPage.findViewById(R.id.menuBtn);
		this.menuBtn.setOnClickListener(onClickListener);
		
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[]{leftView, acbuwaPage};
		this.scrollView.initViews(children, new SizeCallBackForMenu(this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
	}
	

	
	private OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			scrollView.clickMenuBtn();
		}
	};
}
