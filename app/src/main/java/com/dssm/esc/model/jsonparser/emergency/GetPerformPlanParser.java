package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	// precautionId 预案id
	// planResId 演练计划id
	// planInfoId
	public GetPerformPlanParser(String planInfoId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(planInfoId);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final String planInfoId) {

		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETPERFORMBYEXECUTEPEOPLEID + "?planInfoId="
				+ planInfoId, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
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

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Log.i("GetPerformPlanParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = planExecuteListParser(t);
				Log.i("GetPerformPlanParser", "GetPerformPlanParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

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
