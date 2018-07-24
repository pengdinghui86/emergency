package com.dssm.esc.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.PlanNameRowEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjListEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.widget.MyScrollView;
import com.dssm.esc.view.widget.SegmentControl;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 预案启动详情界面
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_planstar_detail)
public class PlanStarDetailActivity extends BaseActivity implements
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
    @ViewInject(R.id.segment_control_plandetail)
    SegmentControl mSegmentControl;
    /**
     * 事件编号
     */
    @ViewInject(R.id.event_number)
    private TextView event_number;
    /**
     * 事件名称
     */
    @ViewInject(R.id.event_name)
    private TextView event_name;
    /**
     * 事件提交人
     */
    @ViewInject(R.id.event_people)
    private TextView eventpeople;
    /**
     * 提交时间
     */
    @ViewInject(R.id.event_time)
    private TextView event_time;
    /**
     * 已用时
     */
    @ViewInject(R.id.event_over_time)
    private TextView event_over_time;

    /**
     * 行业类型
     */
    @ViewInject(R.id.business_type)
    private TextView business_type;
    /**
     * 事件等级
     */
    @ViewInject(R.id.event_level)
    private TextView event_level;
    /**
     * 事件类型
     */
    @ViewInject(R.id.event_type)
    private TextView event_type;

    /**
     * 事件场景
     */
    @ViewInject(R.id.event_background)
    private TextView event_background;
    /**
     * 预案名称
     */
    @ViewInject(R.id.plan_name)
    private TextView plan_name;
    /**
     * 事件描述
     */
    @ViewInject(R.id.event_des)
    private TextView event_des;
    /**
     * 应对建议
     */
    @ViewInject(R.id.suggestion)
    private TextView suggestion;
    /**
     * 事件详情的总布局
     */
    @ViewInject(R.id.event_detail_ll)
    MyScrollView event_detail_ll;
    /**
     * 预案处置的总布局
     */
    @ViewInject(R.id.plan_done_ll)
    private LinearLayout plan_done_ll;
    /**
     * 应急预案布局
     */
    @ViewInject(R.id.plan_ll)
    private LinearLayout plan_ll;
    /**
     * 演练预案布局
     */
    @ViewInject(R.id.plan_ll_2)
    private LinearLayout plan_ll_2;
    /**
     * 演练预案名称
     */
    @ViewInject(R.id.plan_name2)
    private TextView plan_name2;
    /**
     * 参考预案布局
     */
    @ViewInject(R.id.referPlan_name_ll)
    private LinearLayout referPlan_name_ll;
    /**
     * 参考预案
     */
    @ViewInject(R.id.referPlan_name)
    private TextView referPlan_name;
    /**
     * 其他预案布局
     */
    @ViewInject(R.id.otherReferPlan_name_ll)
    private LinearLayout otherReferPlan_name_ll;
    /**
     * 其他预案
     */
    @ViewInject(R.id.otherReferPlan_name)
    private TextView otherReferPlan_name;
    /**
     * 分类预案布局
     */
    @ViewInject(R.id.categoryPlan_name_ll)
    private LinearLayout categoryPlan_name_ll;
    /**
     * 分类预案
     */
    @ViewInject(R.id.categoryPlan_name)
    private TextView categoryPlan_name;
    /**
     * 预案处置的编辑框
     */
    @ViewInject(R.id.etc)
    private EditText etc;
    /**
     * 预案处置的编辑框的删除按钮
     */
    @ViewInject(R.id.img_delete)
    private ImageView img_delete;
    /**
     * 预案处置的驳回按钮
     */
    @ViewInject(R.id.dismisscount)
    private Button dismisscount;
    @ViewInject(R.id.start_tv)
    private TextView start_tv;

    /**
     * 预案处置的启动按钮
     */
    @ViewInject(R.id.start)
    private Button start;
    /**
     * 预案id 可以多选，以“|”隔开
     */
    private List<String> palnIds = new ArrayList<String>();
    /**
     * 预案名称 可以多选，以“|”隔开
     */
    private List<String> palnNames = new ArrayList<String>();
    /**
     * 参考预案 可以多选，以“|”隔开
     */
    private String referPlan = "";
    private String referPlanName = "";
    /**
     * 其他预案 可以多选，以“|”隔开
     */
    private String otherReferPlan = "";
    private String otherReferPlanName = "";
    /**
     * 分类预案 可以多选，以“|”隔开
     */
    private String categoryPlan = "";
    private String categoryPlanName = "";
    /**
     * 1,应急;2,演练
     */
    private String tag;
    /**
     * 预案名称
     */
    private String name = "";
    /**
     * 事件名称
     */
    private String planEveName = "";
    /**
     * 1,预案处置;2,事件详情 (SegmentControl点击)
     */
    private int sem_tags;
    /**
     * 事件ID
     */
    private String id = "";
    /**
     * 事件场景ID
     */
    private String scenarioId = "";
    /**
     * 事件提交人ID
     */
    private String submitterId = "";
    /**
     * 事件类型
     */
    private String eveType = "";
    private String overTime = "";
    /**
     * 已选预案的id，可多选
     */
    private String usePlan = "";// 预案ID
    private String drillPlanId = "";// 演练详细计划ID
    private String eveScenarioId = "";// 事件场景id
    private String opition = "";// 处置建议
    private String eveName = "";// 事件名称
    Runnable runnable = null;
    private PlanStarListDetailObjEntity obj = new PlanStarListDetailObjEntity();
    private String tradeTypeId = "";// 业务类型ID
    private String eveLevelId = "";// 事件等级ID
    private String planResType = "";// 预案来源类型 发送通知使用
    private String planName = "";// 预案名称 发送通知使用
    private String planResName;// 预案来源名称 发送通知使用
    private String hasStartAuth = "false";// 启动权限

    /**
     * 被选的预案id
     */
    private ArrayList<PlanNameRowEntity> selectedIds = new ArrayList<PlanNameRowEntity>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            PlanStarListDetailEntity detailEntity = (PlanStarListDetailEntity) msg.obj;
            switch (msg.what) {
                case 0:
                    if(detailEntity != null) {
                        if (detailEntity.getObj() != null)
                            obj = detailEntity.getObj();
                    }
                    else
                        return;
                    List<PlanStarListDetailObjListEntity> list = obj.getList();
                    for (int i = 0; i < list.size(); i++) {
                        name = name + "," + list.get(i).getName();
                        if (name.subSequence(0, 1).equals(",")) {
                            name = (String) name.subSequence(1, name.length());
                        }
                    }
                    scenarioId = obj.getEveScenarioId();
                    eveType = obj.getEveType();
                    submitterId = obj.getSubmitterId();
                    drillPlanId = obj.getDrillPlanId();// 演练详细计划ID
                    eveScenarioId = obj.getEveScenarioId();// 事件场景id
                    eveName = obj.getEveName();// 事件名称
                    tradeTypeId = obj.getTradeTypeId();
                    eveLevelId = obj.getEveLevelId();
                    planResType = obj.getPlanResType();
                    planResName = obj.getPlanResName();

                    event_number.setText(obj.getEveCode());
                    event_name.setText(obj.getEveName());
                    eventpeople.setText(obj.getSubmitter());
                    final String subTime = obj.getSubTime();
                    event_time.setText(subTime);
                    String nowTime = obj.getNowTime();
                    overTime = Utils.getInstance().getOverTime(nowTime, subTime);
                    event_over_time.setText(overTime);
                    business_type.setText(obj.getTradeType());
                    event_level.setText(obj.getEveLevel());
                    String eveType2 = obj.getEveType();
                    if (eveType2.equals("1")) {
                        event_type.setText("应急");
//                        start.setVisibility(View.VISIBLE);
                        start.setEnabled(true);
//                        start_tv.setVisibility(View.VISIBLE);
                    } else if (eveType2.equals("2")) {
                        event_type.setText("演练");
                        hasStartAuth = obj.getHasStartAuth();
                        if (hasStartAuth.equals("false")) {
//                            start.setVisibility(View.GONE);
                            start.setEnabled(false);
//                            start_tv.setVisibility(View.GONE);
                        } else if (hasStartAuth.equals("true")) {
//                            start.setVisibility(View.VISIBLE);
                            start.setEnabled(true);
//                            start_tv.setVisibility(View.VISIBLE);
                        }
                    }
                    event_background.setText(obj.getEveScenarioName());
                    plan_name.setText(name);
                    event_des.setText(obj.getEveDescription());
                    suggestion.setText(obj.getDealAdvice());
                    Log.i("预案启动详情-预案名称", name);
                    plan_name2.setText(name);

                    break;
                case 1:

                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_planstar_detail);
        View findViewById = findViewById(R.id.planstar_detail);
        findViewById.setFitsSystemWindows(true);
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        id = intent.getStringExtra("id");
        planEveName = intent.getStringExtra("name");
        // isStarter = intent.getStringExtra("isStarter");
        initview();
        segmentControlListDate();
        sem_tags = 1;// 默认预案处置
        initData(sem_tags);
    }

    private EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

        @Override
        public void setEmergencySeviceImplListListenser(
                Object object, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            PlanStarListDetailEntity detailEntity = null;
            if (object != null) {
                detailEntity = (PlanStarListDetailEntity) object;

            } else if (stRerror != null) {
                detailEntity = new PlanStarListDetailEntity();

            } else if (Exceptionerror != null) {
                detailEntity = new PlanStarListDetailEntity();
                ToastUtil.showToast(PlanStarDetailActivity.this,
                        Const.NETWORKERROR);
            }
            Message message = new Message();
            message.what = 0;
            message.obj = detailEntity;
            handler.sendMessage(message);
        }
    };

    private void initview() {
        // TODO Auto-generated method stub
        back.setVisibility(View.VISIBLE);

        title.setText("预案启动");
        if (tag.equals("1")) {

            plan_ll.setVisibility(View.VISIBLE);
            plan_ll_2.setVisibility(View.GONE);
        } else if (tag.equals("2")) {
            plan_ll.setVisibility(View.GONE);
            plan_ll_2.setVisibility(View.VISIBLE);
        }

        Control.getinstance().getEmergencyService().getPlanStarListDetail(id, listListenser);

        referPlan_name_ll.setOnClickListener(this);// 参考预案布局
        otherReferPlan_name_ll.setOnClickListener(this);// 其他预案布局
        categoryPlan_name_ll.setOnClickListener(this);// 分类预案布局
        img_delete.setOnClickListener(this);
        dismisscount.setOnClickListener(this);
        start.setOnClickListener(this);
        // setNetListener(this);
    }

    private void initData(int sem_tags) {
        if (sem_tags == 1) {// 1,预案处置
            event_detail_ll.setVisibility(View.GONE);
            plan_done_ll.setVisibility(View.VISIBLE);
            etc.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    // TODO Auto-generated method stub

                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    // TODO Auto-generated method stub

                }

                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        img_delete.setVisibility(View.INVISIBLE);
                    } else {
                        img_delete.setVisibility(View.VISIBLE);
                    }
                }
            });

        } else if (sem_tags == 2) {// 2,事件详情
            event_detail_ll.setVisibility(View.VISIBLE);
            plan_done_ll.setVisibility(View.GONE);
        }

    }

    /**
     * selectButton控制list数据
     *
     * @version 1.0
     * @createTime 2015-9-7,下午3:23:05
     * @updateTime 2015-9-7,下午3:23:05
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void segmentControlListDate() {
        // TODO Auto-generated method stub
        mSegmentControl
                .setmOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
                    @Override
                    public void onSegmentControlClick(int index) {
                        switch (index) {
                            case 0:// 1,预案处置
                                sem_tags = 1;

                                break;
                            case 1:// 2,事件详情
                                sem_tags = 2;
                                break;
                        }
                        initData(sem_tags);
                    }
                });
    }

    private ArrayList<PlanNameRowEntity> resutList4 = new ArrayList<PlanNameRowEntity>();
    private ArrayList<PlanNameRowEntity> resutList5 = new ArrayList<PlanNameRowEntity>();
    private ArrayList<PlanNameRowEntity> resutList6 = new ArrayList<PlanNameRowEntity>();

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.referPlan_name_ll: // 参考预案布局
                if (id != null) {

                    Intent intent = new Intent(PlanStarDetailActivity.this,
                            PlanNameActivity.class);
                    // intent.putExtra("tag", tag);
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("id", scenarioId);
                    bundle4.putInt("plantags", 1);
                    bundle4.putString("tags", "1");
                    bundle4.putSerializable("arrlist", resutList4);
                    intent.putExtras(bundle4);
                    // intent.putExtra("tags", "1");// 预案执行
                    // intent.putExtra("id", scenarioId);
                    // intent.putExtra("selectedIds", selectedIds);
                    // intent.putExtra("plantags", 1);// 参考预案
                    startActivityForResult(intent, 4);
                } else {
                    ToastUtil.showToast(PlanStarDetailActivity.this, "请先选择事件场景");
                }
                break;
            case R.id.otherReferPlan_name_ll: // 其他预案布局
                if (id != null) {

                    Intent intent = new Intent(PlanStarDetailActivity.this,
                            PlanNameActivity.class);
                    // intent.putExtra("tag", tag);
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("id", scenarioId);
                    bundle4.putInt("plantags", 2);
                    bundle4.putString("tags", "1");
                    bundle4.putSerializable("arrlist", resutList5);
                    intent.putExtras(bundle4);
                    // intent.putExtra("tags", "1");// 预案执行
                    // intent.putExtra("id", scenarioId);
                    // intent.putExtra("selectedIds", selectedIds);
                    // intent.putExtra("plantags", 2);// 其他预案
                    startActivityForResult(intent, 5);
                } else {
                    ToastUtil.showToast(PlanStarDetailActivity.this, "请先选择事件场景");
                }
                break;
            case R.id.categoryPlan_name_ll: // 分类预案布局
                if (id != null) {

                    Intent intent = new Intent(PlanStarDetailActivity.this,
                            PlanNameActivity.class);
                    // intent.putExtra("tag", tag);
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("id", tradeTypeId);
                    bundle4.putInt("plantags", 3);
                    bundle4.putString("tags", "1");
                    bundle4.putSerializable("arrlist", resutList6);
                    intent.putExtras(bundle4);
                    // intent.putExtra("tags", "1");// 预案执行
                    // intent.putExtra("selectedIds", selectedIds);
                    // intent.putExtra("id", scenarioId);
                    // intent.putExtra("plantags", 3);// 分类预案
                    startActivityForResult(intent, 6);
                } else {
                    ToastUtil.showToast(PlanStarDetailActivity.this, "请先选择事件场景");
                }
                break;

            case R.id.img_delete:// 预案处置的编辑框的删除按钮
                etc.setText("");
                break;
            case R.id.dismisscount:// 预案处置的驳回按钮
                opition = etc.getText().toString().trim();
                if (!id.equals("") && !planEveName.equals("")
                        && !submitterId.equals("") && !eveType.equals("")) {
                    new android.app.AlertDialog.Builder(PlanStarDetailActivity.this)
                            .setMessage("确定驳回该事件？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            Log.i("id", id);
                                            Log.i("name", planEveName);
                                            Log.i("submitterId", submitterId);
                                            Log.i("eveType", eveType);

                                            bohuiPlan();

                                        }
                                    })
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {

                                        }
                                    }).show();
                } else {
                    ToastUtil.showToast(PlanStarDetailActivity.this, "信息不完整！");
                }

                break;
            case R.id.start:// 预案处置的启动按钮
                String planname = plan_name.getText().toString().trim();
                opition = etc.getText().toString().trim();
                if (tag.equals("1") && palnIds.size() > 0 && palnNames.size() > 0) {
                    for (int i = 0; i < palnIds.size(); i++) {
                        usePlan = usePlan + "|" + palnIds.get(i);
                    }
                    if (usePlan.subSequence(0, 1).equals("|")) {
                        usePlan = (String) usePlan.subSequence(1, usePlan.length());
                    }

                    for (int i = 0; i < palnNames.size(); i++) {
                        planName = planName + "|" + palnNames.get(i);
                    }
                    if (planName.subSequence(0, 1).equals("|")) {
                        planName = (String) planName.subSequence(1,
                                planName.length());
                    }

                    // usePlan 已选预案 可以多选，以“|”隔开
                    // opition 处置意见
                    // planEveId 事件ID
                    // eveType 事件类型 1为应急，2为演练
                    // planEveName 事件名称
                    // eveScenarioId 事件场景ID
                    // drillPlanId 演练详细计划ID
                    // submitterId 提交人ID 发送通知使用
                    // tradeTypeId 业务类型ID 发送通知使用
                    // eveLevelId 事件等级ID 发送通知使用
                    // planResType 预案来源类型 发送通知使用
                    // planResName 预案来源名称 发送通知使用
                    // planName 预案名称 发送通知使用
                    // planId 预案ID 发送通知使用
                    Log.i("planEveId", id);
                    Log.i("usePlan", usePlan);
                    Log.i("opition", opition);
                    Log.i("eveType", eveType);
                    Log.i("planEveName", eveName);
                    Log.i("eveScenarioId", eveScenarioId);
                    Log.i("drillPlanId", drillPlanId);
                    Log.i("submitterId", submitterId);// 提交人ID
                    Log.i("tradeTypeId", tradeTypeId);// 业务类型ID
                    Log.i("eveLevelId", eveLevelId);// 事件等级ID
                    // planName=obj.getPlanName();
                    // planId=obj.getPlanId();
                    Log.i("planResName", planResName);// 预案来源名称 发送通知使用
                    Log.i("planResType", planResType);// 预案来源类型 发送通知使用
                    Log.i("planName", planName);// 预案名称 发送通知使用
                    Log.i("planId", usePlan);// 预案ID 发送通知使用
                    if (!id.equals("") && !opition.equals("")) {
                        startPlan();
                    } else {
                        ToastUtil.showToast(PlanStarDetailActivity.this, "处理意见必填！");
                    }
                } else if (tag.equals("2") && !planname.equals("")
                        && planname.length() > 0 && planname != null) {
                    if (!opition.equals("")) {
                        startPlan();
                    } else {
                        ToastUtil.showToast(PlanStarDetailActivity.this, "处理意见必填！");
                    }
                } else {
                    ToastUtil.showToast(PlanStarDetailActivity.this, "请选择预案！");
                }
                break;

        }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 4:
                String str4 = "";
                selectedIds.clear();
                if (data != null && resultCode == RESULT_OK) {

                    resutList4 = (ArrayList<PlanNameRowEntity>) data
                            .getSerializableExtra("arrlist");
                    ArrayList<PlanNameRowEntity> typelist4 = new ArrayList<PlanNameRowEntity>();
                    if (resutList4 != null && resutList4.size() > 0) {
                        typelist4.clear();
                        typelist4.addAll(resutList4);
                        if (typelist4.size() > 0) {
                            for (int i = 0; i < typelist4.size(); i++) {
                                if (typelist4.get(i).isSelect()) {

                                    str4 = str4 + "," + typelist4.get(i).getName();
                                    referPlan = referPlan + "|"
                                            + typelist4.get(i).getId();
                                    referPlanName = referPlanName + "|"
                                            + typelist4.get(i).getName();
                                    PlanNameRowEntity entity = new PlanNameRowEntity();
                                    entity.setId(typelist4.get(i).getId());
                                    selectedIds.add(entity);
                                }

                            }
                            if (referPlan.subSequence(0, 1).equals("|")) {
                                referPlan = (String) referPlan.subSequence(1,
                                        referPlan.length());
                            }
                            if (referPlanName.subSequence(0, 1).equals("|")) {
                                referPlanName = (String) referPlanName.subSequence(
                                        1, referPlanName.length());
                            }
                            palnIds.add(referPlan);
                            palnNames.add(referPlanName);
                        }
                    }
                }
                if (str4.length() > 0) {

                    referPlan_name.setText(str4.substring(1, str4.length()));
                }
//			else {
//				referPlan_name.setText(str4);
//			}
                break;
            case 5:
                String str5 = "";
                selectedIds.clear();
                if (data != null && resultCode == RESULT_OK) {
                    resutList5 = (ArrayList<PlanNameRowEntity>) data
                            .getSerializableExtra("arrlist");
                    ArrayList<PlanNameRowEntity> typelist5 = new ArrayList<PlanNameRowEntity>();
                    if (resutList5 != null && resutList5.size() > 0) {
                        typelist5.clear();
                        typelist5.addAll(resutList5);

                        if (typelist5.size() > 0) {
                            for (int i = 0; i < typelist5.size(); i++) {
                                if (typelist5.get(i).isSelect()) {

                                    str5 = str5 + "," + typelist5.get(i).getName();
                                    otherReferPlan = otherReferPlan + "|"
                                            + typelist5.get(i).getId();
                                    otherReferPlanName = otherReferPlanName + "|"
                                            + typelist5.get(i).getName();
                                    PlanNameRowEntity entity = new PlanNameRowEntity();
                                    entity.setId(typelist5.get(i).getId());
                                    selectedIds.add(entity);
                                }
                            }
                            if (otherReferPlan.subSequence(0, 1).equals("|")) {
                                otherReferPlan = (String) otherReferPlan
                                        .subSequence(1, otherReferPlan.length());
                            }
                            if (otherReferPlanName.subSequence(0, 1).equals("|")) {
                                otherReferPlanName = (String) otherReferPlanName
                                        .subSequence(1, otherReferPlanName.length());
                            }
                            palnIds.add(otherReferPlan);
                            palnNames.add(otherReferPlanName);
                        }
                    }

                }
                if (str5.length() > 0) {

                    otherReferPlan_name.setText(str5.substring(1, str5.length()));
                }
//			else {
//				otherReferPlan_name.setText(str5);
//			}
                break;

            case 6:
                String str6 = "";
                selectedIds.clear();
                if (data != null && resultCode == RESULT_OK) {
                    resutList6 = (ArrayList<PlanNameRowEntity>) data
                            .getSerializableExtra("arrlist");
                    ArrayList<PlanNameRowEntity> typelist6 = new ArrayList<PlanNameRowEntity>();
                    if (resutList6 != null && resutList6.size() > 0) {
                        typelist6.clear();
                        typelist6.addAll(resutList6);
                        if (typelist6.size() > 0) {
                            for (int i = 0; i < typelist6.size(); i++) {
                                if (typelist6.get(i).isSelect()) {
                                    str6 = str6 + "," + typelist6.get(i).getName();
                                    categoryPlan = categoryPlan + "|"
                                            + typelist6.get(i).getId();
                                    categoryPlanName = categoryPlanName + "|"
                                            + typelist6.get(i).getName();
                                    PlanNameRowEntity entity = new PlanNameRowEntity();
                                    entity.setId(typelist6.get(i).getId());
                                    selectedIds.add(entity);
                                }
                            }
                            if (categoryPlan.subSequence(0, 1).equals("|")) {
                                categoryPlan = (String) categoryPlan.subSequence(1,
                                        categoryPlan.length());
                            }
                            if (categoryPlanName.subSequence(0, 1).equals("|")) {
                                categoryPlanName = (String) categoryPlanName
                                        .subSequence(1, categoryPlanName.length());
                            }
                            palnIds.add(categoryPlan);
                            palnNames.add(categoryPlanName);
                        }
                    }
                }
                if (str6.length() > 0) {
                    categoryPlan_name.setText(str6.substring(1, str6.length()));
                }
//			else {
//				categoryPlan_name.setText(str6);
//			}
                break;
        }

    }

    private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser startListener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

        @Override
        public void setEmergencySeviceImplListenser(
                Boolean backflag, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            if (backflag) {
                ToastUtil.showToast(PlanStarDetailActivity.this, "操作成功");
            } else if (backflag == false) {
                ToastUtil.showToast(PlanStarDetailActivity.this, "操作失败");
            } else if (stRerror != null) {
                ToastUtil.showToast(PlanStarDetailActivity.this, stRerror);
            } else if (Exceptionerror != null) {
                ToastUtil.showToast(PlanStarDetailActivity.this, Exceptionerror);
            }
            EventBus.getDefault().post(new mainEvent("refres"));// 刷新预案启动列表
            finish();
            // if (Utils.getInstance().progressDialog.isShowing()) {
            Utils.getInstance().hideProgressDialog();
            // }
        }
    };

    /**
     * 预案启动
     */
    private void startPlan() {
        Utils.getInstance().showProgressDialog(PlanStarDetailActivity.this, "",
                Const.SUBMIT_MESSAGE);
        Log.i("预案启动选的预案ids", usePlan);
        Control.getinstance().getEmergencyService().planStar(id, usePlan, obj, startListener);
    }

    private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

        @Override
        public void setEmergencySeviceImplListenser(
                Boolean backflag, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            String str = null;
            if (backflag) {
                str = stRerror;
                ToastUtil.showToast(PlanStarDetailActivity.this,
                        str);
                EventBus.getDefault().post(new mainEvent("refres"));// 刷新预案启动列表
                finish();
            } else if (backflag == false) {
                ToastUtil.showToast(PlanStarDetailActivity.this,
                        stRerror);
            } else if (stRerror != null) {

                ToastUtil.showToast(PlanStarDetailActivity.this,
                        stRerror);
            } else if (Exceptionerror != null) {

                ToastUtil.showToast(PlanStarDetailActivity.this,
                        Exceptionerror);
            }

            // if (Utils.getInstance().progressDialog.isShowing()) {
            Utils.getInstance().hideProgressDialog();
            // }
        }
    };

    /**
     * 预案驳回
     */
    private void bohuiPlan() {
        Utils.getInstance().showProgressDialog(PlanStarDetailActivity.this, "",
                Const.SUBMIT_MESSAGE);
        Control.getinstance().getEmergencyService().planStarBohui(id, planEveName, submitterId, eveType, listenser);
    }

    @Override
    public void initNetData() {
        // TODO Auto-generated method stub
        initData(sem_tags);
    }
}
