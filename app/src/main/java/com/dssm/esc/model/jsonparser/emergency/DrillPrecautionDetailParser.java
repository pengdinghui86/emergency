package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.DrillProcationDetailEntity;
import com.dssm.esc.model.entity.emergency.DrillProcationDetailObjEntity;
import com.dssm.esc.model.entity.emergency.DrillProjectDetailObjPreinfoEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.DemoApplication;

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
 * 演练计划预案详情解析
 * 
 * @author zsj
 * 
 */
public class DrillPrecautionDetailParser {
	public DrillProcationDetailEntity userEntity;
	private final WeakReference<OnDataCompleterListener> wr;

	public DrillPrecautionDetailParser(String detailPlanId,String drillPlanName,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(detailPlanId,drillPlanName);
	}

	/**
	 * 发送请求
	 * @param detailPlanId 详细演练计划id
	 */
	public void request(final String detailPlanId,final String drillPlanName) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.DRILLPROJECTNAMEDETAIL);
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
		params.addParameter("drillPlanId", detailPlanId);
		params.addParameter("drillPlanName", drillPlanName);
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i("DrillPrecautionDetail", t);
				userEntity = getDrillProjectDetail(t);
				Log.i("DrillPrecautionDetail", "DrillPrecautionDetailParser" + userEntity);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(
						userEntity, null);

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
							request(detailPlanId, drillPlanName);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(detailPlanId, drillPlanName);
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
	 * 获取演练计划详情
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public DrillProcationDetailEntity getDrillProjectDetail(String t) {
		DrillProcationDetailEntity detailEntity = new DrillProcationDetailEntity();
		try {
			JSONObject jsonObject = new JSONObject(t);
			if (t.contains("success")) {
				detailEntity.setSuccess(jsonObject.getString("success"));
				detailEntity.setMessage(jsonObject.getString("message"));
				DrillProcationDetailObjEntity objEntity = new DrillProcationDetailObjEntity();
				if (jsonObject.getString("success").equals("true")) {
					JSONObject jsonObject3 = jsonObject.getJSONObject("obj");
					objEntity.setExPlanId(jsonObject3.getString("exPlanId"));
					objEntity.setEmergType(jsonObject3.getString("emergType"));
					if(jsonObject3.has("precautionList"))
						objEntity.setPrecautionList(jsonObject3.getString("precautionList"));
					else
						objEntity.setPrecautionList("");
					if(jsonObject3.has("referPlan"))
						objEntity.setReferPlan(jsonObject3.getString("referPlan"));
					else
						objEntity.setReferPlan("");
					if(jsonObject3.has("referProcess"))
						objEntity.setReferProcess(jsonObject3.getString("referProcess"));
					else
						objEntity.setReferProcess("");
					objEntity.setDrillPlanName(jsonObject3
							.getString("drillPlanName"));
					objEntity.setDrillPlanId(jsonObject3
							.getString("drillPlanId"));
					List<DrillProjectDetailObjPreinfoEntity> list = new ArrayList<DrillProjectDetailObjPreinfoEntity>();
					JSONArray jsonArray = jsonObject3.getJSONArray("preInfo");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
						DrillProjectDetailObjPreinfoEntity preinfoEntity = new DrillProjectDetailObjPreinfoEntity();
						preinfoEntity.setPrecautionId(jsonObject2
								.getString("precautionId"));
						preinfoEntity.setPrecautionName(jsonObject2
								.getString("precautionName"));
						preinfoEntity.setDrillPrecautionId(jsonObject2
								.getString("drillPrecautionId"));
						preinfoEntity.setDetailPlanId(jsonObject2
								.getString("detailPlanId"));
						
						
						preinfoEntity.setPrecautionType(jsonObject2
								.getString("precautionType"));
						list.add(preinfoEntity);

					}
					objEntity.setPreInfo(list);
					
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
