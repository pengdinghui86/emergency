package com.dssm.esc.view.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.model.analytical.ControlSevice;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
import com.dssm.esc.model.entity.user.UserObjEntity;
import com.dssm.esc.model.entity.user.UserPersonIdEntity;
import com.dssm.esc.status.RealTimeTrackingStatus;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.ControlActivity;
import com.dssm.esc.view.activity.IntroductionActivity;
import com.dssm.esc.view.activity.SubmitInfomationActivity;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 实时跟踪适配器
 */
public class RealTimeTrackingAdapter extends BaseAdapter {

    private List<FlowChartPlanEntity.FlowChart> arraylist;
    private Context context;
    private UserSevice sevice;
    private String roleCode;
    private ControlSevice csevice;
    private String curDate;
    private MySharePreferencesService service;
    private String parentState;

    public RealTimeTrackingAdapter(Context context, String parentState,
                                   List<FlowChartPlanEntity.FlowChart> list,
                                   UserSevice sevice, String roleCode, ControlSevice csevice, String curDate, MySharePreferencesService service) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.arraylist = list;
        this.sevice = sevice;
        this.roleCode = roleCode;
        this.csevice = csevice;
        this.curDate = curDate;
        this.service = service;
        this.parentState = parentState;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arraylist.size();
    }

    @Override
    public FlowChartPlanEntity.FlowChart getItem(int position) {
        // TODO Auto-generated method stub
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub

        final FlowChartPlanEntity.FlowChart entity = getItem(position);
        ViewHolder mhHolder = null;
        if (convertView == null) {
            mhHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_listview_real_time_track, null);
            mhHolder.executePeople = (TextView) convertView.findViewById(R.id.executePeople);
            mhHolder.executePeopleTitle = (TextView) convertView.findViewById(R.id.executePeopleTitle);
            mhHolder.name_step = (TextView) convertView.findViewById(R.id.name_step);
            mhHolder.status = (TextView) convertView.findViewById(R.id.status);
            mhHolder.content = (TextView) convertView.findViewById(R.id.content);

            mhHolder.overtime = (TextView) convertView.findViewById(R.id.overtime);
            mhHolder.predict_time = (TextView) convertView.findViewById(R.id.predict_time);
            mhHolder.predict_time_title = (ImageView) convertView.findViewById(R.id.predict_time_title);
            mhHolder.over_time_title = (ImageView) convertView.findViewById(R.id.over_time_title);
//            mhHolder.ivdot = (ImageView) convertView.findViewById(R.id.dot);
            mhHolder.postiontv = (TextView) convertView.findViewById(R.id.postiontv);
            mhHolder.jumptv = (TextView) convertView.findViewById(R.id.jumptv);

            /**
             * 新增
             * 2017/10/23
             */
            mhHolder.ll_executePeople = (LinearLayout) convertView.findViewById(R.id.ll_executePeople);
            mhHolder.ll_time = (LinearLayout) convertView.findViewById(R.id.ll_time);

            /**
             * 新增
             * 2017/10/12
             */
            mhHolder.tv_start = (TextView) convertView.findViewById(R.id.tv_start);
            mhHolder.tv_pause = (TextView) convertView.findViewById(R.id.tv_pause);
            mhHolder.tv_cancel_jump = (TextView) convertView.findViewById(R.id.tv_cancel_jump);

            convertView.setTag(mhHolder);
        } else {
            mhHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 新增
         * 2018/4/25
         */
        //新增节点
        if (!entity.getOrderNum().equals("null") && !entity.getOrderNum().equals("")) {
            mhHolder.postiontv.setText(entity.getParentOrderNum() + entity.getOrderNum() + ".");
        }
        else {
            if (!entity.getEditOrderNum().equals("null") && !entity.getEditOrderNum().equals("")) {
                mhHolder.postiontv.setText(entity.getParentOrderNum() + entity.getEditOrderNum() + ".");
            }
            else
                mhHolder.postiontv.setText("");
        }
        if (!entity.getExecutePeople().equals("null")
                && !entity.getExecutePeople().equals("")) {
            mhHolder.executePeople.setText(entity.getExecutePeople());
            final String executePeopleId = entity.getExecutePeopleId();
            if (!executePeopleId.equals("null") && !executePeopleId.equals("")) {
                peopleClick(mhHolder, executePeopleId);
            }
        }
        else {
            String executePeopleName = "";
            if(!"".equals(entity.getExecutorA()))
                executePeopleName += ",A:" + entity.getExecutorA();
            if(!"".equals(entity.getExecutorB()))
                executePeopleName += ",B:" + entity.getExecutorB();
            if(!"".equals(entity.getExecutorC()))
                executePeopleName += ",C:" + entity.getExecutorC();
            if(executePeopleName.length() > 0)
                executePeopleName = executePeopleName.substring(1, executePeopleName.length());
            mhHolder.executePeople.setText(executePeopleName);
        }
        if (!entity.getName().equals("null")) {
            mhHolder.name_step.setText(entity.getName());
        }
        String overSeconds = entity.getActualAfterDuration();
        String overTime = Utils.getInstance().convert2TimeStyle(overSeconds);
        mhHolder.overtime.setText(overTime);
        mhHolder.v_top = (View) convertView.findViewById(R.id.item_list_view_real_time_track_v_top);
        mhHolder.v_bottom = (View) convertView.findViewById(R.id.item_list_view_real_time_track_v_bottom);
        mhHolder.iv = (ImageView) convertView.findViewById(R.id.item_list_view_real_time_track_iv);
        mhHolder.v_split = (View) convertView.findViewById(R.id.item_list_view_real_time_track_v_split);
        mhHolder.iv.setBackgroundResource(R.drawable.circle_unstart_bg);
        mhHolder.iv.setImageResource(R.drawable.unexecuted);
        if(entity.getParentProcessStepId() == null || "".equals(entity.getParentProcessStepId()))
            mhHolder.iv.setVisibility(View.VISIBLE);
        else
            mhHolder.iv.setVisibility(View.GONE);
        if(position == 0)
        {
            mhHolder.v_top.setVisibility(View.INVISIBLE);
            mhHolder.v_bottom.setVisibility(View.VISIBLE);
            if(arraylist.size() > position + 1)
            {
                if(arraylist.get(position + 1).getParentProcessStepId() == null || "".equals(arraylist.get(position + 1).getParentProcessStepId()))
                    mhHolder.v_split.setVisibility(View.VISIBLE);
                else
                    mhHolder.v_split.setVisibility(View.GONE);
            }
        }
        else if(position == arraylist.size() - 1)
        {
            mhHolder.v_top.setVisibility(View.VISIBLE);
            mhHolder.v_bottom.setVisibility(View.INVISIBLE);
            mhHolder.v_split.setVisibility(View.GONE);
        }
        else
        {
            mhHolder.v_top.setVisibility(View.VISIBLE);
            mhHolder.v_bottom.setVisibility(View.VISIBLE);
            if(arraylist.size() > position + 1)
            {
                if(arraylist.get(position + 1).getParentProcessStepId() == null || "".equals(arraylist.get(position + 1).getParentProcessStepId()))
                    mhHolder.v_split.setVisibility(View.VISIBLE);
                else
                    mhHolder.v_split.setVisibility(View.GONE);
            }
        }

        String status = "";
        // 全部完成：1，部分完成：2，跳过：3，正在执行：4，可执行：5,准备执行：6，未执行：7，未选择执行：27
        if (!entity.getStatus().equals("null") && !entity.getStatus().equals("")) {

            /**
             * 新增
             * 2017/10/12
             */
            if (10 < Integer.parseInt(entity.getStatus())
                    && Integer.parseInt(entity.getStatus()) < 20) {
                status = "暂停";
                setDefaultBackground(mhHolder, R.color.red);
                mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
                mhHolder.status.setText(status);

                setItemColor(entity, mhHolder);
                mhHolder.iv.setBackgroundResource(R.drawable.circle_over_time_bg);
                mhHolder.iv.setImageResource(R.drawable.over_time);
            }
            switch (Integer.parseInt(entity.getStatus())) {
                case 1:
                    status = "已执行";
                    //新增,2018/7/26
                    //待启动或已启动或已授权的情况下，更改状态显示为流程未启动
                    if(parentState.equals("0") || parentState.equals("2") || parentState.equals("1"))
                    {
                        status = "流程未启动";
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
                    }
                    else {
                        setDefaultBackground(mhHolder, R.color.green_b);
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_blue));
                        mhHolder.iv.setBackgroundResource(R.drawable.circle_complete_bg);
                        mhHolder.iv.setImageResource(R.drawable.event_process_plan_authorize);
                    }
                    mhHolder.status.setText(status);
                    setItemColor(entity, mhHolder);
                    break;
                case 2:
                    status = "已执行";
                    //新增,2018/7/26
                    //待启动或已启动或已授权的情况下，更改状态显示为流程未启动
                    if(parentState.equals("0") || parentState.equals("2") || parentState.equals("1")) {
                        status = "流程未启动";
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
                    }
                    else {
                        setDefaultBackground(mhHolder, R.color.green_b);
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_blue));
                        mhHolder.iv.setBackgroundResource(R.drawable.circle_complete_bg);
                        mhHolder.iv.setImageResource(R.drawable.event_process_plan_authorize);
                    }
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                case 3:
                    status = "已执行";
                    //新增,2018/7/26
                    //待启动或已启动或已授权的情况下，更改状态显示为流程未启动
                    if(parentState.equals("0") || parentState.equals("2") || parentState.equals("1")) {
                        status = "流程未启动";
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
                    }
                    else {
                        setDefaultBackground(mhHolder, R.color.green_b);
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_blue));
                        mhHolder.iv.setBackgroundResource(R.drawable.circle_complete_bg);
                        mhHolder.iv.setImageResource(R.drawable.event_process_plan_authorize);

                    }
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                case 4:
                    status = "执行中";
                    setDefaultBackground(mhHolder, R.color.yellow_dot);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_yellow));
                    mhHolder.status.setText(status);
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_execute_bg);
                    mhHolder.iv.setImageResource(R.drawable.event_process_plan_execute);
                    setItemColor(entity, mhHolder);
                    break;
                case 5:
                    status = "可执行";
                    //新增,2018/7/26
                    //待启动或已启动或已授权的情况下，更改状态显示为流程未启动
                    if(parentState.equals("0") || parentState.equals("2") || parentState.equals("1")) {
                        status = "流程未启动";
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
                    }
                    else {
                        setDefaultBackground(mhHolder, R.color.blue_dot);
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_purple));
                    }
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                case 6:
                    status = "准备执行";
                    setDefaultBackground(mhHolder, R.color.green_a);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_green));
                    mhHolder.status.setText(status);
                    setItemColor(entity, mhHolder);
                    break;
                case 7:
                    status = "未执行";
                    //新增,2018/4/24
                    //待启动或已启动或已授权的情况下，更改状态显示为流程未启动
                    if(parentState.equals("0") || parentState.equals("2") || parentState.equals("1")) {
                        status = "流程未启动";
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
                    }
                    else {
                        setDefaultBackground(mhHolder, R.color.red);
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
                    }
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                /**
                 * 新增
                 * 2017/10/12
                 */
                case 8:
                    status = "自动执行中";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_yellow));
                    mhHolder.status.setText(status);
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_execute_bg);
                    mhHolder.iv.setImageResource(R.drawable.event_process_plan_execute);
                    setItemColor(entity, mhHolder);
                    break;

                case 9:
                    status = "已执行";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_blue));
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_complete_bg);
                    mhHolder.iv.setImageResource(R.drawable.event_process_plan_authorize);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 10:
                    status = "执行失败";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_red));
                    mhHolder.status.setText(status);
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_over_time_bg);
                    mhHolder.iv.setImageResource(R.drawable.over_time);
                    setItemColor(entity, mhHolder);
                    break;

                case 20:
                    status = "接收超时";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_red));
                    mhHolder.status.setText(status);
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_over_time_bg);
                    mhHolder.iv.setImageResource(R.drawable.over_time);
                    setItemColor(entity, mhHolder);
                    break;

                case 21:
                    status = "执行异常";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_red));
                    mhHolder.status.setText(status);
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_over_time_bg);
                    mhHolder.iv.setImageResource(R.drawable.over_time);
                    setItemColor(entity, mhHolder);
                    break;

                case 22:
                    status = "异常解除";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_red));
                    mhHolder.status.setText(status);
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_over_time_bg);
                    mhHolder.iv.setImageResource(R.drawable.over_time);
                    setItemColor(entity, mhHolder);
                    break;

                case 23:
                    status = "跳过";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_blue));
                    mhHolder.status.setText(status);
                    setItemColor(entity, mhHolder);
                    break;

                case 24:
                    status = "跳过";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_blue));
                    mhHolder.status.setText(status);
                    setItemColor(entity, mhHolder);
                    break;

                case 25:
                    status = "执行超时";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_red));
                    mhHolder.status.setText(status);
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_over_time_bg);
                    mhHolder.iv.setImageResource(R.drawable.over_time);
                    setItemColor(entity, mhHolder);
                    break;

                /**
                 * 添加执行完成状态
                 * 2017.10.18
                 */
                case 26:
                    status = "已执行";
                    //新增,2018/7/26
                    //待启动或已启动或已授权的情况下，更改状态显示为流程未启动
                    if(parentState.equals("0") || parentState.equals("2") || parentState.equals("1")) {
                        status = "流程未启动";
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
                    }
                    else {
                        setDefaultBackground(mhHolder, R.color.red);
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_red));
                    }
                    mhHolder.status.setText(status);
                    mhHolder.iv.setBackgroundResource(R.drawable.circle_complete_bg);
                    mhHolder.iv.setImageResource(R.drawable.event_process_plan_authorize);
                    setItemColor(entity, mhHolder);
                    break;
                /**
                 * 新增
                 * 2018/6/28
                 */
                case 27:
                    status = "未选择执行";
                    //新增,2018/7/26
                    //待启动或已启动或已授权的情况下，更改状态显示为流程未启动
                    if(parentState.equals("0") || parentState.equals("2") || parentState.equals("1"))
                        status = "流程未启动";
                    else {
                        setDefaultBackground(mhHolder, R.color.green_b);
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.green_b));
                    }
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                default:
                    if(status.equals("")) {
                        setDefaultBackground(mhHolder, R.color.red);
                        mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_red));
                        mhHolder.status.setText("状态未知");
                        setItemColor(entity, mhHolder);
                    }
                    break;
            }
        }

        /**
         * 新增
         * 2017/10/12
         */
        mhHolder.jumptv.setVisibility(View.GONE);
        mhHolder.tv_start.setVisibility(View.GONE);
        mhHolder.tv_pause.setVisibility(View.GONE);
        mhHolder.tv_cancel_jump.setVisibility(View.GONE);

        if ((roleCode.equals("R001") || roleCode.equals("R002")
                || roleCode.equals("R003")) && entity.getStatus().equals("5")
                && parentState.equals("3") && !entity.getNodeStepType().equals("ExclusiveGateway")) {
            mhHolder.tv_pause.setVisibility(View.VISIBLE);
            mhHolder.tv_pause.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Dialog dialog = null;
                    dialog = new AlertDialog.Builder(parent.getContext())
                            .setTitle("您确定要暂停？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //暂停传0，开启传1
                                            planPause(entity.getPlanPerformId(), entity.getPlanInfoId(), "0");
                                        }
                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框
                }
            });
        }

        if ((roleCode.equals("R001") || roleCode.equals("R002")
                || roleCode.equals("R003")) && entity.getStatus().equals("6")
                && parentState.equals("3") && !entity.getNodeStepType().equals("ExclusiveGateway")) {
            mhHolder.tv_pause.setVisibility(View.VISIBLE);
            mhHolder.tv_pause.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Dialog dialog = null;
                    dialog = new AlertDialog.Builder(parent.getContext())
                            .setTitle("您确定要暂停？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //暂停传0，开启传1
                                            planPause(entity.getPlanPerformId(), entity.getPlanInfoId(), "0");
                                        }
                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框
                }
            });
        }

        if ((roleCode.equals("R001") || roleCode.equals("R002"))
                && entity.getStatus().equals(RealTimeTrackingStatus.BEFORE)
                && parentState.equals("3") && !entity.getNodeStepType().equals("ExclusiveGateway")) {
            mhHolder.jumptv.setVisibility(View.VISIBLE);
            mhHolder.jumptv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Dialog dialog = null;
                    dialog = new AlertDialog.Builder(parent.getContext())
                            .setTitle("您确定要跳过？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //跳过传0，取消跳过传1
                                            planjump2(entity.getPlanPerformId(), entity.getPlanInfoId(), "0");
                                        }
                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框
                }
            });
            if(entity.getStatus().equals(RealTimeTrackingStatus.BEFORE)) {
                mhHolder.tv_pause.setVisibility(View.VISIBLE);
                mhHolder.tv_pause.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Dialog dialog = null;
                        dialog = new AlertDialog.Builder(parent.getContext())
                                .setTitle("您确定要暂停？")
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //暂停传0，开启传1
                                                planPause(entity.getPlanPerformId(), entity.getPlanInfoId(), "0");
                                            }
                                        })

                                .setNegativeButton("取消",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).create(); // 创建对话框
                        dialog.show(); // 显示对话框
                    }
                });
            }
        }
        else
            mhHolder.jumptv.setVisibility(View.GONE);

        if ((roleCode.equals("R001") || roleCode.equals("R002"))
                && parentState.equals("3")
                && entity.getStatus().equals("23")) {
            mhHolder.tv_cancel_jump.setVisibility(View.VISIBLE);
            mhHolder.tv_cancel_jump.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Dialog dialog = null;
                    dialog = new AlertDialog.Builder(parent.getContext())
                            .setTitle("您确定要取消跳过？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //跳过传0，取消跳过传1
                                            planjump2(entity.getPlanPerformId(), entity.getPlanInfoId(), "1");
                                        }
                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框
                }
            });
        }

        if ((roleCode.equals("R001") || roleCode.equals("R002")
                || roleCode.equals("R003")) && (entity.getStatus().equals(RealTimeTrackingStatus.EXECUTING)
                || entity.getStatus().equals(RealTimeTrackingStatus.EXCEPTION_OPTION_TIME_OUT))
                && parentState.equals("3") && !entity.getNodeStepType().equals("ExclusiveGateway")) {
            mhHolder.jumptv.setVisibility(View.VISIBLE);
            mhHolder.jumptv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Dialog dialog = null;
                    dialog = new AlertDialog.Builder(parent.getContext())
                            .setTitle("您确定要跳过？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            planjump(entity.getPlanPerformId(), entity.getPlanInfoId());
                                        }
                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框
                }
            });
        }

        if ((roleCode.equals("R001") || roleCode.equals("R002")
                || roleCode.equals("R003"))
                && parentState.equals("3")
                && 10 < Integer.parseInt(entity.getStatus())
                && Integer.parseInt(entity.getStatus()) < 20) {
            mhHolder.tv_start.setVisibility(View.VISIBLE);
            mhHolder.tv_start.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Dialog dialog = null;
                    dialog = new AlertDialog.Builder(parent.getContext())
                            .setTitle("您确定要开启？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //暂停传0，开启传1
                                            planPause(entity.getPlanPerformId(), entity.getPlanInfoId(), "1");
                                        }
                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框
                }
            });
        }

        if (!entity.getMessage().equals("null")) {
            mhHolder.content.setText(entity.getMessage());
        } else {
            mhHolder.content.setText("");
        }
        String duration;
        if (entity.getDuration().equals("null") || entity.getDuration().equals(""))
            duration = "0";
        else
            duration = entity.getDuration();
        long result = Utils.getInstance().compareTime(overSeconds, duration);
        if (result <= 0) {
            mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.color_state_green));
            mhHolder.over_time_title.setImageResource(R.drawable.used_time_normal);
        } else {
            mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.color_state_red));
            mhHolder.over_time_title.setImageResource(R.drawable.used_time_over);
        }
        if (!entity.getDuration().equals("null")) {
            mhHolder.predict_time.setText(entity.getDuration() + "秒");
        } else {
            mhHolder.predict_time.setText("");
        }

        /**
         * 子预案节点不需要执行人，预计用时
         * 2018.7.23
         */
        if(entity.getNodeStepType().equals("CallActivity")) {
            mhHolder.ll_executePeople.setVisibility(View.GONE);
            mhHolder.predict_time_title.setVisibility(View.GONE);
            mhHolder.predict_time.setVisibility(View.GONE);
        }
