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
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ControlSevice;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
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
import com.dssm.esc.view.widget.MyFlowView;
import com.dssm.esc.view.widget.MyProgressBar;
import com.dssm.esc.view.widget.NSSetPointValueToSteps;
import com.dssm.esc.view.widget.SegmentControl;
import com.dssm.esc.view.widget.Title_Layout;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 指挥中心界面
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-15
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
public class ControlActivity extends BaseActivity implements OnClickListener,
        MainActivity.onInitNetListener, AutoListView.OnRefreshListener, AutoListView.OnLoadListener {
    /**
     * 标题 布局
     */
    @ViewInject(id = R.id.title)
    private Title_Layout title_ll;
    /**
     * 标题
     */
    @ViewInject(id = R.id.tv_actionbar_title)
    private TextView title;

    private TextView tv_editData;

    /**
     * 返回按钮
     */
    @ViewInject(id = R.id.iv_actionbar_back)
    private ImageView back;
    /**
     * 从上个页面带来的数据
     */
    PlanEntity planEntity;
    /**
     * 可点击转换界面的button
     */
    @ViewInject(id = R.id.segment_control_control)
    private SegmentControl msegment;
    /**
     * 点击button转换：1，实时跟踪2，流程监,3，资源筹
     */
    private int sem_tag;
    /**
     * 实时跟踪的 include布局
     */
    @ViewInject(id = R.id.realtime_tracking)
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
    /** 实时跟踪的列表当前页面 */
    // private int i = 1;
    /**
     * 资源筹备 include布局
     */
    @ViewInject(id = R.id.resource_preparation)
    private View resource_preparation;
    /**
     * 资源筹备的应急接收情况查看详情
     */
    private TextView look_details_receive;
    /**
     * 资源筹备的小组签到情况查看详情
     */
    private TextView look_details_sigin;

    private TextView no_sigin_number, sigin_number, emergency_grop_number;

    NSSetPointValueToSteps nsSetPointValueToSteps;
    LinearLayout lin_group, lin_group_sign;
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
                case AutoListView.LOAD:
                    rlistview.onLoadComplete();
                    list.addAll(result);
                    rlistview.setResultSize(result.size(), i);
                    radapter.notifyDataSetChanged();
                    break;
                case 2:
                    WindowManager wm = (WindowManager) ControlActivity.this
                            .getSystemService(Context.WINDOW_SERVICE);
                    nsSetPointValueToSteps = new NSSetPointValueToSteps();
                    try {
                        nsSetPointValueToSteps.exampleSteps(result);
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        View findViewById = findViewById(R.id.control);
        findViewById.setFitsSystemWindows(true);
        my_flow_view = (MyFlowView) findViewById(R.id.my_flow_view);
        tv_editData = (TextView) findViewById(R.id.tv_actionbar_editData);
        tv_editData.setText("刷新");
        tv_editData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                flowChartPlanData();
            }
        });
        planEntity = (PlanEntity) getIntent().getSerializableExtra("PlanEntity");
        sevice = Control.getinstance().getUserSevice();
        csevice = Control.getinstance().getControlSevice();
        service = MySharePreferencesService.getInstance(this);
        // 回显
        map = service.getPreferences();
        roleCode = map.get("roleCode");
        Log.i("roleCode", roleCode);
        initView();
        sevice.getUserPower(new UserSeviceImpl.UserSeviceImplListListenser() {

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
                                String status = "";
                                switch (Integer.parseInt(planEntity.getState())) {
                                    case 0:
                                        status = "待启动";
                                        stop.setVisibility(View.GONE);
                                        break;
                                    case 1:
                                        status = "已启动";
                                        // mhHolder.statetv.setTextColor(Color.RED);
                                        stop.setVisibility(View.GONE);
                                        break;
                                    case 2:
                                        status = "已授权 ";
                                        // mhHolder.statetv.setTextColor(Color.RED);
                                        stop.setVisibility(View.VISIBLE);
                                        stop.setText("启动");
                                        break;
                                    case 3:
                                        status = "执行中";
                                        // mhHolder.statetv.setTextColor(R.color.green_my);
                                        stop.setVisibility(View.VISIBLE);
                                        stop.setText("中止");
                                        break;
                                    case 4:
                                        status = "完成";
                                        // mhHolder.statetv.setTextColor(R.color.green_my);
                                        stop.setVisibility(View.GONE);
                                        break;
                                    case 5:
                                        status = "强行中止";
                                        // mhHolder.statetv.setTextColor(Color.RED);
                                        stop.setVisibility(View.GONE);
                                        break;
//                                    case 6:
//                                        status = "暂停中";
//                                        // mhHolder.statetv.setTextColor(Color.RED);
//                                        stop.setVisibility(View.VISIBLE);
//                                        stop.setBackgroundResource(R.drawable.btbg_green);
//                                        stop.setText("启动");
//                                        break;
                                }
                            }
                        } else {
                            stop.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        segmentControlListDate();
    }

    private void initView() {
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText(planEntity.getPlanName());
        stop = (Button) realtime_tracking.findViewById(R.id.stop);
        rlistview = (AutoListView) realtime_tracking
                .findViewById(R.id.realtime_track_listview);

        rlistview.setOnRefreshListener(this);
        rlistview.setOnLoadListener(this);

        sem_tag = 1;
        initData(sem_tag);
        stop.setOnClickListener(this);

        look_details_receive = (TextView) resource_preparation
                .findViewById(R.id.look_details_receive);
        look_details_sigin = (TextView) resource_preparation
                .findViewById(R.id.look_details_sigin);
        look_details_receive.setOnClickListener(this);
        look_details_sigin.setOnClickListener(this);
        lin_group = (LinearLayout) resource_preparation
                .findViewById(R.id.lin_grop);
        lin_group_sign = (LinearLayout) resource_preparation
                .findViewById(R.id.lin_grop_sigin);

        no_sigin_number = (TextView) resource_preparation
                .findViewById(R.id.no_sigin_number);
        sigin_number = (TextView) resource_preparation
                .findViewById(R.id.sigin_number);
        emergency_grop_number = (TextView) resource_preparation
                .findViewById(R.id.emergency_grop_number);

    }

    private void initData(int sem_tags) {
        if (sem_tags == 1) {
            realtime_tracking.setVisibility(View.VISIBLE);
            resource_preparation.setVisibility(View.GONE);
            my_flow_view.setVisibility(View.GONE);
            queryProcessTrack(0);
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

    /**
     * 资源筹备数据初始化
     *
     * @version 1.0
     * @createTime 2015-9-17,下午4:08:37
     * @updateTime 2015-9-17,下午4:08:37
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */

    private void initProgressData() {
        // TODO Auto-generated method stub
        Utils.getInstance().showProgressDialog(ControlActivity.this, "",
                Const.LOAD_MESSAGE);
        csevice.getNoticeAndSignList(planEntity.getId(),
                new ControlServiceImpl.ControlServiceImplBackValueListenser<SignUserEntity>() {

                    @Override
                    public void setControlServiceImplListenser(
                            SignUserEntity backValue, String stRerror,
                            String Exceptionerror) {
                        // TODO Auto-generated method stub
                        lin_group.removeAllViews();
                        lin_group_sign.removeAllViews();
                        if (backValue != null) {
                            no_sigin_number.setText(backValue.getTotalNoSignNum() + "人");
                            sigin_number.setText(backValue.getTotalSignNum() + "人");
                            emergency_grop_number.setText(backValue.getTotalNeedSignNum() + "人");
                            for (SignUserEntity.Notice notice : backValue.getNoticeList()) {
                                View view = getLayoutInflater().inflate(R.layout.progress_percent, null);
                                TextView emergency_grop_name = (TextView) view.findViewById(R.id.gropname_tv);
                                TextView emergency_grop_percent = (TextView) view.findViewById(R.id.percent_tv);
                                MyProgressBar emergency_grop_progressBar = (MyProgressBar) view.findViewById(R.id.progressBar);
                                emergency_grop_name.setText(notice.getEmergTeam());
                                emergency_grop_percent.setText(notice.getNoticeNum()
                                        + "/"
                                        + notice.getNeedNoticeNum());
                                emergency_grop_progressBar.setMax(Integer
                                        .parseInt(notice.getNeedNoticeNum()));
                                emergency_grop_progressBar.setProgress(Integer
                                        .parseInt(notice.getNoticeNum()));
                                lin_group.addView(view);
                            }
                            for (SignUserEntity.Sign sign : backValue.getSignList()) {
                                View view = getLayoutInflater().inflate(
                                        R.layout.progress_percent, null);
                                TextView emergency_grop_name2 = (TextView) view.findViewById(R.id.gropname_tv);
                                TextView emergency_grop_percent2 = (TextView) view.findViewById(R.id.percent_tv);
                                MyProgressBar emergency_grop_progressBar2 = (MyProgressBar) view.findViewById(R.id.progressBar);
                                emergency_grop_name2.setText(sign
                                        .getEmergTeam());
                                emergency_grop_percent2.setText(sign
                                        .getSignNum()
                                        + "/"
                                        + sign.getNeedSignNum());
                                emergency_grop_progressBar2.setMax(Integer
                                        .parseInt(sign.getNeedSignNum()));
                                emergency_grop_progressBar2.setProgress(Integer
                                        .parseInt(sign.getSignNum()));
                                lin_group_sign.addView(view);
                            }
                        }
                        // if (Utils.getInstance().progressDialog.isShowing()) {
                        Utils.getInstance().hideProgressDialog();
                        // }
                    }
                });

    }

    private void segmentControlListDate() {
        // TODO Auto-generated method stub
        msegment.setmOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                switch (index) {
                    case 0:// 1，实时跟踪
                        sem_tag = 1;
                        tv_editData.setVisibility(View.INVISIBLE);
                        break;
                    case 1:// 2，流程监
                        sem_tag = 2;
                        tv_editData.setVisibility(View.VISIBLE);
                        break;
                    case 2:// 3，资源筹
                        sem_tag = 3;
                        tv_editData.setVisibility(View.INVISIBLE);
                        break;
                }
                initData(sem_tag);
            }
        });
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

            queryProcessTrack(0);
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
                                                        .equals("null")) {
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
            case R.id.look_details_receive:// 应急通知接收情况详情
                Intent intent = new Intent(ControlActivity.this,
                        GroupSigninDetail.class);
                intent.putExtra("tag", "1");
                intent.putExtra("id", planEntity.getId());
                startActivity(intent);
                break;
            case R.id.look_details_sigin:// 应急小组签到情况详情
                Intent intent2 = new Intent(ControlActivity.this,
                        GroupSigninDetail.class);
                intent2.putExtra("tag", "2");
                intent2.putExtra("id", planEntity.getId());
                startActivity(intent2);
                break;

        }
    }

    /**
     * 预案中止
     */
    private void stopPlan(String stopcause) {
        // TODO Auto-generated method stub
        Utils.getInstance().showProgressDialog(ControlActivity.this, "",
                Const.SUBMIT_MESSAGE);
        // 此处要传终止终止意见，没有输入框
        csevice.stopPlan(planEntity, stopcause,
                new ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean>() {

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
                                    Const.NETWORKERROR + Exceptionerror,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // if (Utils.getInstance().progressDialog.isShowing()) {
                        Utils.getInstance().hideProgressDialog();
                        // }
                    }
                });
    }

    /**
     * 预案启动
     */
    private void planStart() {
        // TODO Auto-generated method stub
        Utils.getInstance().showProgressDialog(ControlActivity.this, "",
                Const.LOAD_MESSAGE);
        csevice.starPlan(planEntity.getId(),
                new ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean>() {

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
                                    Const.NETWORKERROR + Exceptionerror,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // if (Utils.getInstance().progressDialog.isShowing()) {
                        Utils.getInstance().hideProgressDialog();
                        // }
                    }
                });
    }

    // 从网络获取的总的list
    private List<FlowChartPlanEntity.FlowChart> allList = new ArrayList<FlowChartPlanEntity.FlowChart>();
    private int num = 20;// 每次显示20条
    // 服务器当前时间
    private String curDate = "";

    /**
     * 实时跟踪数据初始化
     */
    private void queryProcessTrack(final int what) {
        // if (Utils.getInstance().progressDialog==null) {
        //
        // Utils.getInstance().showProgressDialog(ControlActivity.this, "",
        // Const.LOAD_MESSAGE);
        // }
        if (what == 0) {// 刷新和第一次加载
            csevice.queryProcessTrack(
                    planEntity.getId(),
                    new ControlServiceImpl.ControlServiceImplBackValueListenser<FlowChartPlanEntity>() {

                        @Override
                        public void setControlServiceImplListenser(
                                FlowChartPlanEntity backValue, String stRerror,
                                String Exceptionerror) {
                            // TODO Auto-generated method stub

                            List<FlowChartPlanEntity.FlowChart> data = null;
                            if (backValue != null) {
                                data = (List<FlowChartPlanEntity.FlowChart>) backValue.getData();
                                Collections.sort(data, new Comparator<FlowChartPlanEntity.FlowChart>() {
                                    @Override
                                    public int compare(FlowChartPlanEntity.FlowChart o1, FlowChartPlanEntity.FlowChart o2) {
                                        if (o1.getEditOrderNum() == null || "".equals(o1.getEditOrderNum()))
                                            o1.setEditOrderNum("0");
                                        if (o2.getEditOrderNum() == null || "".equals(o2.getEditOrderNum()))
                                            o2.setEditOrderNum("0");
                                        if(Integer.parseInt(o1.getEditOrderNum()) > Integer.parseInt(o2.getEditOrderNum())) {
                                            return 1;
                                        }
                                        else if(Integer.parseInt(o1.getEditOrderNum()) < Integer.parseInt(o2.getEditOrderNum())) {
                                            return -1;
                                        }
                                        else {
                                            return 0;
                                        }
                                    }
                                });
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
                                        Const.NETWORKERROR + Exceptionerror,
                                        Toast.LENGTH_SHORT).show();
                            }
                            Message message = handler.obtainMessage();
                            // message.obj = data;
                            // handler.sendMessage(message);
                            message.what = 0;
                            if(data == null)
                                return;
                            if (data.size() > 20) {// 如果超过20条，则分页
                                List<FlowChartPlanEntity.FlowChart> subList = data.subList(0, 20);
                                message.obj = subList;
                            } else {
                                message.obj = data;
                            }
                            handler.sendMessage(message);
                            allList = data;
                            // if
                            // (Utils.getInstance().progressDialog.isShowing())
                            // {
                            // Utils.getInstance().hideProgressDialog();
                            // }

                        }
                    });
        } else if (what == 1) {// 加载更多
            // 本地做分页，加载20条以后的数据，默认每20条分一页
            Log.i("list测试长度", allList.size() + "");
            Log.i("num", num + "");
            List<FlowChartPlanEntity.FlowChart> datalist2;
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
    }

    /**
     * 流程监控数据初始化
     */
    private void flowChartPlanData() {
        Utils.getInstance().showProgressDialog(ControlActivity.this, "", Const.LOAD_MESSAGE);
        csevice.flowChartPlan(planEntity.getId(),
                new ControlServiceImpl.ControlServiceImplBackValueListenser<FlowChartPlanEntity>() {

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
                                    Const.NETWORKERROR + Exceptionerror,
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
                });

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

            queryProcessTrack(0);
        }
    }

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub
        queryProcessTrack(1);
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        i = 1;
        queryProcessTrack(0);
    }
}
