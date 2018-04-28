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
 * 预案授权解析
 * 
 * @author zsj
 * 
 */
public class PlanAuthParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanAuthParser(String id, String planAuthOpition, String planName,
			String planResName,String planResType,String planId,String planStarterId,String submitterId, OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(id, planAuthOpition,planName,planResName,planResType, planId, planStarterId, submitterId);
	}

	/**
	 * 发送请求
	 * 
	 * @param id

	 */
	public void request(final String id,final String planAuthOpition,final String planName,
			final String planResName,final String planResType,final String planId,final String planStarterId,final String submitterId) {

		AjaxParams params = new AjaxParams();
		if (id != null) {
			// id 事件id
			// planAuthOpition 预案授权处置意见
			params.put("id", id);
			params.put("planAuthOpition", planAuthOpition);
			params.put("planName", planName);
			params.put("planResName", planResName);
			params.put("planResType", planResType);
			params.put("planId", planId);
			params.put("planStarterId", planStarterId);
			params.put("submitterId", submitterId);
		}
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.PLANAUTH, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request( id,  planAuthOpition,  planName,
							 planResName, planResType, planId, planStarterId, submitterId);
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
				Log.i("PlanAuthParser", "PlanAuthParser" + t);
				map = planAuthParse(t);
				Log.i("PlanAuthParser", "PlanAuthParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

			}

		});

	}

	/**
	 * 预案授权解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> planAuthParse(String t) {
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
