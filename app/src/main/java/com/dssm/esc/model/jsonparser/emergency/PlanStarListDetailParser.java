package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanStarListDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjListEntity;
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
import java.util.List;


/**
 * 预案启动事件详情
 * @author zsj
 *
 */
public class PlanStarListDetailParser {
	public PlanStarListDetailEntity detailEntity;
	private final WeakReference<OnDataCompleterListener> wr;

	public PlanStarListDetailParser(String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(id);
	}

	/**
	 * 发送请求
	 * @param id 事件id
	 */
	public void request(final String id) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.PLANSTARDETAIL);
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
		// params.put("userName",userEntity.getUsername());
		// params.put("password", userEntity.getPassword());
		params.addParameter("id", id);
		Log.i("未启动事件详情id", DemoApplication.getInstance().getUrl()+HttpUrl.PLANSTARDETAIL);
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i("PlanStarListDetail", "PlanStarListDetailParser" + t);
				detailEntity = getPlanStarListDetail(t);
				Log.i("PlanStarListDetail", "PlanStarListDetailParser" + detailEntity);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(
						detailEntity, null);

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
	 * 解析事件详情
	 */
	public PlanStarListDetailEntity getPlanStarListDetail(String t) {
		PlanStarListDetailEntity detailEntity = new PlanStarListDetailEntity();
		try {
			JSONObject jsonObject = new JSONObject(t);
			if (t.contains("success")) {
				detailEntity.setSuccess(jsonObject.getString("success"));
				PlanStarListDetailObjEntity objEntity = new PlanStarListDetailObjEntity();
				if (jsonObject.getString("success").equals("true")) {
					JSONObject jsonObject3 = jsonObject.getJSONObject("obj");
					objEntity.setId(jsonObject3.getString("id"));
					objEntity.setEveCode(jsonObject3.getString("eveCode"));
					objEntity.setEveName(jsonObject3.getString("eveName"));
					objEntity.setSubmitter(jsonObject3.getString("submitter"));
					objEntity.setSubTime(jsonObject3.getString("subTime"));
					objEntity.setNowTime(jsonObject3.getString("nowTime"));
					objEntity.setTradeType(jsonObject3.getString("tradeType"));
					objEntity.setEveLevel(jsonObject3.getString("eveLevel"));
//					objEntity.setEveScenarioName(jsonObject3.getString("eveScenarioName"));
//					objEntity.setEveScenarioId(jsonObject3.getString("eveScenarioId"));
					objEntity.setDealAdvice(jsonObject3.getString("dealAdvice"));
					objEntity.setDiscoverer(jsonObject3.getString("discoverer"));
					objEntity.setDiscoveryTime(jsonObject3.getString("discoveryTime"));
					String eveType = jsonObject3.getString("eveType");
					objEntity.setEveType(eveType);
					objEntity.setSubmitterId(jsonObject3.getString("submitterId"));
					objEntity.setEveDescription(jsonObject3.getString("eveDescription"));
					objEntity.setDrillPlanId(jsonObject3.getString("drillPlanId"));
					objEntity.setPlanResName(jsonObject3.getString("eveName"));
					objEntity.setPlanResType(eveType);
//					submitterId	提交人ID	发送通知使用
//					tradeTypeId	业务类型ID	发送通知使用
//					eveLevelId	事件等级ID	发送通知使用
					objEntity.setTradeTypeId(jsonObject3.getString("tradeTypeId"));
					objEntity.setEveLevelId(jsonObject3.getString("eveLevelId"));
					if (objEntity.getEveType().equals("2")) {//演练才有
						if(jsonObject3.has("hasStartAuth"))
							objEntity.setHasStartAuth(jsonObject3.getString("hasStartAuth"));
					}
					ArrayList<PlanStarListDetailObjListEntity> list = new ArrayList<PlanStarListDetailObjListEntity>();
					JSONArray jsonArray = jsonObject3.getJSONArray("list");
					for (int i = 0; i < jsonArray.length(); i++) {
						if (jsonArray.opt(i)!=null) {
							JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
							PlanStarListDetailObjListEntity listEntity = new PlanStarListDetailObjListEntity();
							listEntity.setName(jsonObject2.getString("name"));
							listEntity.setProcessId(jsonObject2.getString("processId"));
							listEntity.setSceneName(jsonObject2.getString("sceneName"));
							listEntity.setPlanType(jsonObject2.getString("planType"));
							if(jsonObject2.has("hasStartAuth"))
								listEntity.setHasStartAuth(jsonObject2.getString("hasStartAuth"));
							else
								listEntity.setHasStartAuth("false");
							list.add(listEntity);
						}
					}
					objEntity.setList(list);
					
				}
				detailEntity.setObj(objEntity);
				return detailEntity;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return detailEntity;
		}

		return detailEntity;
	}
}
