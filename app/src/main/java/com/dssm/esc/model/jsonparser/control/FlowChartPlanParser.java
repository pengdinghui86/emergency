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


public class FlowChartPlanParser {
	String TAG = "FlowChartPlanParser";
	private FlowChartPlanEntity flowChartPlanEntity;
	private final WeakReference<ControlCompleterListenter<FlowChartPlanEntity>> wr;

	public FlowChartPlanParser(String planInfoId,
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
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.FLOW_CHART_PLAN);
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
		Log.i("流程监控url", DemoApplication.getInstance().getUrl()+HttpUrl.FLOW_CHART_PLAN + "?planInfoId=" + planInfoId);
		final ControlCompleterListenter<FlowChartPlanEntity> completeListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
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
						request(planInfoId);
					}
					responseMsg = httpEx.getMessage();
                    //					errorResult = httpEx.getResult();

                } else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
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
					String beginTime = jsonObject.getString("beginTime");
					if (!beginTime.equals("") && beginTime != null
							&& beginTime.length() > 0
							&& !beginTime.equals("null")) {
						entity.setBeginTime(beginTime);
					} else {
						entity.setBeginTime("");
					}

					/**
					 * 新增
					 * 2017/10/13
					 */
					if(jsonObject.has("code")){
						entity.setCode(isNull(jsonObject.getString("code")));
					}

					entity.setCreateTime(jsonObject.getString("createTime"));
					entity.setCreateUser(jsonObject.getString("createUser"));
					entity.setDrillId(jsonObject.getString("drillId"));
					entity.setDuration(isNull(jsonObject.getString("duration")));

					String endTime = jsonObject.getString("endTime");
					if (!endTime.equals("") && endTime != null
							&& endTime.length() > 0 && !endTime.equals("null")) {
						entity.setEndTime(endTime);
					} else {
						entity.setEndTime("");
					}
					String executePeople = jsonObject.getString("executePeople");
					if (!executePeople.equals("") && executePeople != null
							&& executePeople.length() > 0
							&& !executePeople.equals("null")) {
						entity.setExecutePeople(executePeople);
					} else {
						entity.setExecutePeople("");
					}
					entity.setExecutePeopleId(isNull(jsonObject.getString("executePeopleId")));
					entity.setExecutePeopleType(isNull(jsonObject.getString("executePeopleType")));
					entity.setExecutorA(isNull(jsonObject.getString("executorA")));
					entity.setExecutorB(isNull(jsonObject.getString("executorB")));
					entity.setExecutorC(isNull(jsonObject.getString("executorC")));
					String id = jsonObject.getString("id");
					entity.setId(isNull(id));
					entity.setIsShow(isNull(jsonObject.getString("isShow")));
					entity.setManualDetailId(isNull(jsonObject.getString("manualDetailId")));
					entity.setMessage(isNull(jsonObject.getString("message")));
					entity.setName(isNull(jsonObject.getString("name")));
					entity.setOrderNum(isNull(jsonObject.getString("orderNum")));
					entity.setEditOrderNum(isNull(jsonObject
							.getString("editOrderNum")));

					entity.setpId(isNull(jsonObject.getString("pId")));
					entity.setPlanEveId(isNull(jsonObject
							.getString("planEveId")));
					entity.setPlanInfoId(isNull(jsonObject
							.getString("planInfoId")));
					entity.setPrecautionId(isNull(jsonObject
							.getString("precautionId")));
					entity.setRemark(isNull(jsonObject.getString("remark")));
					String status = jsonObject.getString("status");
					if (!status.equals("") && status != null
							&& status.length() > 0 && !status.equals("null")) {
						entity.setStatus(status);
					} else {
						entity.setStatus("");
					}
					entity.setStyle(isNull(jsonObject.getString("style")));
					entity.setType(isNull(jsonObject.getString("type")));
					entity.setNodeStepType(isNull(jsonObject.getString("nodeStepType")));
					entity.setParentProcessStepId(isNull(jsonObject.getString("parentProcessStepId")));
					entity.setUpdateTime(isNull(jsonObject
							.getString("updateTime")));
					entity.setUpdateUser(isNull(jsonObject
							.getString("updateUser")));
					JSONArray jsonArray = jsonObject.getJSONArray("process");
					List<FlowChartPlanEntity.FlowChart.ProcessEntity> processlist = new ArrayList<FlowChartPlanEntity.FlowChart.ProcessEntity>();
					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObject object2 = (JSONObject) jsonArray.opt(j);
						FlowChartPlanEntity.FlowChart.ProcessEntity processEntity = entity.new ProcessEntity();
						// processEntity.setNextid("1");
						String rowId = object2.getString("id");
						if (!rowId.equals(id)) {
							processEntity
							.setNextid(isNull(rowId));
							processEntity.setNextname(isNull(object2
									.getString("name")));
							processlist.add(processEntity);
						}
					}
					entity.setProcesslist(processlist);

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

	}
}
