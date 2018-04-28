package com.dssm.esc.model.jsonparser.control;

import android.text.TextUtils;
import android.util.Log;

import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FlowChartPlanParser {
	String TAG = "FlowChartPlanParser";
	private FlowChartPlanEntity flowChartPlanEntity;
	private FinalHttp finalHttp;
	private ControlCompleterListenter<FlowChartPlanEntity> completeListener;

	public FlowChartPlanParser(String planInfoId,
			ControlCompleterListenter<FlowChartPlanEntity> completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.completeListener = completeListener;
		request(planInfoId);
	}

	/**
	 * 
	 * 发送请求
	 */
	public void request(final String planInfoId) {
		AjaxParams params = new AjaxParams();
		params.put("planInfoId", planInfoId);
		Log.i("流程监控url", DemoApplication.getInstance().getUrl()+HttpUrl.FLOW_CHART_PLAN + "?planInfoId=" + planInfoId);
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.FLOW_CHART_PLAN, params,
				new AjaxCallBack<String>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);

						completeListener.controlParserComplete(null, strMsg);
						Log.i("onFailure", "strMsg" + strMsg);
						if (errorNo == 518) {
							Utils.getInstance().relogin();
							request(planInfoId);
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Log.i(TAG, TAG + t);
						MyCookieStore.setcookieStore(finalHttp);
						flowChartPlanEntity = getPlanListParse(t);
						Log.i(TAG, TAG + flowChartPlanEntity);
						completeListener.controlParserComplete(
								flowChartPlanEntity, null);

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
