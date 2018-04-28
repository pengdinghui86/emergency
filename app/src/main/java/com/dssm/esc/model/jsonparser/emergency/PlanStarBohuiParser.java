package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
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
 * 预案驳回解析
 * 
 * @author zsj
 * 
 */
public class PlanStarBohuiParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanStarBohuiParser(String planEveId, String planEveName,
			String submitterId, String eveType,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(planEveId, planEveName, submitterId, eveType);
	}

	/**
	 * 发送请求
	 * 
	 * @param planEveId
	 *            预案id
	 * @param planEveName
	 *            预案名称
	 * @param submitterId
	 *            事件提交人ID
	 * @param eveType
	 *            事件类型
	 */
	public void request(final String planEveId,final String planEveName,final String submitterId,
			final String eveType) {
//		planEveId	事件ID	
//		planEveName	事件名称	
//		submitterId	事件提交人	发送通知使用
//		eveType	事件类型	发送通知使用
		AjaxParams params = new AjaxParams();
		if (planEveId != null) {

			params.put("planEveId", planEveId);
			params.put("planEveName", planEveName);
			params.put("submitterId", submitterId);
			params.put("eveType", eveType);
		}
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.PLANSTARD_BOHUI, params,
				new AjaxCallBack<String>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);

						OnEmergencyCompleterListener
								.onEmergencyParserComplete(null, strMsg);
						Log.i("onFailure", "strMsg" + strMsg);
						if (errorNo==518) {
							Utils.getInstance().relogin();
							request(  planEveId,  planEveName, submitterId,
									  eveType);
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
						Log.i("PlanStarBohuiParser", "PlanStarBohuiParser" + t);
						map = planStarBohuiParser(t);
						Log.i("PlanStarBohuiParser", "PlanStarBohuiParser"
								+ map);
						OnEmergencyCompleterListener
								.onEmergencyParserComplete(map, null);

					}

				});

	}

	/**
	 * 预案驳回解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> planStarBohuiParser(String t) {
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