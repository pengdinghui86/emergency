package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjEntity;
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
 * 预案启动解析
 * 
 * @author zsj
 * 
 */
public class PlanStarParser {
	public Map<String, String> map;
	private final WeakReference<OnDataCompleterListener> wr;

	public PlanStarParser(String id, String usePlan,
			PlanStarListDetailObjEntity detailObjEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(id, usePlan, detailObjEntity);
	}

	/**
	 * 发送请求
	 * 
	 * @param detailObjEntity
	 *            事件详情实体类
	 * @param id
	 *            事件id
	 * @param usePlan
	 *            已选预案
	 */
	public void request(final String id,final String usePlan,
			final	PlanStarListDetailObjEntity detailObjEntity) {

		String url=DemoApplication.getInstance().getUrl()+HttpUrl.PLANSTARD;
		RequestParams params = new RequestParams(url);
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
		// usePlan 已选预案 可以多选，以“|”隔开
		// opition 处置意见
		// planEveId 事件ID
		// eveType 事件类型 1为应急，2为演练
		// planEveName 事件名称
		// eveScenarioId 事件场景ID
		// drillPlanId 演练详细计划ID
//---------------新加的字段------------------------------
//		submitterId	提交人ID	发送通知使用
//		tradeTypeId	业务类型ID	发送通知使用
//		eveLevelId	事件等级ID	发送通知使用
//		planResType	预案来源类型	发送通知使用
//		planResName	预案来源名称	发送通知使用
//		planName	预案名称	发送通知使用
//		planId	预案ID	发送通知使用
	
		params.addParameter("planEveId", id);
		params.addParameter("usePlan", usePlan);

		params.addParameter("opition", detailObjEntity.getDealAdvice());// 处置建议
		params.addParameter("eveType", detailObjEntity.getEveType());// 事件类型
		String eveName = detailObjEntity.getEveName();

		params.addParameter("planEveName",eveName );// 事件名称
		params.addParameter("eveScenarioId", detailObjEntity.getEveScenarioId());// 事件场景id
		params.addParameter("drillPlanId", detailObjEntity.getDrillPlanId());// 演练详细计划ID

		params.addParameter("submitterId", detailObjEntity.getSubmitterId());// 提交人ID
		params.addParameter("tradeTypeId", detailObjEntity.getTradeTypeId());// 业务类型ID
		params.addParameter("eveLevelId", detailObjEntity.getEveLevelId());// 事件等级ID


		params.addParameter("planResName", detailObjEntity.getPlanResName());// 预案来源名称	发送通知使用
		params.addParameter("planResType", detailObjEntity.getPlanResType());// 预案来源类型	发送通知使用
		params.addParameter("planName", detailObjEntity.getPlanName());// 预案名称	发送通知使用
		params.addParameter("planId", detailObjEntity.getPlanId());// 预案ID	发送通知使用
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("PlanStarParser", "PlanStarParser" + t);
				map = planStarBohuiParser(t);
				Log.i("PlanStarParser", "PlanStarParser" + map);
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
						request(id,  usePlan,
								detailObjEntity);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(id,  usePlan,
							detailObjEntity);
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
