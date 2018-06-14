package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.DrillProjectNameEntity;
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
 * 演练计划表解析类
 * 
 * @author zsj
 * 
 */
public class DrillProjectNameParser {
	private List<DrillProjectNameEntity> list;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public DrillProjectNameParser(
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
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
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GET_DRILLPROJECTNAME);
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
				Log.i("DrillProjectNameParser", t);
				list = drillProjectNameParser(t);
				Log.i("DrillProjectNameParser", "DrillProjectNameParser" + list);
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
