package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户退出登录解析
 * 
 * @author zsj
 * 
 */
public class UserLogoutParser {
	public Map<String, String> map;
	private final WeakReference<OnDataCompleterListener> wr;

	public UserLogoutParser(String userId,
                            OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(userId);
	}

	/**
	 * 发送请求
	 * 
	 * @param userId 用户id
	 */
	public void request(final String userId) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl() + HttpUrl.LOGOUT);
		params.setReadTimeout(60 * 1000);
		//增加session
		if(!MySharePreferencesService.getInstance(
				DemoApplication.getInstance().getApplicationContext()).getcontectName(
				"JSESSIONID").equals("")) {
			StringBuilder sbSession = new StringBuilder();
			sbSession.append("JSESSIONID").append("=")
					.append(MySharePreferencesService.getInstance(
							DemoApplication.getInstance().getApplicationContext()).getcontectName(
							"JSESSIONID")).append("; path=/; domain=")
					.append(MySharePreferencesService.getInstance(
							DemoApplication.getInstance().getApplicationContext()).getcontectName(
							"DOMAIN"));
			params.addHeader("Cookie", sbSession.toString());
		}
		final OnDataCompleterListener onUserParseLoadCompleteListener = wr.get();
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				map = logoutParse(t);
				if(onUserParseLoadCompleteListener != null)
					onUserParseLoadCompleteListener.onEmergencyParserComplete(map,
						null);

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

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
	 * 用户退出登录解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> logoutParse(String t) {
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
