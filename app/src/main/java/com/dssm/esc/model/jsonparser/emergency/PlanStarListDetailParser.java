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

import java.util.ArrayList;
import java.util.List;


/**
 * 预案启动事件详情
 * @author zsj
 *
 */
public class PlanStarListDetailParser {
	public PlanStarListDetailEntity detailEntity;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanStarListDetailParser(String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(id);
	}

	/**
	 * 发送请求
	 * @param id 事件id
	 */
	public void request(final String id) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.PLANSTARDETAIL+"?id="+id);
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
		Log.i("未启动事件详情id", DemoApplication.getInstance().getUrl()+HttpUrl.PLANSTARDETAIL+"?id="+id);
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("PlanStarListDetail", "PlanStarListDetailParser" + t);
				detailEntity = getPlanStarListDetail(t);
				Log.i("PlanStarListDetail", "PlanStarListDetailParser" + detailEntity);
				OnEmergencyCompleterListener.onEmergencyParserComplete(
						detailEntity, null);

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
						request(id);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else { //其他错误
					errorResult = "其他错误";
				}
				OnEmergencyCompleterListener.onEmergencyParserComplete(null, errorResult);
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
	 * 获取演练计划详情
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
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
					objEntity.setEveScenarioName(jsonObject3.getString("eveScenarioName"));
					objEntity.setEveScenarioId(jsonObject3.getString("eveScenarioId"));
					objEntity.setDealAdvice(jsonObject3.getString("dealAdvice"));
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
						
						objEntity.setHasStartAuth(jsonObject3.getString("hasStartAuth"));
					}
					
					
					
					List<PlanStarListDetailObjListEntity> list = new ArrayList<PlanStarListDetailObjListEntity>();
					JSONArray jsonArray = jsonObject3.getJSONArray("list");
					for (int i = 0; i < jsonArray.length(); i++) {
						if (jsonArray.opt(i)!=null) {
							
						
						JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
						PlanStarListDetailObjListEntity listEntity = new PlanStarListDetailObjListEntity();
						if (eveType.equals("2")) {
							
							listEntity.setName(jsonObject2
									.getString("precautionName"));
						}else {
							listEntity.setName(jsonObject2
									.getString("name"));
						}
						
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
