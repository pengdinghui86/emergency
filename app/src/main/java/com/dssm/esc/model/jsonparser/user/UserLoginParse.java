package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.entity.user.UserAttributesEntity;
import com.dssm.esc.model.entity.user.UserEntity;
import com.dssm.esc.model.entity.user.UserLoginObjEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	FinalHttp finalHttp;
	OnDataCompleterListener OnUserParseLoadCompleteListener;

	public UserLoginParse(String userName, String password,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnUserParseLoadCompleteListener = completeListener;
		request(userName, password);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final String userName, final String password) {

		AjaxParams params = new AjaxParams();
		// params.put("userName",userEntity.getUsername());
		// params.put("password", userEntity.getPassword());
		params.put("loginName", userName);
		params.put("password", password);
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.LOGIN, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnUserParseLoadCompleteListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(userName,password);
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
				Log.i("LoginParse", t);
				MyCookieStore.setcookieStore(finalHttp);
				userEntity = loginParse(t, userName, password);
				Log.i("LoginParse", "LoginParse" + userEntity);
				OnUserParseLoadCompleteListener.onEmergencyParserComplete(
						userEntity, null);

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
