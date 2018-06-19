package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
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
	OnDataCompleterListener OnEmergencyCompleterListener;

	// id 编号
	// suspendType 中止类型 类型为authSuspend
	// planSuspendOpition2 预案中止原因
	public PlanSuspandParser(PlanSuspandEntity suspandEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(suspandEntity);
	}

	/**
	 * 发送请求
	 * 

	 */
	public void request(final PlanSuspandEntity suspandEntity) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.PLANSUSPAND);
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
		params.addParameter("id", suspandEntity.getId());
		params.addParameter("suspendType", suspandEntity.getSuspendType());
		params.addParameter("planSuspendOpition", suspandEntity.getPlanSuspendOpition());
		params.addParameter("planName", suspandEntity.getPlanName());
		params.addParameter("planResName", suspandEntity.getPlanResName());
		
		params.addParameter("planResType", suspandEntity.getPlanResType());
		params.addParameter("planId", suspandEntity.getPlanId());
		params.addParameter("eveLevelId", suspandEntity.getEveLevelId());
		params.addParameter("planStarterId", suspandEntity.getPlanStarterId());
		params.addParameter("planAuthorId", suspandEntity.getPlanAuthorId());
		params.addParameter("submitterId", suspandEntity.getSubmitterId());
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("PlanSuspandParser", "PlanSuspandParser" + t);
				map = loginRoleParse(t);
				Log.i("UserLoginRoleParser", "UserLoginRoleParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
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
						request(suspandEntity);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(suspandEntity);
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
