package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 暂停和开启
 * 2017/10/13
 */
public class PausePlanParser {
	String TAG = "PausePlanParser";
	public Map<String, String> map;
	private FinalHttp finalHttp;
	private ControlCompleterListenter<Map<String, String>> completerListenter;

	public PausePlanParser(String id, String planInfoId, String stopOrStart,
                           ControlCompleterListenter<Map<String, String>> completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
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
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		params.put("planInfoId", planInfoId);
		params.put("stopOrStart", stopOrStart);

		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.PAUSEPLAN, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				completerListenter.controlParserComplete(null, strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo == 518) {
					Utils.getInstance().relogin();
					request(id, planInfoId,stopOrStart);
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
				MyCookieStore.setcookieStore(finalHttp);
				Log.i(TAG, TAG + t);
				map = setStarPlanParser(t);
				Log.i(TAG, TAG + map);
				completerListenter.controlParserComplete(map, null);

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
