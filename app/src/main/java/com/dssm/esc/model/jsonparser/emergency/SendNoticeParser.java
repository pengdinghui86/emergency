package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.SendNoticyEntity;
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
 * 发送通告解析
 * 
 * @author zsj
 * 
 */
public class SendNoticeParser {
	public Map<String, String> map;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public SendNoticeParser(SendNoticyEntity noticyEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(noticyEntity);
	}

	/**
	 * 发送请求
	 */
	public void request(final SendNoticyEntity noticyEntity) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.SENDNOTICE);
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
		//		id	接收人岗位标识	逗号隔开
		//		sendType	发送方式	0系统，1邮件，2短信，3 APP，逗号隔开
		//		content	通知内容
		//		busType	通知类型	协同:busType='collaborNotice',
		//		通告:busType='displayNotice'
		//		planInfoId	预案执行编号
		//		coorStage	协同--阶段
		//		sendObj	协同—对象	发送对象为对监管时启用，JSONArray类型，包含输入的联系号码和邮箱
		params.addParameter("id", noticyEntity.getId());
		params.addParameter("sendType", noticyEntity.getSendType());
		params.addParameter("content", noticyEntity.getContent());

		params.addParameter("busType", noticyEntity.getBusType());
		params.addParameter("planInfoId", noticyEntity.getPlanInfoId());

		params.addParameter("coorStage", noticyEntity.getCoorStage());
		params.addParameter("sendObj", noticyEntity.getSendObj());

		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("SendNoticeParser", "SendNoticeParser" + t);
				map = assignParser(t);
				Log.i("SendNoticeParser", "SendNoticeParser" + map);
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
						request(noticyEntity);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(noticyEntity);
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
	 * 发送通告解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> assignParser(String t) {
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
