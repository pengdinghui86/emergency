package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.entity.user.UserObjEntity;
import com.dssm.esc.model.entity.user.UserPersonIdEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 实时跟踪查询执行人电话
 * @author zsj
 *
 */
public class GetPhoneByExecuteParser {


	public UserPersonIdEntity entity;
	FinalHttp finalHttp;
	OnDataCompleterListener OnUserParseLoadCompleteListener;

	public GetPhoneByExecuteParser(String executePeopleId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnUserParseLoadCompleteListener = completeListener;
		request(executePeopleId);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 * @param userEntity
	 */
	public void request(final String executePeopleId) {

		
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.QUERYUSERBYEXECUTEPEOPELID+executePeopleId, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnUserParseLoadCompleteListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(executePeopleId);
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
				Log.i("GetPhoneByExecuteParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				entity = getUserMessage(t);
				Log.i("GetPhoneByExecuteParser", "GetPhoneByExecuteParser" + entity);
				OnUserParseLoadCompleteListener.onEmergencyParserComplete(
						entity, null);

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
