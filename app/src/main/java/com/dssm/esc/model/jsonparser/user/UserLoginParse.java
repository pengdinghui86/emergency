package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.entity.user.UserAttributesEntity;
import com.dssm.esc.model.entity.user.UserEntity;
import com.dssm.esc.model.entity.user.UserLoginObjEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;


/**
 * 登陆访问网络，解析数据
 * 
 * @author Administrator
 * 
 */
public class UserLoginParse {
	public UserEntity userEntity;
	OnDataCompleterListener OnUserParseLoadCompleteListener;

	public UserLoginParse(String userName, String password,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnUserParseLoadCompleteListener = completeListener;
		request(userName, password);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final String userName, final String password) {
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.LOGIN);
		params.addParameter("loginName", userName);
		params.addParameter("password", password);
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("LoginParse", t);
				userEntity = loginParse(t, userName, password);
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
				Log.i("LoginParse", "LoginParse" + userEntity);
				OnUserParseLoadCompleteListener.onEmergencyParserComplete(
						userEntity, null);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

				String responseMsg = "";
				String errorResult = ex.toString();
				if (ex instanceof HttpException) { //网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
//					if(responseCode == 518) {
//						Utils.getInstance().relogin();
//						request(userName,password);
//					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else { //其他错误
					errorResult = "其他错误";
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
	 * 用户登录解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public UserEntity loginParse(String t, String userName, String password) {
		UserEntity userEntity = new UserEntity();
		userEntity.setUsername(userName);
		userEntity.setPassword(password);
		try {
			JSONObject jsonObject = new JSONObject(t);
			if (t.contains("success")) {
				userEntity.setSuccess(jsonObject.getString("success"));
				userEntity.setMessage(jsonObject.getString("message"));
				if (jsonObject.getString("success").equals("true")) {
					List<UserLoginObjEntity> obj = new ArrayList<UserLoginObjEntity>();
					JSONArray jsonArray = jsonObject.getJSONArray("obj");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
						UserLoginObjEntity objEntity = new UserLoginObjEntity();

						objEntity.setRoleId(jsonObject2.getString("roleId"));
						objEntity
								.setRoleName(jsonObject2.getString("roleName"));
						objEntity
						.setRoleCode(jsonObject2.getString("roleCode"));
						obj.add(objEntity);
					}
					userEntity.setObj(obj);
				} else if (jsonObject.getString("success").equals("false")) {
					userEntity.setObjString(jsonObject.getString("obj"));
					return userEntity;
				}
				if (jsonObject.has("attributes")) {
					JSONObject jsonObject2 =jsonObject.getJSONObject("attributes") ;
					UserAttributesEntity attributesEntity = new UserAttributesEntity();
					attributesEntity.setId(jsonObject2.getString("id"));// 用户id
					// attributesEntity.setLoginName(jsonObject2
					// .getString("loginName"));// 用户名
					// attributesEntity.setPassword(jsonObject2.getString("password"));//
					// 密码
					attributesEntity.setName(jsonObject2.getString("name"));// 姓名
					attributesEntity.setRoleIds(jsonObject2.getString("roleIds"));// 角色id
					attributesEntity.setRoleNames(jsonObject2
							.getString("roleNames"));// 角色
					attributesEntity.setPostId(jsonObject2.getString("postId"));// 岗位id
					attributesEntity.setPostFlag(jsonObject2.getString("postFlag"));// 岗位标识
					attributesEntity.setEmpCode(jsonObject2.getString("empCode"));// 用户编号
					attributesEntity.setBusinessTypeId(jsonObject2
							.getString("businessTypeId"));// 行业类型id
					attributesEntity.setBusinessTypeName(jsonObject2
							.getString("businessTypeName"));// 行业类型
					
					userEntity.setAttributes(attributesEntity);
				}
				return userEntity;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return userEntity;
		}

		return userEntity;
	}
}
