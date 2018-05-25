package com.dssm.esc.model.jsonparser.control;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
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
 * 5.2.6应急通知接收及人员签到详情
 * @author zsj
 *
 */
public class GetSignUserInfoDetailParser {
	private List<GroupEntity> list;
	FinalHttp finalHttp;
	private ControlCompleterListenter<List<GroupEntity>> completeListener;

	public GetSignUserInfoDetailParser(String planInfoId,
			ControlCompleterListenter<List<GroupEntity>> completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.completeListener = completeListener;
		request(planInfoId);
	}

	/**
	 * 发送请求
	 * 
	 * @param planInfoId
	 *            预案执行编号
	 */
	public void request(final String planInfoId) {

		finalHttp.get(DemoApplication.getInstance().getUrl()+HttpUrl.SIGN_USER_LIST_COUNT_DETAIL + planInfoId,
				new AjaxCallBack<String>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);

						completeListener.controlParserComplete(null, strMsg);
						Log.i("onFailure", "strMsg" + strMsg);
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

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Log.i("GetSignUserInfoParser", t);
						MyCookieStore.setcookieStore(finalHttp);
						list = planStarListParser(t);
						Log.i("GetSignUserInfoParser", "GetSignUserInfoParser"
								+ list);
						completeListener.controlParserComplete(list, null);

					}

				});
	}

	/**
	 * 签到详情数据解析
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
							.setGroupname(jsonObject2.getString("emergTeam"));
					List<ChildEntity> list2 = new ArrayList<ChildEntity>();
					JSONArray jsonArray2 = jsonObject2
							.getJSONArray("teamUserList");
					for (int j = 0; j < jsonArray2.length(); j++) {
						JSONObject jsonObject = (JSONObject) jsonArray2.opt(j);
						ChildEntity childEntity = new ChildEntity();

						childEntity.setSex(jsonObject.getString("sex") == null ? "" : jsonObject.getString("sex"));
						childEntity.setChild_id(jsonObject.getString("id"));
						childEntity.setEmergTeam(jsonObject
								.getString("emergTeam"));
						childEntity.setZhiwei(jsonObject.getString("postName"));
						childEntity.setRoleTypeName(jsonObject
								.getString("roleTypeName"));
						childEntity.setName(jsonObject.getString("userName"));
						childEntity.setPhoneNumber(jsonObject
								.getString("telephone"));
						childEntity
								.setNoticeState(jsonObject.getString("noticeState"));
						childEntity
						.setSignin(jsonObject.getString("signState"));
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
