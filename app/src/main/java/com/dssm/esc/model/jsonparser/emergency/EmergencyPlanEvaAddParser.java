package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.EmergencyPlanEvaAddEntity;
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
 * 添加评估解析类
 * 
 * @author zsj
 * 
 */
public class EmergencyPlanEvaAddParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public EmergencyPlanEvaAddParser(String tag,
			EmergencyPlanEvaAddEntity addEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(tag, addEntity);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final String tag, final EmergencyPlanEvaAddEntity addEntity) {
		AjaxParams params = new AjaxParams();
		params.put("tradeType", addEntity.getTradeType());
		params.put("eveLevel", addEntity.getEveLevel());
		params.put("eveDescription", addEntity.getEveDescription());
		params.put("eveScenarioId", addEntity.getEveScenarioId());
		params.put("eveScenarioName", addEntity.getEveScenarioName());
		params.put("emergType", addEntity.getEmergType());
		params.put("eveName", addEntity.getEveName());
		String drillPlanName = addEntity.getDrillPlanName();
		params.put("drillPlanName", drillPlanName);
		params.put("dealAdvice", addEntity.getDealAdvice());
		params.put("referPlan", addEntity.getReferPlan());
		params.put("otherReferPlan", addEntity.getOtherReferPlan());
		params.put("categoryPlan", addEntity.getCategoryPlan());
		params.put("eveType", addEntity.getEveType());
		params.put("drillPlanId", addEntity.getDrillPlanId());
		params.put("exPlanId", addEntity.getExPlanId());
		String url = null;
		if (tag.equals("1")) {// 应急
			url = DemoApplication.getInstance().getUrl()+HttpUrl.EMERGENCY_PLANEVA_ADD;
		} else if (tag.equals("2")) {// 演练
			url = DemoApplication.getInstance().getUrl()+HttpUrl.DRILL_ADD;
		}
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
					request(tag,addEntity);
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
				map = emergencyPlaneveAddParser(t);

				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

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
