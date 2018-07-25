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
 * 预案驳回解析
 * 
 * @author zsj
 * 
 */
public class PlanStarBohuiParser {
	public Map<String, String> map;
	private final WeakReference<OnDataCompleterListener> wr;

	public PlanStarBohuiParser(String planEveId, String planEveName,
			String submitterId, String eveType,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(planEveId, planEveName, submitterId, eveType);
	}

	/**
	 * 发送请求
	 * 
	 * @param planEveId
	 *            预案id
	 * @param planEveName
	 *            预案名称
	 * @param submitterId
	 *            事件提交人ID
	 * @param eveType
	 *            事件类型
	 */
	public void request(final String planEveId,final String planEveName,final String submitterId,
			final String eveType) {
//		planEveId	事件ID	
//		planEveName	事件名称	
//		submitterId	事件提交人	发送通知使用
//		eveType	事件类型	发送通知使用
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.PLANSTARD_BOHUI);
		params.setReadTimeout(60 * 1000);
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
		if (planEveId != null) {

			params.addParameter("planEveId", planEveId);
			params.addParameter("planEveName", planEveName);
			params.addParameter("submitterId", submitterId);
			params.addParameter("eveType", eveType);
		}
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						if(DemoApplication.sessionTimeoutCount > 0)
							DemoApplication.sessionTimeoutCount = 0;
						Log.i("PlanStarBohuiParser", "PlanStarBohuiParser" + t);
						map = planStarBohuiParser(t);
						Log.i("PlanStarBohuiParser", "PlanStarBohuiParser"
								+ map);
						if(onEmergencyCompleteListener != null)
							onEmergencyCompleteListener
								.onEmergencyParserComplete(map, null);

					}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

				String responseMsg = "";
				String errorResult = ex.toString();
				if (ex instanceof HttpException) { //网络错误
					errorResult = "网络错误";
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					if(responseCode == 518) {
						errorResult = "登录超时";
						Utils.getInstance().relogin();
						if(DemoApplication.sessionTimeoutCount < 2)
							request(planEveId,  planEveName, submitterId,
								eveType);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 2)
						request(planEveId,  planEveName, submitterId,
							eveType);
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
	 * 预案驳回解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> planStarBohuiParser(String t) {
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