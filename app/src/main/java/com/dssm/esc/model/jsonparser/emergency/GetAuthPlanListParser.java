package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.user.ButtonEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.DemoApplication;

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
		if (a == 0) {//已授权
			url = DemoApplication.getInstance().getUrl()+HttpUrl.AUTHLIST;
		} else if (a == 1) {// 代授权列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETAUTHLIST;
		} else if (a == 2) {// 已启动预案列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETSTARTLIST;
		} else if (a == 3) {// 人员指派列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETASSIGNLIST;
		} else if (a == 4) {// 协同通告列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETBYSTATELIST;
		} else if (a == 5) {// 指挥与展示列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GET_PLAN_LIST;
		} else if (a == 6) {// 预案执行列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETWEITPERFROMPRELIST;
		} else if (a == 7) {// 待签到事件列表
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GETPERSONPLAN;
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
				if(a == 7)
					list = signListParser(t);
				else
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
	 * 事件列表数据解析
	 */
	public List<PlanStarListEntity> eventListParser(String t) {
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
					listEntity.setSubmitterId(jsonObject2.getString("submitterId"));
					if(jsonObject2.has("closeTime"))
					{
						if("null".equals(jsonObject2.getString("closeTime")))
							listEntity.setCloseTime("");
						else
							listEntity.setCloseTime(jsonObject2.getString("closeTime"));
					}
					else
						listEntity.setCloseTime("");
					String isSign = "1";
					if(jsonObject2.has("isSign"))
					{
						if("0".equals(jsonObject2.getString("isSign")))
							isSign = "0";
					}
					list.add(listEntity);
					JSONArray jsonArray2 = new JSONArray(jsonObject2.getString("planInfos"));
					for(int j = 0; j < jsonArray2.length(); j++)
					{
						PlanStarListEntity suspandEntity = new PlanStarListEntity();
						JSONObject jsonObject = (JSONObject) jsonArray2.opt(j);
						if(jsonObject.has("isStarter"))
							suspandEntity.setIsStarter(jsonObject.getString("isStarter"));
						suspandEntity.setId(jsonObject.getString("id"));
						if(jsonObject.has("planPerformId"))
						    suspandEntity.setPlanPerformId(jsonObject.getString("planPerformId"));
						else
                            suspandEntity.setPlanPerformId("");
                        suspandEntity.setPlanName(jsonObject.getString("planName"));
						if(jsonObject.has("sceneName"))
							suspandEntity.setSceneName(jsonObject.getString("sceneName"));
						else
							suspandEntity.setSceneName("");
                        boolean checkExecutePeople = false;
                        if(jsonObject.has("checkExecutePeople"))
                        {
							String check = jsonObject.getString("checkExecutePeople");
                            if("true".equals(jsonObject.getString("checkExecutePeople")))
                                checkExecutePeople = true;
                        }
                        suspandEntity.setCheckExecutePeople(checkExecutePeople);
                        suspandEntity.setIsSign(isSign);
                        //指挥与展示启动与停止按钮权限判断
                        if(a == 5)
						{
							boolean btnPlanStart = false;
							if(MainActivity.buttonList != null && MainActivity.buttonList .size() > 0)
							{
								for(ButtonEntity buttonEntity : MainActivity.buttonList)
								{
									if("BTN_QDZZ".equals(buttonEntity.getBtnMark()))
									{
										btnPlanStart = true;
										break;
									}
								}
							}
							suspandEntity.setBtnPlanStart(btnPlanStart);
						}
						suspandEntity.setPlanResName(jsonObject.getString("planResName"));
						suspandEntity.setPlanResType(jsonObject.getString("planResType"));
						suspandEntity.setState(jsonObject.getString("state"));
						suspandEntity.setPlanId(jsonObject.getString("planId"));
						suspandEntity.setPlanResId(jsonObject.getString("planResId"));
						if(jsonObject.has("drillPrecautionId"))
						    suspandEntity.setPrecautionId(jsonObject.getString("drillPrecautionId"));
                        if(jsonObject.has("planStarterId"))
                            suspandEntity.setPlanStarterId(jsonObject.getString("planStarterId"));
						if (a == 0) {
							if(jsonObject.has("isAuthor"))
								suspandEntity.setIsAuthor(jsonObject.getString("isAuthor"));
							else
								suspandEntity.setIsAuthor("true");
						}
						else {
                            if(jsonObject.has("isAuthor"))
                                suspandEntity.setIsAuthor(jsonObject.getString("isAuthor"));
                            else
                                suspandEntity.setIsAuthor("false");
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

	public List<PlanStarListEntity> signListParser(String t) {
		List<PlanStarListEntity> list = new ArrayList<PlanStarListEntity>();
		try {
			boolean checkSign = false;
			JSONObject jsonObject3 = new JSONObject(t);
			if(jsonObject3.has("checkSign")) {
				String isSigned = jsonObject3.get("checkSign").toString();
				if(null != isSigned && "true".equals(isSigned))
					checkSign = true;
			}
			if(jsonObject3.has("signList")) {
				JSONArray jsonArray = new JSONArray(jsonObject3.get("signList").toString());
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
						listEntity.setCheckSign(checkSign);
						list.add(listEntity);
						JSONArray jsonArray2 = new JSONArray(jsonObject2.getString("planInfos"));
						for (int j = 0; j < jsonArray2.length(); j++) {
							PlanStarListEntity suspandEntity = new PlanStarListEntity();
							JSONObject jsonObject = (JSONObject) jsonArray2.opt(j);
							if (jsonObject.has("isStarter"))
								suspandEntity.setIsStarter(jsonObject.getString("isStarter"));
							suspandEntity.setId(jsonObject.getString("id"));
							if (jsonObject.has("planPerformId"))
								suspandEntity.setPlanPerformId(jsonObject.getString("planPerformId"));
							else
								suspandEntity.setPlanPerformId("");
							suspandEntity.setPlanName(jsonObject.getString("planName"));
							if (jsonObject.has("sceneName"))
								suspandEntity.setSceneName(jsonObject.getString("sceneName"));
							else
								suspandEntity.setSceneName("");
							suspandEntity.setPlanResName(jsonObject.getString("planResName"));
							suspandEntity.setPlanResType(jsonObject.getString("planResType"));
							suspandEntity.setState(jsonObject.getString("state"));
							suspandEntity.setPlanId(jsonObject.getString("planId"));
							suspandEntity.setPlanResId(jsonObject.getString("planResId"));
							if (jsonObject.has("isAuthor"))
								suspandEntity.setIsAuthor(jsonObject.getString("isAuthor"));
							else
								suspandEntity.setIsAuthor("false");
							suspandEntity.setDataType(1);
							list.add(suspandEntity);
						}
					}
				}
			}
			return list;
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}
	}
}
