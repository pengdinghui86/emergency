package com.dssm.esc.model.jsonparser.message;

import android.content.Context;
import android.util.Log;

import com.dssm.esc.model.entity.message.FirstAllMessagesEntity;
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
 * 获取信息列表解析
 * 
 * @author zsj
 * 
 */
public class GetFirstAllMessageListParser {
	private List<FirstAllMessagesEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetFirstAllMessageListParser(Context context,String msgType, String isconfirm,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(context,msgType, isconfirm);
	}

	/**
	 * 发送请求
	 * 
	 * @param msgTypea
	 *            1,任务通知2,系统通知3,紧急通知4,我的消息
	 * @param isconfirm
	 *            是否收到通知,true：查询已经接收确认过的消息，false：查询没有接收确认过的消息，不传递此参数查询全部
	 */
	public void request(final Context context,final String msgType, final String isconfirm) {

		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETFIRSTALLMESSAGES + "?isconfirm=" + isconfirm
				, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "t" + t);
				Log.i("onFailure", "strMsg" + strMsg);
				Log.i("onFailure", "errorNo" + errorNo);
				//strMsgresponse status error code:518
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(context,msgType,isconfirm);
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
//				boolean save = Utils.getInstance()
//						.save(context, t,Const.TOAST_TEXT);// 将json字符串保存到文件中
//				if (save) {
//					list = Utils.getInstance().getFirstMessageListParser(msgType,t);
//				}
				list = getFirstMessageListParser(msgType,t);
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
