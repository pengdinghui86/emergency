package com.dssm.esc.model.jsonparser.user;

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
 * 用户重新登录
 * @author zsj
 *
 */
public class UserReLoginParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnUserParseLoadCompleteListener;

	public UserReLoginParser(String userName, String password,String roleId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnUserParseLoadCompleteListener = completeListener;
		request(userName,password,roleId);
	}

	/**
	 * 发送请求
	 * 
	 * @param roleId
	 *            角色id
	 */
	public void request( final String userName, final String password,final String roleId) {
//		loginName	用户名	
//		password	密码	
//		roleId	角色ID	
		AjaxParams params = new AjaxParams();
		if (roleId != null) {
			params.put("loginName", userName);
			params.put("password", password);
			params.put("roleId", roleId);
		}
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.RELOGIN, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnUserParseLoadCompleteListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(userName,password,roleId);
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
				Log.i("UserReLoginParser", "UserReLoginParser" + t);
				map = reloginParse(t);
				Log.i("UserReLoginParser", "UserReLoginParser" + map);
				OnUserParseLoadCompleteListener.onEmergencyParserComplete(map,
						null);

			}

		});

	}

	/**
	 * 用户重新登录解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> reloginParse(String t) {
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
