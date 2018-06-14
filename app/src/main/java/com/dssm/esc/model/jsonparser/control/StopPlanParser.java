package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
		private ControlCompleterListenter<Map<String, String>> completerListenter;

		public StopPlanParser(PlanEntity entity, String planSuspendOpition , ControlCompleterListenter<Map<String, String>> completeListener) {
			// TODO Auto-generated constructor stub
			this.completerListenter = completeListener;
			request(entity,planSuspendOpition);
		}

		/**
		 * 发送请求
		 * 

		 */
		public void request(final PlanEntity entity,final String planSuspendOpition) {

			RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.STAR_PLAN);
			params.addParameter("id", entity.getId());
			params.addParameter("planResType", entity.getPlanResType());
				// planSuspendOpition 预案终止原因
			params.addParameter("planSuspendOpition", planSuspendOpition);
			params.addParameter("planName", entity.getPlanName());
			params.addParameter("planResName", entity.getPlanResName());
//				planId	预案ID	中止时发送通知使用
//				tradeTypeId	业务类型ID	中止时发送通知使用
//				eveLevelId	事件等级ID	中止时发送通知使用
//				planStarterId	预案启动人	中止时发送通知使用
//				planAuthorId	预案授权人	中止时发送通知使用
//				submitterId	事件提交人	中止时发送通知使用
			params.addParameter("planId", entity.getPlanId());
			params.addParameter("tradeTypeId", entity.getTradeTypeId());
			params.addParameter("planStarterId", entity.getPlanStarterId());
			params.addParameter("eveLevelId", entity.getEveLevelId());
			params.addParameter("planAuthorId", entity.getPlanAuthorId());
			params.addParameter("submitterId", entity.getSubmitterId());
			x.http().post(params, new Callback.CommonCallback<String>() {

				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					Log.i(TAG, TAG + t);
					map = setStopPlanParser(t);
					Log.i(TAG, TAG + map);
					completerListenter.controlParserComplete(map,
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
							request(entity,planSuspendOpition);
						}
						responseMsg = httpEx.getMessage();
						errorResult = httpEx.getResult();
					} else { //其他错误

					}
					completerListenter.controlParserComplete(null, errorResult);
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
