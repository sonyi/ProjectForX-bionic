package com.imcore.xbionic.product.ui;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.login.ui.XLoginActivity;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentActivity extends Activity implements OnClickListener {
	private ImageView mBack;
	private EditText mCommentText, mCommentTitle;
	private TextView mSendComment;
	private RatingBar mRatingBar;
	private long productDetailId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_comment);
		mBack = (ImageView) findViewById(R.id.iv_product_comment_back);
		mCommentText = (EditText) findViewById(R.id.et_product_comment_text);
		mCommentTitle = (EditText) findViewById(R.id.et_product_comment_title);
		mSendComment = (TextView) findViewById(R.id.tv_pro_comment_sendcomment);
		mRatingBar = (RatingBar) findViewById(R.id.rat_pro_comment_ratingbar);
		mBack.setOnClickListener(this);

		productDetailId = getIntent().getLongExtra("productDetailId", 0);
		// ToastUtil.showToast(this, productDetailId+"");

		mSendComment.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_pro_comment_sendcomment) {
			addComment();
			return;
		} else if (v.getId() == R.id.iv_product_comment_back) {
			finish();
		}
	}

	private String userId;
	private String token;
	private String title;
	private String comment;
	private int star;

	private void addComment() {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		if (userId.equals("") || token.equals("")) {
			new AlertDialog.Builder(this)
					.setTitle("您还未登陆，请先登陆.....")
					.setPositiveButton("登陆",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent intent = new Intent(
											CommentActivity.this,
											XLoginActivity.class);
									intent.putExtra(Const.LOGIN_KEY,
											Const.LOGIN_AT_BUY_VALUE);
									startActivity(intent);
								}
							}).create().show();
			return;
		}
		title = mCommentTitle.getText().toString();
		comment = mCommentText.getText().toString();
		star = (int) mRatingBar.getRating();
		if (comment == null || comment.equals("")) {
			ToastUtil.showToast(this, "评论内容不能为空");
			return;
		}

		String url = Constant.HOST + "/product/comments/add.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (response != null) {
							// Log.i("sign", response);
							ToastUtil.showToast(CommentActivity.this, "发表成功");
							finish();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", userId);
				params.put("token", token);
				params.put("id", productDetailId + "");
				params.put("comment", comment);
				params.put("star", star + "");
				params.put("title", title);
				// Log.i("sign", productQuantity.id + "----" + sumBuy);
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

}
