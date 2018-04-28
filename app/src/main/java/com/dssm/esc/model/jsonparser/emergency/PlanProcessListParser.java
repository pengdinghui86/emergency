package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanProcessEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanProcessListParser(String planInfoId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
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
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETQUERYPROCESSLIST+planInfoId, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request( planInfoId);
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
				Log.i("PlanProcessListParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = planProcessListParser(t);
				Log.i("PlanProcessListParser", "PlanProcessListParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

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
