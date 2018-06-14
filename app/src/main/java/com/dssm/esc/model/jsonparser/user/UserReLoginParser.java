package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户重新登录
 * @author zsj
 *
 */
public class UserReLoginParser {
	public Map<String, String> map;
	OnDataCompleterListener OnUserParseLoadCompleteListener;

	public UserReLoginParser(String userName, String password,String roleId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
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
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.RELOGIN);
		if (roleId != null) {
			params.addParameter("loginName", userName);
			params.addParameter("password", password);
			params.addParameter("roleId", roleId);
		}
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("UserReLoginParser", "UserReLoginParser" + t);
				map = reloginParse(t);
				Log.i("UserReLoginParser", "UserReLoginParser" + map);
				OnUserParseLoadCompleteListener.onEmergencyParserComplete(map,
						null);

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				String responseMsg = "";
				String errorResult = ex.toString();
				if (ex instanceof HttpException) { //网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					if(responseCode == 518) {
						Utils.getInstance().relogin();
					}
					responseMsg = httpEx.getMessage();
					errorResult = httpEx.getResult();
				} else { //其他错误

				}
				OnUserParseLoadCompleteListener.onEmergencyParserComplete(null, errorResult);
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
