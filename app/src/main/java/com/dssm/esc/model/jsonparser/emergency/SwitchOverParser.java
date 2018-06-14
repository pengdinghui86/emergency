package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
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
 * 任务切换数据解析
 * 
 * @author zsj
 * 
 */
public class SwitchOverParser {
	public Map<String, String> map;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public SwitchOverParser(String id, String planInfoId, String status,
			String message, String nodeStepType, String branch, OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(id, planInfoId, status, message, nodeStepType, branch);
	}

	/**
	 * 发送请求
	 * 
	 * @param id

	 */
	public void request(final String id,final String planInfoId,final String status,
			final String message, final String nodeStepType, final String branch) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.SWITHOVER);
		if (id != null) {
			// id 流程节点编号
			// planInfoId 预案执行编号
			// status 完成状态
			// message 提交信息
			params.addParameter("id", id);
			params.addParameter("planInfoId", planInfoId);
			params.addParameter("status", status);
			params.addParameter("message", message);
			params.addParameter("nodeStepType", nodeStepType);
			if(nodeStepType.equals("ExclusiveGateway"))
				params.addParameter("branch", branch);
		}
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("SwitchOverParser", "SwitchOverParser" + t);
				map = planChangeParse(t);
				Log.i("SwitchOverParser", "SwitchOverParser" + map);
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
						request(id, planInfoId, status,
								message, nodeStepType, branch);
					}
					responseMsg = httpEx.getMessage();
					errorResult = httpEx.getResult();
				} else { //其他错误

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
	 * 任务切换解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> planChangeParse(String t) {
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
