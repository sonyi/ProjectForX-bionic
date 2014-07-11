package com.imcore.xbionic.product.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;

public class ProductMainActivity extends Activity {
	ExpandableListView expandableListView;
	private ArrayList<String> groups;
	ListViewAdapter treeViewAdapter;

	public int[] gro = {R.drawable.ic_priduct_main_upbackground,R.drawable.ic_priduct_main_downbackground};
	public String[][] child = { { "" }, { "" }};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_main);
		init();
	}


	private void init() {
		treeViewAdapter = new ListViewAdapter(this,ListViewAdapter.PaddingLeft >> 1);
		expandableListView = (ExpandableListView) findViewById(R.id.exp_product_main_list);

		List<ListViewAdapter.TreeNode> treeNode = treeViewAdapter.GetTreeNode();
		for (int i = 0; i < gro.length; i++) {
			ListViewAdapter.TreeNode node = new ListViewAdapter.TreeNode();
			node.parent = gro[i];
			for (int ii = 0; ii < child[i].length; ii++) {
				node.childs.add(child[i][ii]);
			}
			treeNode.add(node);
		}

		treeViewAdapter.UpdateTreeNode(treeNode);
		expandableListView.setAdapter(treeViewAdapter);
	}
	
}
