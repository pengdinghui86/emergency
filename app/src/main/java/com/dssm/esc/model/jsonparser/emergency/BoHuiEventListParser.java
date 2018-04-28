package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
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
 * 驳回列表解析
 * @author zsj
 *
 */
public class BoHuiEventListParser {
	private List<BoHuiListEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public BoHuiEventListParser(OnDataCompleterListener completeListener) {
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
	public void request() {
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GET_BOHUILIST, new AjaxCallBack<String>() {
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
				Log.i("BoHuiListParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = boHuiListParser(t);
				Log.i("BoHuiListParser", "BoHuiListParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

			}

		});
	}

	/**
	 * 驳回列表数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<BoHuiListEntity> boHuiListParser(String t) {
		List<BoHuiListEntity> list = new ArrayList<BoHuiListEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					BoHuiListEntity entity = new BoHuiListEntity();
					JSONObject object = (JSONObject) jsonArray.opt(i);
					entity.setDrillPlanId(object.getString("drillPlanId"));
					entity.setDrillPlanName(object.getString("drillPlanName"));
					entity.setEmergType(object.getString("emergType"));
					entity.setEveCode(object.getString("eveCode"));
					entity.setEveLevel(object.getString("eveLevel"));
					entity.setEveName(object.getString("eveName"));
					entity.setEveScenarioId(object.getString("eveScenarioId"));
					entity.setEveScenarioName(object.getString("eveScenarioName"));
					entity.setEveType(object.getString("eveType"));
					entity.setId(object.getString("id"));
					entity.setState(object.getString("state"));
					entity.setTradeType(object.getString("tradeType"));
					entity.setUpdateTime(object.getString("updateTime"));
					entity.setSubmitterId(object.getString("submitterId"));
					entity.setSubmitter(object.getString("submitter"));
					list.add(entity);
				}
				return list;
			} else {
				return list;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}

		
	}

}
