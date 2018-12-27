package com.dssm.esc.model.jsonparser.message;

import com.dssm.esc.model.entity.message.HistoryNoticeEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 获取历史通知列表解析
 */
public class GetHistoryNoticeListParser {
	private List<HistoryNoticeEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetHistoryNoticeListParser(String msgType,
                                      OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(msgType);
	}

	/**
	 * @param msgType
	 * 不传查全部，0系统，1邮件，2短信，3APP
	 */
	public void request(final String msgType) {
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.HISTORYNOTICE + msgType);
		params.setReadTimeout(60 * 1000);
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
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				list = historyNoticeListParser(msgType,t);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(list,
						null);

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
						if(DemoApplication.sessionTimeoutCount < 5)
							request(msgType);
                    }
                    responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
                	Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(msgType);
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
	 * 获取历史通知数据解析
	 */
	public List<HistoryNoticeEntity> historyNoticeListParser(String msgType,
			String t) {
		ArrayList<HistoryNoticeEntity> list = new ArrayList<HistoryNoticeEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
					HistoryNoticeEntity entity = new HistoryNoticeEntity();
					entity.setCreateTime(jsonObject
							.getString("createTime"));
					entity.setSendType(jsonObject
							.getString("sendType"));
					entity.setMessage(jsonObject
							.getString("message"));
					entity.setId(jsonObject
							.getString("id"));
					entity.setSender(jsonObject
							.getString("sender"));
					entity.setSenderId(jsonObject
							.getString("senderId"));
					entity.setCreateUser(jsonObject
							.getString("createUser"));
					entity.setReceiver(jsonObject
							.getString("receiver"));
					entity.setReceiverId(jsonObject
							.getString("receiverId"));
					list.add(entity);
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
