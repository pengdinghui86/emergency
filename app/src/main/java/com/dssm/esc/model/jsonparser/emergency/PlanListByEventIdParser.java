package com.dssm.esc.model.jsonparser.emergency;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
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
 * 根据事件编号查询所属预案列表
 */
public class PlanListByEventIdParser {
	private List<PlanStarListEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	public PlanListByEventIdParser(String id, OnDataCompleterListener completeListener) {
		wr = new WeakReference<>(completeListener);
		request(id);
	}

	public void request(final String id) {
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl() + HttpUrl.GETPLANLISTBYID + id);
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
				list = planStarListParser(t);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(list,
						null);

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

	public List<PlanStarListEntity> planStarListParser(String t) {
		List<PlanStarListEntity> list = new ArrayList<PlanStarListEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					PlanStarListEntity suspandEntity = new PlanStarListEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					
//				    	id	预案ID	
//				    	suspendType	中止类型	启动时中止，类型为null
//				    	planSuspendOpition	中止原因	
//				    	planName	预案名称	发送通知使用
//				    	planResName	预案来源名称	发送通知使用
//				    	planResType	预案来源类型	发送通知使用
//				    	planId	预案ID	发送通知使用
//				    	tradeTypeId	业务类型ID	发送通知使用
//				    	eveLevelId	事件等级ID	发送通知使用
//				    	planStarterId	预案启动人	发送通知使用
//				    	planAuthorId	预案授权人	发送通知使用
//				    	submitterId	事件提交人	发送通知使用
					suspandEntity.setId(jsonObject2.getString("id"));
					suspandEntity.setPlanName(jsonObject2.getString("planName"));
					suspandEntity.setPlanResName(jsonObject2.getString("planResName"));
					suspandEntity.setSceneName(jsonObject2.getString("sceneName"));
					suspandEntity.setPlanResType(jsonObject2.getString("planResType"));
					suspandEntity.setPlanId(jsonObject2.getString("planId"));
					suspandEntity.setState(jsonObject2.getString("state"));
					suspandEntity.setPlanResId(jsonObject2.getString("planResId"));
					list.add(suspandEntity);
				}
				return list;
			} else {
				return list;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}
	}
}
