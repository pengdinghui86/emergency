package com.dssm.esc.model.jsonparser.control;

import android.text.TextUtils;
import android.util.Log;

import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
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
 * 实时跟踪列表解析
 * 
 * @author zsj
 * 
 */
public class QueryProcessTrackParser {
	String TAG = "QueryProcessTrackParser";
	private FlowChartPlanEntity flowChartPlanEntity;
	private final WeakReference<ControlCompleterListenter<FlowChartPlanEntity>> wr;

	public QueryProcessTrackParser(String planInfoId,
			ControlCompleterListenter<FlowChartPlanEntity> completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(planInfoId);
	}

	/**
	 * 
	 * 发送请求
	 */
	public void request(final String planInfoId) {
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.QUERYPROCESSTRACK);
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
		params.addParameter("planInfoId", planInfoId);
		Log.i("实时跟踪url", DemoApplication.getInstance().getUrl()+HttpUrl.QUERYPROCESSTRACK+"?planInfoId="+planInfoId);
		final ControlCompleterListenter<FlowChartPlanEntity> completeListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						if(DemoApplication.sessionTimeoutCount > 0)
							DemoApplication.sessionTimeoutCount = 0;
						Log.i(TAG, TAG + t);
						flowChartPlanEntity = getPlanListParse(t);
						Log.i(TAG, TAG + flowChartPlanEntity);
						if(completeListener != null)
							completeListener.controlParserComplete(
								flowChartPlanEntity, null);

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

	private FlowChartPlanEntity getPlanListParse(String t) {
		FlowChartPlanEntity fentity = null;
		try {
			JSONObject object = new JSONObject(t);
			if (!TextUtils.isEmpty(t)) {
				fentity = new FlowChartPlanEntity();
				fentity.setCurDate(object.getString("curDate"));
				fentity.setDuration(object.getString("duration"));
				fentity.setState(object.getString("state"));
				JSONArray array = object.getJSONArray("data");
				List<FlowChartPlanEntity.FlowChart> list = new ArrayList<FlowChartPlanEntity.FlowChart>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = (JSONObject) array.opt(i);
					FlowChartPlanEntity.FlowChart entity = fentity.new FlowChart();
//					"executePeople": "李大霄", 
//		            "planInfoId": "660a1a39-00ce-4ada-bf21-e38bcb2d119a", 
//		            "executePeopleType": "1", 
//		            "status": "5", 
//		            "orderNum": 1, 
//		            "beginTime": null, 
//		            "type": "milestone", 
//		            "endTime": null, 
//		            "id": "sid-8E7256F4-8BA7-48DD-A4D2-006D7E8281C4", 
//		            "message": null, 
//		            "process": "id=sid-FBB66041-9C00-4670-ABE1-0F9A9908F19D,name=", 
//		            "pId": null, 
//		            "duration": 12, 
//		            "name": "灾难事件通告1"

					/**
					 * 新增
					 * 2017/10/13
					 */
					if(jsonObject.has("code")){
						entity.setCode(isNull(jsonObject.getString("code")));
					}

					entity.setExecutePeople(jsonObject
							.getString("executePeople"));//步骤执行人
					entity.setExecutePeopleId(jsonObject
							.getString("executePeopleId"));//步骤执行人ID
					entity.setPlanInfoId(jsonObject.getString("planInfoId"));
					entity.setExecutePeopleType(jsonObject.getString("executePeopleType"));
					entity.setNodeStepType(jsonObject.getString("nodeStepType"));
					entity.setParentProcessStepId(isNull(jsonObject.getString("parentProcessStepId")));
					entity.setStatus(jsonObject.getString("status"));//完成状态
					entity.setOrderNum(jsonObject.getString("orderNum"));
					//2018.8.14修改，后台统一规定，实时流程监控用OrderNum，流程图用EditOrderNum
//					entity.setEditOrderNum(jsonObject.getString("editOrderNum"));
					entity.setEditOrderNum("");
					entity.setBeginTime(jsonObject.getString("beginTime"));//开始时间（已用时：结束时间减去开始时间）
					entity.setType(jsonObject.getString("type"));
					entity.setEndTime(jsonObject.getString("endTime"));//结束时间
					entity.setId(jsonObject.getString("id"));
					entity.setMessage(jsonObject.getString("message"));//信息内容
					entity.setProcess(jsonObject.getString("process"));
					entity.setpId(jsonObject.getString("pId"));
					entity.setDuration(jsonObject.getString("duration"));//预计用时，单位秒
					entity.setName(jsonObject.getString("name"));//步骤名称
					list.add(entity);
				}
				fentity.setData(list);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fentity;
		}

		return fentity;
	}

	private String isNull(String status) {
		if (!status.equals("") && status != null && status.length() > 0
				&& !status.equals("null")) {
			return status;
		} else {
			return "";
		}

	}}
