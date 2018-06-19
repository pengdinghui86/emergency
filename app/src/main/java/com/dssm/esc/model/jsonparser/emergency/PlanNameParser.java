package com.dssm.esc.model.jsonparser.emergency;

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
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanNameParser(int tags, String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(tags, id);
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
	public void request(final int tags, final String id) {
		String url = null;
		if (tags == 1) {// 默认
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GET_PREBTSCENARIOID + "?id=" + id;
		} else if (tags == 2) {// 其他
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GET_OTEHERPREBTSCENARIOID + "?excludeScene=" + id
					+ "&auditState=2";
		} else if (tags == 3) {// 总预案
			url = DemoApplication.getInstance().getUrl()+HttpUrl.GET_CATEGORYPREBTSCENARIOID + "?businessType=" + id
					+ "&auditState=2";
		}
		RequestParams params = new RequestParams(url);
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
				Log.i("PlanNameParser", t);
				if (tags == 1 || tags == 3) {
					list = businessTypeParser2(t);
					Log.i("PlanNameParser", "PlanNameParser" + list);
					OnEmergencyCompleterListener.onEmergencyParserComplete(
							list, null);

				} else if (tags == 2) {
					planNameSelectEntity = businessTypeParser(t);
					Log.i("PlanNameParser", "PlanNameParser"
							+ planNameSelectEntity);
					OnEmergencyCompleterListener.onEmergencyParserComplete(
							planNameSelectEntity, null);
				}

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
						request(tags, id);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(tags, id);
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
						planNameRowEntity.setHasStartAuth(jsonObject2
								.getString("hasStartAuth"));
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

					planNameRowEntity.setName(jsonObject2.getString("name"));
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
