package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
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
 * 终止预案解析
 * @author Administrator
 *
 */
public class StopPlanParser {
        String TAG="StopPlanParser";
		public Map<String, String> map;
		private FinalHttp finalHttp;
		private ControlCompleterListenter<Map<String, String>> completerListenter;

		public StopPlanParser(PlanEntity entity, String planSuspendOpition , ControlCompleterListenter<Map<String, String>> completeListener) {
			// TODO Auto-generated constructor stub
			finalHttp = Utils.getInstance().getFinalHttp();
			MyCookieStore.getcookieStore(finalHttp);
			this.completerListenter = completeListener;
			request(entity,planSuspendOpition);
		}

		/**
		 * 发送请求
		 * 

		 */
		public void request(final PlanEntity entity,final String planSuspendOpition) {

			AjaxParams params = new AjaxParams();
				params.put("id", entity.getId());
				params.put("planResType", entity.getPlanResType());
				// planSuspendOpition 预案终止原因
				params.put("planSuspendOpition", planSuspendOpition);
				params.put("planName", entity.getPlanName());
				params.put("planResName", entity.getPlanResName());
//				planId	预案ID	中止时发送通知使用
//				tradeTypeId	业务类型ID	中止时发送通知使用
//				eveLevelId	事件等级ID	中止时发送通知使用
//				planStarterId	预案启动人	中止时发送通知使用
//				planAuthorId	预案授权人	中止时发送通知使用
//				submitterId	事件提交人	中止时发送通知使用
				params.put("planId", entity.getPlanId());
				params.put("tradeTypeId", entity.getTradeTypeId());
				params.put("planStarterId", entity.getPlanStarterId());
				params.put("eveLevelId", entity.getEveLevelId());
				params.put("planAuthorId", entity.getPlanAuthorId());
				params.put("submitterId", entity.getSubmitterId());
			finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.STOP_PLAN, params, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);

					completerListenter.controlParserComplete(null, strMsg);
					Log.i("onFailure", "strMsg" + strMsg);
					if (errorNo==518) {
						Utils.getInstance().relogin();
						request(entity,planSuspendOpition);
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
					Log.i(TAG, TAG + t);
					map = setStopPlanParser(t);
					Log.i(TAG, TAG + map);
					completerListenter.controlParserComplete(map,
							null);

				}

			});

		}

		/**
		 * 预案授权解析数据
		 * 
		 * @param t
		 * @return
		 * @throws JSONException
		 */
		public Map<String, String> setStopPlanParser(String t) {
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
