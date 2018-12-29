package com.dssm.esc.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ControlSevice;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
import com.dssm.esc.model.entity.control.NoticeSignUserEntity;
import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.entity.control.SignUserEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.RealTimeTrackingAdapter;
import com.dssm.esc.view.widget.AutoListView;
import com.dssm.esc.view.widget.CustomProgressBar;
import com.dssm.esc.view.widget.MyFlowView;
import com.dssm.esc.view.widget.NSSetPointValueToSteps;
import com.dssm.esc.view.widget.RingChartView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 指挥中心界面
 */
@ContentView(R.layout.activity_control)
public class ControlActivity extends BaseActivity implements OnClickListener,
        MainActivity.onInitNetListener, AutoListView.OnRefreshListener, AutoListView.OnLoadListener {

    /**
     * 返回按钮
     */
    @ViewInject(R.id.iv_actionbar_back)
    private ImageView back;
    /**
     * 跳转至协同公告按钮
     */
    @ViewInject(R.id.iv_actionbar_send_msg)
    private ImageView iv_send_msg;
    /**
     * 从上个页面带来的数据
     */
    PlanEntity planEntity;
    @ViewInject(R.id.rb_real_track)
    private RadioButton rb_real_track;
    @ViewInject(R.id.rb_process_monitor)
    private RadioButton rb_process_monitor;
    @ViewInject(R.id.rb_resource_prepare)
    private RadioButton rb_resource_prepare;
    /**
     * 点击button转换：1，实时跟踪2，流程监,3，资源筹
     */
    private int sem_tag;
    //查询状态0=全部，1=执行中，2=未执行，3=已执行
    private int sem_status = 0;
    /**
     * 实时跟踪的 include布局
     */
    @ViewInject(R.id.realtime_tracking)
    private View realtime_tracking;
    /**
     * 实时跟踪查询全部
     */
    @ViewInject(R.id.real_time_track_rb_all)
    private RadioButton real_time_track_rb_all;
    /**
     * 实时跟踪查询执行中步骤列表
     */
    @ViewInject(R.id.real_time_track_rb_executing)
    private RadioButton real_time_track_rb_executing;
    /**
     * 实时跟踪查询未执行步骤列表
     */
    @ViewInject(R.id.real_time_track_rb_wait_execute)
    private RadioButton real_time_track_rb_wait_execute;
    /**
     * 实时跟踪查询已执行步骤列表
     */
    @ViewInject(R.id.real_time_track_rb_executed)
    private RadioButton real_time_track_rb_executed;
    /**
     * 暂无数据
     */
    @ViewInject(R.id.ll_no_data_page)
    private LinearLayout ll_no_data_page;
    /**
     * 实时跟踪的列表
     */
    private AutoListView rlistview;

    private MyFlowView my_flow_view;

    /**
     * 实时跟踪的列表的适配器
     */
    private RealTimeTrackingAdapter radapter;
    /**
     * 实时跟踪的列表的数据源
     */
    private List<FlowChartPlanEntity.FlowChart> list = new ArrayList<FlowChartPlanEntity.FlowChart>();
    private List<FlowChartPlanEntity.FlowChart> alist = new ArrayList<FlowChartPlanEntity.FlowChart>();
    private List<FlowChartPlanEntity.FlowChart> flowCharts = new ArrayList<>();
    /**
     * 接收和签到数据整合
     */
    private List<NoticeSignUserEntity> noticeSignlist = new ArrayList<>();
    /**
     * 人数最多的小组的总人数数量,用来控制每一个小组的进度条的长度比例
     */
    private int maxNum = 0;
    /**
     * 全体总人数
     */
    private int totalNum = 0;
    /**
     * 已签到总人数
     */
    private int totalSignNum = 0;
    /**
     * 已接收总人数
     */
    private int totalNoticeNum = 0;
    /**
     * 资源筹备 include布局
     */
    @ViewInject(R.id.resource_preparation)
    private View resource_preparation;
    @ViewInject(R.id.resource_prepare_rcv)
    private RingChartView ringChartView;
    /**
     * 资源筹备的应急接收情况查看详情
     */
    private LinearLayout resource_prepare_ll_receive;
    /**
     * 资源筹备的小组签到情况查看详情
     */
    private LinearLayout resource_prepare_ll_sign;
    /**
     * 资源筹备总接收数量
     */
    private TextView resource_prepare_tv_receive;
    /**
     * 资源筹备总签到数量
     */
    private TextView resource_prepare_tv_sign;

    NSSetPointValueToSteps nsSetPointValueToSteps;
    LinearLayout lin_group;
    private UserSevice sevice;
    private ControlSevice csevice;

    /**
     * 当前页面
     */
    private int i = 1;
    /**
     * 保存用户信息
     */
    private MySharePreferencesService service = null;
    /**
     * 保存用户名和密码的map集合
     */
    private Map<String, String> map;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            /** 接收子集合 */
            List<FlowChartPlanEntity.FlowChart> result = (List<FlowChartPlanEntity.FlowChart>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    rlistview.onRefreshComplete();
                    /** 总集合清理 */
                    list.clear();
                    /** 总集合添加 */
                    list.addAll(result);
                    radapter = new RealTimeTrackingAdapter(ControlActivity.this, planEntity.getState(),
                            list, sevice, roleCode, csevice, curDate, service);
                    rlistview.setAdapter(radapter);
                    radapter.notifyDataSetChanged();
                    setResultSize(result.size(), i);
                    break;
                case 2:
                    WindowManager wm = (WindowManager) ControlActivity.this
                            .getSystemService(Context.WINDOW_SERVICE);
                    nsSetPointValueToSteps = new NSSetPointValueToSteps();
                    try {
                        nsSetPointValueToSteps.exampleSteps(result, "");
                        nsSetPointValueToSteps.proveStepPosition();
                        my_flow_view.setData(nsSetPointValueToSteps);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };

    /**
     * 角色编号（R003）流程控制管理员有跳过此步骤的权限
     */
    private String roleCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View findViewById = findViewById(R.id.control);
        findViewById.setFitsSystemWindows(true);
        my_flow_view = (MyFlowView) findViewById(R.id.my_flow_view);

        planEntity = (PlanEntity) getIntent().getSerializableExtra("PlanTreeEntity");
        sevice = Control.getinstance().getUserSevice();
        csevice = Control.getinstance().getControlSevice();
        service = MySharePreferencesService.getInstance(this);
        // 回显
        map = service.getPreferences();
        roleCode = map.get("roleCode");
        Log.i("roleCode", roleCode);
        initView();
        Utils.getInstance().showProgressDialog(
                ControlActivity.this, "",
                Const.SUBMIT_MESSAGE);
    }

    private void addIndex(List<FlowChartPlanEntity.FlowChart> result) {
        for(FlowChartPlanEntity.FlowChart flowChart : result) {
            int index = 0;
            String parentOrderNum = "";
            String parentId = flowChart.getParentProcessStepId();
            while (parentId != null && !"".equals(parentId)) {
                index++;
                int i = 0;
                for (FlowChartPlanEntity.FlowChart flowChart1 : result) {
                    if(flowChart1.getId().equals(parentId)) {
                        parentId = flowChart1.getParentProcessStepId();
                        String num = "";
                        if (!flowChart1.getOrderNum().equals("null")) {
                            num = flowChart1.getOrderNum() + ".";
                        }
                        else if (!flowChart1.getEditOrderNum().equals("null")) {
                            num = flowChart1.getEditOrderNum() + ".";
                        }
                        parentOrderNum = num + parentOrderNum;
                        i++;
                        break;
                    }
                }
                if(i == 0)
                    break;
            }
            flowChart.setIndex(index);
            flowChart.setParentOrderNum(parentOrderNum);
        }
    }

    private void reSort(List<FlowChartPlanEntity.FlowChart> result, String parentId, int index) {
        for(FlowChartPlanEntity.FlowChart flowChart : result) {
            if(flowChart.getIndex() == index
                    && parentId.equals(flowChart.getParentProcessStepId())) {
                flowCharts.add(flowChart);
                if("CallActivity".equals(flowChart.getNodeStepType())) {
                    reSort(result, flowChart.getId(), index + 1);
                }
            }
        }
    }

    private void initView() {
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        iv_send_msg.setOnClickListener(this);
        rb_real_track.setOnClickListener(this);
        rb_process_monitor.setOnClickListener(this);
        rb_resource_prepare.setOnClickListener(this);
        rlistview = (AutoListView) realtime_tracking
                .findViewById(R.id.realtime_track_listview);

        rlistview.setOnRefreshListener(this);
        rlistview.setLoadEnable(false);

        sem_tag = 1;
        initData(sem_tag);
        real_time_track_rb_all.setOnClickListener(this);
        real_time_track_rb_wait_execute.setOnClickListener(this);
        real_time_track_rb_executing.setOnClickListener(this);
        real_time_track_rb_executed.setOnClickListener(this);

        resource_prepare_ll_receive = (LinearLayout) resource_preparation
                .findViewById(R.id.resource_prepare_ll_receive);
        resource_prepare_ll_sign = (LinearLayout) resource_preparation
                .findViewById(R.id.resource_prepare_ll_sign);
        resource_prepare_tv_receive = (TextView) resource_preparation
                .findViewById(R.id.resource_prepare_tv_receive);
        resource_prepare_tv_sign = (TextView) resource_preparation
                .findViewById(R.id.resource_prepare_tv_sign);
        resource_prepare_ll_receive.setOnClickListener(this);
        resource_prepare_ll_sign.setOnClickListener(this);
        lin_group = (LinearLayout) resource_preparation
                .findViewById(R.id.lin_grop);
    }

    private void initData(int sem_tags) {
        if (sem_tags == 1) {
            realtime_tracking.setVisibility(View.VISIBLE);
            resource_preparation.setVisibility(View.GONE);
            my_flow_view.setVisibility(View.GONE);
            queryProcessTrack();
        } else if (sem_tags == 2) {
            my_flow_view.setVisibility(View.VISIBLE);
            realtime_tracking.setVisibility(View.GONE);
            resource_preparation.setVisibility(View.GONE);
            flowChartPlanData();
        } else if (sem_tags == 3) {
            realtime_tracking.setVisibility(View.GONE);
            resource_preparation.setVisibility(View.VISIBLE);
            my_flow_view.setVisibility(View.GONE);
            initProgressData();
        }
    }

    private void showTrackListByStatus(int status)
    {
        List<FlowChartPlanEntity.FlowChart> list = new ArrayList<FlowChartPlanEntity.FlowChart>();
        //排除流程未启动情况
        if(!planEntity.getState().equals("0") && !planEntity.getState().equals("2") && !planEntity.getState().equals("1")) {

        }
        switch (status) {
            //0=全部，1=执行中，2=未执行，3=执行完成
            case 0:
                list = allList;
                break;
            case 1:
                String[] statusList = new String[]{"4", "8", "10", "21", "22", "25"};
                for (FlowChartPlanEntity.FlowChart flowChart : allList) {
                    if (useLoop(statusList, flowChart.getStatus()))
                        list.add(flowChart);
                }
                break;
            case 2:
                String[] statusList2 = new String[]{"5", "6", "7", "20"};
                for (FlowChartPlanEntity.FlowChart flowChart : allList) {
                    if (useLoop(statusList2, flowChart.getStatus())
                            || (10 < Integer.parseInt(flowChart.getStatus())
                            && Integer.parseInt(flowChart.getStatus()) < 20))
                        list.add(flowChart);
                }
                break;
            case 3:
                String[] statusList3 = new String[]{"1", "2", "3", "9", "23", "24", "26", "27"};
                for (FlowChartPlanEntity.FlowChart flowChart : allList) {
                    if (useLoop(statusList3, flowChart.getStatus())
                            || ((null == flowChart.getType()) ? false
                            : flowChart.getType().equals("drillNew")))
                        list.add(flowChart);
                }
                break;
        }
        Message message = handler.obtainMessage();
        message.what = 0;
        message.obj = list;
        handler.sendMessage(message);
    }

    public static boolean useLoop(String[] arr, String value) {
        boolean flag = false;
        for (String s : arr) {
            if (s.equals(value)) {
                return true;
            }
        }
        return flag;
    }

    private ControlServiceImpl.ControlServiceImplBackValueListenser<SignUserEntity> signUserEntityControlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<SignUserEntity>() {

        @Override
        public void setControlServiceImplListenser(
                SignUserEntity backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            lin_group.removeAllViews();
            if (backValue != null) {
                processUserData(backValue.getNoticeList(), backValue.getSignList());
                if(totalNum > 0) {
                    ringChartView.setOutProgress(totalSignNum * 100f / totalNum);
                    ringChartView.setInnerProgress(totalNoticeNum * 100f / totalNum);
                    resource_prepare_tv_receive.setText(totalNoticeNum + "人");
                    resource_prepare_tv_sign.setText(totalSignNum + "人");
                }
                else {
                    ringChartView.setOutProgress(0);
                    ringChartView.setInnerProgress(0);
                    resource_prepare_tv_receive.setText("0人");
                    resource_prepare_tv_sign.setText("0人");
                }
                ringChartView.setDescription(totalNum + "人");
                for (NoticeSignUserEntity entity : noticeSignlist) {
                    View view = getLayoutInflater().inflate(R.layout.progress_percent, null);
                    TextView emergency_grop_name = (TextView) view.findViewById(R.id.gropname_tv);
                    CustomProgressBar emergency_notice_progressBar = (CustomProgressBar) view.findViewById(R.id.progress_percent_cpb_notice);
                    CustomProgressBar emergency_sign_progressBar = (CustomProgressBar) view.findViewById(R.id.progress_percent_cpb_sign);
                    emergency_grop_name.setText(entity.getNotice().getEmergTeam());
                    if(entity.getNotice() != null) {
                        String noticeDescription = entity.getNotice().getNoticeNum()
                                + "/"
                                + entity.getNotice().getNeedNoticeNum();
                        int noticeProgress = (int) (Double.parseDouble(entity.getNotice().getNoticeNum()) * 100 / Double.parseDouble(entity.getNotice().getNeedNoticeNum()));
                        double noticeWidthPercent = Double.parseDouble(entity.getNotice().getNeedNoticeNum()) / maxNum * 0.9;
                        emergency_notice_progressBar.setData(noticeProgress, noticeWidthPercent, noticeDescription);
                    }
                    else
                        emergency_notice_progressBar.setData(0, 0, "");
                    if(entity.getSign() != null) {
                        String signDescription = entity.getSign().getSignNum()
                                + "/"
                                + entity.getSign().getNeedSignNum();
                        int signProgress = (int) (Double.parseDouble(entity.getSign().getSignNum()) * 100 / Double.parseDouble(entity.getSign().getNeedSignNum()));
                        double signWidthPercent = Double.parseDouble(entity.getSign().getNeedSignNum()) / maxNum * 0.9;
                        emergency_sign_progressBar.setData(signProgress, signWidthPercent, signDescription);
                    }
                    else
                        emergency_sign_progressBar.setData(0, 0, "");
                    lin_group.addView(view);
                }
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    private void processUserData(List<SignUserEntity.Notice> noticeList, List<SignUserEntity.Sign> signList)
    {
        noticeSignlist.clear();
        totalNum = 0;
        totalSignNum = 0;
        totalNoticeNum = 0;
        maxNum = 0;
        for(SignUserEntity.Notice notice : noticeList)
        {
            NoticeSignUserEntity item = new NoticeSignUserEntity();
            item.setNotice(notice);
            totalNum = totalNum + Integer.parseInt(notice.getNeedNoticeNum());
            totalNoticeNum = totalNoticeNum + Integer.parseInt(notice.getNoticeNum());
            if(maxNum < Integer.parseInt(notice.getNeedNoticeNum()))
                maxNum = Integer.parseInt(notice.getNeedNoticeNum());
            for(SignUserEntity.Sign sign : signList)
            {
                if(notice.getEmergTeamId().equals(sign.getEmergTeamId()))
                {
                    item.setSign(sign);
                    totalSignNum = totalSignNum + Integer.parseInt(sign.getSignNum());
                    break;
                }
            }
            noticeSignlist.add(item);
        }
    }

    /**
     * 资源筹备数据初始化
     */
    private void initProgressData() {
        // TODO Auto-generated method stub
        Utils.getInstance().showProgressDialog(ControlActivity.this, "",
                Const.LOAD_MESSAGE);
        csevice.getNoticeAndSignList(planEntity.getId(), signUserEntityControlServiceImplBackValueListenser);

    }

    /***
     * 打开EventBus开关
     */
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 接收适配器的跳过此步骤的操作后刷新界面
     *
     * @param data
     */
    public void onEvent(mainEvent data) {
        if (data.getData().equals("jump")) {

            queryProcessTrack();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_actionbar_back:
                EventBus.getDefault().post(new mainEvent("r"));// 刷新列表界面
                ControlActivity.this.finish();
                break;
            case R.id.iv_actionbar_send_msg:
                Intent intent1 = new Intent(ControlActivity.this,
                        SendCollaborationActivity.class);
                intent1.putExtra("id", planEntity.getId());
                intent1.putExtra("name", planEntity.getPlanName());
                intent1.putExtra("precautionId",
                        planEntity.getPlanId());
                startActivity(intent1);
                break;
            case R.id.resource_prepare_ll_receive:// 应急通知接收情况详情
                Intent intent = new Intent(ControlActivity.this,
                        GroupSignAssignDetailActivity.class);
                intent.putExtra("tag", "1");
                intent.putExtra("id", planEntity.getId());
                startActivity(intent);
                break;
            case R.id.resource_prepare_ll_sign:// 应急小组签到情况详情
                Intent intent2 = new Intent(ControlActivity.this,
                        GroupSignAssignDetailActivity.class);
                intent2.putExtra("tag", "2");
                intent2.putExtra("id", planEntity.getId());
                startActivity(intent2);
                break;
            case R.id.rb_real_track:
                rb_real_track.setChecked(true);
                rb_process_monitor.setChecked(false);
                rb_resource_prepare.setChecked(false);
                sem_tag = 1;
                initData(sem_tag);
                break;
            case R.id.rb_process_monitor:
                rb_real_track.setChecked(false);
                rb_process_monitor.setChecked(true);
                rb_resource_prepare.setChecked(false);
                sem_tag = 2;
                initData(sem_tag);
                break;
            case R.id.rb_resource_prepare:
                rb_real_track.setChecked(false);
                rb_process_monitor.setChecked(false);
                rb_resource_prepare.setChecked(true);
                sem_tag = 3;
                initData(sem_tag);
                break;
            case R.id.real_time_track_rb_all:
                real_time_track_rb_all.setChecked(true);
                real_time_track_rb_executing.setChecked(false);
                real_time_track_rb_wait_execute.setChecked(false);
                real_time_track_rb_executed.setChecked(false);
                sem_status = 0;
                showTrackListByStatus(sem_status);
                break;
            case R.id.real_time_track_rb_executing:
                real_time_track_rb_all.setChecked(false);
                real_time_track_rb_executing.setChecked(true);
                real_time_track_rb_wait_execute.setChecked(false);
                real_time_track_rb_executed.setChecked(false);
                sem_status = 1;
                showTrackListByStatus(sem_status);
                break;
            case R.id.real_time_track_rb_wait_execute:
                real_time_track_rb_all.setChecked(false);
                real_time_track_rb_executing.setChecked(false);
                real_time_track_rb_wait_execute.setChecked(true);
                real_time_track_rb_executed.setChecked(false);
                sem_status = 2;
                showTrackListByStatus(sem_status);
                break;
            case R.id.real_time_track_rb_executed:
                real_time_track_rb_all.setChecked(false);
                real_time_track_rb_executing.setChecked(false);
                real_time_track_rb_wait_execute.setChecked(false);
                real_time_track_rb_executed.setChecked(true);
                sem_status = 3;
                showTrackListByStatus(sem_status);
                break;
        }
    }

    private ControlServiceImpl.ControlServiceImplBackValueListenser<FlowChartPlanEntity> flowChartPlanEntityControlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<FlowChartPlanEntity>() {

        @Override
        public void setControlServiceImplListenser(
                FlowChartPlanEntity backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub

            List<FlowChartPlanEntity.FlowChart> data = null;
            if (backValue != null) {
                data = (List<FlowChartPlanEntity.FlowChart>) backValue.getData();
                curDate = backValue.getCurDate();
                alist = data;
                if (backValue.getState().equals("2")) {
//                    stop.setText("启动");
//                    stop.setBackgroundResource(R.drawable.btbg_green);
                } else if (backValue.getState().equals("3")) {
//                    stop.setText("中止");
//                    stop.setBackgroundResource(R.drawable.btbg_red);
                } else if (backValue.getState().equals("6")) {
//                    stop.setText("启动");
//                    stop.setBackgroundResource(R.drawable.btbg_green);
                } else {
//                    stop.setVisibility(View.GONE);
                }
            } else if (Exceptionerror != null) {
                data = new ArrayList<FlowChartPlanEntity.FlowChart>();
                Toast.makeText(ControlActivity.this,
                        Const.NETWORKERROR,
                        Toast.LENGTH_SHORT).show();
            }
            if(data == null)
                return;
            flowCharts.clear();
            //增加子预案层级标记
            addIndex(data);
            //根据子预案层级关系重新排序
            reSort(data, "", 0);
            data.clear();
            data.addAll(flowCharts);
            allList = data;
            showTrackListByStatus(sem_status);
            Utils.getInstance().hideProgressDialog();
        }
    };

    // 从网络获取的总的list
    private List<FlowChartPlanEntity.FlowChart> allList = new ArrayList<FlowChartPlanEntity.FlowChart>();
    private int num = 20;// 每次显示20条
    // 服务器当前时间
    private String curDate = "";

    /**
     * 实时跟踪数据初始化
     */
    private void queryProcessTrack() {
        Utils.getInstance().showProgressDialog(ControlActivity.this, "", Const.LOAD_MESSAGE);
        csevice.queryProcessTrack(
                planEntity.getId(), flowChartPlanEntityControlServiceImplBackValueListenser);
    }

    private ControlServiceImpl.ControlServiceImplBackValueListenser<FlowChartPlanEntity> chartPlanEntityControlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<FlowChartPlanEntity>() {

        @Override
        public void setControlServiceImplListenser(
                FlowChartPlanEntity backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub

            List<FlowChartPlanEntity.FlowChart> data = null;
            if (backValue != null) {
                data = (ArrayList<FlowChartPlanEntity.FlowChart>) backValue.getData();
            } else if (Exceptionerror != null) {
                data = new ArrayList<FlowChartPlanEntity.FlowChart>();
                Toast.makeText(ControlActivity.this,
                        Const.NETWORKERROR,
                        Toast.LENGTH_SHORT).show();
            } else if (stRerror != null) {
                data = new ArrayList<FlowChartPlanEntity.FlowChart>();
                Toast.makeText(ControlActivity.this, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else {
                data = new ArrayList<FlowChartPlanEntity.FlowChart>();
                Toast.makeText(ControlActivity.this,
                        "流程节点错误，请联系管理员。", Toast.LENGTH_SHORT)
                        .show();
            }
            Message message = handler.obtainMessage();
            message.what = 2;
            message.obj = data;
            handler.sendMessage(message);

            // if (Utils.getInstance().progressDialog.isShowing()) {
            Utils.getInstance().hideProgressDialog();
            // }
        }
    };

    /**
     * 流程监控数据初始化
     */
    private void flowChartPlanData() {
        Utils.getInstance().showProgressDialog(ControlActivity.this, "", Const.LOAD_MESSAGE);
        csevice.flowChartPlan(planEntity.getId(), chartPlanEntityControlServiceImplBackValueListenser);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        list.clear();
    }

    @Override
    public void initNetData() {
        // TODO Auto-generated method stub
        if (!planEntity.getId().equals("")) {
            queryProcessTrack();
        }
    }

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        i = 1;
        queryProcessTrack();
    }

    void setResultSize(int resultSize, int i) {
        if (resultSize == 0 && i == 1) {
            rlistview.setVisibility(View.GONE);
            ll_no_data_page.setVisibility(View.VISIBLE);
        } else {
            ll_no_data_page.setVisibility(View.GONE);
            rlistview.setVisibility(View.VISIBLE);
        }
    }
}
