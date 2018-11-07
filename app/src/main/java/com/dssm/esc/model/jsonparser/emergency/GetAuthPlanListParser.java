package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取授权决策列表
 */
public class GetAuthPlanListParser {
	private List<PlanStarListEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetAuthPlanListParser(int a, OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(a);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 * @param a
	 *            0，已授权， 1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示
	 */
	public void request(final int a) {
		String url = "";
		if (a == 0) {
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETSTARTLIST;
		} else if (a == 1) {// 代授权列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETAUTHLIST;
		} else if (a == 2) {// 待签到列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETPERSONPLAN;
		} else if (a == 3) {// 人员指派列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETASSIGNLISTBYSTATE;
		} else if (a == 4) {// 协同通告列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETBYSTATELIST;
		} else if (a==5 || a == 6) {// 已启动预案列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETSTARTLIST;
		}
		RequestParams params = new RequestParams(url);
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
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i("GetAuthListParser", t);
				list = planListParser(t, a);
				Log.i("GetAuthListParser", "GetAuthListParser" + list);
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(list,
						null);

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
							request(a);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(a);
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(null, errorResult);
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
	 * 获取授权决策列表数据解析
	 */
	public List<PlanStarListEntity> getAuthlistParser(String t, int a) {
		List<PlanStarListEntity> list = new ArrayList<>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					PlanStarListEntity listEntity = new PlanStarListEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					listEntity.setId(jsonObject2.getString("id"));
					listEntity.setEveName(jsonObject2.getString("planName"));
					listEntity.setPlanResName(jsonObject2.getString("planResName"));
					listEntity.setPlanResType(jsonObject2.getString("planResType"));
					listEntity.setState(jsonObject2.getString("state"));
					listEntity.setPlanId(jsonObject2.getString("planId"));
					listEntity.setPlanResId(jsonObject2.getString("planResId"));
					if (a==0) {
						listEntity.setIsAuthor(jsonObject2.getString("isAuthor"));
					}
					if (a == 4) {
						listEntity.setPrecautionId(jsonObject2
								.getString("planId"));
					}
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

	/**
	 * 预案列表数据解析
	 */
	public List<PlanStarListEntity> planListParser(String t, int a) {
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
					list.add(listEntity);
					JSONArray jsonArray2 = new JSONArray(jsonObject2.getString("planInfos"));
					for(int j = 0; j < jsonArray2.length(); j++)
					{
						PlanStarListEntity suspandEntity = new PlanStarListEntity();
						JSONObject jsonObject = (JSONObject) jsonArray2.opt(j);
						if(jsonObject.has("isStarter"))
							suspandEntity.setIsStarter(jsonObject.getString("isStarter"));
						suspandEntity.setId(jsonObject.getString("id"));
						suspandEntity.setPlanName(jsonObject.getString("planName"));
						suspandEntity.setPlanResName(jsonObject.getString("planResName"));
						suspandEntity.setPlanResType(jsonObject.getString("planResType"));
						suspandEntity.setState(jsonObject.getString("state"));
						suspandEntity.setPlanId(jsonObject.getString("planId"));
						suspandEntity.setPlanResId(jsonObject.getString("planResId"));
						if (a == 0) {
							if(jsonObject.has("isAuthor"))
								suspandEntity.setIsAuthor(jsonObject.getString("isAuthor"));
							else
								suspandEntity.setIsAuthor("true");
						}
						if (a == 4) {
							suspandEntity.setPrecautionId(jsonObject
									.getString("planId"));
						}
						suspandEntity.setDataType(1);
						list.add(suspandEntity);
					}
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
