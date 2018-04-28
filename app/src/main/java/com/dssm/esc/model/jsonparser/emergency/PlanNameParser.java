package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.PlanNameRowEntity;
import com.dssm.esc.model.entity.emergency.PlanNameSelectEntity;
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
 * 预案名称解析类
 * 
 * @author zsj
 * 
 */
public class PlanNameParser {
	private PlanNameSelectEntity planNameSelectEntity;
	private List<PlanNameRowEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public PlanNameParser(int tags, String id,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
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
		finalHttp.get(url, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request( tags,id);
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
				Log.i("PlanNameParser", t);
				MyCookieStore.setcookieStore(finalHttp);
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
