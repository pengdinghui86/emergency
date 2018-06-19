package com.dssm.esc.model.jsonparser.message;

import android.content.Context;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.List;


/**
 * 获取消息列表
 * 
 * @author zsj
 * 
 */
public class GetMessageListParser {
	private List<MessageInfoEntity> list;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetMessageListParser(Context context, String msgType,
			String isconfirm,String tag,

			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(context, msgType, isconfirm,tag);
	}

	/**
	 * 发送请求
	 * 
	 * @param context
	 * @param msgType
	 *            1,任务通知2,系统通知3,紧急通知4,我的消息
	 */
	public void request(final Context context, final String msgType,
			final String isconfirm,final String tag) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETMESSAGE + "?isconfirm=" + isconfirm + "&msgType=" + msgType);
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
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("GetMessageListParser", t);
				list = getMessageListParser(t,tag);
				Log.i("GetMessageListParser", "GetMessageListParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				String responseMsg = "";
				String errorResult = "";
				if (ex instanceof HttpException) { //网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					if(responseCode == 518) {
						Utils.getInstance().relogin();
						request(context, msgType, isconfirm, tag);
					}
//					responseMsg = httpEx.getMessage();
//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(context, msgType, isconfirm, tag);
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
	 * 获取消息列表数据解析
	 *
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<MessageInfoEntity> getMessageListParser(String t,String tag) {
		ArrayList<MessageInfoEntity> list = new ArrayList<MessageInfoEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					MessageInfoEntity listEntity = new MessageInfoEntity();

					listEntity
							.setMessageId(isNull(jsonObject2.getString("id")));
					listEntity.setSenderId(isNull(jsonObject2
							.getString("senderId")));
					listEntity
							.setSender(isNull(jsonObject2.getString("sender")));
					listEntity.setTime(isNull(jsonObject2
							.getString("createTime")));
					if (tag.equals("4")) {//个人消息，加上发送人
						listEntity.setMessage(isNull("【"+jsonObject2.getString("sender")+"】"+jsonObject2
								.getString("message")));
					}else {
						listEntity.setMessage(isNull(jsonObject2
								.getString("message")));
					}
					listEntity.setReceiverId(isNull(jsonObject2
							.getString("receiverId")));
					listEntity.setReceiver(isNull(jsonObject2
							.getString("receiver")));
					list.add(listEntity);
				}
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}
		return list;

	}

	private String isNull(String string) {
		if (!string.equals("") && string != null && !string.equals("null")
				&& string.length() > 0) {
			return string;
		} else {

			return "";
		}
	}
}
