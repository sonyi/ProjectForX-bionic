package com.imcore.xbionic.http;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.imcore.xbionic.util.JsonUtil;

public class DataRequest extends Request<String> {
	private Response.Listener<String> mListener;
	
	public DataRequest(int method,String url,Response.Listener<String> listener,Response.ErrorListener errorListener) {
	   super(method, url, errorListener);
	   mListener = listener;
	}
	
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
			//Log.i("sign", json);
			int status = Integer.parseInt(JsonUtil.getJsonValueByKey(json, "status"));
			if (status == 200) {
				String data = JsonUtil.getJsonValueByKey(json, "data");
				String total = JsonUtil.getJsonValueByKey(json, "total");
				JSONObject j = new JSONObject();
				j.put("total", total);
				j.put("data", data);
				//Log.i("sign", total);
				//Log.i("sign", j.toString());
				return Response.success(j.toString(), HttpHeaderParser.parseCacheHeaders(response));
			} else {
				String message = JsonUtil.getJsonValueByKey(json, "message");
				return Response.error(new VolleyError(message));
			}
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}
	
	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}
}
