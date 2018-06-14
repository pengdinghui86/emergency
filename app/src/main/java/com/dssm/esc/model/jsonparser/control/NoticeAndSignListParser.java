package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.control.SignUserEntity;
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

import java.util.ArrayList;
import java.util.List;


/**
 * 5.2.5	应急通知接收及人员签到情况详情
 * @author Administrator
 *
 */
public class NoticeAndSignListParser {
	String TAG="NoticeAndSignListParser";
	private SignUserEntity entity;
	private ControlCompleterListenter<SignUserEntity> completeListener;
	
	public NoticeAndSignListParser(String planInfoId,ControlCompleterListenter<SignUserEntity> completeListener) {
		// TODO Auto-generated constructor stub
		this.completeListener=completeListener;
		request(planInfoId);
	}
	
	/**
	 * 
	 * 发送请求
	 */
	public void request(final String planInfoId){

		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.SIGN_USER_LIST_COUNT);
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
		params.addParameter("planInfoId", planInfoId);
		x.http().get(params, new Callback.CommonCallback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i(TAG, TAG+t);
				entity=getNoticeAndSignListParser(t);
				Log.i(TAG, TAG+entity);
				completeListener.controlParserComplete(entity, null);
				
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
				} else { //其他错误
					errorResult = "其他错误";
				}
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
   private SignUserEntity getNoticeAndSignListParser(String t) {
	   SignUserEntity entity=null;
	   try {
		   if (!t.equals("")&&!t.equals("null")) {
		 JSONObject object=new JSONObject(t);
			entity =new SignUserEntity();
			entity.setTotalNeedSignNum(object.getString("totalNeedSignNum"));
			entity.setTotalNoSignNum(object.getString("totalNoSignNum"));
			entity.setTotalSignNum(object.getString("totalSignNum"));
			JSONArray arraynotice=object.getJSONArray("noticeList");
			List<SignUserEntity.Notice>listnotice=new ArrayList<SignUserEntity.Notice>();
			for (int i = 0; i < arraynotice.length(); i++) {
				JSONObject jsonObject=(JSONObject)arraynotice.opt(i);
				SignUserEntity.Notice notice=entity.new Notice();
				notice.setEmergTeam(jsonObject.getString("emergTeam"));
				notice.setEmergTeamId(jsonObject.getString("emergTeamId"));
				notice.setNeedNoticeNum(jsonObject.getString("needNoticeNum"));
				notice.setNoticeNum(jsonObject.getString("noticeNum"));
				listnotice.add(notice);
			}
			entity.setNoticeList(listnotice);
			JSONArray arraysign=object.getJSONArray("signList");
			List<SignUserEntity.Sign>listsign=new ArrayList<SignUserEntity.Sign>();
			for (int i = 0; i < arraysign.length(); i++) {
				JSONObject jsonObject=(JSONObject)arraysign.opt(i);
				SignUserEntity.Sign sign=entity.new Sign();
				sign.setEmergTeam(jsonObject.getString("emergTeam"));;
				sign.setEmergTeamId(jsonObject.getString("emergTeamId"));
				sign.setNeedSignNum(jsonObject.getString("needSignNum"));
				sign.setSignNum(jsonObject.getString("signNum"));
				listsign.add(sign);
			}
			entity.setSignList(listsign);
		}
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return entity;
	}
	   
    return entity;
}
}
