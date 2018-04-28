package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.DrillProjectNameEntity;
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
 * 演练计划表解析类
 * 
 * @author zsj
 * 
 */
public class DrillProjectNameParser {
	private List<DrillProjectNameEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public DrillProjectNameParser(
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request();
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void request() {

		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GET_DRILLPROJECTNAME, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
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
				Log.i("DrillProjectNameParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = drillProjectNameParser(t);
				Log.i("DrillProjectNameParser", "DrillProjectNameParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

			}

		});
	}

	/**
	 * 演练计划解析
	 * 
	 * @param t
	 * @return
	 */
	public List<DrillProjectNameEntity> drillProjectNameParser(String t) {
		List<DrillProjectNameEntity> list = new ArrayList<DrillProjectNameEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					DrillProjectNameEntity drillProjectNameEntity = new DrillProjectNameEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					drillProjectNameEntity.setId(jsonObject2.getString("id"));
					drillProjectNameEntity.setName(jsonObject2.getString("name"));
					drillProjectNameEntity.setInitialPlanId(jsonObject2.getString("initialPlanId"));
					list.add(drillProjectNameEntity);
				}
			} else {
				return list;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}

		return list;
	}

}
