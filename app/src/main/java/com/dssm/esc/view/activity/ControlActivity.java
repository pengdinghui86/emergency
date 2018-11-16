package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
import com.dssm.esc.model.entity.control.NoticeSignUserEntity;
import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.entity.control.SignUserEntity;
import com.dssm.esc.model.entity.user.ButtonEntity;
import com.dssm.esc.model.entity.user.UserPowerEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.ToastUtil;
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
    /**
     * 实时跟踪的 include布局
     */
    @ViewInject(R.id.realtime_tracking)
    private View realtime_tracking;
    /**
     * 实时跟踪的 终止按钮
     */
    private Button stop;
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
    /**
     * 资源筹备 include布局
     */
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
                    rlistview.setResultSize(result.size(), i);
                    radapter = new RealTimeTrackingAdapter(ControlActivity.this, planEntity.getState(),
                            list, sevice, roleCode, csevice, curDate, service);
                    rlistview.setAdapter(radapter);
                    radapter.notifyDataSetChanged();

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

    private UserSeviceImpl.UserSeviceImplListListenser listListener = new UserSeviceImpl.UserSeviceImplListListenser() {

        @Override
        public void setUserSeviceImplListListenser(Object object,
                String stRerror, String Exceptionerror) {
            // TODO Auto-generated method stub
            if (object != null) {
                UserPowerEntity entity = (UserPowerEntity) object;
                List<ButtonEntity> btns = entity.getBtns();
                // 指挥与展示启动终止按钮：BTN_QDZZ
                for (int i = 0; i < btns.size(); i++) {
                    ButtonEntity buttonEntity = btns.get(i);
                    if (buttonEntity.getBtnMark().equals("BTN_QDZZ")) {
                        if (!planEntity.getState().equals("null")
                                && !planEntity.getState().equals("")) {
                            switch (Integer.parseInt(planEntity.getState())) {
                                case 0:
                                    stop.setVisibility(View.GONE);
                                    break;
                                case 1:
                                    stop.setVisibility(View.GONE);
                                    break;
                                case 2:
                                    stop.setVisibility(View.VISIBLE);
                                    stop.setText("启动");
                                    break;
                                case 3:
                                    stop.setVisibility(View.VISIBLE);
                                    stop.setText("中止");
                                    break;
                                case 4:
                                    stop.setVisibility(View.GONE);
                                    break;
                                case 5:
                                    stop.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    } else {
                        stop.setVisibility(View.GONE);
                    }
                }
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_control);
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
        sevice.getUserPower(listListener);
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
        rb_real_track.setOnClickListener(this);
        rb_process_monitor.setOnClickListener(this);
        rb_resource_prepare.setOnClickListener(this);
        stop = (Button) realtime_tracking.findViewById(R.id.stop);
        rlistview = (AutoListView) realtime_tracking
                .findViewById(R.id.realtime_track_listview);

        rlistview.setOnRefreshListener(this);
        rlistview.setLoadEnable(false);

        sem_tag = 1;
        initData(sem_tag);
        stop.setOnClickListener(this);

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
                        double noticeWidthPercent = Double.parseDouble(entity.getNotice().getNeedNoticeNum()) / maxNum;
                        emergency_notice_progressBar.setData(noticeProgress, noticeWidthPercent, noticeDescription);
                    }
                    else
                        emergency_notice_progressBar.setData(0, 0, "");
                    if(entity.getSign() != null) {
                        String signDescription = entity.getSign().getSignNum()
                                + "/"
                                + entity.getSign().getNeedSignNum();
                        int signProgress = (int) (Double.parseDouble(entity.getSign().getSignNum()) * 100 / Double.parseDouble(entity.getSign().getNeedSignNum()));
                        double signWidthPercent = Double.parseDouble(entity.getSign().getNeedSignNum()) / maxNum;
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

    private String getCode = null;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.stop:// 终止/启动
                Dialog dialog = null;
                if (stop.getText().toString().equals("中止")) {
                    dialog = new AlertDialog.Builder(ControlActivity.this)
                            .setTitle("您确定要中止？")

                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent2 = new Intent(
                                                    ControlActivity.this,
                                                    SubmitInfomationActivity.class);
                                            intent2.putExtra("tag", "5");// 中止原因
                                            startActivityForResult(intent2, 0);

                                        }

                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框
                } else if (stop.getText().toString().equals("启动")) {
                    dialog = new AlertDialog.Builder(ControlActivity.this)
                            .setTitle("您确定要启动？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @SuppressWarnings("deprecation")
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            for (FlowChartPlanEntity.FlowChart entity : alist) {
                                                if (entity.getExecutePeople()
                                                        .equals("null") && !entity.getNodeStepType().equals("CallActivity")) {
                                                    Toast.makeText(
                                                            ControlActivity.this,
                                                            entity.getName()
                                                                    + "没有执行人",
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                    return;
                                                }
                                            }
                                            View view = LayoutInflater.from(
                                                    ControlActivity.this).inflate(
                                                    R.layout.editcode, null);
                                            final EditText et = (EditText) view
                                                    .findViewById(R.id.vc_code);
                                            getCode = Utils.getInstance().code();
                                            new AlertDialog.Builder(
                                                    ControlActivity.this)
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
                                                                                ControlActivity.this,
                                                                                "验证码错误",
                                                                                Toast.LENGTH_SHORT)
                                                                                .show();
                                                                    } else {

                                                                        planStart();
                                                                    }
                                                                }
                                                            })
                                                    .setNegativeButton("取消", null)
                                                    .show();

                                        }

                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框
                }

                break;
            case R.id.iv_actionbar_back:
                EventBus.getDefault().post(new mainEvent("r"));// 刷新列表界面
                ControlActivity.this.finish();
                break;
            case R.id.resource_prepare_ll_receive:// 应急通知接收情况详情
                Intent intent = new Intent(ControlActivity.this,
                        GroupSigninDetail.class);
                intent.putExtra("tag", "1");
                intent.putExtra("id", planEntity.getId());
                startActivity(intent);
                break;
            case R.id.resource_prepare_ll_sign:// 应急小组签到情况详情
                Intent intent2 = new Intent(ControlActivity.this,
                        GroupSigninDetail.class);
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
        }
    }

    private ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean> controlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean>() {

        @Override
        public void setControlServiceImplListenser(
                Boolean backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated
            // method stub
            if (backValue) {
                ToastUtil.showToast(ControlActivity.this, "中止成功");
                stop.setBackgroundResource(R.drawable.btbg_blue);
                stop.setText("启动");
                stop.setVisibility(View.GONE);
                EventBus.getDefault().post(new mainEvent("r"));// 刷新列表界面
                // 终止操作
                ControlActivity.this.finish();

            } else if (stRerror != null) {
                Toast.makeText(ControlActivity.this, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else if (Exceptionerror != null) {
                Toast.makeText(ControlActivity.this,
                        Const.NETWORKERROR,
                        Toast.LENGTH_SHORT).show();
            }
            // if (Utils.getInstance().progressDialog.isShowing()) {
            Utils.getInstance().hideProgressDialog();
            // }
        }
    };

    /**
     * 预案中止
     */
    private void stopPlan(String stopcause) {
        // TODO Auto-generated method stub
        Utils.getInstance().showProgressDialog(ControlActivity.this, "",
                Const.SUBMIT_MESSAGE);
        // 此处要传终止终止意见，没有输入框
        csevice.stopPlan(planEntity, stopcause, controlServiceImplBackValueListenser);
    }

    private ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean> controlServiceImplBackValueStartListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean>() {

        @Override
        public void setControlServiceImplListenser(
                Boolean backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated
            // method stub
            if (backValue) {
                ToastUtil.showToast(ControlActivity.this, "启动成功");
                stop.setBackgroundResource(R.drawable.btbg_red);
                stop.setText("中止");
                EventBus.getDefault().post(new mainEvent("r"));// 刷新列表界面
                // 终止操作
                ControlActivity.this.finish();
                // queryProcessTrack();
            } else if (stRerror != null) {
                Toast.makeText(ControlActivity.this, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else if (Exceptionerror != null) {
                Toast.makeText(ControlActivity.this,
                        Const.NETWORKERROR,
                        Toast.LENGTH_SHORT).show();
            }
            // if (Utils.getInstance().progressDialog.isShowing()) {
            Utils.getInstance().hideProgressDialog();
            // }
        }
    };

    /**
     * 预案启动
     */
    private void planStart() {
        // TODO Auto-generated method stub
        Utils.getInstance().showProgressDialog(ControlActivity.this, "",
                Const.LOAD_MESSAGE);
        csevice.starPlan(planEntity.getId(), controlServiceImplBackValueStartListenser);
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
                    stop.setText("启动");
                    stop.setBackgroundResource(R.drawable.btbg_green);
                } else if (backValue.getState().equals("3")) {
                    stop.setText("中止");
                    stop.setBackgroundResource(R.drawable.btbg_red);
                } else if (backValue.getState().equals("6")) {
                    stop.setText("启动");
                    stop.setBackgroundResource(R.drawable.btbg_green);
                } else {
                    stop.setVisibility(View.GONE);
                }
            } else if (Exceptionerror != null) {
                data = new ArrayList<FlowChartPlanEntity.FlowChart>();
                Toast.makeText(ControlActivity.this,
                        Const.NETWORKERROR,
                        Toast.LENGTH_SHORT).show();
            }
            Message message = handler.obtainMessage();
            // message.obj = data;
            // handler.sendMessage(message);
            message.what = 0;
            if(data == null)
                return;
            flowCharts.clear();
            //增加子预案层级标记
            addIndex(data);
            //根据子预案层级关系重新排序
            reSort(data, "", 0);
            data.clear();
            data.addAll(flowCharts);
            message.obj = data;
            handler.sendMessage(message);
            allList = data;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == RESULT_OK) {

            switch (requestCode) {
                case 0:
                    String stopcause = "";
                    stopcause = data.getStringExtra("info");
                    if (!stopcause.equals("") && stopcause.length() > 0) {
                        stopPlan(stopcause);
                    } else {
                        ToastUtil.showToast(ControlActivity.this, "中止原因为必填项");
                    }
                    break;
            }
        } else {
            Log.i("=====", "data为null");
        }
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
}
