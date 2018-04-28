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
 * 用户选择角色解析
 * 
 * @author zsj
 * 
 */
public class UserLoginRoleParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnUserParseLoadCompleteListener;

	public UserLoginRoleParser(String roleId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnUserParseLoadCompleteListener = completeListener;
		request(roleId);
	}

	/**
	 * 发送请求
	 * 
	 * @param roleId
	 *            角色id
	 */
	public void request(final String roleId) {

		AjaxParams params = new AjaxParams();
		if (roleId != null) {

			params.put("roleId", roleId);
		}
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.LOGINROLE, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnUserParseLoadCompleteListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(roleId);
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
				Log.i("UserLoginRoleParser", "UserLoginRoleParser" + t);
				map = loginRoleParse(t);
				Log.i("UserLoginRoleParser", "UserLoginRoleParser" + map);
				OnUserParseLoadCompleteListener.onEmergencyParserComplete(map,
						null);

			}

		});

	}

	/**
	 * 用户角色选择解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> loginRoleParse(String t) {
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
