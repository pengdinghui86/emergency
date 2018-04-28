package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 根据用户id获取用户名
 * 
 * @author zsj
 * 
 */
public class GetUserNameByIdParser {
	private Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetUserNameByIdParser(String userId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(userId);
	}

	/**
	 * 发送请求
	 * 
	 * @param usrId
	 *            用户id
	 */
	public void request(final String usrId) {

		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETUSERNAMEBYID + usrId,
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
							request(usrId);
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
						Log.i("GetUserNameById", t);
						MyCookieStore.setcookieStore(finalHttp);
						 map = getUserNameByid(t);
						Log.i("GetUserNameById", "GetUserNameById" + map);
						OnEmergencyCompleterListener.onEmergencyParserComplete(
								map, null);

					}

				});
	}

	/**
	 * 获取用户姓名
	 * 
	 * @param t
	 * @return
	 */
	public Map<String, String> getUserNameByid(String t) {

		Map<String, String>map=new HashMap<String, String>();
		try {
			JSONObject object=new JSONObject(t);
		if (t.contains("name")) {
		map.put("name", object.getString("name"));
			//return map;
		}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		return map;
	}

}
