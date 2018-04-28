package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailObjEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 获取预案详情
 * 
 * @author zsj
 * 
 */
public class GetPlanDetailParser {
	public PlanDetailEntity detailEntity;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetPlanDetailParser(String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
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

		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETPLANDETAIL + id, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(id);
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
				MyCookieStore.setcookieStore(finalHttp);
				Log.i("GetPlanDetailParser", "GetPlanDetailParser" + t);
				detailEntity = getPlanDetail(t);
				Log.i("GetPlanDetailParser", "GetPlanDetailParser"
						+ detailEntity);
				OnEmergencyCompleterListener.onEmergencyParserComplete(
						detailEntity, null);

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
