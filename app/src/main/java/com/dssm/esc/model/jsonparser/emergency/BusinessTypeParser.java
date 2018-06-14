package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
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
 * 业务类型和事件等级解析类
 * 
 * @author zsj
 * 
 */
public class BusinessTypeParser {
	private List<BusinessTypeEntity> list;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public BusinessTypeParser(int tag,OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnEmergencyCompleterListener = completeListener;
		request(tag);
	}

	/**
	 * 
	 * 发送请求
	 * 
	 */
	public void request(final int tag) {
		String url=null;
		if (tag==1) {//业务类型
			url= DemoApplication.getInstance().getUrl()+HttpUrl.GET_BUSINESSTYPE;
		}else if (tag==2) {//事件等级
			url=DemoApplication.getInstance().getUrl()+HttpUrl.GET_EVENTLEVEL;
		}
		RequestParams params = new RequestParams(url);
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				Log.i("BusinessTypeParser", t);
				list = businessTypeParser(t);
				Log.i("BusinessTypeParser", "BusinessTypeParser" + list);
				OnEmergencyCompleterListener.onEmergencyParserComplete(list,
						null);

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
						request(tag);
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
	 * 业务类型和事件等级数据解析
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public List<BusinessTypeEntity> businessTypeParser(String t) {
		List<BusinessTypeEntity> list = new ArrayList<BusinessTypeEntity>();
		try {
			JSONArray jsonArray = new JSONArray(t);
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					businessTypeEntity.setId(jsonObject2.getString("id"));
					businessTypeEntity.setName(jsonObject2.getString("name"));
					list.add(businessTypeEntity);
				}
			} else {
				return list;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}

		return list;
	}

}
