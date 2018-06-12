package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
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
 * 应急小组列表解析
 * 
 * @author zsj
 * 
 */
public class GetEmergencyGropDataParser {
	private List<GroupEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetEmergencyGropDataParser(String planInfoId,String precautionId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(planInfoId,precautionId);
	}

	/**
	 * 发送请求
	 * 
	 * @param planInfoId
	 *            预案执行编号
	 */
	public void request(final String planInfoId,final String precautionId) {
//		planInfoId	预案执行id	
//		precautionId	预案id	
//		modelName	类型	sendNotice
		Log.i("协同通告预案小组url", DemoApplication.getInstance().getUrl()+HttpUrl.GETEMERGENCYGROPDATA + planInfoId+"&precautionId="+precautionId+"&modelName=sendNotice");
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETEMERGENCYGROPDATA + planInfoId+"&precautionId="+precautionId+"&modelName=sendNotice",
				new AjaxCallBack<String>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);

						OnEmergencyCompleterListener.onEmergencyParserComplete(
								null, strMsg);
						Log.i("onFailure", "strMsg" + strMsg);
						if (errorNo==518) {
							Utils.getInstance().relogin();
							request(planInfoId,precautionId);
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
						Log.i("GetEmergencyGropData", t);
						MyCookieStore.setcookieStore(finalHttp);
						list = planStarListParser(t);
						Log.i("GetEmergencyGropData", "GetEmergencyGropDataParser"
								+ list);
						OnEmergencyCompleterListener.onEmergencyParserComplete(
								list, null);

					}

				});
	}

	/**
	 * 应急小组数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<GroupEntity> planStarListParser(String t) {
		List<GroupEntity> list = new ArrayList<GroupEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					GroupEntity groupEntity = new GroupEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					groupEntity
					.setGroup_id(jsonObject2.getString("pId"));
					groupEntity
							.setGroupname(jsonObject2.getString("termName"));
					List<ChildEntity> list2 = new ArrayList<ChildEntity>();
					JSONArray jsonArray2 = jsonObject2
							.getJSONArray("teamUserList");
					for (int j = 0; j < jsonArray2.length(); j++) {
						JSONObject jsonObject = (JSONObject) jsonArray2.opt(j);
						ChildEntity childEntity = new ChildEntity();
						childEntity.setOnlyId(jsonObject.getString("id"));
						childEntity.setpId(jsonObject.getString("pId"));
						childEntity.setChild_id(jsonObject.getString("postFlag"));
						childEntity.setZhiwei(jsonObject.getString("post"));
						childEntity.setEmail(jsonObject
								.getString("email"));
						childEntity.setName(jsonObject.getString("name"));
						childEntity.setPhoneNumber(jsonObject
								.getString("phoneNumOne"));
						//childEntity.setIsChecked(false);
//						childEntity
//								.setSignin(jsonObject.getString("signState"));
						list2.add(childEntity);
					}
					groupEntity.setcList(list2);
					list.add(groupEntity);
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
