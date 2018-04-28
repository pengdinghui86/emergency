package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.SendNoticyEntity;
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
 * 发送通告解析
 * 
 * @author zsj
 * 
 */
public class SendNoticeParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public SendNoticeParser(SendNoticyEntity noticyEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(noticyEntity);
	}

	/**
	 * 发送请求
	 */
	public void request(final SendNoticyEntity noticyEntity) {

		AjaxParams params = new AjaxParams();
//		id	接收人岗位标识	逗号隔开
//		sendType	发送方式	0系统，1邮件，2短信，3 APP，逗号隔开
//		content	通知内容	
//		busType	通知类型	协同:busType='collaborNotice',
//		通告:busType='displayNotice'
//		planInfoId	预案执行编号	
//		coorStage	协同--阶段	
//		sendObj	协同—对象	发送对象为对监管时启用，JSONArray类型，包含输入的联系号码和邮箱
			params.put("id", noticyEntity.getId());
			params.put("sendType", noticyEntity.getSendType());
			params.put("content", noticyEntity.getContent());
			
			params.put("busType", noticyEntity.getBusType());
			params.put("planInfoId", noticyEntity.getPlanInfoId());
			
			params.put("coorStage", noticyEntity.getCoorStage());
			params.put("sendObj", noticyEntity.getSendObj());
			

		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.SENDNOTICE, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(noticyEntity);
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
				map = assignParser(t);
				Log.i("SendNoticeParser", "SendNoticeParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

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
