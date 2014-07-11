package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.util.DisplayUtil;

public class ListViewAdapter extends BaseExpandableListAdapter implements
		OnItemClickListener {
	public static int ItemHeight;//每项的高度
	public static int ItemWidth;
	public static final int PaddingLeft = 0;
	private int myPaddingLeft;
	
	private MyGridView toolbarGrid;



	private List<TreeNode> treeNodes = new ArrayList<TreeNode>();

	private Context parentContext;
	private LayoutInflater layoutInflater;

	static public class TreeNode {
		Object parent;
		List<Object> childs = new ArrayList<Object>();
	}

	public ListViewAdapter(Context view, int myPaddingLeft) {
		parentContext = view;
		this.myPaddingLeft = myPaddingLeft;
		getData();
		int height = DisplayUtil.getScreenHeight(view);
		ItemHeight = (height - DisplayUtil.dip2Px(parentContext, 76))/2;
		ItemWidth = DisplayUtil.getScreenWidth(view);	
	}

	public List<TreeNode> GetTreeNode() {
		return treeNodes;
	}

	public void UpdateTreeNode(List<TreeNode> nodes) {
		treeNodes = nodes;
	}

	public void RemoveAll() {
		treeNodes.clear();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return treeNodes.get(groupPosition).childs.get(childPosition);
	}

	public int getChildrenCount(int groupPosition) {
		return treeNodes.get(groupPosition).childs.size();
	}

	static public ImageView getImageView(Context context) {
		LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(ItemWidth, ItemHeight);
		ImageView imgView = new ImageView(context);
		imgView.setScaleType(ImageView.ScaleType.FIT_XY);
		imgView.setLayoutParams(lay);
		
		return imgView;
	}

	/**
	 * 可自定义ExpandableListView
	 */
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			layoutInflater = (LayoutInflater) parentContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.view, null);
			toolbarGrid = (MyGridView) convertView
					.findViewById(R.id.GridView_toolbar);
			toolbarGrid.setNumColumns(4);//设置每行列数
			toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
			toolbarGrid.setHorizontalSpacing(2);// 水平间隔
			
			if(groupPosition == 0){
				toolbarGrid.setAdapter(new gridAdapter(itemInfo));
			}else if(groupPosition == 1){
				toolbarGrid.setAdapter(new gridAdapter(itemIn));
			}
			
			
			//toolbarGrid.setAdapter(new gridAdapter(data.get(groupPosition)));
		

			toolbarGrid.setOnItemClickListener(this);
			Log.i("position", groupPosition+"");
		}
		return convertView;
	}
	
	ArrayList<ArrayList<ArrayList<String>>> data;
	ArrayList<ArrayList<String>> itemInfo;
	ArrayList<ArrayList<String>> itemIn;
	
	private void getData(){
//		GetInfoFormInternet gi = new GetInfoFormInternet(parentContext);
//		ArrayList<String> groups = gi.getGroups();
//		Log.i("gi", groups.toString());
		
//		for(String s : groups){
			GetInfoFormInternet getInfo = new GetInfoFormInternet(parentContext);
			itemInfo = getInfo.getItemInfo("100001");
//			data.add(itemInfo);
//		}
			
			GetInfoFormInternet getIn = new GetInfoFormInternet(parentContext);
			itemIn = getIn.getItemInfo("100002");
	}
	
	
	
	
	
//	GetInfoFormInternet getIn = new GetInfoFormInternet(parentContext);
//	private ArrayList<ArrayList<String>> itemIn = getIn.getItemInfo("100002");
	
	private class gridAdapter extends BaseAdapter {
		private ArrayList<String> itemName;
		ArrayList<String> itemUrl;
		gridAdapter(ArrayList<ArrayList<String>> itemInfo){
			itemName = itemInfo.get(0);
			itemUrl = itemInfo.get(1);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if(view == null){
				view = LayoutInflater.from(parentContext).inflate(R.layout.item_menu, null);
				vh = new ViewHolder();
				vh.img = (ImageView) view.findViewById(R.id.item_image);
				vh.text = (TextView) view.findViewById(R.id.item_text);
				view.setTag(vh);
			}else{
				vh = (ViewHolder) view.getTag();
			}
			//vh.img.setImageBitmap(itemBitmap.get(position));
			vh.text.setText(itemName.get(position));
			getImag(vh.img, itemUrl.get(position));
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemName.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemName.size();
		}
		
		class ViewHolder{
			ImageView img;
			TextView text;
		}
	};
	
	
	private void getImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				parentContext.getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_launcher, R.drawable.ic_launcher);
		loader.get(url, listener, 400, 400);
	}

	/**
	 * 可自定义list
	 */
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ImageView imgView = getImageView(this.parentContext);
		imgView.setImageResource(Integer.parseInt(getGroup(groupPosition).toString()));
		return imgView;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public Object getGroup(int groupPosition) {
		return treeNodes.get(groupPosition).parent;
	}

	public int getGroupCount() {
		return treeNodes.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(parentContext, "当前选中的是:" + position,
				Toast.LENGTH_SHORT).show();

	}
}