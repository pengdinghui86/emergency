package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.ChildEntity;
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
 * 待执行步骤列表解析
 * 
 * @author zsj
 * 
 */
public class GetPerformPlanParser {
	private List<ChildEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	// precautionId 预案id
	// planResId 演练计划id
	// planInfoId
	public GetPerformPlanParser(String planInfoId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(planInfoId);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final String planInfoId) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETPERFORMBYEXECUTEPEOPLEID + "?planInfoId="
				+ planInfoId);
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
				Log.i("GetPerformPlanParser", t);
				list = planExecuteListParser(t);
				Log.i("GetPerformPlanParser", "GetPerformPlanParser" + list);
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
							request(planInfoId);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(planInfoId);
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
	 * 获取待执行列表数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<ChildEntity> planExecuteListParser(String t) {
		List<ChildEntity> list = new ArrayList<ChildEntity>();
		try {
			JSONObject jsonObject = new JSONObject(t);
			if (t.contains("data")) {
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						ChildEntity listEntity = new ChildEntity();
						JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
						listEntity.setChild_id(jsonObject2.getString("id"));
						listEntity.setPrecautionId(jsonObject2
								.getString("precautionId"));
						listEntity
								.setProcessName(jsonObject2.getString("name"));
						listEntity.setStatus(jsonObject2.getString("status"));
						listEntity.setPlanInfoId(jsonObject2
								.getString("planInfoId"));
						listEntity.setManualDetailId(jsonObject2
								.getString("manualDetailId"));
						// manualDetailId 操作手册详细内容记录id
						// planResType 演练计划类型 1、事件 2、演练计划
						// drillPrecautionId 演练计划id
						// name 步骤名称
						list.add(listEntity);
					}
				}
			}

		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}
		return list;
	}

}
