package com.dssm.esc.model.jsonparser.contact;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 发送紧急通知消息
 * @author zsj
 *
 */
public class SendNoticeParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public SendNoticeParser(String postId, String sendType, 
			String content, OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(postId, sendType,content);
	}

	/**
	 * 发送请求
	 * 
	 * @param id
	 * @param suspendType
	 * @param planSuspendOpition2
	 */
	public void request(final String postId, final String sendType,
			final String content) {

		AjaxParams params = new AjaxParams();
		if (postId != null) {
//			id	接收人岗位标识	用逗号隔开
//			sendType	发送方式	0系统，1邮件，2短信，3 APP，逗号隔开
//			content	通知内容	
			params.put("id", postId);
			params.put("sendType", sendType);
			params.put("content", content);
		}
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.SENDNOTICECONTACT, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(postId,sendType,content);
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
				MyCookieStore.setcookieStore(finalHttp);
				Log.i("SendNoticeParser", "SendNoticeParser" + t);
				map = sendMessageParse(t);
				Log.i("SendNoticeParser", "SendNoticeParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

			}

		});

	}

	/**
	 * 发送消息解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> sendMessageParse(String t) {
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