//        /**
//         * 添加执行完成状态
//         * 2017.10.23
//         */
//        else if (26 == Integer.parseInt(entity.getStatus())&& !(null == entity.getType()) ? false : entity.getType().equals("drillNew")) {
//            mhHolder.ll_executePeople.setVisibility(View.GONE);
//            mhHolder.ll_time.setVisibility(View.GONE);
//        }
        /**
         * 2018.4.24 新增节点不显示执行人和已用时信息
         */
        else if((null == entity.getType()) ? false : entity.getType().equals("drillNew")) {
            mhHolder.ll_executePeople.setVisibility(View.GONE);
            mhHolder.ll_time.setVisibility(View.GONE);
        }
        else {
            mhHolder.ll_executePeople.setVisibility(View.VISIBLE);
            mhHolder.ll_time.setVisibility(View.VISIBLE);
            mhHolder.predict_time_title.setVisibility(View.VISIBLE);
            mhHolder.predict_time.setVisibility(View.VISIBLE);
        }

        //根据子预案层级设置缩进幅度
//        convertView.setPadding(entity.getIndex() * 20, 0, 0, 0);
        return convertView;
    }

    private void setDefaultBackground(ViewHolder mhHolder, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL); // 画框
        drawable.setColor(color);
//        mhHolder.ivdot.setBackgroundDrawable(drawable);
    }

    private void setItemColor(FlowChartPlanEntity.FlowChart entity, ViewHolder mhHolder) {
        if (!TextUtils.isEmpty(entity.getCode())) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL); // 画框
            drawable.setColor(Color.parseColor(entity.getCode()));
