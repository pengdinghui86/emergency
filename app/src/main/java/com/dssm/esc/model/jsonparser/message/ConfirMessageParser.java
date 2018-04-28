package com.dssm.esc.model.jsonparser.message;

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
 * 确认消息解析
 * 
 * @author zsj
 * 
 */
public class ConfirMessageParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public ConfirMessageParser(String msgType,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(msgType);
	}



	/**
	 * 发送请求
	 * 
	 * @param msgType
	 *            app消息类型（1：任务通知，2：系统通知，3：我的消息，4：紧急通知）
	 */

	public void request( final String msgType) {

		AjaxParams params = new AjaxParams();
		if (msgType != null) {
			// id 消息id
			params.put("msgType", msgType);
		}
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.CONFREMSG, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(msgType);
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
				Log.i("ConfirMessageParser", "ConfirMessageParser" + t);
				map = ConfirMesgParser(t);
				Log.i("ConfirMessageParser", "ConfirMessageParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

			}

		});

	}

	/**
	 * 确认消息解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> ConfirMesgParser(String t) {
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