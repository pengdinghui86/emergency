package com.dssm.esc.model.jsonparser.message;

import android.content.Context;
import android.util.Log;

import com.dssm.esc.model.entity.message.MessageInfoEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetMessageListParser(Context context, String msgType,
			String isconfirm,String tag,

			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(context, msgType, isconfirm,tag);
	}

	/**
	 * 发送请求
	 * 
	 * @param msgTypea
	 *            1,任务通知2,系统通知3,紧急通知4,我的消息
	 * @param pageIndex
	 * @param pageSize
	 */
	public void request(final Context context, final String msgType,
			final String isconfirm,final String tag) {

		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETMESSAGE + "?isconfirm=" + isconfirm
				+ "&msgType=" + msgType, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);

				Log.i("onFailure", "t" + t);
				Log.i("onFailure", "errorNo" + errorNo);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(context, msgType, isconfirm, tag);
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
				Log.i("GetMessageListParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = getMessageListParser(t,tag);
				Log.i("GetMessageListParser", "GetMessageListParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

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
