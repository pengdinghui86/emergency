package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
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
 * 预案列表数据解析
 * @author zsj
 *
 */
public class PlanStarEventListParser {
	private List<PlanStarListEntity> list;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanStarEventListParser(OnDataCompleterListener completeListener) {
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

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETPLANSTARLIST);
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
				Log.i("PlanStarListParser", t);
				list = planStarListParser(t);
				Log.i("PlanStarListParser", "PlanStarListParser" + list);
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
	 * 待启动事件列表数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<PlanStarListEntity> planStarListParser(String t) {
		List<PlanStarListEntity> list = new ArrayList<PlanStarListEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					PlanStarListEntity listEntity = new PlanStarListEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					listEntity.setId(jsonObject2.getString("id"));
					listEntity.setEveName(jsonObject2.getString("eveName"));
					listEntity.setState(jsonObject2.getString("state"));
					listEntity.setEveLevel(jsonObject2.getString("eveLevel"));
					listEntity.setTradeType(jsonObject2.getString("tradeType"));
					listEntity.setEveCode(jsonObject2.getString("eveCode"));
					listEntity.setEveType(jsonObject2.getString("eveType"));
					
					
//					 "id": "7c80a341-06e3-437b-b714-281b2b7ebc69", 
//				        "eveLevel": "Ⅰ级（重大事件）", 
//				        "tradeType": "信息科技", 
//				        "eveType": "1", 
//				        "state": "0", 
//				        "eveCode": "SJ-YJ-1445326290309", 
//				        "eveName": "廖裕卿测试应急1"
					list.add(listEntity);
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
