package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanProcessEntity;
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
 * 预案步骤列表
 * 
 * @author zsj
 * 
 */
public class PlanProcessListParser {
	private List<PlanProcessEntity> list;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanProcessListParser(String planInfoId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(planInfoId);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final String planInfoId) {
		Log.i("planInfoId", DemoApplication.getInstance().getUrl()+HttpUrl.GETQUERYPROCESSLIST+planInfoId);
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETQUERYPROCESSLIST + planInfoId);
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
				Log.i("PlanProcessListParser", t);
				list = planProcessListParser(t);
				Log.i("PlanProcessListParser", "PlanProcessListParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
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
						request(planInfoId);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request(planInfoId);
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
	 * 预案步骤列表数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<PlanProcessEntity> planProcessListParser(String t) {
		List<PlanProcessEntity> list = new ArrayList<PlanProcessEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					PlanProcessEntity listEntity = new PlanProcessEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					listEntity.setId(jsonObject2.getString("id"));
					listEntity.setPlanInfoId(jsonObject2
							.getString("planInfoId"));
					listEntity.setName(jsonObject2.getString("name"));
					listEntity.setExecutePeople(jsonObject2
							.getString("executePeople"));
					listEntity.setExecutePeopleType(jsonObject2
							.getString("executePeopleType"));
					
					
					String executorA = jsonObject2
							.getString("executorA");
					String executorB = jsonObject2
							.getString("executorB");
					String executorC = jsonObject2
							.getString("executorC");
					listEntity.setExecutorA(executorA);
					listEntity.setExecutorB(executorB);
					listEntity.setExecutorC(executorC);
					Log.i("executorA及长度",executorA+":"+ executorA.length()+"");
					Log.i("executorB及长度", executorB+":"+ executorB.length()+"");
					Log.i("executorC及长度", executorC+":"+ executorC.length()+"");
					if (!executorA.equals("")&&executorA.length()>0&&executorA!=null&&!executorA.equals("null")) {
						Log.i("executorA解析", executorA);
						Log.i("executorAName解析", jsonObject2.getString("executorAName"));
						listEntity.setExecutorAName(jsonObject2.getString("executorAName"));
						listEntity.setSignStateA(jsonObject2
								.getString("signStateA"));
					}
					if (!executorB.equals("")&&executorB.length()>0&&executorB!=null&&!executorB.equals("null")) {
						Log.i("executorB解析", executorB);
						Log.i("executorBName解析", jsonObject2.getString("executorBName"));
						listEntity.setExecutorBName(jsonObject2.getString("executorBName"));
						listEntity.setSignStateB(jsonObject2
								.getString("signStateB"));
					}
					if (executorC.length()>0&&executorC!=null&&!executorC.equals("")&&!executorC.equals("null")) {
						Log.i("executorC解析", executorC);
						Log.i("executorCName解析", jsonObject2.getString("executorCName"));
						listEntity.setExecutorCName(jsonObject2.getString("executorCName"));
						listEntity.setSignStateC(jsonObject2
								.getString("signStateC"));
					}
					list.add(listEntity);
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
