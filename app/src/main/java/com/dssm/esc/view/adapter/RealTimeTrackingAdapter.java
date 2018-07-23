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
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-15
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
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
                    R.layout.item_listview_realtimetrack, null);
            mhHolder.executePeople = (TextView) convertView.findViewById(R.id.executePeople);
            mhHolder.name_step = (TextView) convertView.findViewById(R.id.name_step);
            mhHolder.status = (TextView) convertView.findViewById(R.id.status);
            mhHolder.content = (TextView) convertView.findViewById(R.id.content);

            mhHolder.overtime = (TextView) convertView.findViewById(R.id.overtime);
            mhHolder.predict_time = (TextView) convertView.findViewById(R.id.predict_time);
            mhHolder.ivdot = (ImageView) convertView.findViewById(R.id.dot);
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
        if (!entity.getEditOrderNum().equals("null")) {
            mhHolder.postiontv.setText(entity.getParentOrderNum() + entity.getEditOrderNum() + ".");
        }
        else {
            if (!entity.getOrderNum().equals("null")) {
                mhHolder.postiontv.setText(entity.getParentOrderNum() + entity.getOrderNum() + ".");
            }
        }
        if (!entity.getExecutePeople().equals("null")) {
            mhHolder.executePeople.setText(entity.getExecutePeople());
            final String executePeopleId = entity.getExecutePeopleId();
            if (!executePeopleId.equals("null") && !executePeopleId.equals("")) {
                peopleClick(mhHolder, executePeopleId);

            } else {
                ToastUtil.showToast(context, "未查找到执行人！");
            }

        }
        else
            mhHolder.executePeople.setText("");
        if (!entity.getName().equals("null")) {
            mhHolder.name_step.setText(entity.getName());
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
//                mhHolder.ivdot.setImageResource(R.drawable.mini_red_dot);
                setDefaultBackground(mhHolder, R.color.red);
                mhHolder.status.setTextColor(Color.RED);
                mhHolder.status.setText(status);

                setItemColor(entity, mhHolder);

            }
            switch (Integer.parseInt(entity.getStatus())) {
                case 1:
                    status = "全部完成";
                    setDefaultBackground(mhHolder, R.color.green_b);
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_green);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.green_b));
                    mhHolder.status.setText("已执行");

                    setItemColor(entity, mhHolder);
                    break;
                case 2:
                    status = "部分完成";
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_green);
                    setDefaultBackground(mhHolder, R.color.green_b);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.green_b));
                    mhHolder.status.setText("已执行");

                    setItemColor(entity, mhHolder);
                    break;
                case 3:
                    status = "跳过";
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_green);
                    setDefaultBackground(mhHolder, R.color.green_b);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.green_b));
                    mhHolder.status.setText("已执行");

                    setItemColor(entity, mhHolder);
                    break;
                case 4:
                    status = "执行中";
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_yellow);
                    setDefaultBackground(mhHolder, R.color.yellow_dot);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.yellow_dot));
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                case 5:
                    status = "可执行";// 蓝的
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_blue);
                    setDefaultBackground(mhHolder, R.color.blue_dot);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.blue_dot));
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                case 6:
                    status = "准备执行";// 青绿色
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_blue_green);
                    setDefaultBackground(mhHolder, R.color.green_a);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.green_a));
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                case 7:
                    status = "未执行";
                    //新增,2018/4/24
                    //待启动或已启动或已授权的情况下，更改状态显示为流程未启动
                    if(parentState.equals("0") || parentState.equals("2") || parentState.equals("1"))
                        status = "流程未启动";
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_red_dot);
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                /**
                 * 新增
                 * 2017/10/12
                 */
                case 8:
                    status = "自动执行中";
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_red_dot);
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 9:
                    status = "已执行";
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_red_dot);
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 10:
                    status = "执行失败";
