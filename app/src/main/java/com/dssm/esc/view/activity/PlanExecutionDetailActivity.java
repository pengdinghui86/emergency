package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailObjEntity;
import com.dssm.esc.status.RealTimeTrackingStatus;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 预案执行详情界面
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-14
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_palndetail)
public class PlanExecutionDetailActivity extends BaseActivity implements
        OnClickListener, MainActivity.onInitNetListener {
    /**
     * 标题
     */
    @ViewInject(R.id.tv_actionbar_title)
    private TextView title;
    /**
     * 返回按钮
     */
    @ViewInject(R.id.iv_actionbar_back)
    private ImageView back;
    /** 1，应急；2，演练 */
    // private String tag;
    /**
     * 预案名称
     */
    @ViewInject(R.id.event_name)
    private TextView event_name;
    /**
     * 预案类型
     */
    @ViewInject(R.id.planTypeNametv)
    private TextView event_desc;
    /**
     * 所属事件
     */
    @ViewInject(R.id.planResTypetv)
    private TextView planResTypetv;
    /**
     * 预案来源
     */
    @ViewInject(R.id.planResNametv)
    private TextView planResNametv;
    /**
     * 执行总布局
     */
    @ViewInject(R.id.execute_ll)
    private LinearLayout execute_ll;
    /**
     * 切换总布局
     */
    @ViewInject(R.id.change_ll)
    private LinearLayout change_ll;
    /**
     * 执行按钮
     */
    @ViewInject(R.id.execute)
    private TextView execute;
    /**
     * 操作手册
     */
    @ViewInject(R.id.operation_menu)
    private TextView operation_menu;
    /**
     * 操作手册布局下方的分隔线
     */
    @ViewInject(R.id.view_operation)
    private View view_operation;
    /**
     * 操作手册布局
     */
    @ViewInject(R.id.ll_operation_menu)
    private LinearLayout ll_operation_menu;
    /**
     * 操作手册
     */
    @ViewInject(R.id.operation_menu2)
    private ImageView operation_menu2;

    /**
     * 切换的完成状态布局
     */
    @ViewInject(R.id.done_status_ll)
    private LinearLayout done_status_ll;
    /**
     * 切换的完成状态
     */
    @ViewInject(R.id.done_status)
    private TextView done_status;
    /**
     * 提交信息布局
     */
    @ViewInject(R.id.submit_infomation_ll)
    private LinearLayout submit_infomation_ll;
    /**
     * 提交信息
     */
    @ViewInject(R.id.submit_infomation)
    private TextView submit_infomation;
    /**
     * 切换按钮
     */
    @ViewInject(R.id.change)
    private TextView change;
    /**
     * 已完成总布局
     */
    @ViewInject(R.id.done_ok_ll)
    private LinearLayout done_ok_ll;
    /**
     * 提交信息
     */
    @ViewInject(R.id.alltime)
    private TextView alltime;
    /**
     * 已完成的 完成状态
     */
    @ViewInject(R.id.done_status_done)
    private TextView done_status_done;
    /**
     * 已完成按钮
     */
    @ViewInject(R.id.okdone)
    private TextView okdone;
    private String planInfoId = "";
    private String id = "";
    private ChildEntity childEntity = null;
    private GroupEntity groupEntity = null;
    private String planResType = "";
    private String drillPrecautionId = "";
    private PlanDetailObjEntity obj;
    private String message = "";
    private String changeStatus = "";
    private String branchId = "";
    /**
     * 完成状态
     */
    private String okState = "";
    /**
     * 预案启动人
     */
    @ViewInject(R.id.planstarter)
    private TextView planstarter;

    /**
     * 预案启动时间
     */
    @ViewInject(R.id.startime)
    private TextView startime;
    /**
     * 预案摘要
     */
    @ViewInject(R.id.plansummary)
    private TextView plansummary;
    /**
     * 预案启动人
     */
    @ViewInject(R.id.advice)
    private TextView advice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_palndetail);
        View findViewById = findViewById(R.id.palndetail);
        findViewById.setFitsSystemWindows(true);
        Intent intent = getIntent();
        childEntity = (ChildEntity) intent.getSerializableExtra("entity");
        groupEntity = (GroupEntity) intent.getSerializableExtra("groupEntity");
        planInfoId = childEntity.getPlanInfoId();
        id = childEntity.getChild_id();
        initView();
    }

    private void initView() {
        back.setVisibility(View.VISIBLE);
        title.setText(childEntity.getProcessName());
        String status = childEntity.getStatus();
        submit_infomation_ll.setOnClickListener(this);
        operation_menu.setOnClickListener(this);
        operation_menu2.setOnClickListener(this);
        execute.setOnClickListener(this);
        done_status_ll.setOnClickListener(this);
        okdone.setOnClickListener(this);
        change.setOnClickListener(this);
        initData();
        if (status.equals("4") && groupEntity.getState().equals("3")) {
            change_ll.setVisibility(View.VISIBLE);
            execute_ll.setVisibility(View.GONE);
        } else if (status.equals("5") && groupEntity.getState().equals("3")) {
            change_ll.setVisibility(View.GONE);
            execute.setText(getString(R.string.execute_start));
            execute_ll.setVisibility(View.VISIBLE);

        } else if ((status.equals(RealTimeTrackingStatus.EXCEPTION_EXCEED)
                || status.equals(RealTimeTrackingStatus.EXCEPTION_OPTION_TIME_OUT))
                && groupEntity.getState().equals("3")) {
            change_ll.setVisibility(View.GONE);
            execute.setText(getString(R.string.execute_error));
            execute_ll.setVisibility(View.VISIBLE);

        } else if (status.equals(RealTimeTrackingStatus.EXCEPTION_OPTION_STOP)
                && groupEntity.getState().equals("3")) {
            change_ll.setVisibility(View.GONE);
            execute.setText(getString(R.string.error_cancel));
            execute_ll.setVisibility(View.VISIBLE);

        } else {
            change_ll.setVisibility(View.GONE);
            execute_ll.setVisibility(View.GONE);

        }

        if(childEntity.getNodeStepType().equals("CallActivity")) {
            ll_operation_menu.setVisibility(View.GONE);
            view_operation.setVisibility(View.GONE);
            execute.setVisibility(View.GONE);
            change.setVisibility(View.GONE);
            done_status_ll.setVisibility(View.GONE);
            submit_infomation_ll.setVisibility(View.GONE);
            done_ok_ll.setVisibility(View.GONE);
        }
//		setNetListener(this);
    }

    private void initData() {
        Control.getinstance().getEmergencyService().getPlanDetail(planInfoId,
                new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

                    @Override
                    public void setEmergencySeviceImplListListenser(
                            Object object, String stRerror,
                            String Exceptionerror) {
                        // TODO Auto-generated method stub
                        if (object != null) {
                            PlanDetailEntity planDetailEntity = (PlanDetailEntity) object;
                            obj = planDetailEntity.getObj();
                            event_name.setText(obj.getPlanName());

                            planstarter.setText(obj.getPlanStarter());
                            startime.setText(obj.getPlanStartTime());
                            plansummary.setText(obj.getSummary());
                            advice.setText(obj.getPlanStartOpition());
                            event_desc.setText(obj.getPlanTypeName());
                            String planResType2 = obj.getPlanResType();
                            if (planResType2.equals("1")) {
                                planResTypetv.setText("应急");
                            } else if (planResType2.equals("2")) {
                                planResTypetv.setText("演练");
                            }
                            planResNametv.setText(obj.getPlanResName());
                            // if (tag.equals("2")) {
                            planResType = obj.getPlanResType();
                            drillPrecautionId = obj.getDrillPrecautionId();
                        } else if (stRerror != null) {
                            ToastUtil.showToast(
                                    PlanExecutionDetailActivity.this, stRerror);
                        } else if (Exceptionerror != null) {
                            ToastUtil.showToast(PlanExecutionDetailActivity.this,
                                    Const.NETWORKERROR + ":" + Exceptionerror);
                        }
                    }
                });
    }

    /**
     * 带回来的集合
     */
    List<BusinessTypeEntity> resutList = new ArrayList<BusinessTypeEntity>();

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.operation_menu:// 操作手册
                Intent intent3 = new Intent(PlanExecutionDetailActivity.this,
                        OperationMenuActivity.class);
                intent3.putExtra("manualDetailId", childEntity.getManualDetailId());
                intent3.putExtra("planResType", planResType);
                intent3.putExtra("drillPrecautionId", drillPrecautionId);
                intent3.putExtra("name", childEntity.getProcessName());
                intent3.putExtra("tag", "2");// 步骤点进去
                startActivity(intent3);
                break;
            case R.id.operation_menu2:// 操作手册
                Intent intent4 = new Intent(PlanExecutionDetailActivity.this,
                        OperationMenuActivity.class);
                intent4.putExtra("manualDetailId", childEntity.getManualDetailId());
                intent4.putExtra("planResType", planResType);
                intent4.putExtra("drillPrecautionId", drillPrecautionId);
                intent4.putExtra("name", childEntity.getProcessName());
                intent4.putExtra("tag", "2");// 步骤点进去
                startActivity(intent4);
                break;
            case R.id.execute:// 执行
                Utils.getInstance().showProgressDialog(
                        PlanExecutionDetailActivity.this, "", Const.SUBMIT_MESSAGE);
                Control.getinstance().getEmergencyService().getBeginPlan(id, planInfoId,
                        new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

                            @Override
                            public void setEmergencySeviceImplListenser(
                                    Boolean backflag, String stRerror,
                                    String Exceptionerror) {
                                // TODO Auto-generated method stub
                                String str = null;
                                if (backflag) {
                                    str = stRerror;
//                                    if(execute.getText().toString().equals(getString(R.string.error_cancel))) {
//
//                                    }
//                                    else {
//
//                                    }
                                    execute_ll.setVisibility(View.GONE);
                                    change_ll.setVisibility(View.VISIBLE);
                                    EventBus.getDefault().post(
                                            new mainEvent("refre"));// 刷新预案执行列表
                                    ToastUtil.showLongToast(
                                            PlanExecutionDetailActivity.this, str);
                                } else if (backflag == false) {
                                    str = stRerror;
                                    ToastUtil.showLongToast(
                                            PlanExecutionDetailActivity.this, str);
                                } else if (stRerror != null) {

                                    str = stRerror;
                                    ToastUtil.showLongToast(
                                            PlanExecutionDetailActivity.this, str);
                                } else if (Exceptionerror != null) {

                                    str = Const.NETWORKERROR + Exceptionerror;
                                    ToastUtil.showLongToast(
                                            PlanExecutionDetailActivity.this, str);
                                }
//							if (Utils.getInstance().progressDialog.isShowing()) {
                                Utils.getInstance().hideProgressDialog();
//							}
                            }
                        });
                break;
            case R.id.submit_infomation_ll:// 提交信息布局
                Intent intent2 = new Intent(PlanExecutionDetailActivity.this,
                        SubmitInfomationActivity.class);
                intent2.putExtra("tag", "1");// 预案执行
                startActivityForResult(intent2, 0);
                break;
            case R.id.done_status_ll:// 完成状态布局
                Intent intent = new Intent(PlanExecutionDetailActivity.this,
                        EmergencyTypeActivity.class);
                // intent.putExtra("tag", tag);
                Bundle bundle = new Bundle();
                if(childEntity.getNodeStepType() != null && "ExclusiveGateway".equals(childEntity.getNodeStepType())) {
                    bundle.putString("nodeStepType", childEntity.getNodeStepType());
                    bundle.putSerializable("arrlist", (Serializable) childEntity.getBranches());
                }
                else
                    bundle.putSerializable("arrlist", (Serializable) resutList);
                bundle.putString("tags", "4");
                intent.putExtras(bundle);
                startActivityForResult(intent, 4);

                break;

            case R.id.change:// 切换

                Log.i("id", id);
                Log.i("planInfoId", planInfoId);
                Log.i("changeStatus", changeStatus);
                Log.i("message", message);
                if (!message.equals("")) {

                    if (!id.equals("") && !planInfoId.equals("")
                            && !changeStatus.equals("")) {
                        Utils.getInstance().showProgressDialog(
                                PlanExecutionDetailActivity.this, "",
                                Const.SUBMIT_MESSAGE);
                        Control.getinstance().getEmergencyService().swichOver(id, planInfoId, changeStatus, message, childEntity.getNodeStepType(), branchId,
                                new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

                                    @Override
                                    public void setEmergencySeviceImplListenser(
                                            Boolean backflag, String stRerror,
                                            String Exceptionerror) {
                                        // TODO Auto-generated method stub
                                        String str = null;
                                        if (backflag) {
                                            str = stRerror;
                                            ToastUtil
                                                    .showLongToast(
                                                            PlanExecutionDetailActivity.this,
                                                            str);
                                            change_ll.setVisibility(View.GONE);
                                            done_ok_ll.setVisibility(View.VISIBLE);
                                            // String overTime2 =
                                            // getOverTime(executeTime);
                                            // Log.i("overTime2", overTime2);
                                            alltime.setText(message);
                                            if (changeStatus.equals("2")) {//
                                                done_status_done.setText("部分完成");
                                            } else if (changeStatus.equals("1")) {
                                                done_status_done.setText("全部完成");
                                            } else if (changeStatus.equals("3")) {
                                                done_status_done.setText("跳过");
                                            }
                                            else
                                                done_status_done.setText(changeStatus);
                                            EventBus.getDefault().post(
                                                    new mainEvent("refre"));// 刷新预案执行列表
                                        } else if (backflag == false) {
                                            str = stRerror;
                                            ToastUtil
                                                    .showLongToast(
                                                            PlanExecutionDetailActivity.this,
                                                            str);
                                        } else if (stRerror != null) {
                                            str = stRerror;
                                            ToastUtil
                                                    .showLongToast(
                                                            PlanExecutionDetailActivity.this,
                                                            str);
                                        } else if (Exceptionerror != null) {

                                            str = Const.NETWORKERROR
                                                    + Exceptionerror;
                                            ToastUtil
                                                    .showLongToast(
                                                            PlanExecutionDetailActivity.this,
                                                            str);
                                        }
                                        if (Utils.getInstance().progressDialog
                                                .isShowing()) {
                                            Utils.getInstance()
                                                    .hideProgressDialog();
                                        }
                                    }
                                });
                    } else {
                        ToastUtil.showLongToast(PlanExecutionDetailActivity.this,
                                "提交信息不完整");
                    }
                } else {
                    ToastUtil.showLongToast(PlanExecutionDetailActivity.this,
                            "完成情况必填");
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == RESULT_OK) {

            switch (requestCode) {
                case 0:
                    message = data.getStringExtra("info");
                    submit_infomation.setText(message);
                    break;
                case 4:
                    String status = "";
                    resutList = (ArrayList<BusinessTypeEntity>) data
                            .getSerializableExtra("arrlist");
                    ArrayList<BusinessTypeEntity> typelist = new ArrayList<BusinessTypeEntity>();
                    if (resutList != null && resutList.size() > 0) {
                        typelist.clear();
                        typelist.addAll(resutList);

                        if(childEntity.getNodeStepType().equals("ExclusiveGateway")) {
                            for (int i = 0; i < typelist.size(); i++) {
                                BusinessTypeEntity businessTypeEntity = typelist.get(i);
                                boolean select = businessTypeEntity.isSelect();
                                if (select) {
                                    branchId = businessTypeEntity.getId();
                                    status = businessTypeEntity.getName();
                                    break;
                                }
                            }
                            changeStatus = status;
                        }
                        else {
                            for (int i = 0; i < typelist.size(); i++) {
                                BusinessTypeEntity businessTypeEntity = typelist.get(i);
                                boolean select = businessTypeEntity.isSelect();
                                if (select) {
                                    status = businessTypeEntity.getName();
                                }
                            }
                            if (status.equals("部分完成")) {
                                changeStatus = "2";
                            } else if (status.equals("全部完成")) {
                                changeStatus = "1";
                            } else if (status.equals("跳过")) {
                                changeStatus = "3";
                            }
                        }
                        okState = status;
                        done_status.setText(status);
                    }
                    break;
            }
        } else {
            Log.i("=====", "data为null");
        }
    }

    @Override
    public void initNetData() {
        // TODO Auto-generated method stub
        initData();
    }

}
