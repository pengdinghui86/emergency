package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
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
 * 签到详情解析
 * 
 * @author zsj
 * 
 */
public class GetSignEmergencyInfoParser {
	private List<GroupEntity> list;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public GetSignEmergencyInfoParser(String planInfoId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(planInfoId);
	}

	/**
	 * 发送请求
	 * 
	 * @param planInfoId
	 *            预案执行编号
	 */
	public void request(final String planInfoId) {
Log.i("签到详情url", DemoApplication.getInstance().getUrl()+HttpUrl.GETSIGNUSERINFO + planInfoId);
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETSIGNUSERINFO + planInfoId);
		x.http().get(params, new Callback.CommonCallback<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						Log.i("GetSignUserInfoParser", t);
						list = planStarListParser(t);
						Log.i("GetSignUserInfoParser", "GetSignUserInfoParser"
								+ list);
						OnEmergencyCompleterListener.onEmergencyParserComplete(
								list, null);

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
						childEntity.setOnlyId(jsonObject.getString("id"));
						childEntity.setChild_id(jsonObject.getString("postFlag"));
						childEntity.setEmergTeam(jsonObject
								.getString("emergTeam"));
						childEntity.setZhiwei(jsonObject.getString("postName"));
						childEntity.setRoleTypeName(jsonObject
								.getString("roleTypeName"));
						childEntity.setName(jsonObject.getString("userName"));
						childEntity.setPhoneNumber(jsonObject
								.getString("telephone"));
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
