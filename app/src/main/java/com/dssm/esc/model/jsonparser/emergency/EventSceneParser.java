package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
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
 * 事件场景解析类
 * @author zsj
 *
 */
public class EventSceneParser {
	private List<BusinessTypeEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public EventSceneParser(OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request();
	}

	/**
	 * 
	 * 发送请求
	 * 
	 * @param userEntity
	 */
	@SuppressWarnings("unchecked")
	public void request() {
		
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GET_EVENTSCENCE_FIELDS, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request();
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
				Log.i("EventSceneParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = businessTypeParser(t);
				Log.i("EventSceneParser", "EventSceneParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

			}

		});
	}

	/**
	 * 事件场景数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<BusinessTypeEntity> businessTypeParser(String t) {
		List<BusinessTypeEntity> list = new ArrayList<BusinessTypeEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					BusinessTypeEntity eventSceneEntity = new BusinessTypeEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					eventSceneEntity.setId(jsonObject2.getString("id"));
					eventSceneEntity.setName(jsonObject2.getString("sceneName"));
					list.add(eventSceneEntity);
				}
			} else {
				return list;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}

		return list;
	}


}
