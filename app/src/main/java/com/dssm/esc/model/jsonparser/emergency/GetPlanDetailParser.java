package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailObjEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * 获取预案详情
 * 
 * @author zsj
 * 
 */
public class GetPlanDetailParser {
	public PlanDetailEntity detailEntity;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetPlanDetailParser(String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(id);
	}

	/**
	 * 发送请求
	 * 
	 * @param id
	 *            事件id
	 */
	public void request(final String id) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETPLANDETAIL + id);
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
				Log.i("GetPlanDetailParser", "GetPlanDetailParser" + t);
				detailEntity = getPlanDetail(t);
				Log.i("GetPlanDetailParser", "GetPlanDetailParser"
						+ detailEntity);
				OnEmergencyCompleterListener.onEmergencyParserComplete(
						detailEntity, null);

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
						request(id);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(id);
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
	 * 获取预案详情
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public PlanDetailEntity getPlanDetail(String t) {
		PlanDetailEntity detailEntity = new PlanDetailEntity();
		try {
			JSONObject jsonObject2 = new JSONObject(t);
			PlanDetailObjEntity entity = new PlanDetailObjEntity();
			JSONObject jsonObject = jsonObject2.getJSONObject("obj");
			entity.setId(jsonObject.getString("id"));
			entity.setPlanId(jsonObject.getString("planId"));
			entity.setPlanName(jsonObject.getString("planName"));
			entity.setPlanStarter(jsonObject.getString("planStarter"));
			entity.setPlanStartTime(jsonObject.getString("planStartTime"));
			entity.setNowTime(jsonObject.getString("nowTime"));
			
			entity.setPlanType(jsonObject.getString("planType"));
			entity.setPlanTypeName(jsonObject.getString("planTypeName"));
			entity.setSummary(jsonObject.getString("summary"));
			 entity.setEveDescription(jsonObject.getString("eveDescription"));
			entity.setPlanStartOpition(jsonObject.getString("planStartOpition"));
			entity.setEveName(jsonObject.getString("eveName"));
			entity.setSceneName(jsonObject.getString("sceneName"));
			entity.setState(jsonObject.getString("state"));
			
			entity.setEveType(jsonObject.getString("eveType"));
//			planResName	预案来源名称	发送通知使用
//			tradeTypeId	业务类型ID	发送通知使用
//			eveLevelId	事件等级ID	发送通知使用
//			planStarterId	预案启动人	发送通知使用
//			planAuthorId	预案授权人	发送通知使用
//			submitterId	事件提交人	发送通知使用
			entity.setEmergType(jsonObject.getString("emergType"));
			entity.setPlanResName(jsonObject.getString("planResName"));
			entity.setTradeTypeId(jsonObject.getString("tradeTypeId"));
			entity.setEveLevelId(jsonObject.getString("eveLevelId"));
			entity.setPlanStarterId(jsonObject.getString("planStarterId"));
			entity.setPlanAuthorId(jsonObject.getString("planAuthorId"));
			entity.setSubmitterId(jsonObject.getString("submitterId"));
//			planResType	演练计划类型	1、事件 2、演练计划
//			drillPrecautionId	演练计划id	
			entity.setPlanResType(jsonObject.getString("planResType"));
			entity.setDrillPrecautionId(jsonObject.getString("drillPrecautionId"));
			
//			entity.setSubTime(jsonObject.getString("subTime"));
//			entity.setNowTime(jsonObject.getString("nowTime"));
			
			entity.setPlanAuthTime(isNull(jsonObject.getString("planAuthTime")));
			entity.setPlanAuthOpition(isNull(jsonObject.getString("planAuthOpition")));
			entity.setPlanAuthor(isNull(jsonObject.getString("planAuthor")));
			detailEntity.setObj(entity);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return detailEntity;
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
