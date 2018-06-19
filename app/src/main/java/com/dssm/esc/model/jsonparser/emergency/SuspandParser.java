package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
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

import java.util.HashMap;
import java.util.Map;


/**
 * 预案中止解析
 * @author zsj
 *
 */
public class SuspandParser {
	public Map<String, String> map;
	OnDataCompleterListener OnEmergencyCompleterListener;

	// id 编号
	// suspendType 中止类型 类型为authSuspend
	// planSuspendOpition2 预案中止原因
	public SuspandParser(PlanSuspandEntity suspandEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(suspandEntity);
	}

	/**
	 * 发送请求
	 * 
	 * @param suspandEntity 预案中止实体类
	 */
	public void request(final PlanSuspandEntity suspandEntity) {
		String url=DemoApplication.getInstance().getUrl()+HttpUrl.SUSPAND;
		RequestParams params = new RequestParams(url);
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
		//		id	预案ID
		//		suspendType	中止类型	启动时中止，类型为null
		//		planSuspendOpition	中止原因
		//		planName	预案名称	发送通知使用
		//		planResName	预案来源名称	发送通知使用
		//		planResType	预案来源类型	发送通知使用
		//		planId	预案ID	发送通知使用
		//		tradeTypeId	业务类型ID	发送通知使用
		//		eveLevelId	事件等级ID	发送通知使用
		//		planStarterId	预案启动人	发送通知使用
		//		planAuthorId	预案授权人	发送通知使用
		//		submitterId	事件提交人	发送通知使用
		params.addParameter("id", suspandEntity.getId());
		params.addParameter("suspendType", suspandEntity.getSuspendType());
		params.addParameter("planSuspendOpition", suspandEntity.getPlanSuspendOpition());
		params.addParameter("planName", suspandEntity.getPlanName());
		params.addParameter("planResName", suspandEntity.getPlanResName());

		params.addParameter("planResType", suspandEntity.getPlanResType());
		params.addParameter("planId", suspandEntity.getPlanId());
		params.addParameter("eveLevelId", suspandEntity.getEveLevelId());
		params.addParameter("planStarterId", suspandEntity.getPlanStarterId());
		params.addParameter("planAuthorId", suspandEntity.getPlanAuthorId());
		params.addParameter("submitterId", suspandEntity.getSubmitterId());

		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("SuspandParser", "SuspandParser" + t);
				map = loginRoleParse(t);
				Log.i("SuspandParser", "SuspandParser" + map);
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
						request(suspandEntity);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(suspandEntity);
				} else { //其他错误
					errorResult = "其他错误";
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
	 * 预案中止解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> loginRoleParse(String t) {
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
