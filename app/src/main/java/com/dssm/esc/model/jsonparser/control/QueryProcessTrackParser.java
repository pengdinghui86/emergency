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


/**
 * 实时跟踪列表解析
 * 
 * @author zsj
 * 
 */
public class QueryProcessTrackParser {
	String TAG = "QueryProcessTrackParser";
	private FlowChartPlanEntity flowChartPlanEntity;
	private FinalHttp finalHttp;
	private ControlCompleterListenter<FlowChartPlanEntity> completeListener;

	public QueryProcessTrackParser(String planInfoId,
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
		Log.i("实时跟踪url", DemoApplication.getInstance().getUrl()+HttpUrl.QUERYPROCESSTRACK+"?planInfoId="+planInfoId);
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.QUERYPROCESSTRACK, params,
				new AjaxCallBack<String>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);

						completeListener.controlParserComplete(null, strMsg);
						Log.i("onFailure", "strMsg" + strMsg);
						if (errorNo==518) {
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
					entity.setStatus(jsonObject.getString("status"));//完成状态
					entity.setOrderNum(jsonObject.getString("orderNum"));
					entity.setEditOrderNum(jsonObject.getString("editOrderNum"));
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
