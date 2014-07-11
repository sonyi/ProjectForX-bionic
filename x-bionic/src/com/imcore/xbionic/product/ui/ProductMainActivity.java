package com.imcore.xbionic.product.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imcore.xbionic.R;
import com.imcore.xbionic.product.ui.ListViewAdapter.TreeNode;

public class ProductMainActivity extends Activity {
	ExpandableListView expandableListView;

	ListViewAdapter treeViewAdapter;

	public int[] groups = {R.drawable.ic_priduct_main_upbackground,R.drawable.ic_priduct_main_downbackground};
	//public String[] groups = { "列表1", "列表2" };

	public String[][] child = { { "" }, { "" }};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_main);

		treeViewAdapter = new ListViewAdapter(this,
				ListViewAdapter.PaddingLeft >> 1);
		expandableListView = (ExpandableListView) findViewById(R.id.exp_product_main_list);

		List<ListViewAdapter.TreeNode> treeNode = treeViewAdapter.GetTreeNode();
		for (int i = 0; i < groups.length; i++) {
			ListViewAdapter.TreeNode node = new ListViewAdapter.TreeNode();
			node.parent = groups[i];
			for (int ii = 0; ii < child[i].length; ii++) {
				node.childs.add(child[i][ii]);
			}
			treeNode.add(node);
		}

		treeViewAdapter.UpdateTreeNode(treeNode);
		expandableListView.setAdapter(treeViewAdapter);
	}
}
