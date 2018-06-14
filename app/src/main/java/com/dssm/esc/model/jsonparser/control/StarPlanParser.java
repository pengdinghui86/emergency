package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

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
 * 5.2.3	流程启动
 * @author Administrator
 *
 */
public class StarPlanParser {
       String TAG="StarPlanParser";
		public Map<String, String> map;
		private ControlCompleterListenter<Map<String, String>> completerListenter;

		public StarPlanParser(String id,ControlCompleterListenter<Map<String, String>> completeListener) {
			// TODO Auto-generated constructor stub
			this.completerListenter = completeListener;
			request(id);
		}

		/**
		 * 发送请求
		 * 
		 * @param id

		 */
		public void request(final String id) {

			RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.STAR_PLAN);
			params.addParameter("id", id);
			x.http().post(params, new Callback.CommonCallback<String>() {

				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					Log.i(TAG, TAG + t);
					map = setStarPlanParser(t);
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
							request(id);
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
		public Map<String, String> setStarPlanParser(String t) {
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
