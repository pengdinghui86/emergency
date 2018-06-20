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


/**
 * 根据对象和阶段获取对应的配置内容解析
 * 
 * @author zsj
 * 
 */
public class GetNotiConfigContentParser {
	private GetProjectEveInfoEntity entity;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetNotiConfigContentParser(String precautionId, String type,
			String stage, OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(precautionId, type, stage);
	}

	/**
	 * 
	 * 发送请求
	 *
	 */
	public void request(final String precautionId, final String type, final String stage) {
		// precautionId 预案id
		// type 发送对象
		// stage 阶段
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETNOTICONFIGCONTENT + "?precautionId="
				+ precautionId + "&type=" + type + "&stage=" + stage);
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
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("GetNotiConfigContent", t);
				entity = getEventValuationParser(t);
				Log.i("GetNotiConfigContent",
						"GetNotiConfigContentParser" + entity);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(
						entity, null);

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

				String responseMsg = "";
				String errorResult = ex.toString();
				if (ex instanceof HttpException) { //网络错误
					errorResult = "网络错误";
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					if(responseCode == 518) {
						errorResult = "登录超时";
						Utils.getInstance().relogin();
						request(precautionId, type, stage);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					request(precautionId, type, stage);
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
	 * 根据对象和阶段获取对应的配置内容
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public GetProjectEveInfoEntity getEventValuationParser(String t) {
		GetProjectEveInfoEntity entity = new GetProjectEveInfoEntity();
		try {
			JSONObject jsonObject = new JSONObject(t);

			entity.setTradeTypeName(jsonObject.getString("content"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return entity;
	}

}
