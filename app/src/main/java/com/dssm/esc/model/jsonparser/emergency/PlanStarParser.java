package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjEntity;
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
 * 预案启动解析
 * 
 * @author zsj
 * 
 */
public class PlanStarParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanStarParser(String id, String usePlan,
			PlanStarListDetailObjEntity detailObjEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
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

		AjaxParams params = new AjaxParams();
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
	
			params.put("planEveId", id);
			params.put("usePlan", usePlan);

			params.put("opition", detailObjEntity.getDealAdvice());// 处置建议
			params.put("eveType", detailObjEntity.getEveType());// 事件类型
			String eveName = detailObjEntity.getEveName();
			
			params.put("planEveName",eveName );// 事件名称
			params.put("eveScenarioId", detailObjEntity.getEveScenarioId());// 事件场景id
			params.put("drillPlanId", detailObjEntity.getDrillPlanId());// 演练详细计划ID
			
			params.put("submitterId", detailObjEntity.getSubmitterId());// 提交人ID
			params.put("tradeTypeId", detailObjEntity.getTradeTypeId());// 业务类型ID
			params.put("eveLevelId", detailObjEntity.getEveLevelId());// 事件等级ID
			
			
			params.put("planResName", detailObjEntity.getPlanResName());// 预案来源名称	发送通知使用
			params.put("planResType", detailObjEntity.getPlanResType());// 预案来源类型	发送通知使用
			params.put("planName", detailObjEntity.getPlanName());// 预案名称	发送通知使用
			params.put("planId", detailObjEntity.getPlanId());// 预案ID	发送通知使用
		String url=DemoApplication.getInstance().getUrl()+HttpUrl.PLANSTARD;
		finalHttp.post(url, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request( id,  usePlan,
							 detailObjEntity);
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
				Log.i("PlanStarParser", "PlanStarParser" + t);
				map = planStarBohuiParser(t);
				Log.i("PlanStarParser", "PlanStarParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

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
