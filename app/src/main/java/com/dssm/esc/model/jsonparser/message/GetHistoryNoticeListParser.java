package com.dssm.esc.model.jsonparser.message;

import com.dssm.esc.model.entity.message.FirstAllMessagesEntity;
import com.dssm.esc.model.entity.message.MessageInfoEntity;
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
	private List<FirstAllMessagesEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetHistoryNoticeListParser(String msgType,
                                      OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(msgType);
	}

	/**
	 * @param msgType
	 * 0全部，1短信，2系统，3邮件，4APP
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
				list = getFirstMessageListParser(msgType,t);
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
	 * 获取消息列表数据解析
	 */
	public List<FirstAllMessagesEntity> getFirstMessageListParser(String msgType,
			String t) {
		ArrayList<FirstAllMessagesEntity> alist = new ArrayList<FirstAllMessagesEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				ArrayList<MessageInfoEntity> list = new ArrayList<MessageInfoEntity>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
					FirstAllMessagesEntity allMessagesEntity = new FirstAllMessagesEntity();
//					if (jsonObject.getString("msgType").toString()
//							.equals(msgType.toString())) {
						allMessagesEntity.setUnreadCount(jsonObject
								.getString("unreadCount"));
						allMessagesEntity.setMsgType(jsonObject
								.getString("msgType"));
						allMessagesEntity.setMsgType(msgType);
						
						JSONArray jsonArray2 = jsonObject.getJSONArray("list");
						
						for (int j = 0; j < jsonArray2.length(); j++) {
							MessageInfoEntity listEntity = new MessageInfoEntity();
							JSONObject jsonObject2 = jsonArray2
									.getJSONObject(j);
							listEntity.setMessageId(jsonObject2.getString("id"));
							listEntity.setSenderId(jsonObject2
									.getString("senderId"));
							listEntity.setSender(jsonObject2
									.getString("sender"));
							listEntity.setTime(jsonObject2
									.getString("createTime"));
							listEntity.setMessage(jsonObject2
									.getString("message"));
							listEntity.setReceiverId(jsonObject2
									.getString("receiverId"));
							listEntity.setReceiver(jsonObject2
									.getString("receiver"));
							listEntity.setEveName(jsonObject2
									.getString("eveName"));
							listEntity.setPlanName(jsonObject2
									.getString("planName"));
							listEntity.setEveType(jsonObject2
									.getString("eveType"));
							listEntity.setSender(jsonObject2
									.getString("sender"));
							listEntity.setModelFlag(jsonObject2
									.getString("modelFlag"));
							list.add(listEntity);
						}
						allMessagesEntity.setList(list);
						alist.add(allMessagesEntity);
					}
				//}
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return alist;
		}
		return alist;

	}
	
}
