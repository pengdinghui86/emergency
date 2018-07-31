package com.dssm.esc.model.jsonparser.user;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanNameRowEntity;
import com.dssm.esc.model.entity.emergency.PlanNameSelectEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
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
 * 其他预案的查询
 * @author zsj
 *
 */
public class SearchOtherscenPlanParser {
	private PlanNameSelectEntity planNameSelectEntity;
	private final WeakReference<OnDataCompleterListener> wr;

	public SearchOtherscenPlanParser(String name,String id,OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(name,id);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final String name,final String id) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.OTHERSCENDETAILLIST+name+"&excludeScene="+id);
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
		final OnDataCompleterListener onUserParseLoadCompleteListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i("SearchOtherscenPlan", t);
				 planNameSelectEntity= getSearchPlanlistParser(t);
				Log.i("SearchOtherscenPlan", "SearchOtherscenPlanParser" + planNameSelectEntity);
				if(onUserParseLoadCompleteListener != null)
					onUserParseLoadCompleteListener.onEmergencyParserComplete(planNameSelectEntity,
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
							request(name, id);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(name, id);
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(onUserParseLoadCompleteListener != null)
					onUserParseLoadCompleteListener.onEmergencyParserComplete(null, errorResult);
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
	 * 查询得到的预案名称数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public PlanNameSelectEntity getSearchPlanlistParser(String t) {
		PlanNameSelectEntity planNameSelectEntity = new PlanNameSelectEntity();
		List<PlanNameRowEntity> list = new ArrayList<PlanNameRowEntity>();
		try {
			JSONObject jsonObject = new JSONObject(t);
			if (t.contains("rows")) {
				JSONArray jsonArray = jsonObject.getJSONArray("rows");
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						PlanNameRowEntity planNameRowEntity = new PlanNameRowEntity();
						JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
						planNameRowEntity.setId(jsonObject2.getString("id"));

						planNameRowEntity
								.setName(jsonObject2.getString("name"));
						planNameRowEntity.setSummary(jsonObject2
								.getString("summary"));
						list.add(planNameRowEntity);

					}
				}

				planNameSelectEntity.setRows(list);
				return planNameSelectEntity;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return planNameSelectEntity;
		}

		return planNameSelectEntity;
		
	}


}
