package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.ActivityCollector;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户重新登录
 * @author zsj
 *
 */
public class UserReLoginParser {
	public Map<String, String> map;
	private final WeakReference<OnDataCompleterListener> wr;

	public UserReLoginParser(String userName, String password,String roleId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
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
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.RELOGIN);
		params.setReadTimeout(60 * 1000);
		if (roleId != null) {
			params.addParameter("loginName", userName);
			params.addParameter("password", password);
			params.addParameter("roleId", roleId);
		}
		final OnDataCompleterListener onUserParseLoadCompleteListener = wr.get();
		DemoApplication.sessionTimeoutCount++;
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("UserReLoginParser", "UserReLoginParser" + t);
				map = reloginParse(t);
				if ("true".equals(map.get("success"))) {
					DemoApplication.sessionTimeoutCount = 0;
					// 保存cookie的值
					DbCookieStore instance = DbCookieStore.INSTANCE;
					List<HttpCookie> cookies = instance.getCookies();
					for (int i = 0; i < cookies.size(); i++) {
						HttpCookie cookie = cookies.get(i);
						if (cookie.getName() != null && cookie.getName().equals("JSESSIONID")) {
							MySharePreferencesService.getInstance(
									DemoApplication.getInstance().getApplicationContext()).saveContactName(
									"JSESSIONID", cookie.getValue());
							MySharePreferencesService.getInstance(
									DemoApplication.getInstance().getApplicationContext()).saveContactName(
									"DOMAIN", cookie.getDomain());
							Log.i("session name --> ", cookie.getName());
							Log.i("session value --> ", cookie.getValue());
							Log.i("session domain --> ", cookie.getDomain());
							break;
						}
					}
					Log.i("UserReLoginParser", "UserReLoginParser" + map);
				}
				if (onUserParseLoadCompleteListener != null)
					onUserParseLoadCompleteListener.onEmergencyParserComplete(map,
							null);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				if(DemoApplication.sessionTimeoutCount > 1) {
					DemoApplication.getInstance().return2Login();
					return;
				}
				String errorResult = "";
				if (ex instanceof HttpException) { //网络错误
					errorResult = "网络错误";
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(onUserParseLoadCompleteListener != null)
					onUserParseLoadCompleteListener.onEmergencyParserComplete(null, errorResult);
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

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
