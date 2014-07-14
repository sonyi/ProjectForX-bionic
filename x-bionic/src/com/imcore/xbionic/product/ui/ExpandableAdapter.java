package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductCatagory;
import com.imcore.xbionic.model.ProductItem;
import com.imcore.xbionic.util.DisplayUtil;
import com.imcore.xbionic.util.ToastUtil;

public class ExpandableAdapter extends BaseExpandableListAdapter {
	private ArrayList<ProductCatagory> mProductGroups;
	private HashMap<String, ArrayList<ProductItem>> mProductItems;
	private int[] mGroupsImg = { R.drawable.ic_product_main_upbackground,
			R.drawable.ic_product_main_downbackground };
	private Context context;
	private GridViewForItem mGridViewForItem;
	private int[] child = {1};
	public static int ItemHeight;//每项的高度
	public static int ItemWidth;
	
	
	ExpandableAdapter(Context context,
			ArrayList<ProductCatagory> mProductGroups,
			HashMap<String, ArrayList<ProductItem>> mProductItems) {
		this.context = context;
		this.mProductGroups = mProductGroups;
		this.mProductItems = mProductItems;
		
		int height = DisplayUtil.getScreenHeight(context);
		ItemHeight = (height - DisplayUtil.dip2Px(context, 50))/2;
		ItemWidth = DisplayUtil.getScreenWidth(context);	
	}

	@Override
	public int getGroupCount() {
		return mProductGroups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//return groupPosition;
		return child.length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mProductGroups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child[0];
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
//		View view = convertView;
//		GroupViewHolder gvh;
//		if (view == null) {
//			gvh = new GroupViewHolder();
//			view = LayoutInflater.from(context).inflate(
//					R.layout.view_product_groups, parent, false);
//			
//			gvh.imgView = (ImageView) view
//					.findViewById(R.id.iv_product_group_img);
			
			ImageView view = getImageView(context);
			view.setImageResource(mGroupsImg[groupPosition]);
			
//			view.setTag(gvh);
//		} else {
//			gvh = (GroupViewHolder) view.getTag();
//		}
//		gvh.imgView.setImageResource(mGroupsImg[groupPosition]);
			
		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null){
			view = LayoutInflater.from(context).inflate(R.layout.view_product_item_gridview, null);
		}
		mGridViewForItem = (GridViewForItem) view.findViewById(R.id.gri_product_item);
		mGridViewForItem.setAdapter(new ItemAdapter(groupPosition));
		
		
		mGridViewForItem.setOnItemClickListener(new ItemOnClickListener(groupPosition));
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	
	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				context.getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.ic_launcher, R.drawable.ic_launcher);
		loader.get(url, listener, 400, 400);
	}

	
	private class ItemAdapter extends BaseAdapter{
		private int groupPosition;
		ItemAdapter(int groupPosition){
			this.groupPosition = groupPosition;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mProductItems.get(groupPosition+"").size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mProductItems.get(groupPosition+"").get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ChildViewHolder cvh;
			if (view == null) {
				cvh = new ChildViewHolder();
				view = LayoutInflater.from(context).inflate(
						R.layout.view_product_item_layout, parent, false);

				cvh.mImageView = (ImageView) view
						.findViewById(R.id.iv_product_item_image);
				cvh.textView = (TextView) view
						.findViewById(R.id.tv_product_item_text);
				view.setTag(cvh);
			} else {
				cvh = (ChildViewHolder) view.getTag();
			}
			setImag(cvh.mImageView, Constant.IMAGE_HOST + mProductItems.get(groupPosition+"").get(position).imageUrl+ "_L.png") ;
			cvh.textView.setText(mProductItems.get(groupPosition+"").get(position).name);
			return view;
		}
	}
	
	
	class GroupViewHolder {
		ImageView imgView;
	}

	class ChildViewHolder {
		ImageView mImageView;
		TextView textView;
	}

	private ImageView getImageView(Context context) {
		LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(ItemWidth, ItemHeight);
		ImageView imgView = new ImageView(context);
		imgView.setScaleType(ImageView.ScaleType.FIT_XY);
		imgView.setLayoutParams(lay);
		return imgView;
	}
	

	private class ItemOnClickListener implements OnItemClickListener {
		private int groupPosition;
		ItemOnClickListener(int groupPosition){
			this.groupPosition = groupPosition;
			
		}
		

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String navId = mProductGroups.get(groupPosition).code+"";
			String subNavId = mProductItems.get(groupPosition+"").get(position).id+"";
//			ToastUtil.showToast(context, navId +"------"+ subNavId);
			Intent intent = new Intent(context,ProductListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("navId", navId);
			bundle.putString("subNavId", subNavId);
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	}
}
