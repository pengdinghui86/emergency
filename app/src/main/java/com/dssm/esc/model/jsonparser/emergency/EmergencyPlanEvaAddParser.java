package com.dssm.esc.model.jsonparser.emergency;

import com.dssm.esc.model.entity.emergency.EmergencyPlanEvaAddEntity;
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
 * 添加评估解析类
 * 
 * @author zsj
 * 
 */
public class EmergencyPlanEvaAddParser {
	public Map<String, String> map;
	private final WeakReference<OnDataCompleterListener> wr;

	public EmergencyPlanEvaAddParser(String tag,
			EmergencyPlanEvaAddEntity addEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(tag, addEntity);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final String tag, final EmergencyPlanEvaAddEntity addEntity) {
		String url = null;
		if (tag.equals("1")) {// 应急
			url = DemoApplication.getInstance().getUrl()+HttpUrl.EMERGENCY_PLANEVA_ADD;
		} else if (tag.equals("2")) {// 演练
			url = DemoApplication.getInstance().getUrl()+HttpUrl.DRILL_ADD;
		}
		RequestParams params = new RequestParams(url);
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
		params.addParameter("tradeType", addEntity.getTradeType());
		params.addParameter("eveLevel", addEntity.getEveLevel());
		params.addParameter("eveDescription", addEntity.getEveDescription());
		params.addParameter("eveScenarioId", addEntity.getEveScenarioId());
		params.addParameter("eveScenarioName", addEntity.getEveScenarioName());
		params.addParameter("emergType", addEntity.getEmergType());
		params.addParameter("eveName", addEntity.getEveName());
		String drillPlanName = addEntity.getDrillPlanName();
		params.addParameter("drillPlanName", drillPlanName);
		params.addParameter("dealAdvice", addEntity.getDealAdvice());
		params.addParameter("referPlan", addEntity.getReferPlan());
		params.addParameter("otherReferPlan", addEntity.getOtherReferPlan());
		params.addParameter("categoryPlan", addEntity.getCategoryPlan());
		params.addParameter("eveType", addEntity.getEveType());
		params.addParameter("drillPlanId", addEntity.getDrillPlanId());
		params.addParameter("exPlanId", addEntity.getExPlanId());
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				map = emergencyPlaneveAddParser(t);

				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(map,
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
						request(tag, addEntity);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					request(tag, addEntity);
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
	 * 添加评估数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> emergencyPlaneveAddParser(String t) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject(t);
			if (t.contains("success")) {
				map.put("success", object.getString("success"));
				map.put("message", object.getString("message"));
			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

		return map;
	}

}
