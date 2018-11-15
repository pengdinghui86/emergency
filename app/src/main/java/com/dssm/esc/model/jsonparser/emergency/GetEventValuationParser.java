package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 获取事件评估信息解析
 * 
 * @author zsj
 * 
 */
public class GetEventValuationParser {
	private GetProjectEveInfoEntity entity;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetEventValuationParser(String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(id);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 * @param id
	 *            事件编号
	 */
	public void request(final String id) {
		Log.i("评估信息id", id);
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GET_REJECTEVENTINFO + id);
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
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i("GetEventValuationParser", t);
				entity = getEventValuationParser(t);
				Log.i("GetEventValuationParser",
						"GetEventValuationParser" + entity);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(
						entity, null);

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
						if(DemoApplication.sessionTimeoutCount < 5)
							Utils.getInstance().relogin();
						request(id);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(id);
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
	 * 获取事件对应的评估信息
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public GetProjectEveInfoEntity getEventValuationParser(String t) {
		GetProjectEveInfoEntity entity = new GetProjectEveInfoEntity();
		try {
			JSONObject jsonObject = new JSONObject(t);
			entity.setId(jsonObject.getString("id"));

			entity.setTradeTypeId(jsonObject.getString("tradeTypeId"));
			entity.setTradeTypeName(jsonObject.getString("tradeType"));
			entity.setEveLevelId(jsonObject.getString("eveLevelId"));
			entity.setEveLevelName(jsonObject.getString("eveLevelName"));
			entity.setEveDescription(jsonObject.getString("eveDescription"));
			entity.setEveDiscover(jsonObject.getString("discoverer"));
			entity.setEveDiscoveryTime(jsonObject.getString("discoveryTime"));
			if(jsonObject.has("eveScenarioId")) {
				entity.setEveScenarioId(jsonObject.getString("eveScenarioId"));
			}
			entity.setEveType(jsonObject.getString("eveType"));
			entity.setEmergType(jsonObject.getString("emergType"));
			if(jsonObject.has("eveScenarioName")) {
				entity.setEveScenarioName(jsonObject.getString("eveScenarioName"));
			}
			entity.setEveName(jsonObject.getString("eveName"));
			entity.setDealAdvice(jsonObject.getString("dealAdvice"));
			List<Map<String, String>> referPlanList = new ArrayList<Map<String, String>>();
			JSONArray jsonArray_referPlan = jsonObject
					.getJSONArray("referPlan");
			for (int i = 0; i < jsonArray_referPlan.length(); i++) {
				JSONObject jsonObject2 = (JSONObject) jsonArray_referPlan
						.opt(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", jsonObject2.getString("id"));
				map.put("name", jsonObject2.getString("name"));
				referPlanList.add(map);
			}
			entity.setReferPlan(referPlanList);
			if(jsonObject.has("otherReferPlan")) {
				List<Map<String, String>> otherReferPlanList = new ArrayList<Map<String, String>>();
				JSONArray jsonArray_otherReferPlan = jsonObject
						.getJSONArray("otherReferPlan");
				for (int i = 0; i < jsonArray_otherReferPlan.length(); i++) {
					JSONObject jsonObject3 = (JSONObject) jsonArray_otherReferPlan
							.opt(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", jsonObject3.getString("id"));
					map.put("name", jsonObject3.getString("name"));
					otherReferPlanList.add(map);
				}
				entity.setOtherReferPlan(otherReferPlanList);
			}
			if(jsonObject.has("categoryPlan")) {
				List<Map<String, String>> categoryPlanList = new ArrayList<Map<String, String>>();
				JSONArray jsonArray_categoryPlan = jsonObject
						.getJSONArray("categoryPlan");
				for (int i = 0; i < jsonArray_categoryPlan.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray_categoryPlan
							.opt(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", jsonObject2.getString("id"));
					map.put("name", jsonObject2.getString("name"));
					categoryPlanList.add(map);
				}
				entity.setCategoryPlan(categoryPlanList);
			}
			entity.setDrillPlanName(jsonObject.getString("drillPlanName"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return entity;
	}
	/**
	 * 判断服务器返回的数据是否为空
	 * @param string
	 * @return
	 */
		private String isNull(String string) {
			if (!string.equals("") && string != null && !string.equals("null")
					&& string.length() > 0) {
				return string;
			} else {

				return "";
			}
		}

}