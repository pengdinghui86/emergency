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
 * 业务类型和事件等级解析类
 * 
 * @author zsj
 * 
 */
public class BusinessTypeParser {
	private List<BusinessTypeEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public BusinessTypeParser(int tag,OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(tag);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final int tag) {
		String url=null;
		if (tag==1) {//业务类型
			url= DemoApplication.getInstance().getUrl()+HttpUrl.GET_BUSINESSTYPE;
		}else if (tag==2) {//事件等级
			url=DemoApplication.getInstance().getUrl()+HttpUrl.GET_EVENTLEVEL;
		}
		finalHttp.get(url, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(tag);
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
				Log.i("BusinessTypeParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = businessTypeParser(t);
				Log.i("BusinessTypeParser", "BusinessTypeParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

			}

		});
	}

	/**
	 * 业务类型和事件等级数据解析
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
					BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					businessTypeEntity.setId(jsonObject2.getString("id"));
					businessTypeEntity.setName(jsonObject2.getString("name"));
					list.add(businessTypeEntity);
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
