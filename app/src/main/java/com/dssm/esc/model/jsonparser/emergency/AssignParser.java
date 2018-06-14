package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
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
 * 人员指派
 * @author zsj
 *
 */
public class AssignParser {
	public Map<String, String> map;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public AssignParser(String id,String planInfoId ,String executePeopleId, String executePeople,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(id,planInfoId,executePeopleId,executePeople);
	}

	/**
	 * 发送请求
	 * @param planInfoId 预案执行编号
	 */
	public void request(final String id,final String planInfoId ,final String executePeopleId, final String executePeople) {

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.ASSIGN);
		
		if (planInfoId != null ) {
//			id	流程步骤id
//			planInfoId	预案执行id
//			executePeopleId	执行人id
//			executePeople	执行人姓名
			params.addParameter("id", id);
			params.addParameter("planInfoId", planInfoId);
			params.addParameter("executePeopleId", executePeopleId);
			params.addParameter("executePeople", executePeople);
			
			

		}
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("AssignParser", "AssignParser" + t);
				map = assignParser(t);
				Log.i("AssignParser", "AssignParser" + map);
				OnEmergencyCompleterListener.onEmergencyParserComplete(map,
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
						request(id, planInfoId , executePeopleId,  executePeople);
					}
					responseMsg = httpEx.getMessage();
					errorResult = httpEx.getResult();
				} else { //其他错误

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
	 * 人员指派解析数据
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public Map<String, String> assignParser(String t) {
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
