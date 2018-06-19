package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


/**
 * 事件重新评估解析
 * @author zsj
 *
 */
public class ReValuationEventParser {
	public Map<String, String> map;
	private final WeakReference<OnDataCompleterListener> wr;

	public ReValuationEventParser(GetProjectEveInfoEntity entity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(entity);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final GetProjectEveInfoEntity entity) {
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.RE_VALUTEEVENT);
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
		//		id	事件编号
		//		tradeType	行业类型ID
		//		eveLevel	事件等级ID
		//		eveDescription	事件描述
		//		eveScenarioId	事件场景ID	可不选
		//		eveScenarioName	事件场景名称	可为空
		//		eveName	事件名称
		//		dealAdvice	处置建议
		//		referPlan	参考预案	可以多选，以“|”隔开
		//		otherReferPlan	其他预案	可以多选，以“|”隔开
		//		categoryPlan	分类预案	可以多选，以“|”隔开
		params.addParameter("id", entity.getId());
		params.addParameter("tradeType", entity.getTradeTypeId());
		params.addParameter("eveLevel", entity.getEveLevelId());
		params.addParameter("eveDescription",entity.getEveDescription());
		params.addParameter("eveScenarioId",entity.getEveScenarioId());
		params.addParameter("eveScenarioName", entity.getEveScenarioName());
		params.addParameter("eveName", entity.getEveName());
		params.addParameter("dealAdvice", entity.getDealAdvice());
		params.addParameter("referPlan", entity.getReferPlanIds());
		params.addParameter("otherReferPlan", entity.getOtherReferPlanIds());
		params.addParameter("categoryPlan", entity.getCategoryPlanIds());
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("ReValuationEventParser", t);
				map = reValuationEventParser(t);
				Log.i("ReValuationEventParser",
						"ReValuationEventParser" + map);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener
						.onEmergencyParserComplete(map, null);

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
						request(entity);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(entity);
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
	 * 重新评估数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> reValuationEventParser(String t) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject(t);
			if (t.contains("success")) {
				map.put("success", object.getString("success"));
				map.put("message", object.getString("message"));
			} else {
				 return null;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

		return map;
	}

}
