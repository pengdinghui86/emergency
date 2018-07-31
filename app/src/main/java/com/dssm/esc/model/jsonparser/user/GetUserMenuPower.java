package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.entity.user.ButtonEntity;
import com.dssm.esc.model.entity.user.MenuEntity;
import com.dssm.esc.model.entity.user.UserPowerEntity;
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
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 获取用户权限控制
 * 
 * @author zsj
 * 
 */
public class GetUserMenuPower {

	public UserPowerEntity powerEntity;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetUserMenuPower(
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request();
	}

	/**
	 * 
	 * 发送请求
	 *
	 */
	public void request() {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETMENUPOWER);
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
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i("GetUserMenuPower", t);
				powerEntity = getUserMenuPower(t);
				Log.i("GetUserMenuPower", "GetUserMenuPower" + powerEntity);
				if(onUserParseLoadCompleteListener != null)
					onUserParseLoadCompleteListener.onEmergencyParserComplete(
						powerEntity, null);

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				String responseMsg = "";
				String errorResult = ex.toString();
				if (ex instanceof HttpException) { //网络错误
					errorResult = "网络错误";
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					if(responseCode == 518) {
						errorResult = "登录超时";
						Utils.getInstance().relogin();
						if(DemoApplication.sessionTimeoutCount < 5)
							request();
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request();
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
	public UserPowerEntity getUserMenuPower(String t) {
		UserPowerEntity powerEntity = new UserPowerEntity();
		List<MenuEntity> menuList=new ArrayList<MenuEntity>();
		List<ButtonEntity> btlist=new ArrayList<ButtonEntity>();
		try {
			JSONObject jsonObject = new JSONObject(t);
			JSONArray jsonArray = jsonObject.getJSONArray("menu");
			for (int i = 0; i < jsonArray.length(); i++) {
				MenuEntity menuEntity = new MenuEntity();
				JSONObject jsonObject2 =(JSONObject) jsonArray.opt(i);
				menuEntity.setName(jsonObject2.getString("name"));
				menuEntity.setMark(jsonObject2.getString("mark"));
				menuEntity.setCode(jsonObject2.getString("code"));
				menuList.add(menuEntity);
			}
			powerEntity.setMenu(menuList);
			JSONArray jsonArray2 = jsonObject.getJSONArray("btns");
			for (int i = 0; i < jsonArray2.length(); i++) {
				ButtonEntity buttonEntity = new ButtonEntity();
				JSONObject jsonObject2 =(JSONObject) jsonArray2.opt(i);
				buttonEntity.setBtnName(jsonObject2.getString("btnName"));
				buttonEntity.setMenuMark(jsonObject2.getString("menuMark"));
				buttonEntity.setBtnMark(jsonObject2.getString("btnMark"));
				buttonEntity.setMenuName(jsonObject2.getString("menuName"));
				btlist.add(buttonEntity);
			}
			powerEntity.setBtns(btlist);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return powerEntity;
	}
}
