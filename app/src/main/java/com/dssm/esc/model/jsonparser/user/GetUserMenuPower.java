package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.entity.user.ButtonEntity;
import com.dssm.esc.model.entity.user.MenuEntity;
import com.dssm.esc.model.entity.user.UserPowerEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	FinalHttp finalHttp;
	OnDataCompleterListener OnUserParseLoadCompleteListener;

	public GetUserMenuPower(
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnUserParseLoadCompleteListener = completeListener;
		request();
	}

	/**
	 * 
	 * 发送请求
	 * 
	 * @param userEntity
	 */
	public void request() {

		
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETMENUPOWER, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnUserParseLoadCompleteListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request();
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
				Log.i("GetUserMenuPower", t);
				MyCookieStore.setcookieStore(finalHttp);
				powerEntity = getUserMenuPower(t);
				Log.i("GetUserMenuPower", "GetUserMenuPower" + powerEntity);
				OnUserParseLoadCompleteListener.onEmergencyParserComplete(
						powerEntity, null);

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
