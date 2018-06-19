package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.entity.user.UserObjEntity;
import com.dssm.esc.model.entity.user.UserPersonIdEntity;
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


/**
 * 实时跟踪查询执行人电话
 * @author zsj
 *
 */
public class GetPhoneByExecuteParser {

	public UserPersonIdEntity entity;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetPhoneByExecuteParser(String executePeopleId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(executePeopleId);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 * @param executePeopleId
	 */
	public void request(final String executePeopleId) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.QUERYUSERBYEXECUTEPEOPELID+executePeopleId);
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
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("GetPhoneByExecuteParser", t);
				entity = getUserMessage(t);
				Log.i("GetPhoneByExecuteParser", "GetPhoneByExecuteParser" + entity);
				if(onUserParseLoadCompleteListener != null)
					onUserParseLoadCompleteListener.onEmergencyParserComplete(
						entity, null);

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
						request(executePeopleId);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(executePeopleId);
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
	 * 用户获取权限解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public UserPersonIdEntity getUserMessage(String t) {
		UserPersonIdEntity  entity = new UserPersonIdEntity();
		try {
			JSONObject jsonObject = new JSONObject(t);
			entity.setSuccess(jsonObject.getString("success"));
			entity.setMessage(jsonObject.getString("message"));
			JSONObject jsonObject2 = jsonObject.getJSONObject("obj");
			UserObjEntity objEntity =new UserObjEntity();
			objEntity.setId(jsonObject2.getString("id"));
			objEntity.setUserId(jsonObject2.getString("userId"));
			objEntity.setSex(jsonObject2.getString("sex"));
			objEntity.setPostName(jsonObject2.getString("postName"));
			objEntity.setPhoneNumTwo(isNull(jsonObject2.getString("phoneNumTwo")));
			
			objEntity.setEmail(isNull(jsonObject2.getString("email")));
			objEntity.setName(isNull(jsonObject2.getString("name")));
			objEntity.setPhoneNumOne(isNull(jsonObject2.getString("phoneNumOne")));
			objEntity.setNumbuer(isNull(jsonObject2.getString("numbuer")));
			
			objEntity.setDepName(isNull(jsonObject2.getString("depName")));
			objEntity.setOrgName(isNull(jsonObject2.getString("orgName")));
			objEntity.setPostFlag(isNull(jsonObject2.getString("postFlag")));
			entity.setObj(objEntity);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return entity;
	}
	/**
	 * 判断服务器返回的数据是否为空
	 * @param string
	 * @return
	 */
		private String isNull(String string) {
			if (!string.equals("") && string != null && !string.equals("null")
					&& string.length() > 0) {
				return string;
			} else {

				return "";
			}
		}
}
