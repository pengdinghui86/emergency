package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.control.SignUserEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	private FinalHttp finalHttp;
	private ControlCompleterListenter<SignUserEntity> completeListener;
	
	public NoticeAndSignListParser(String planInfoId,ControlCompleterListenter<SignUserEntity> completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp= Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.completeListener=completeListener;
		request(planInfoId);
	}
	
	/**
	 * 
	 * 发送请求
	 */
	public void request(final String planInfoId){
	
	   AjaxParams params =new AjaxParams();
	   params.put("planInfoId", planInfoId);
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.SIGN_USER_LIST_COUNT,params, new AjaxCallBack<String>() {
         
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				
				completeListener.controlParserComplete(null, strMsg);
				Log.i("onFailure", "strMsg"+strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(planInfoId);
					}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Log.i(TAG, TAG+t);
				MyCookieStore.setcookieStore(finalHttp);
				entity=getNoticeAndSignListParser(t);
				Log.i(TAG, TAG+entity);
				completeListener.controlParserComplete(entity, null);
				
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
