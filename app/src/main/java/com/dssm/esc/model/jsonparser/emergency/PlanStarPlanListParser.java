package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 已启动的预案列表解析
 * @author zsj
 *
 */
public class PlanStarPlanListParser {
	private List<PlanStarListEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanStarPlanListParser(OnDataCompleterListener completeListener) {
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
		
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETSTARTLIST, new AjaxCallBack<String>() {
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
				Log.i("PlanStarListParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = planStarListParser(t);
				Log.i("PlanStarListParser", "PlanStarListParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

			}

		});
	}

	/**
	 * 已启动事件列表数据解析
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
					PlanStarListEntity suspandEntity = new PlanStarListEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					
//				    	id	预案ID	
//				    	suspendType	中止类型	启动时中止，类型为null
//				    	planSuspendOpition	中止原因	
//				    	planName	预案名称	发送通知使用
//				    	planResName	预案来源名称	发送通知使用
//				    	planResType	预案来源类型	发送通知使用
//				    	planId	预案ID	发送通知使用
//				    	tradeTypeId	业务类型ID	发送通知使用
//				    	eveLevelId	事件等级ID	发送通知使用
//				    	planStarterId	预案启动人	发送通知使用
//				    	planAuthorId	预案授权人	发送通知使用
//				    	submitterId	事件提交人	发送通知使用
				        suspandEntity.setId(jsonObject2.getString("id"));
					suspandEntity.setPlanName(jsonObject2.getString("planName"));
					suspandEntity.setPlanResName(jsonObject2.getString("planResName"));
					suspandEntity.setPlanResType(jsonObject2.getString("planResType"));
					suspandEntity.setPlanId(jsonObject2.getString("planId"));
					suspandEntity.setState(jsonObject2.getString("state"));
					suspandEntity.setIsStarter(jsonObject2.getString("isStarter"));
					list.add(suspandEntity);
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
