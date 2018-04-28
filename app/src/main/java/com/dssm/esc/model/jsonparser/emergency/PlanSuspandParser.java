package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
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
 * 预案终止(授权决策)
 * 
 * @author zsj
 * 
 */
public class PlanSuspandParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	// id 编号
	// suspendType 中止类型 类型为authSuspend
	// planSuspendOpition2 预案中止原因
	public PlanSuspandParser(PlanSuspandEntity suspandEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(suspandEntity);
	}

	/**
	 * 发送请求
	 * 

	 */
	public void request(final PlanSuspandEntity suspandEntity) {

		AjaxParams params = new AjaxParams();
		params.put("id", suspandEntity.getId());
		params.put("suspendType", suspandEntity.getSuspendType());
		params.put("planSuspendOpition", suspandEntity.getPlanSuspendOpition());
		params.put("planName", suspandEntity.getPlanName());
		params.put("planResName", suspandEntity.getPlanResName());
		
		params.put("planResType", suspandEntity.getPlanResType());
		params.put("planId", suspandEntity.getPlanId());
		params.put("eveLevelId", suspandEntity.getEveLevelId());
		params.put("planStarterId", suspandEntity.getPlanStarterId());
		params.put("planAuthorId", suspandEntity.getPlanAuthorId());
		params.put("submitterId", suspandEntity.getSubmitterId());
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.PLANSUSPAND, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(suspandEntity);
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
				Log.i("PlanSuspandParser", "PlanSuspandParser" + t);
				map = loginRoleParse(t);
				Log.i("UserLoginRoleParser", "UserLoginRoleParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

			}

		});

	}

	/**
	 * 预案中止解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> loginRoleParse(String t) {
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
