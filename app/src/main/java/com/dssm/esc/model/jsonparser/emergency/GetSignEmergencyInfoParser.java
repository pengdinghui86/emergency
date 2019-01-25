package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanTreeEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.DemoApplication;

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
 * 签到详情解析
 * 
 * @author zsj
 * 
 */
public class GetSignEmergencyInfoParser {
	private List<PlanTreeEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetSignEmergencyInfoParser(String planInfoId,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
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
		final OnDataCompleterListener onEmergencyCompleteListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						if(DemoApplication.sessionTimeoutCount > 0)
							DemoApplication.sessionTimeoutCount = 0;
						Log.i("GetSignUserInfoParser", t);
						list = planStarListParser(t);
						Log.i("GetSignUserInfoParser", "GetSignUserInfoParser"
								+ list);
						if(onEmergencyCompleteListener != null)
							onEmergencyCompleteListener.onEmergencyParserComplete(
								list, null);

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
						if(DemoApplication.sessionTimeoutCount < 5)
							request(planInfoId);
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();

				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request(planInfoId);
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(onEmergencyCompleteListener != null)
					onEmergencyCompleteListener.onEmergencyParserComplete(null, errorResult);
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
	 * 2018.7.3接口返回的数据结构变动
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<PlanTreeEntity> planStarListParser(String t) {
		List<PlanTreeEntity> planList = new ArrayList<>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int k = 0; k < jsonArray.length(); k++) {
					PlanTreeEntity planEntity = new PlanTreeEntity();
					JSONObject jsonObject3 = (JSONObject) jsonArray.opt(k);
					planEntity.setName(jsonObject3.getString("name"));
					List<GroupEntity> list = new ArrayList<>();
					JSONArray jsonArray3 = jsonObject3
							.getJSONArray("emeGroups");
					for (int i = 0; i < jsonArray3.length(); i++) {
						GroupEntity groupEntity = new GroupEntity();
						JSONObject jsonObject2 = (JSONObject) jsonArray3.opt(i);
						groupEntity.setGroupname(jsonObject2.getString("emergTeam"));
						List<ChildEntity> list2 = new ArrayList<ChildEntity>();
						JSONArray jsonArray2 = jsonObject2
								.getJSONArray("users");
						for (int j = 0; j < jsonArray2.length(); j++) {
							JSONObject jsonObject = (JSONObject) jsonArray2.opt(j);
							ChildEntity childEntity = new ChildEntity();

							childEntity.setSex(jsonObject.getString("sex") == null ? "" : jsonObject.getString("sex"));
							childEntity.setChild_id(jsonObject.getString("id"));
							childEntity.setPostFlag(jsonObject.getString("postFlag"));
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
					planEntity.setEmeGroups(list);
					planList.add(planEntity);
				}
				return planList;
			} else {
				return planList;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return planList;
		}
	}
}
