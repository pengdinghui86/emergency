package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
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
	private final WeakReference<OnDataCompleterListener> wr;

	public PlanAuthParser(String id, String planAuthOpition, String planName,
			String planResName,String planResType,String planId,String planStarterId,String submitterId, OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(id, planAuthOpition,planName,planResName,planResType, planId, planStarterId, submitterId);
	}

	/**
	 * 发送请求
	 * 
	 * @param id

	 */
	public void request(final String id,final String planAuthOpition,final String planName,
			final String planResName,final String planResType,final String planId,final String planStarterId,final String submitterId) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.PLANAUTH);
		//增加session
		if(!MySharePreferencesService.getInstance(
				DemoApplication.getInstance().getApplicationContext()).getcontectName(
				"JSESSIONID").equals("")) {
			StringBuilder sbSession = new StringBuilder();
			sbSession.append("JSESSIONID").append("=")
					.append(MySharePreferencesService.getInstance(
							DemoApplication.getInstance().getApplicationContext()).getcontectName(
							"JSESSIONID")).append("; path=/; domain=")
					.append(MySharePreferencesService.getInstance(
							DemoApplication.getInstance().getApplicationContext()).getcontectName(
							"DOMAIN"));
			params.addHeader("Cookie", sbSession.toString());
		}
		if (id != null) {
			// id 事件id
			// planAuthOpition 预案授权处置意见
			params.addParameter("id", id);
			params.addParameter("planAuthOpition", planAuthOpition);
			params.addParameter("planName", planName);
			params.addParameter("planResName", planResName);
			params.addParameter("planResType", planResType);
			params.addParameter("planId", planId);
			params.addParameter("planStarterId", planStarterId);
			params.addParameter("submitterId", submitterId);
		}
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("PlanAuthParser", "PlanAuthParser" + t);
				map = planAuthParse(t);
				Log.i("PlanAuthParser", "PlanAuthParser" + map);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(map,
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
						request(id,  planAuthOpition,  planName,
								planResName, planResType, planId, planStarterId, submitterId);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(id,  planAuthOpition,  planName,
							planResName, planResType, planId, planStarterId, submitterId);
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(null, errorResult);
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
