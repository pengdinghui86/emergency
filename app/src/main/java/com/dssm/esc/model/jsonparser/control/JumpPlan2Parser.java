package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
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
 * 非“执行中”跳过此步骤
 * 
 *
 */
public class JumpPlan2Parser {
	String TAG = "JumpPlan2Parser";
	public Map<String, String> map;
	private ControlCompleterListenter<Map<String, String>> completerListenter;

	public JumpPlan2Parser(String id, String planInfoId,String stopOrStart,
                           ControlCompleterListenter<Map<String, String>> completeListener) {
		// TODO Auto-generated constructor stub
		this.completerListenter = completeListener;
		request(id, planInfoId,stopOrStart);
	}

	/**
	 * 发送请求
	 * 
	 * @param id

	 */
	public void request(final String id, final String planInfoId,final String stopOrStart) {
		// id 流程节点编号
		// planInfoId 预案执行编号
		// status 完成状态
		// message 提交信息
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.JUMPPLAN2);
		params.addParameter("id", id);
		params.addParameter("planInfoId", planInfoId);
		params.addParameter("stopOrStart", stopOrStart);
//		params.put("status", "3");
//		params.put("message", "流程控制管理员跳过执行");
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i(TAG, TAG + t);
				map = setStarPlanParser(t);
				Log.i(TAG, TAG + map);
				completerListenter.controlParserComplete(map, null);

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
						request(id, planInfoId,stopOrStart);
					}
					responseMsg = httpEx.getMessage();
					errorResult = httpEx.getResult();
				} else { //其他错误

				}
				completerListenter.controlParserComplete(null, errorResult);
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
	 * 预案跳过解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> setStarPlanParser(String t) {
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
