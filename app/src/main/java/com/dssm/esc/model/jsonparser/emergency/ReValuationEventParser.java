package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
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
 * 事件重新评估解析
 * @author zsj
 *
 */
public class ReValuationEventParser {
	public Map<String, String> map;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public ReValuationEventParser(GetProjectEveInfoEntity entity,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(entity);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final GetProjectEveInfoEntity entity) {
		AjaxParams params = new AjaxParams();
//		id	事件编号	
//		tradeType	行业类型ID	
//		eveLevel	事件等级ID	
//		eveDescription	事件描述	
//		eveScenarioId	事件场景ID	可不选
//		eveScenarioName	事件场景名称	可为空
//		eveName	事件名称	
//		dealAdvice	处置建议	
//		referPlan	参考预案	可以多选，以“|”隔开
//		otherReferPlan	其他预案	可以多选，以“|”隔开
//		categoryPlan	分类预案	可以多选，以“|”隔开
		params.put("id", entity.getId());
		params.put("tradeType", entity.getTradeTypeId());
		params.put("eveLevel", entity.getEveLevelId());
		params.put("eveDescription",entity.getEveDescription());
		params.put("eveScenarioId",entity.getEveScenarioId());
		params.put("eveScenarioName", entity.getEveScenarioName());
		params.put("eveName", entity.getEveName());
		params.put("dealAdvice", entity.getDealAdvice());
		params.put("referPlan", entity.getReferPlanIds());
		params.put("otherReferPlan", entity.getOtherReferPlanIds());
		params.put("categoryPlan", entity.getCategoryPlanIds());
		
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.RE_VALUTEEVENT, params,
				new AjaxCallBack<String>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);

						OnEmergencyCompleterListener
								.onEmergencyParserComplete(null, strMsg);
						Log.i("onFailure", "strMsg" + strMsg);
						if (errorNo==518) {
							Utils.getInstance().relogin();
							request(entity);
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
						Log.i("ReValuationEventParser", t);
						MyCookieStore.setcookieStore(finalHttp);
						map = reValuationEventParser(t);
						Log.i("ReValuationEventParser",
								"ReValuationEventParser" + map);
						OnEmergencyCompleterListener
								.onEmergencyParserComplete(map, null);

					}

				});

	}

	/**
	 * 重新评估数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> reValuationEventParser(String t) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject(t);
			if (t.contains("success")) {
				map.put("success", object.getString("success"));
				map.put("message", object.getString("message"));
			} else {
				 return null;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

		return map;
	}

}
