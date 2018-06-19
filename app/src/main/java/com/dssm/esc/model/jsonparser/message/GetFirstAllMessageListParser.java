package com.dssm.esc.model.jsonparser.message;

import android.content.Context;
import android.util.Log;

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
 * 获取信息列表解析
 * 
 * @author zsj
 * 
 */
public class GetFirstAllMessageListParser {
	private List<FirstAllMessagesEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetFirstAllMessageListParser(Context context,String msgType, String isconfirm,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(context,msgType, isconfirm);
	}

	/**
	 * 发送请求
	 * 
	 * @param context
	 * @param msgType
	 *            1,任务通知2,系统通知3,紧急通知4,我的消息
	 * @param isconfirm
	 *            是否收到通知,true：查询已经接收确认过的消息，false：查询没有接收确认过的消息，不传递此参数查询全部
	 */
	public void request(final Context context,final String msgType, final String isconfirm) {
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETFIRSTALLMESSAGES + "?isconfirm=" + isconfirm);
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
				Log.i("GetMessageListParser", t);
//				boolean save = Utils.getInstance()
//						.save(context, t,Const.TOAST_TEXT);// 将json字符串保存到文件中
//				if (save) {
//					list = Utils.getInstance().getFirstMessageListParser(msgType,t);
//				}
				list = getFirstMessageListParser(msgType,t);
				Log.i("GetMessageListParser", "GetMessageListParser" + list);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(list,
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
                        request(context,msgType,isconfirm);
                    }
                    responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(context,msgType,isconfirm);
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
	 * 
	 * @param msgType
	 *            app消息类型（1：任务通知，2：系统通知，3：我的消息，4：紧急通知）
	 * @param t
	 * @return
	 * @throws JSONException
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
