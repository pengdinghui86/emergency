package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 预案中止解析
 * @author zsj
 *
 */
public class SuspandParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	// id 编号
	// suspendType 中止类型 类型为authSuspend
	// planSuspendOpition2 预案中止原因
	public SuspandParser(PlanSuspandEntity suspandEntity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(suspandEntity);
	}

	/**
	 * 发送请求
	 * 
	 * @param suspandEntity 预案中止实体类
	 */
	public void request(final PlanSuspandEntity suspandEntity) {

		AjaxParams params = new AjaxParams();
//		id	预案ID	
//		suspendType	中止类型	启动时中止，类型为null
//		planSuspendOpition	中止原因	
//		planName	预案名称	发送通知使用
//		planResName	预案来源名称	发送通知使用
//		planResType	预案来源类型	发送通知使用
//		planId	预案ID	发送通知使用
//		tradeTypeId	业务类型ID	发送通知使用
//		eveLevelId	事件等级ID	发送通知使用
//		planStarterId	预案启动人	发送通知使用
//		planAuthorId	预案授权人	发送通知使用
//		submitterId	事件提交人	发送通知使用
			params.put("id", suspandEntity.getId());
			params.put("suspendType", suspandEntity.getSuspendType());
			params.put("planSuspendOpition", suspandEntity.getPlanSuspendOpition());
			params.put("planName", suspandEntity.getPlanName());
			params.put("planResName", suspandEntity.getPlanResName());
			
			params.put("planResType", suspandEntity.getPlanResType());
			params.put("planId", suspandEntity.getPlanId());
			params.put("eveLevelId", suspandEntity.getEveLevelId());
			params.put("planStarterId", suspandEntity.getPlanStarterId());
			params.put("planAuthorId", suspandEntity.getPlanAuthorId());
			params.put("submitterId", suspandEntity.getSubmitterId());
			String url=DemoApplication.getInstance().getUrl()+HttpUrl.SUSPAND;
			
		finalHttp.post(url, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(suspandEntity);
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
				Log.i("SuspandParser", "SuspandParser" + t);
				map = loginRoleParse(t);
				Log.i("SuspandParser", "SuspandParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
						null);

			}

		});

	}

	/**
	 * 预案中止解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> loginRoleParse(String t) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject(t);
			if (t.contains("success")) {
				map.put("success", object.getString("success"));
				map.put("message", object.getString("message"));
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

		return map;
	}
}
