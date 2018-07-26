package com.dssm.esc.model.jsonparser.emergency;

import android.util.Log;

import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

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
 * 获取预案执行列表解析
 *
 * @author zsj
 */
public class GetPrecautionByPlanResParser {
    private List<GroupEntity> list;
    private final WeakReference<OnDataCompleterListener> wr;

    public GetPrecautionByPlanResParser(OnDataCompleterListener completeListener) {
        // TODO Auto-generated constructor stub
        wr = new WeakReference<>(completeListener);
        request();
    }

    /**
     * 发送请求
     */
    public void request() {
        Log.i("预案执行URL", DemoApplication.getInstance().getUrl() + HttpUrl.GETWEITPERFROMPRELIST);
        RequestParams params = new RequestParams(DemoApplication.getInstance().getUrl()+HttpUrl.GETWEITPERFROMPRELIST);
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
                list = planExecuteListParser(t);
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
                        if(DemoApplication.sessionTimeoutCount < 3)
                            request();
                    }
                    responseMsg = httpEx.getMessage();
                    //					errorResult = httpEx.getResult();

                } else if(errorResult.equals("java.lang.NullPointerException")) {
                    errorResult = "登录超时";
                    Utils.getInstance().relogin();
                    if(DemoApplication.sessionTimeoutCount < 3)
                        request();
                }
                else { //其他错误
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
                        /**
                         * 判断节点
                         * 2018/5/2
                         */
                        if(jsonObject.has("nodeStepType")){
                            childEntity.setNodeStepType(isNull(jsonObject.getString("nodeStepType")));
                            if("ExclusiveGateway".equals(jsonObject.getString("nodeStepType")))
                            {
                                if(jsonObject.has("branches")&&!"".equals(jsonObject.getString("branches"))) {
                                    List<BusinessTypeEntity> branches = new ArrayList<>();
                                    JSONArray jsonArray3 = jsonObject.getJSONArray("branches");
                                    for (int k = 0; k < jsonArray3.length(); k++) {
                                        JSONObject jsonObject3 = (JSONObject) jsonArray3.opt(k);
                                        BusinessTypeEntity branch = new BusinessTypeEntity();
                                        branch.setId(jsonObject3.getString("id"));
                                        branch.setName(jsonObject3.getString("name"));
                                        branches.add(branch);
                                    }
                                    childEntity.setBranches(branches);
                                }
                            }
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
                        childEntity.setParentProcessStepId(isNull(jsonObject.getString("parentProcessStepId")));
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
