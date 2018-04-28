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
 * 任务切换数据解析
 * 
 * @author zsj
 * 
 */
public class SwitchOverParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public SwitchOverParser(String id, String planInfoId, String status,
			String message, OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(id, planInfoId, status, message);
	}

	/**
	 * 发送请求
	 * 
	 * @param id

	 */
	public void request(final String id,final String planInfoId,final String status,
			final String message) {

		AjaxParams params = new AjaxParams();
		if (id != null) {
			// id 流程节点编号
			// planInfoId 预案执行编号
			// status 完成状态
			// message 提交信息
			params.put("id", id);
			params.put("planInfoId", planInfoId);
			params.put("status", status);
			params.put("message", message);

		}
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.SWITHOVER, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request( id, planInfoId, status,
							message);
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
				Log.i("SwitchOverParser", "SwitchOverParser" + t);
				map = planChangeParse(t);
				Log.i("SwitchOverParser", "SwitchOverParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

			}

		});

	}

	/**
	 * 任务切换解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> planChangeParse(String t) {
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
