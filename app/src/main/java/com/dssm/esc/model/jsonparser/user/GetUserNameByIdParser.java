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
 * 根据用户id获取用户名
 * 
 * @author zsj
 * 
 */
public class GetUserNameByIdParser {
	private Map<String, String> map;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetUserNameByIdParser(String userId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
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

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETUSERNAMEBYID + usrId);
		x.http().get(params, new Callback.CommonCallback<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						Log.i("GetUserNameById", t);
						 map = getUserNameByid(t);
						Log.i("GetUserNameById", "GetUserNameById" + map);
						OnEmergencyCompleterListener.onEmergencyParserComplete(
								map, null);

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
						request(usrId);
					}
					responseMsg = httpEx.getMessage();
					errorResult = httpEx.getResult();
				} else { //其他错误

				}
				OnEmergencyCompleterListener.onEmergencyParserComplete(null, errorResult);
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
