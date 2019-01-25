package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.DemoApplication;

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
 * 终止预案解析
 * @author Administrator
 *
 */
public class StopPlanParser {
	String TAG="StopPlanParser";
	public Map<String, String> map;
	private final WeakReference<ControlCompleterListenter<Map<String, String>>> wr;

	public StopPlanParser(PlanEntity entity, String planSuspendOpition , ControlCompleterListenter<Map<String, String>> completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(entity,planSuspendOpition);
	}

	/**
	 * 发送请求
	 *

	 */
	public void request(final PlanEntity entity,final String planSuspendOpition) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.STOP_PLAN);
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
		params.addParameter("id", entity.getId());
		params.addParameter("planResType", entity.getPlanResType());
			// planSuspendOpition 预案终止原因
		params.addParameter("planSuspendOpition", planSuspendOpition);
		params.addParameter("planName", entity.getPlanName());
		params.addParameter("planResName", entity.getPlanResName());
//				planId	预案ID	中止时发送通知使用
//				tradeTypeId	业务类型ID	中止时发送通知使用
//				eveLevelId	事件等级ID	中止时发送通知使用
//				planStarterId	预案启动人	中止时发送通知使用
//				planAuthorId	预案授权人	中止时发送通知使用
//				submitterId	事件提交人	中止时发送通知使用
		params.addParameter("planId", entity.getPlanId());
		params.addParameter("tradeTypeId", entity.getTradeTypeId());
		params.addParameter("planStarterId", entity.getPlanStarterId());
		params.addParameter("eveLevelId", entity.getEveLevelId());
		params.addParameter("planAuthorId", entity.getPlanAuthorId());
		params.addParameter("submitterId", entity.getSubmitterId());
		final ControlCompleterListenter<Map<String, String>> completeListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i(TAG, TAG + t);
				map = setStopPlanParser(t);
				Log.i(TAG, TAG + map);
				if(completeListener != null)
					completeListener.controlParserComplete(map,
						null);

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
						if(DemoApplication.sessionTimeoutCount < 5)
							request(entity, planSuspendOpition);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(entity, planSuspendOpition);
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(completeListener != null)
					completeListener.controlParserComplete(null, errorResult);
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
	public Map<String, String> setStopPlanParser(String t) {
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
