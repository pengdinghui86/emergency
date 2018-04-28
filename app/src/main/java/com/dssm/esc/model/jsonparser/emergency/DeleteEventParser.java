package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

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
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public DeleteEventParser(String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
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

		AjaxParams params = new AjaxParams();
		

		if (id != null ) {

			params.put("ids", id);
			
		}
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.DELETEEVENT, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(id);
					}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				MyCookieStore.setcookieStore(finalHttp);
				Log.i("DeleteEventParser", "DeleteEventParser" + t);
				map = deleteEventParser(t);
				Log.i("DeleteEventParser", "DeleteEventParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

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
