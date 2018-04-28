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
 * 获取预案执行列表解析
 *
 * @author zsj
 */
public class GetPrecautionByPlanResParser {
    private List<GroupEntity> list;
    FinalHttp finalHttp;
    OnDataCompleterListener OnEmergencyCompleterListener;

    public GetPrecautionByPlanResParser(OnDataCompleterListener completeListener) {
        // TODO Auto-generated constructor stub
        finalHttp = Utils.getInstance().getFinalHttp();
        MyCookieStore.getcookieStore(finalHttp);
        this.OnEmergencyCompleterListener = completeListener;
        request();
    }

    /**
     * 发送请求
     */
    public void request() {
        Log.i("预案执行URL", DemoApplication.getInstance().getUrl() + HttpUrl.GETWEITPERFROMPRELIST);
        finalHttp.get(DemoApplication.getInstance().getUrl() + HttpUrl.GETWEITPERFROMPRELIST,
                new AjaxCallBack<String>() {
                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);

                        OnEmergencyCompleterListener.onEmergencyParserComplete(
                                null, strMsg);
                        Log.i("onFailure", "strMsg" + strMsg);
                        if (errorNo == 518) {
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
                    //    Log.i("GetPrecautionByPlanResParser", t);
                        MyCookieStore.setcookieStore(finalHttp);
                        list = planExecuteListParser(t);
                        OnEmergencyCompleterListener.onEmergencyParserComplete(
                                list, null);

                    }

                });
    }

    /**
     * 获取预案执行列表数据解析
     *
     * @param t
     * @return
     * @throws JSONException
     */
    public List<GroupEntity> planExecuteListParser(String t) {
        List<GroupEntity> list = new ArrayList<GroupEntity>();
        try {
            JSONArray jsonArray = new JSONArray(t);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    GroupEntity listEntity = new GroupEntity();
                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                    // planResType 预案来源类型 1、事件 2、演练计划
                    // drillPrecautionId 演练预案id
                    // planInfoId 执行预案ID
                    listEntity.setGroup_id(isNull(jsonObject2.getString("id")));
                    listEntity.setGroupname(isNull(jsonObject2
                            .getString("planName")));
                    listEntity
                            .setPlanId(isNull(jsonObject2.getString("planId")));
                    listEntity.setPlanResType(isNull(jsonObject2
                            .getString("planResType")));
                    listEntity.setDrillPrecautionId(isNull(jsonObject2
                            .getString("drillPrecautionId")));
                    listEntity.setPlanResId(isNull(jsonObject2
                            .getString("planResId")));
                    listEntity.setState(isNull(jsonObject2
                            .getString("state")));
                    List<ChildEntity> childList = new ArrayList<ChildEntity>();
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("data");
                    for (int j = 0; j < jsonArray2.length(); j++) {
                        JSONObject jsonObject = (JSONObject) jsonArray2.opt(j);
                        ChildEntity childEntity = new ChildEntity();

                        /**
                         * 新增颜色节点
                         * 2017/10/13
                         */
                        if(jsonObject.has("code")){
                            childEntity.setCode(isNull(jsonObject.getString("code")));
                        }

                        childEntity.setChild_id(isNull(jsonObject
                                .getString("id")));
                        childEntity.setPrecautionId(isNull(jsonObject
                                .getString("precautionId")));
                        childEntity.setProcessName(isNull(jsonObject
                                .getString("name")));
                        childEntity.setStatus(isNull(jsonObject
                                .getString("status")));
                        childEntity.setPlanInfoId(isNull(jsonObject
                                .getString("planInfoId")));
                        childEntity.setManualDetailId(isNull(jsonObject
                                .getString("manualDetailId")));
                        childList.add(childEntity);
                    }
                    listEntity.setcList(childList);
                    list.add(listEntity);
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

    /**
     * 判断服务器返回的数据是否为空
     *
     * @param string
     * @return
     */
    private String isNull(String string) {
        if (!string.equals("") && string != null && !string.equals("null")
                && string.length() > 0) {
            return string;
        } else {

            return "";
        }
    }

}
