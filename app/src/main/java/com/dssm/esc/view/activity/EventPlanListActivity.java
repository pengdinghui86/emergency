package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl.ControlServiceImplBackValueListenser;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplListListenser;
import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.BaseActivity.onInitNetListener;
import com.dssm.esc.view.adapter.LeftSlidePlanAdapter;
import com.dssm.esc.view.widget.AutoListView;
import com.dssm.esc.view.widget.RefreshLinearLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 按事件分类展示预案列表界面
 */
@ContentView(R.layout.activity_dismissvaluation)
public class EventPlanListActivity extends BaseActivity implements
        LeftSlidePlanAdapter.IonSlidingViewClickListener, onInitNetListener {
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
    @ViewInject(R.id.dismissv_recyclerView)
    private RecyclerView mRecyclerView;
    /** 适配器 */
    private LeftSlidePlanAdapter adapter;
    /** 下拉刷新控件 */
    @ViewInject(R.id.dismissv_refreshLinearLayout)
    private RefreshLinearLayout refreshLinearLayout;
    /**
     * 数据源
     */
    private List<PlanStarListEntity> list = new ArrayList<PlanStarListEntity>();
    /**
     * 当前页面
     */
    private int i = 1;
    /**
     * 0,已授权；1,待授权；2,已启动预案；3,人员指派；4,协同通告；5,指挥与展示；6,预案执行；
     */
    private String tags;
    private String getCode = "";
    private String signState = "";// 签到状态0:未签到 1：已签到
    private Intent intent = null;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<PlanStarListEntity> result = (List<PlanStarListEntity>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    refreshLinearLayout.onCompleteRefresh();
                    list.clear();
                    list.addAll(result);
                    break;
                case AutoListView.LOAD:
                    refreshLinearLayout.onLoadComplete();
                    list.addAll(result);
                    break;
            }
            refreshLinearLayout.setResultSize(result.size(), i);
            adapter.notifyDataSetChanged();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        View findViewById = findViewById(R.id.dismissvaluation);
        findViewById.setFitsSystemWindows(true);

        Intent intent = getIntent();
        tags = intent.getStringExtra("tags");
        initview();
    }

    /***
     * 打开EventBus开关
     */
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 刷新界面
     */
    public void onEvent(mainEvent data) {
        if (data.getData().equals("r")) {
            initData();
        }
    }

    private void initview() {
        // TODO Auto-generated method stub
        back.setVisibility(View.VISIBLE);
        if (tags.equals("0")) {
            title.setText("已授权预案");
        } else if (tags.equals("1")) {
            title.setText("待授权预案");
        } else if (tags.equals("2")) {
            title.setText("已启动预案");
        } else if (tags.equals("3")) {
            title.setText("人员指派");
        } else if (tags.equals("4")) {
            title.setText("协同通告");
        } else if (tags.equals("5")) {
            title.setText("指挥与展示");
        } else if (tags.equals("6")) {
            title.setText("预案执行");
        }
        adapter = new LeftSlidePlanAdapter(
                EventPlanListActivity.this, list, tags);
        mRecyclerView.setAdapter(adapter);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        initData();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置控制Item增删的动画
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(divider);
        refreshLinearLayout.setOnRefreshListener(new RefreshLinearLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                i = 1;
                initData();
            }
        });
        refreshLinearLayout.setOnLoadListener(new RefreshLinearLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                loadData(AutoListView.LOAD);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(isSlideToBottom(recyclerView))
                    refreshLinearLayout.checkFooter();
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if(isSlideToBottom(recyclerView))
                    refreshLinearLayout.checkFooter();
            }
        });
    }

    @Override
    public void onItemClick(View view, final int position) {
        if (position >= 0 && position < list.size()) {
            if (tags.equals("0")) {
                //已授权预案
                intent = new Intent(
                        EventPlanListActivity.this,
                        PlanSuspandDetilActivity.class);
                intent.putExtra("id", list.get(position)
                        .getId());
                intent.putExtra("isAuthor", list.get(position)
                        .getIsAuthor());

                intent.putExtra("stop", "1");// 已授权的预案
                startActivity(intent);
            } else if (tags.equals("1")) {
                //待授权预案
                intent = new Intent(
                        EventPlanListActivity.this,
                        AutorizateDecDetailActivity.class);
                String closeTime = "";
                String submiterId = "";
                for(int i = position; i >= 0; i--)
                {
                    //预案所属的事件
                    if(list.get(i).getDataType() == 0)
                    {
                        closeTime = list.get(i).getCloseTime();
                        submiterId = list.get(i).getSubmitterId();
                        break;
                    }
                }
                intent.putExtra("id", list.get(position)
                        .getId());
                intent.putExtra("name", list.get(position)
                        .getPlanName());
                intent.putExtra("sceneName", list.get(position)
                        .getSceneName());
                intent.putExtra("planResType", list.get(position)
                        .getPlanResType());
                intent.putExtra("planResName", list.get(position)
                        .getPlanResName());
                //演练时需要
                intent.putExtra("drillPrecautionId", list.get(position)
                        .getPrecautionId());
                intent.putExtra("submitterId", submiterId);
                intent.putExtra("planStarterId", list.get(position)
                        .getPlanStarterId());
                intent.putExtra("planId", list.get(position)
                        .getPlanId());
                intent.putExtra("planResId", list.get(position)
                        .getPlanResId());
                intent.putExtra("isAuthor", list.get(position)
                        .getIsAuthor());
                intent.putExtra("status", list.get(position)
                        .getState());
                intent.putExtra("closeTime", closeTime);
                startActivity(intent);
            } else if (tags.equals("2")) {
                //已启动预案
                intent = new Intent(
                        EventPlanListActivity.this,
                        PlanSuspandDetilActivity.class);
                intent.putExtra("id", list.get(position)
                        .getId());
                intent.putExtra("isAuthor", list.get(position)
                        .getIsAuthor());
                intent.putExtra("stop", "1");// 已授权的预案
                startActivity(intent);
            } else if (tags.equals("3")) {
                //人员指派
                intent = new Intent(
                        EventPlanListActivity.this,
                        PersonnelAssignmentActivity.class);
                intent.putExtra("id", list.get(position)
                        .getId());
                intent.putExtra("isSign", list.get(position)
                        .getIsSign());
                startActivity(intent);
            } else if (tags.equals("4")) {
                //协同通告
                intent = new Intent(
                        EventPlanListActivity.this,
                        SendCollaborativeActivity.class);
                intent.putExtra("id", list.get(position)
                        .getId());
                intent.putExtra("name", list.get(position)
                        .getEveName());
                intent.putExtra("precautionId",
                        list.get(position).getPrecautionId());
                startActivity(intent);
            } else if (tags.equals("5")) {
                // 指挥与展示
                intent = new Intent(EventPlanListActivity.this,
                        ControlActivity.class);
                intent.putExtra("PlanTreeEntity", convert2PlanEntity(list.get(position)));
                startActivity(intent);
            } else if (tags.equals("6")) {
                //预案执行
                intent = new Intent(EventPlanListActivity.this,
                        PlanExecuteActivity.class);
                intent.putExtra("planInfoId", list.get(position).getId());
                intent.putExtra("name", list.get(position).getPlanName() + "-" + list.get(position).getSceneName());
                intent.putExtra("planResType", list.get(position).getPlanResType());
                intent.putExtra("drillPrecautionId", list.get(position).getPrecautionId());
                intent.putExtra("planStatus", list.get(position).getState());
                startActivity(intent);
            }
        }
    }

    private PlanEntity convert2PlanEntity(PlanStarListEntity planStarListEntity)
    {
        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(planStarListEntity.getId());
        planEntity.setPlanName(planStarListEntity.getPlanName());
        planEntity.setPlanResName(planStarListEntity.getPlanResName());
        planEntity.setPlanResType(planStarListEntity.getPlanResType());
        planEntity.setState(planStarListEntity.getState());
        planEntity.setPlanId(planStarListEntity.getPlanId());
        planEntity.setPlanResId(planStarListEntity.getPlanResId());
        return planEntity;
    }

    private PlanSuspandEntity convert2PlanSuspandEntity(PlanStarListEntity planStarListEntity, String opinion)
    {
        PlanSuspandEntity planSuspandEntity = new PlanSuspandEntity();
        String planName = planStarListEntity.getPlanName();
        planSuspandEntity.setId(planStarListEntity.getId());
        planSuspandEntity.setSuspendType("authSuspend");
        planSuspandEntity.setPlanSuspendOpition(opinion);
        planSuspandEntity.setPlanName(planName);
        String planResType = planStarListEntity.getPlanResType();
        if (planResType.equals("1")) {
            planResType="应急";
        } else if (planResType.equals("2")) {
            planResType="演练";
        }
        planSuspandEntity.setPlanResType(planResType);
        planSuspandEntity.setPlanId(planStarListEntity.getPlanId());
        planSuspandEntity.setPlanResName(planStarListEntity.getPlanResName());
        planSuspandEntity.setTradeTypeId(planStarListEntity.getTradeTypeId());
        planSuspandEntity.setEveLevelId(planStarListEntity.getEveLevelId());
        planSuspandEntity.setPlanStarterId(planStarListEntity.getPlanStarterId());
        String planAuthorId = planStarListEntity.getPlanAuthorId();
        if (planAuthorId == null || planAuthorId.equals("")
                || planAuthorId.equals("null")
                || planAuthorId.length() == 0) {
            planSuspandEntity.setPlanAuthorId("");
        }
        else
            planSuspandEntity.setPlanAuthorId(planAuthorId);
        planSuspandEntity.setSubmitterId(planStarListEntity.getSubmitterId());
        return planSuspandEntity;
    }

    @Override
    public void onFunction1BtnClick(View view, final int position) {
        if("5".equals(tags))
        {
            //预案启动
            new android.app.AlertDialog.Builder(EventPlanListActivity.this)
                    .setMessage("确定启动该预案？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    View view = LayoutInflater.from(
                                            EventPlanListActivity.this).inflate(
                                            R.layout.editcode, null);
                                    final EditText et = (EditText) view
                                            .findViewById(R.id.vc_code);
                                    getCode = Utils.getInstance().code();
                                    new AlertDialog.Builder(
                                            EventPlanListActivity.this)
                                            .setTitle("验证码：" + getCode)
                                            .setIcon(
                                                    android.R.drawable.ic_dialog_info)
                                            .setView(view)
                                            .setPositiveButton(
                                                    "确定",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            String v_code = et
                                                                    .getText()
                                                                    .toString()
                                                                    .trim();
                                                            if (!v_code
                                                                    .equals(getCode)) {
                                                                Toast.makeText(
                                                                        EventPlanListActivity.this,
                                                                        "验证码错误",
                                                                        Toast.LENGTH_SHORT)
                                                                        .show();
                                                            } else {

                                                                planStart(list.get(position).getId());
                                                            }
                                                        }
                                                    })
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            }).show();
        }
        else {
            View view1 = LayoutInflater.from(EventPlanListActivity.this).inflate(R.layout.edit_info, null);
            final EditText et = (EditText) view1.findViewById(R.id.et_info);
            new AlertDialog.Builder(EventPlanListActivity.this)
                    .setTitle("请输入处理意见")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(view1)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String info = et.getText().toString()
                                    .trim();
                            if (info.equals("")) {
                                Toast.makeText(
                                        EventPlanListActivity.this,
                                        "处理意见不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                String submitterId = "";
                                for(int i = position; i >= 0; i--)
                                {
                                    if(list.get(i).getDataType() == 0)
                                    {
                                        submitterId = list.get(i).getSubmitterId();
                                        break;
                                    }
                                }
                                planAuth(list.get(position), submitterId, info);
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    @Override
    public void onFunction2BtnClick(View view, final int position) {
        View view2 = LayoutInflater.from(EventPlanListActivity.this).inflate(R.layout.edit_info, null);
        final EditText et = (EditText) view2.findViewById(R.id.et_info);
        new AlertDialog.Builder(EventPlanListActivity.this)
                .setTitle("请输入中止原因")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(view2)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String info = et.getText().toString()
                                .trim();
                        if (info.equals("")) {
                            Toast.makeText(
                                    EventPlanListActivity.this,
                                    "中止原因不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            planSuspand(convert2PlanSuspandEntity(list.get(position), info));
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 预案授权
     */
    private void planAuth(PlanStarListEntity planStarListEntity, String submitterId, String opinion) {
        Utils.getInstance().showProgressDialog(
                EventPlanListActivity.this, "", Const.SUBMIT_MESSAGE);
        Control.getinstance().getEmergencyService().planAuth(planStarListEntity.getId(), opinion, planStarListEntity.getPlanName(), planStarListEntity.getPlanResName(),
                planStarListEntity.getPlanResType(), planStarListEntity.getPlanId(), planStarListEntity.getPlanStarterId(), submitterId, planStarListEntity.getPrecautionId(), planAuthListener);

    }

    private EmergencySeviceImplBackBooleanListenser planAuthListener = new EmergencySeviceImplBackBooleanListenser() {

        @Override
        public void setEmergencySeviceImplListenser(
                Boolean backflag, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            if (backflag) {
                ToastUtil.showToast(
                        EventPlanListActivity.this, "操作成功");
                onEvent(new mainEvent("r"));//刷新列表界面
                Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
                intent.putExtra("msgType", "updatePlanCount");
                sendBroadcast(intent);
                finish();
            } else if (backflag == false) {
                ToastUtil.showToast(EventPlanListActivity.this,
                        "操作失败");
            } else if (stRerror != null) {

                ToastUtil.showLongToast(EventPlanListActivity.this,
                        stRerror);
            } else if (Exceptionerror != null) {

                ToastUtil.showLongToast(EventPlanListActivity.this,
                        Const.NETWORKERROR);
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    /**
     * 预案启动
     */
    private void planStart(String id) {
        // TODO Auto-generated method stub
        Utils.getInstance().showProgressDialog(EventPlanListActivity.this, "",
                Const.LOAD_MESSAGE);
        Control.getinstance().getControlSevice().starPlan(id, planStartListener);
    }

    private ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean> planStartListener = new ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean>() {

        @Override
        public void setControlServiceImplListenser(
                Boolean backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated
            // method stub
            if (backValue) {
                ToastUtil.showToast(EventPlanListActivity.this, "启动成功");
                EventBus.getDefault().post(new mainEvent("r"));// 刷新列表界面
            } else if (stRerror != null) {
                Toast.makeText(EventPlanListActivity.this, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else if (Exceptionerror != null) {
                Toast.makeText(EventPlanListActivity.this,
                        Const.NETWORKERROR,
                        Toast.LENGTH_SHORT).show();
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    /**
     * 预案中止
     */
    private void planSuspand(PlanSuspandEntity planSuspandEntity) {
        Utils.getInstance().showProgressDialog(
                EventPlanListActivity.this, "", Const.SUBMIT_MESSAGE);
        Control.getinstance().getEmergencyService().planSuspand(planSuspandEntity, suspandListener);

    }

    private EmergencySeviceImplBackBooleanListenser suspandListener = new EmergencySeviceImplBackBooleanListenser() {

        @Override
        public void setEmergencySeviceImplListenser(
                Boolean backflag, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            if (backflag) {
                ToastUtil.showToast(
                        EventPlanListActivity.this, "操作成功");
                onEvent(new mainEvent("r"));//刷新列表界面
                Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
                intent.putExtra("msgType", "updatePlanCount");
                sendBroadcast(intent);
            } else if (backflag == false) {
                ToastUtil.showToast(EventPlanListActivity.this,
                        "操作失败");
            } else if (stRerror != null) {

                ToastUtil.showLongToast(EventPlanListActivity.this,
                        stRerror);
            } else if (Exceptionerror != null) {

                ToastUtil.showLongToast(EventPlanListActivity.this,
                        Const.NETWORKERROR);
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        //computeVerticalScrollExtent()是当前屏幕显示的区域高度，
        // computeVerticalScrollOffset() 是当前屏幕之前滑过的距离，
        // 而computeVerticalScrollRange()是整个View控件的高度。
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void initData() {
        loadData(AutoListView.REFRESH);
    }

    /**
     * 从网络获取的总的list
     */
    private List<PlanStarListEntity> allList = new ArrayList<PlanStarListEntity>();
    /**
     * 从网络获取的总的list
     */
    private List<PlanStarListEntity> allList2 = new ArrayList<PlanStarListEntity>();
    /**
     * 每次显示20条
     */
    private int num = 20;//

    private void loadData(final int what) {
        if (what == 0) {// 刷新和第一次加载
            num = 20;
            getAuthList(Integer.parseInt(tags));
        } else if (what == 1) {// 加载更多
            getLoadData();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        list.clear();
    }

    private EmergencySeviceImplBackBooleanListenser listener = new EmergencySeviceImplBackBooleanListenser() {

        @Override
        public void setEmergencySeviceImplListenser(
                Boolean backflag, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            String str = null;
            if (backflag) {
                str = "已签到";
                signState = "1";
                Log.i("signState", signState);
                intent.putExtra("signState", "1");
                startActivity(intent);
            } else if (backflag == false) {
                str = "未签到";
                signState = "0";
                Log.i("signState", signState);
                intent.putExtra("signState", "0");
                startActivity(intent);
            } else if (stRerror != null) {

                str = stRerror;
                ToastUtil.showLongToast(
                        EventPlanListActivity.this, str);
            } else if (Exceptionerror != null) {

                str = Const.NETWORKERROR;
                ToastUtil.showLongToast(
                        EventPlanListActivity.this, str);
            }
        }
    };

    /**
     * 判断用户是否签到
     */
    private void checkUserSignin(int position) {
        Control.getinstance().getEmergencyService().checkEmergencySign(list.get(position - 1).getId(), listener);
    }

    private EmergencySeviceImplListListenser listListener = new EmergencySeviceImplListListenser() {
        @Override
        public void setEmergencySeviceImplListListenser(Object object,
                String stRerror, String Exceptionerror) {
            // TODO Auto-generated method stub
            List<PlanStarListEntity> dataList = null;
            Message message = handler.obtainMessage();
            if (object != null) {
                dataList = (List<PlanStarListEntity>) object;
                Log.i("决策授权列表的长度", dataList.size() + "");

            } else if (stRerror != null) {
                dataList = new ArrayList<PlanStarListEntity>();

            } else if (Exceptionerror != null) {
                dataList = new ArrayList<PlanStarListEntity>();
                ToastUtil.showToast(EventPlanListActivity.this,
                        Const.NETWORKERROR);
            }
            if (dataList.size() > 20) {// 如果超过20条，则分页
                List<PlanStarListEntity> subList = dataList.subList(0, 20);
                message.obj = subList;
            } else {
                message.obj = dataList;
            }
            message.what = 0;
            handler.sendMessage(message);
            allList = dataList;
            Utils.getInstance().hideProgressDialog();
        }
    };

    /**
     * 获取授权列表
     */
    private void getAuthList(int tag) {
		Utils.getInstance().showProgressDialog(EventPlanListActivity.this, "", Const.LOAD_MESSAGE);
        Control.getinstance().getEmergencyService().getAuthlist(tag, listListener);
    }

    /**
     * 获取加载更多的数据
     */
    private void getLoadData() {
        // 本地做分页，加载20条以后的数据，默认每20条分一页
        Log.i("list测试长度", allList.size() + "");
        Log.i("num", num + "");
        List<PlanStarListEntity> datalist2;
        if ((num + 20) <= allList.size()) {
            datalist2 = allList.subList(num, num + 20);
            num += 20;
        } else {
            datalist2 = allList.subList(num, allList.size());
        }
        Message message = handler.obtainMessage();
        message.what = 1;
        message.obj = datalist2;
        handler.sendMessage(message);
    }

    private ControlServiceImplBackValueListenser<List<PlanStarListEntity>> controlServiceImplBackValueListenser = new ControlServiceImplBackValueListenser<List<PlanStarListEntity>>() {

        @Override
        public void setControlServiceImplListenser(
                List<PlanStarListEntity> backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            Message message = handler.obtainMessage();
            message.what = 0;
            List<PlanStarListEntity> dataList = null;
            if (backValue != null) {
                dataList = backValue;

            } else if (stRerror != null) {
                dataList = new ArrayList<PlanStarListEntity>();

            } else if (Exceptionerror != null) {
                dataList = new ArrayList<PlanStarListEntity>();
                ToastUtil.showToast(EventPlanListActivity.this,
                        Const.NETWORKERROR);
            }
            if (dataList.size() > 20) {// 如果超过20条，则分页
                List<PlanStarListEntity> subList = dataList.subList(0, 20);
                message.obj = subList;
            } else {
                message.obj = dataList;
            }
            allList2 = dataList;
            handler.sendMessage(message);
//				if (Utils.getInstance().progressDialog.isShowing()) {
//					Utils.getInstance().hideProgressDialog();
//				}
        }
    };

    /**
     * 获取指挥与展示的数据
     */
    private void getControlData() {
//		Utils.getInstance().showProgressDialog(AutorizationDecisionActivity.this, "", Const.LOAD_MESSAGE);
        Control.getinstance().getControlSevice().getPlanlist(controlServiceImplBackValueListenser);
    }

    @Override
    public void initNetData() {
        // TODO Auto-generated method stub
        initData();
    }
}
