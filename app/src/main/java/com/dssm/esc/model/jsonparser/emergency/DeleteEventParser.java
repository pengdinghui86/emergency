package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;


/**
 * 事件删除解析
 * 
 * @author zsj
 * 
 */
public class DeleteEventParser {
	public Map<String, String> map;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public DeleteEventParser(String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(id);
	}

	/**
	 * 发送请求
	 * 
	
	 * @param id
	 *            事件id
	 
	 */
	public void request(final String id) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.DELETEEVENT);
		if (id != null ) {
			params.addParameter("ids", id);
		}
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("DeleteEventParser", "DeleteEventParser" + t);
				map = deleteEventParser(t);
				Log.i("DeleteEventParser", "DeleteEventParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

				String responseMsg = "";
				String errorResult = ex.toString();
				if (ex instanceof HttpException) { //网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					if(responseCode == 518) {
						Utils.getInstance().relogin();
						request(id);
					}
					responseMsg = httpEx.getMessage();
					errorResult = httpEx.getResult();
				} else { //其他错误

				}
				OnEmergencyCompleterListener.onEmergencyParserComplete(null, errorResult);
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}

		});

	}

	/**
	 * 事件解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 * 
	 */
	public Map<String, String> deleteEventParser(String t) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject(t);
			if (t.contains("success")) {
				map.put("success", object.getString("success"));
				map.put("message", object.getString("message"));
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

		return map;
	}

}
