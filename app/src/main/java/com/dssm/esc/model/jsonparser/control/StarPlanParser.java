package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

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
 * 5.2.3	流程启动
 * @author Administrator
 *
 */
public class StarPlanParser {
       String TAG="StarPlanParser";
		public Map<String, String> map;
		private FinalHttp finalHttp;
		private ControlCompleterListenter<Map<String, String>> completerListenter;

		public StarPlanParser(String id,ControlCompleterListenter<Map<String, String>> completeListener) {
			// TODO Auto-generated constructor stub
			finalHttp = Utils.getInstance().getFinalHttp();
			MyCookieStore.getcookieStore(finalHttp);
			this.completerListenter = completeListener;
			request(id);
		}

		/**
		 * 发送请求
		 * 
		 * @param id

		 */
		public void request(final String id) {

			AjaxParams params = new AjaxParams();
				params.put("id", id);
			finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.STAR_PLAN, params, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);

					completerListenter.controlParserComplete(null, strMsg);
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
					Log.i(TAG, TAG + t);
					map = setStarPlanParser(t);
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
