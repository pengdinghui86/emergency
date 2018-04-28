package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.DrillProcationDetailEntity;
import com.dssm.esc.model.entity.emergency.DrillProcationDetailObjEntity;
import com.dssm.esc.model.entity.emergency.DrillProjectDetailObjPreinfoEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
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
 * 演练计划预案详情解析
 * 
 * @author zsj
 * 
 */
public class DrillPrecautionDetailParser {
	public DrillProcationDetailEntity userEntity;
	FinalHttp finalHttp;
	OnDataCompleterListener OnEmergencyCompleterListener;

	public DrillPrecautionDetailParser(String detailPlanId,String drillPlanName,
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		finalHttp = Utils.getInstance().getFinalHttp();
		MyCookieStore.getcookieStore(finalHttp);
		this.OnEmergencyCompleterListener = completeListener;
		request(detailPlanId,drillPlanName);
	}

	/**
	 * 发送请求
	 * @param detailPlanId 详细演练计划id
	 */
	public void request(final String detailPlanId,final String drillPlanName) {

		AjaxParams params = new AjaxParams();
		// params.put("userName",userEntity.getUsername());
		// params.put("password", userEntity.getPassword());
		params.put("drillPlanId", detailPlanId);
		params.put("drillPlanName", drillPlanName);
		finalHttp.post(DemoApplication.getInstance().getUrl()+HttpUrl.DRILLPROJECTNAMEDETAIL, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);

				OnEmergencyCompleterListener.onEmergencyParserComplete(null,
						strMsg);
				Log.i("onFailure", "strMsg" + strMsg);
				if (errorNo==518) {
					Utils.getInstance().relogin();
					request(detailPlanId,drillPlanName);
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
				MyCookieStore.setcookieStore(finalHttp);
				Log.i("DrillPrecautionDetailParser", t);
				userEntity = getDrillProjectDetail(t);
				Log.i("DrillPrecautionDetailParser", "DrillPrecautionDetailParser" + userEntity);
				OnEmergencyCompleterListener.onEmergencyParserComplete(
						userEntity, null);

			}

		});

	}

	/**
	 * 获取演练计划详情
	 * 
	 * @param t
	 * @return
	 * @throws JSONException
	 */
	public DrillProcationDetailEntity getDrillProjectDetail(String t) {
		DrillProcationDetailEntity detailEntity = new DrillProcationDetailEntity();
		try {
			JSONObject jsonObject = new JSONObject(t);
			if (t.contains("success")) {
				detailEntity.setSuccess(jsonObject.getString("success"));
				detailEntity.setMessage(jsonObject.getString("message"));
				DrillProcationDetailObjEntity objEntity = new DrillProcationDetailObjEntity();
				if (jsonObject.getString("success").equals("true")) {
					JSONObject jsonObject3 = jsonObject.getJSONObject("obj");
					objEntity.setExPlanId(jsonObject3.getString("exPlanId"));
					objEntity.setEmergType(jsonObject3.getString("emergType"));
					objEntity.setDrillPlanName(jsonObject3
							.getString("drillPlanName"));
					objEntity.setDrillPlanId(jsonObject3
							.getString("drillPlanId"));
					List<DrillProjectDetailObjPreinfoEntity> list = new ArrayList<DrillProjectDetailObjPreinfoEntity>();
					JSONArray jsonArray = jsonObject3.getJSONArray("preInfo");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
						DrillProjectDetailObjPreinfoEntity preinfoEntity = new DrillProjectDetailObjPreinfoEntity();
						preinfoEntity.setPrecautionId(jsonObject2
								.getString("precautionId"));
						preinfoEntity.setPrecautionName(jsonObject2
								.getString("precautionName"));
						preinfoEntity.setDrillPrecautionId(jsonObject2
								.getString("drillPrecautionId"));
						preinfoEntity.setDetailPlanId(jsonObject2
								.getString("detailPlanId"));
						
						
						preinfoEntity.setPrecautionType(jsonObject2
								.getString("precautionType"));
						list.add(preinfoEntity);

					}
					objEntity.setPreInfo(list);
					
				}
				detailEntity.setObj(objEntity);
				return detailEntity;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return detailEntity;
		}

		return detailEntity;
	}
}