//                    mhHolder.ivdot.setImageResource(R.drawable.mini_red_dot);
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 20:
                    status = "接收超时";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 21:
                    status = "执行异常";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 22:
                    status = "异常解除";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 23:
                    status = "跳过";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 24:
                    status = "跳过";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                case 25:
                    status = "执行超时";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;

                /**
                 * 添加执行完成状态
                 * 2017.10.18
                 */
                case 26:
                    status = "已执行";
                    setDefaultBackground(mhHolder, R.color.red);
                    mhHolder.status.setTextColor(Color.RED);
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                /**
                 * 新增
                 * 2018/6/28
                 */
                case 27:
                    status = "未选择执行";
                    setDefaultBackground(mhHolder, R.color.green_b);
                    mhHolder.status.setTextColor(context.getResources().getColor(R.color.green_b));
                    mhHolder.status.setText(status);

                    setItemColor(entity, mhHolder);
                    break;
                default:
                    if(status.equals("")) {
                        setDefaultBackground(mhHolder, R.color.red);
                        mhHolder.status.setTextColor(Color.RED);
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
                                            planPause(entity.getId(), entity.getPlanInfoId(), "0");
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
                                            planPause(entity.getId(), entity.getPlanInfoId(), "0");
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
                || entity.getStatus().equals(RealTimeTrackingStatus.BEFORE))
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
                                            planjump2(entity.getId(), entity.getPlanInfoId(), "0");
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
                                                planPause(entity.getId(), entity.getPlanInfoId(), "0");
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

        if ((roleCode.equals("R001") || roleCode.equals("R002")
                || roleCode.equals("R003")) && parentState.equals("3")
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
                                            planjump2(entity.getId(), entity.getPlanInfoId(), "1");
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
                || roleCode.equals("R003")) && (entity.getStatus().equals(RealTimeTrackingStatus.EXCEPTION_OPTION_TIME_OUT)
                || entity.getStatus().equals(RealTimeTrackingStatus.EXCEPTION_OPTION_STOP))
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
                                            planjump2(entity.getId(), entity.getPlanInfoId(), "0");
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
                                            planPause(entity.getId(), entity.getPlanInfoId(), "1");
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


        /**
         * 旧的跳过按钮，两个跳过调用不同接口
         */
        //执行中
        if (roleCode.equals("R003") && entity.getStatus().equals("4")
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
                                            planjump(entity.getId(), entity.getPlanInfoId());
                                        }
                                    })

                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create(); // 创建对话框
                    dialog.show(); // 显示对话框

                    // TplanjumpODO Auto-generated method stub
                    //		planjump(entity.getId(), entity.getPlanInfoId());
                }
            });
        }

        if (!entity.getMessage().equals("null")) {
            mhHolder.content.setText(entity.getMessage());
        } else {
            mhHolder.content.setText("");
        }
        if (entity.getStatus().equals("4")
                || entity.getStatus().equals(RealTimeTrackingStatus.EXCEPTION_OPTION_TIME_OUT)) {
            // 执行中或执行超时
            mhHolder.overtime.setText(Utils.getInstance().setTtimeline(curDate,
                    entity.getBeginTime()));
            if(entity.getStatus().equals("4")) {
                String overSeconds = Utils.getInstance().getOverSeconds(
                        curDate, entity.getBeginTime()) + "";
                long result = Utils.getInstance().compareTime(overSeconds, entity.getDuration());
                if(result < 0)
                    mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.green_my));
                else
                    mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.red));
            }
            else
                mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.red));
            Log.i("entity.getBeginTime()", entity.getBeginTime());
        }
        else if (entity.getStatus().equals(RealTimeTrackingStatus.EXCEPTION_OPTION_STOP)) {
            //执行异常
            mhHolder.overtime.setText(Utils.getInstance().setTtimeline(curDate,
                    entity.getBeginTime()));
            String overSeconds = Utils.getInstance().getOverSeconds(
                    curDate, entity.getBeginTime()) + "";
            long result = Utils.getInstance().compareTime(overSeconds, entity.getDuration());
            if(result < 0)
                mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.green_my));
            else
                mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if (!entity.getEndTime().equals("null")
                && !entity.getBeginTime().equals("")) {
            String overSeconds = Utils.getInstance().getOverSeconds(
                    entity.getEndTime(), entity.getBeginTime()) + "";
            long result = Utils.getInstance().compareTime(overSeconds, entity.getDuration());
            mhHolder.overtime.setText(Utils.getInstance().getOverTime(
                    entity.getEndTime(), entity.getBeginTime()));
            if(result < 0)
                mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.green_my));
            else
                mhHolder.overtime.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            mhHolder.overtime.setText("");
        }
        if (!entity.getDuration().equals("null")) {
            mhHolder.predict_time.setText(entity.getDuration() + "秒");
        } else {
            mhHolder.predict_time.setText("");
        }

        /**
         * 添加执行完成状态
         * 2017.10.23
         * 2018.4.24 新增节点不显示执行人等状态
         */
        if (26 == Integer.parseInt(entity.getStatus()) || ((null == entity.getType()) ? false : entity.getType().equals("drillNew"))) {
            mhHolder.ll_executePeople.setVisibility(View.GONE);
            mhHolder.ll_time.setVisibility(View.GONE);
        } else {
            mhHolder.ll_executePeople.setVisibility(View.VISIBLE);
            mhHolder.ll_time.setVisibility(View.VISIBLE);
        }
        /**
         * 子预案节点不需要执行人、已用时及预计执行时间
         * 2018.7.23
         */
        if(entity.getNodeStepType().equals("CallActivity")) {
            mhHolder.ll_time.setVisibility(View.GONE);
            mhHolder.executePeople.setText("");
        }
        else {
            mhHolder.ll_time.setVisibility(View.VISIBLE);
        }

        //根据子预案层级设置缩进幅度
        convertView.setPadding(entity.getIndex() * 20, 0, 0, 0);
        return convertView;
    }

    private void setDefaultBackground(ViewHolder mhHolder, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL); // 画框
        drawable.setColor(color);
        mhHolder.ivdot.setBackgroundDrawable(drawable);
    }

    private void setItemColor(FlowChartPlanEntity.FlowChart entity, ViewHolder mhHolder) {
        if (!TextUtils.isEmpty(entity.getCode())) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL); // 画框
            drawable.setColor(Color.parseColor(entity.getCode()));
            mhHolder.ivdot.setBackgroundDrawable(drawable);
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
                intent.putExtra("phonenumberOne",
                        obj.getPhoneNumOne());
                intent.putExtra("phonenumberTwo",
                        obj.getPhoneNumTwo());
                intent.putExtra("gangwei",
                        obj.getPostName());
                intent.putExtra("bumen", obj.getDepName());
                intent.putExtra("sex", obj.getSex());
                intent.putExtra("postFlag", obj.getPostFlag());
                intent.putExtra("email", obj.getEmail());
                context.startActivity(intent);
            } else if (stRerror != null) {
                Toast.makeText(context, stRerror,
                        Toast.LENGTH_SHORT).show();
            } else if (Exceptionerror != null) {
                Toast.makeText(
                        context,
                        Const.NETWORKERROR + Exceptionerror,
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
        private TextView executePeople;// 步骤执行人
        private TextView name_step;// 步骤名称
        private TextView status;// 完成状态
        private TextView content;// 信息内容
        private TextView overtime;// （已用时：结束时间减去开始时间）
        private TextView predict_time;// 预计用时，单位秒

        /**
         * 新增
         * 2017/10/23
         */
        private LinearLayout ll_executePeople;
        private LinearLayout ll_time;

        private ImageView ivdot;
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
                        Const.NETWORKERROR + Exceptionerror,
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
                        Const.NETWORKERROR + Exceptionerror,
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
                        Const.NETWORKERROR + Exceptionerror,
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
