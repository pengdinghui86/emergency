package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
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
 * 5.1.1	展示事件列表
 * @author Administrator
 *
 */
public class GetEventlistParser {
	String TAG="GetEvalistParser";
	private  List<BoHuiListEntity>list;
	private final WeakReference<ControlCompleterListenter<List<BoHuiListEntity>>> wr;

	public GetEventlistParser(ControlCompleterListenter<List<BoHuiListEntity>> controlCompleterListenter) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(controlCompleterListenter);
		request();
	}
	
	/**
	 * 
	 * 发送请求
	 */
	public void request(){

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GET_EVEN_LIST);
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
		final ControlCompleterListenter<List<BoHuiListEntity>> completeListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i(TAG, TAG+t);
				list=getGetEvalistParser(t);
				//map=loginParse(t);
				Log.i(TAG, TAG+list);
				if(completeListener != null)
					completeListener.controlParserComplete(list, null);
				
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
						request();
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
					errorResult = "网络错误";
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					Utils.getInstance().relogin();
					request();
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(completeListener != null)
					completeListener.controlParserComplete(null, errorResult);
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}

		});
		
	}
	private List<BoHuiListEntity> getGetEvalistParser(String t){
		List< BoHuiListEntity>list=new ArrayList<BoHuiListEntity>();
		try {
			if (!t.equals("")&&!t.equals("null")) {
			JSONArray array=new JSONArray(t);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object=(JSONObject)array.opt(i);
				BoHuiListEntity entity=new BoHuiListEntity();
				/* "tradeType": "信息科技", 
				    "updateTime": "2015-10-26 10:54:09", 
				    "submitter": "李大霄", 
				    "submitterId": "afed5232-9319-41f1-a7a8-58eef4c6d2e2", 
				    "state": "2", 
				    "id": "801b742e-f412-4dda-9ae3-ff4508dbbcab", 
				    "eveType": "1", 
				    "eveScenarioName": "人为故障", 
				    "eveScenarioId": "20211f1c-083c-4943-9605-53456889000f", 
				    "eveName": "压力测试所用事件", 
				    "eveLevel": "Ⅰ级（重大事件）", 
				    "eveCode": "SJ-YJ-20151026001", 
				    "emergType": null, 
				    "drillPlanName": null, 
				    "drillPlanId": null*/
				entity.setDrillPlanId(object.getString("drillPlanId"));
				entity.setDrillPlanName(object.getString("drillPlanName"));
				entity.setEmergType(object.getString("emergType"));
				entity.setEveCode(object.getString("eveCode"));
				entity.setEveLevel(object.getString("eveLevel"));
				entity.setEveName(object.getString("eveName"));
				entity.setEveScenarioId(object.getString("eveScenarioId"));
				entity.setEveScenarioName(object.getString("eveScenarioName"));
				entity.setEveType(object.getString("eveType"));
				entity.setId(object.getString("id"));
				entity.setState(object.getString("state"));
				entity.setTradeType(object.getString("tradeType"));
				entity.setUpdateTime(object.getString("updateTime"));
				entity.setSubmitterId(object.getString("submitterId"));
				entity.setSubmitter(object.getString("submitter"));
				list.add(entity);
			}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return list;
		}
		
		return list;
	}
}
