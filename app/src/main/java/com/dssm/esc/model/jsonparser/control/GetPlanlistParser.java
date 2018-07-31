package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
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
 * 5.2.1	待展示预案列表
 * @author Administrator
 *
 */
public class GetPlanlistParser {
	String TAG="GetPlanlistParser";
	private List<PlanEntity> list;
	private final WeakReference<ControlCompleterListenter<List<PlanEntity>>> wr;

	public GetPlanlistParser(ControlCompleterListenter<List<PlanEntity>> completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request();
	}
	
	/**
	 * 
	 * 发送请求
	 */
	public void request(){

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GET_PLAN_LIST);
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
		final ControlCompleterListenter<List<PlanEntity>> completeListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i(TAG, TAG+t);
				list=getPlanListParse(t);
				Log.i(TAG, TAG+list);
				if(completeListener != null)
					completeListener.controlParserComplete(list, null);
				
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
							request();
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request();
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(completeListener != null)
					completeListener.controlParserComplete(null, errorResult);
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}

		});
		
	}
   private List<PlanEntity> getPlanListParse(String t) {
	   List<PlanEntity> list=new ArrayList<PlanEntity>();
	   try {
		JSONArray object=new JSONArray(t);
		for (int i = 0; i < object.length(); i++) {
			JSONObject jsonObject=(JSONObject)object.opt(i);
			PlanEntity entity=new PlanEntity();
//			  "planName": "应急响应预案", 
//		        "state": 2, 
//		        "planAuthorId": "94f62638-9e38-4cdd-9c31-08f2c8d1bf24", 
//		        "planId": "8c23c0bd-a017-47b0-9bf8-4fdd29a5441c", 
//		        "drillPrecautionId": null, 
//		        "planResId": "a6b82902-b059-4956-b9dc-3c674c4b97a9", 
//		        "eveLevelId": "57dc2cee-941d-404c-be17-1a51db42f1fb", 
//		        "id": "19047b50-49bd-43ae-9cbf-98bcec9c0d66", 
//		        "submitterId": "afed5232-9319-41f1-a7a8-58eef4c6d2e2", 
//		        "planResType": 1, 
//		        "planResName": "测试中止情况1", 
//		        "planStarterId": "afed5232-9319-41f1-a7a8-58eef4c6d2e2", 
//		        "tradeTypeId": "7f4cd5e3-8cd2-469f-ad34-b8d727fdb938"
			entity.setId(jsonObject.getString("id"));
			entity.setPlanId(jsonObject.getString("planId"));
			entity.setPlanName(jsonObject.getString("planName"));
			entity.setPlanResId(jsonObject.getString("planResId"));
			entity.setPlanResName(jsonObject.getString("planResName"));
			entity.setPlanResType(jsonObject.getString("planResType"));
			entity.setTradeTypeId(jsonObject.getString("tradeTypeId"));
			entity.setEveLevelId(jsonObject.getString("eveLevelId"));
			entity.setPlanStarterId(jsonObject.getString("planStarterId"));
			String planAuthorId = jsonObject.getString("planAuthorId");
			if (planAuthorId.equals("null")) {
				planAuthorId="";
			}
			entity.setPlanAuthorId(planAuthorId);
			entity.setSubmitterId(jsonObject.getString("submitterId"));
			entity.setState(jsonObject.getString("state"));
			String drillPrecautionId = jsonObject.getString("drillPrecautionId");
			if (drillPrecautionId.equals("null")) {
				drillPrecautionId="";
			}
			entity.setDrillPrecautionId(drillPrecautionId);
			list.add(entity);
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
    return list;
}
}
