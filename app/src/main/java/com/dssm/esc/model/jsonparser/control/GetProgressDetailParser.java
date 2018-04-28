package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 5.1.2	展示事件流程
 * @author Administrator
 *
 */
public class GetProgressDetailParser {
	String TAG="GetProgressDetailParser";
	private ProgressDetailEntity entity;
	private FinalHttp finalHttp;
	private ControlCompleterListenter<ProgressDetailEntity> completeListener;
	
	public GetProgressDetailParser(String id,ControlCompleterListenter<ProgressDetailEntity> completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp= Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.completeListener=completeListener;
		request(id);
	}
	
	/**
	 * 
	 * 发送请求
	 */
	public void request(final String id){
	
	   AjaxParams params =new AjaxParams();
	   params.put("id", id);
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.PROGRESS_DETAIL,params, new AjaxCallBack<String>() {
         
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				
				completeListener.controlParserComplete(null, strMsg);
				Log.i("onFailure", "strMsg"+strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(id);
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
				Log.i(TAG, TAG+t);
				MyCookieStore.setcookieStore(finalHttp);
				entity=getProgressDetailParser(t);
				Log.i(TAG, TAG+entity);
				completeListener.controlParserComplete(entity, null);
				
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
