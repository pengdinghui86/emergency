package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 根据对象和阶段获取对应的配置内容解析
 * 
 * @author zsj
 * 
 */
public class GetNotiConfigContentParser {
	private GetProjectEveInfoEntity entity;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetNotiConfigContentParser(String precautionId, String type,
			String stage, OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(precautionId, type, stage);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 * @param id
	 *            事件编号
	 */
	public void request(final String precautionId, final String type, final String stage) {
		// precautionId 预案id
		// type 发送对象
		// stage 阶段
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETNOTICONFIGCONTENT + "?precautionId="
				+ precautionId + "&type=" + type + "&stage=" + stage,
				new AjaxCallBack<String>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);

						OnEmergencyCompleterListener.onEmergencyParserComplete(
								null, strMsg);
						Log.i("onFailure", "strMsg" + strMsg);
						if (errorNo==518) {
							Utils.getInstance().relogin();
							request(precautionId,type,stage);
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
						Log.i("GetNotiConfigContentParser", t);
						MyCookieStore.setcookieStore(finalHttp);
						entity = getEventValuationParser(t);
						Log.i("GetNotiConfigContentParser",
								"GetNotiConfigContentParser" + entity);
						OnEmergencyCompleterListener.onEmergencyParserComplete(
								entity, null);

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
