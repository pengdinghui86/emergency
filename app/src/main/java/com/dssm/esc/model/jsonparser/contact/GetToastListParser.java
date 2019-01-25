package com.dssm.esc.model.jsonparser.contact;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
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
 * 获取应急通知列表
 * 
 * @author zsj
 * 
 */
public class GetToastListParser {
	private List<GroupEntity> list;
	private final WeakReference<OnDataCompleterListener> wr;

	public GetToastListParser(OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<>(completeListener);
		request();
	}

	/**
	 * 发送请求
	 * 
	 */
	public void request() {
		Log.i("应急通知URL", DemoApplication.getInstance().getUrl()+HttpUrl.GETNOTICELIST);
		RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETNOTICELIST);
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
		final OnDataCompleterListener onEmergencyCompleterListener = wr.get();
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				if(DemoApplication.sessionTimeoutCount > 0)
					DemoApplication.sessionTimeoutCount = 0;
				Log.i("GetToastListParser", t);
				list = emergencyContactListParser(t);
				Log.i("GetToastListParser", "GetToastListParser" + list);
				if(onEmergencyCompleterListener != null)
					onEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

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
							request();
					}
					responseMsg = httpEx.getMessage();
					//					errorResult = httpEx.getResult();
				} else if(errorResult.equals("java.lang.NullPointerException")) {
					errorResult = "登录超时";
					Utils.getInstance().relogin();
					if(DemoApplication.sessionTimeoutCount < 5)
						request();
				} else { //其他错误
					errorResult = "其他错误";
				}
				if(onEmergencyCompleterListener != null)
					onEmergencyCompleterListener.onEmergencyParserComplete(null, errorResult);
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
							childEntity.setEmergTeam(jsonObject
									.getString("deptName"));
							childEntity.setOnlyId(jsonObject.getString("id"));
							childEntity.setUserId(jsonObject.getString("userId"));
							childEntity.setZhiwei(jsonObject
									.getString("postName"));
							childEntity.setName(jsonObject.getString("name"));
							childEntity.setPhoneNumber(jsonObject
									.getString("phoneNumOne"));
							 childEntity.setPhoneNumTwo(jsonObject
							 .getString("phoneNumtwo"));
							 childEntity.setEmail(jsonObject
							 .getString("email"));
							 childEntity.setSex(jsonObject
							 .getString("sex"));
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
