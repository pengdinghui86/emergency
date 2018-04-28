package com.dssm.esc.model.jsonparser.contact;

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
 * 获取应急通知列表
 * 
 * @author zsj
 * 
 */
public class GetToastListParser {
	private List<GroupEntity> list;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetToastListParser(OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request();
	}

	/**
	 * 发送请求
	 * 
	 */
	public void request() {
		Log.i("应急通知URL", DemoApplication.getInstance().getUrl()+HttpUrl.GETNOTICELIST);
		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.GETNOTICELIST, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request();
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
				Log.i("GetToastListParser", t);
				MyCookieStore.setcookieStore(finalHttp);
				list = emergencyContactListParser(t);
				Log.i("GetToastListParser", "GetToastListParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

			}

		});
	}

	/**
	 * 应急通知数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<GroupEntity> emergencyContactListParser(String t) {
		List<GroupEntity> list = new ArrayList<GroupEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					GroupEntity groupEntity = new GroupEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					groupEntity.setGroup_id(jsonObject2.getString("parentId"));
					groupEntity.setGroupname(jsonObject2.getString("pName"));
					List<ChildEntity> list2 = new ArrayList<ChildEntity>();
					JSONArray jsonArray2 = jsonObject2.getJSONArray("info");
					if (jsonArray2.length() > 0) {
						for (int j = 0; j < jsonArray2.length(); j++) {
							JSONObject jsonObject = (JSONObject) jsonArray2
									.opt(j);
							ChildEntity childEntity = new ChildEntity();
							childEntity.setpId(jsonObject.getString("parentId"));
							childEntity.setChild_id(jsonObject.getString("postFlag"));
							// childEntity.setEmergTeam(jsonObject
							// .getString("name"));
							childEntity.setOnlyId(jsonObject.getString("id"));
							childEntity.setZhiwei(jsonObject
									.getString("postName"));
							childEntity.setName(jsonObject.getString("name"));
							childEntity.setPhoneNumber(jsonObject
									.getString("phoneNumOne"));
							 childEntity.setPhoneNumTwo(jsonObject
							 .getString("phoneNumtwo"));
							 childEntity.setEmail(jsonObject
							 .getString("email"));
							// childEntity.setSex(jsonObject
							// .getString("sex"));
							list2.add(childEntity);
						}
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