//            mhHolder.ivdot.setBackgroundDrawable(drawable);
            mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
        }
    }

    private UserSeviceImpl.UserSeviceImplListListenser listListener = new UserSeviceImpl.UserSeviceImplListListenser() {

        @Override
        public void setUserSeviceImplListListenser(
                Object object, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub

            UserPersonIdEntity entity = null;
            if (object != null) {
                entity = (UserPersonIdEntity) object;
                UserObjEntity obj = entity.getObj();
                Intent intent = new Intent(context,
                        IntroductionActivity.class);
                //保存联系人
                service.saveContactName(obj.getUserId(), obj.getName());

                intent.putExtra("id", obj.getUserId());
                intent.putExtra("name", obj.getName());
                intent.putExtra("mobileNumber", obj.getPhoneNumOne());
                intent.putExtra("telephoneNumber", obj.getPhoneNumTwo());
                intent.putExtra("post", obj.getPostName());
                intent.putExtra("department", obj.getDepName());
                intent.putExtra("sex", obj.getSex());
                intent.putExtra("email", obj.getEmail());


                context.startActivity(intent);
            } else if (stRerror != null) {
                Toast.makeText(context, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else if (Exceptionerror != null) {
                Toast.makeText(
                        context,
                        Const.REQUESTERROR,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void peopleClick(ViewHolder mhHolder, final String executePeopleId) {
        mhHolder.executePeople.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                sevice.getUserMessageByPersonalId(executePeopleId, listListener);
            }
        });
    }

    class ViewHolder {
        private TextView postiontv;// 第几步
        private TextView executePeopleTitle;// 步骤执行人标题
        private TextView executePeople;// 步骤执行人
        private TextView name_step;// 步骤名称
        private TextView status;// 完成状态
        private TextView content;// 信息内容
        private TextView overtime;// （已用时：结束时间减去开始时间）
        private ImageView predict_time_title;// 预计用时标题
        private ImageView over_time_title;// 预计用时标题
        private TextView predict_time;// 预计用时，单位秒
        private View v_top;
        private View v_bottom;
        private ImageView iv;
        private View v_split;

        /**
         * 新增
         * 2017/10/23
         */
        private LinearLayout ll_executePeople;
        private LinearLayout ll_time;

        private TextView jumptv;// 跳过按钮
        private TextView tv_start;// 开启按钮
        private TextView tv_pause;// 暂停按钮
        private TextView tv_cancel_jump;// 取消跳过按钮
    }

    private ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean> controlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean>() {

        @Override
        public void setControlServiceImplListenser(
                Boolean backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            if (backValue) {
                ToastUtil.showToast(context, "已成功跳过此步骤");

                EventBus.getDefault().post(new mainEvent("jump"));

            } else if (stRerror != null) {
                Toast.makeText(context, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else if (Exceptionerror != null) {
                Toast.makeText(context,
                        Const.REQUESTERROR,
                        Toast.LENGTH_SHORT).show();
            }
            // if (Utils.getInstance().progressDialog.isShowing()) {
            Utils.getInstance().hideProgressDialog();
            // }
        }
    };

    /**
     * 跳过预案
     */
    private void planjump(String id, String planInfoId) {
        // TODO Auto-generated method stub
        Utils.getInstance().showProgressDialog(context, "", Const.LOAD_MESSAGE);
        csevice.jumpplan(id, planInfoId, controlServiceImplBackValueListenser);
    }

    private String strStopOrStart = "0";
    private ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean> controlServiceImplBackValueListenser1 = new ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean>() {

        @Override
        public void setControlServiceImplListenser(
                Boolean backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            if (backValue) {
                if (strStopOrStart.equals("0")) {
                    ToastUtil.showToast(context, "已成功跳过此步骤");
                } else {
                    ToastUtil.showToast(context, "已成功取消跳过此步骤");
                }

                EventBus.getDefault().post(new mainEvent("jump"));

            } else if (stRerror != null) {
                Toast.makeText(context, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else if (Exceptionerror != null) {
                Toast.makeText(context,
                        Const.REQUESTERROR,
                        Toast.LENGTH_SHORT).show();
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    /**
     * 非“执行中”跳过预案
     */
    private void planjump2(String id, String planInfoId, final String stopOrStart) {
        // TODO Auto-generated method stub
        strStopOrStart = stopOrStart;
        Utils.getInstance().showProgressDialog(context, "", Const.LOAD_MESSAGE);
        csevice.jumpplan2(id, planInfoId, stopOrStart, controlServiceImplBackValueListenser1);
    }

    private ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean> planPauseControlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<Boolean>() {

        @Override
        public void setControlServiceImplListenser(
                Boolean backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            if (backValue) {
                if (strStopOrStart.equals("0")) {
                    ToastUtil.showToast(context, "已成功暂停此步骤");
                } else {
                    ToastUtil.showToast(context, "已成功开启此步骤");
                }

                EventBus.getDefault().post(new mainEvent("jump"));

            } else if (stRerror != null) {
                Toast.makeText(context, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else if (Exceptionerror != null) {
                Toast.makeText(context,
                        Const.REQUESTERROR,
                        Toast.LENGTH_SHORT).show();
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    /**
     * 暂停和开启
     * 2017/10/13
     */
    private void planPause(String id, String planInfoId, final String stopOrStart) {
        // TODO Auto-generated method stub
        strStopOrStart = stopOrStart;
        Utils.getInstance().showProgressDialog(context, "", Const.LOAD_MESSAGE);
        csevice.pauseplan(id, planInfoId, stopOrStart, planPauseControlServiceImplBackValueListenser);
    }

}
