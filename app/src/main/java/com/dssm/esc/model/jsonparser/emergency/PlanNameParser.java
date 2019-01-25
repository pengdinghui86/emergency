package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanNameRowEntity;
import com.dssm.esc.model.entity.emergency.PlanNameSelectEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
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
 * 预案名称解析类
 * 
 * @author zsj
 * 
 */
public class PlanNameParser {
	private PlanNameSelectEntity planNameSelectEntity;
	private List<PlanNameRowEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	public PlanNameParser(int tags, String id, String notInSet,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(tags, id, notInSet);
	}

	/**
	 * 发送请求
	 * 
	 * @param tags
	 *            1,默认;2,其他;3,总预案
	 * @param id
	 *            场景id

	 */
	@SuppressWarnings("unchecked")
	public void request(final int tags, final String id, final String notInSet) {
		String url = null;
		if (tags == 1) {// 默认
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GET_PREBTSCENARIOID + "?businessType=" + id;
		} else if (tags == 2) {// 其他
//			url = DemoApplication.getInstance().getUrl()+HttpUrl.GET_OTEHERPREBTSCENARIOID + "?excludeScene=" + id
//					+ "&auditState=2";
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GET_PREBTSCENARIOID + "?businessType=" + id + "&notInSet=" + notInSet;
		} else if (tags == 3) {// 总预案
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GET_CATEGORYPREBTSCENARIOID + "?businessType=" + id
					+ "&auditState=2";
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
				Log.i("PlanNameParser", t);
				if (tags == 1 || tags == 3) {
					list = businessTypeParser2(t);
					Log.i("PlanNameParser", "PlanNameParser" + list);
					if(onEmergencyCompleteListener != null)
						onEmergencyCompleteListener.onEmergencyParserComplete(
							list, null);

				} else if (tags == 2) {
					planNameSelectEntity = businessTypeParser(t);
					Log.i("PlanNameParser", "PlanNameParser"
							+ planNameSelectEntity);
					if(onEmergencyCompleteListener != null)
						onEmergencyCompleteListener.onEmergencyParserComplete(
							planNameSelectEntity, null);
				}

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
							request(tags, id, notInSet);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(tags, id, notInSet);
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
	 * 业务类型和事件等级数据解析
	 * 
	 * @param t
	 *            2,其他
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public PlanNameSelectEntity businessTypeParser(String t) {
		PlanNameSelectEntity planNameSelectEntity = new PlanNameSelectEntity();
		List<PlanNameRowEntity> list = new ArrayList<PlanNameRowEntity>();
		try {
            JSONArray jsonArray = new JSONArray(t);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    PlanNameRowEntity planNameRowEntity = new PlanNameRowEntity();
                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                    planNameRowEntity.setId(jsonObject2.getString("id"));

                    planNameRowEntity
                            .setName(jsonObject2.getString("name"));
                    planNameRowEntity.setSummary(jsonObject2
                            .getString("summary"));
                    planNameRowEntity.setSceneName(jsonObject2
                            .getString("sceneName"));
                    if(jsonObject2.has("hasStartAuth"))
                        planNameRowEntity.setHasStartAuth(jsonObject2
                                .getString("hasStartAuth"));
                    else
                        planNameRowEntity.setHasStartAuth("false");
                    list.add(planNameRowEntity);

                }
            }
            planNameSelectEntity.setRows(list);
            return planNameSelectEntity;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return planNameSelectEntity;
		}
	}

	/**
	 * 业务类型和事件等级数据解析
	 * 
	 * @param t
	 *            1，默认；3，总预案
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<PlanNameRowEntity> businessTypeParser2(String t) {
		List<PlanNameRowEntity> list = new ArrayList<PlanNameRowEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);

			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					PlanNameRowEntity planNameRowEntity = new PlanNameRowEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					planNameRowEntity.setId(jsonObject2.getString("id"));
					if(jsonObject2.has("precautionId"))
						planNameRowEntity.setPrecautionId(jsonObject2.getString("precautionId"));
					else
						planNameRowEntity.setPrecautionId("");
					planNameRowEntity.setName(jsonObject2.getString("name"));
					planNameRowEntity.setSceneName(jsonObject2.getString("sceneName"));
					planNameRowEntity.setSummary(jsonObject2
							.getString("summary"));
					planNameRowEntity.setHasStartAuth(jsonObject2
							.getString("hasStartAuth"));
					
					list.add(planNameRowEntity);

				}
				return list;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return list;
		}
		return list;
	}

}
