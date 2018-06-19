package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
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

import java.util.ArrayList;
import java.util.List;


/**
 * 驳回列表解析
 * @author zsj
 *
 */
public class BoHuiEventListParser {
	private List<BoHuiListEntity> list;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public BoHuiEventListParser(OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request();
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request() {
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GET_BOHUILIST);
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
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("BoHuiListParser", t);
				list = boHuiListParser(t);
				Log.i("BoHuiListParser", "BoHuiListParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
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
						request();
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request();
				} else { //其他错误
					errorResult = "其他错误";
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
