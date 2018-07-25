package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;


/**
 * 5.1.2	展示事件流程
 * @author Administrator
 *
 */
public class GetProgressDetailParser {
	String TAG="GetProgressDetailParser";
	private ProgressDetailEntity entity;
	private final WeakReference<ControlCompleterListenter<ProgressDetailEntity>> wr;

	public GetProgressDetailParser(String id,ControlCompleterListenter<ProgressDetailEntity> completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request(id);
	}
	
	/**
	 * 
	 * 发送请求
	 */
	public void request(final String id){

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.PROGRESS_DETAIL);
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
		params.addParameter("id", id);
		final ControlCompleterListenter<ProgressDetailEntity> completeListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i(TAG, TAG+t);
				entity=getProgressDetailParser(t);
				Log.i(TAG, TAG+entity);
				if(completeListener != null)
					completeListener.controlParserComplete(entity, null);
				
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
						if(DemoApplication.sessionTimeoutCount < 2)
							request(id);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 2)
						request(id);
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
   private ProgressDetailEntity getProgressDetailParser(String t) {
	   ProgressDetailEntity entity=null;
	   try {
		   if (!t.equals("")&&!t.equals("null")) {
		JSONObject object=new JSONObject(t);
			entity =new ProgressDetailEntity();
			entity.setEveStartTime(object.getString("eveStartTime"));
			entity.setNowTime(object.getString("nowTime"));
			entity.setProgressNum(object.getString("progressNum"));
			JSONObject jsonObjectplan=object.getJSONObject("planAuth");
			ProgressDetailEntity.EvenDetail planAuth=entity.new EvenDetail();
			entity.setPlanAuth(setevenDetail(jsonObjectplan, planAuth));
			JSONObject jsonObjecteveClose=object.getJSONObject("eveClose");
			ProgressDetailEntity.EvenDetail eveClose=entity.new EvenDetail();
			entity.setEveClose(setevenDetail(jsonObjecteveClose, eveClose));
			JSONObject jsonObjectplanPerform=object.getJSONObject("planPerform");
			ProgressDetailEntity.EvenDetail planPerform=entity.new EvenDetail();
			entity.setPlanPerform(setevenDetail(jsonObjectplanPerform, planPerform));
			JSONObject jsonObjectpersonSign=object.getJSONObject("personSign");
			ProgressDetailEntity.EvenDetail personSign=entity.new EvenDetail();
			entity.setPersonSign(setevenDetail(jsonObjectpersonSign, personSign));
			JSONObject jsonObjectplanStart=object.getJSONObject("planStart");
			ProgressDetailEntity.EvenDetail planStart=entity.new EvenDetail();
			entity.setPlanStart(setevenDetail(jsonObjectplanStart, planStart));
			JSONObject jsonObjecteveAssess=object.getJSONObject("eveAssess");
			ProgressDetailEntity.EvenDetail eveAssess=entity.new EvenDetail();
			entity.setEveAssess(setevenDetail(jsonObjecteveAssess, eveAssess));
			
		}
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return entity;
	}
	   
    return entity;
}

private ProgressDetailEntity.EvenDetail setevenDetail(JSONObject jsonObjectplan, ProgressDetailEntity.EvenDetail detail)
		throws JSONException {
	if (jsonObjectplan.has("finishTime")) {
	
	detail.setFinishTime(jsonObjectplan.getString("finishTime"));
	
	}else{
	detail.setFinishTime("");
	}
	detail.setProgress(jsonObjectplan.getString("progress"));
	detail.setProgressName(jsonObjectplan.getString("progressName"));
	detail.setRemark(jsonObjectplan.getString("remark"));
	detail.setStartTime(jsonObjectplan.getString("startTime"));
	detail.setState(jsonObjectplan.getString("state"));
	detail.setStateDesc(jsonObjectplan.getString("stateDesc"));
	return detail;
}
}
