package com.dssm.esc.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.view.activity.OperationMenuActivity;

import java.util.List;


/**
 * 预案执行的适配器
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-14
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
public class ExpandListvPlanExecuteAdapter extends BaseExpandableListAdapter {
    List<GroupEntity> groupList;
    Context context;

    public ExpandListvPlanExecuteAdapter(List<GroupEntity> groupList, Context context) {
        this.groupList = groupList;
        // this.childList = childList;
        this.context = context;

    }

    public void setGropList(List<GroupEntity> groupList) {
        this.groupList = groupList;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param dataList
     */
    public void updateListView(List<GroupEntity> dataList) {
        this.groupList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public ChildEntity getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        final GroupEntity groupItem = groupList.get(groupPosition);
        if (groupItem == null || groupItem.getcList() == null
                || groupItem.getcList().isEmpty()) {
            return null;
        }
        return groupItem.getcList().get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        final GroupEntity groupItem = groupList.get(groupPosition);
        if (groupItem == null || groupItem.getcList() == null
                || groupItem.getcList().isEmpty()) {
            // ToastUtil.showToast(context, "没有您要执行的步骤");
            return 0;
        } else {
            return groupItem.getcList().size();
        }

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // if (getChildrenCount(groupPosition)==1&&getChild(groupPosition,
        // childPosition)==null) {
        // TextView textView = new TextView(context);
        // textView.setText("没有你要执行的步骤！");
        // return textView;
        // }else {
        final GroupEntity groupItem = groupList.get(groupPosition);
        final ChildEntity centity = getChild(groupPosition, childPosition);
        childViewHolder cHolder = null;
        if (convertView == null) {
            cHolder = new childViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.child_planexecute, null);
            cHolder.child_img = (ImageView) convertView
                    .findViewById(R.id.child_img);
            cHolder.child_step = (TextView) convertView
                    .findViewById(R.id.child_step);
            cHolder.execute_status = (TextView) convertView
                    .findViewById(R.id.execute_status);
            convertView.setTag(cHolder);
        } else {
            cHolder = (childViewHolder) convertView.getTag();
        }
        cHolder.child_step.setText(centity.getProcessName());
        // （全部完成：1，部分完成：2，跳过：3，正在执行：4，可执行：5,准备执行：6，还未执行：7）
        String status = centity.getStatus();

        if (status.equals("1") || status.equals("2") || status.equals("3")) {//
            cHolder.execute_status.setText("已执行");
            /**
             * 新增颜色节点
             * 2017/10/13
             */
            if (!TextUtils.isEmpty(centity.getCode())){
                cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
            }else {
                cHolder.execute_status.setTextColor(context.getResources().getColor(R.color.green_b));
            }
        } else if (status.equals("4")) {
            cHolder.execute_status.setText("执行中");// 切换按钮
            if (!TextUtils.isEmpty(centity.getCode())){
                cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
            }else {
                cHolder.execute_status.setTextColor(context.getResources().getColor(R.color.yellow_dot));
            }
            // cHolder.execute_status.setTextColor(Color.BLUE);
        } else if (status.equals("5")) {
            cHolder.execute_status.setText("可执行");// 有执行按钮
            if (!TextUtils.isEmpty(centity.getCode())){
                cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
            }else {
                cHolder.execute_status.setTextColor(context.getResources().getColor(R.color.blue_dot));
            }
        } else if (status.equals("6")) {
            cHolder.execute_status.setText("准备执行");
            if (!TextUtils.isEmpty(centity.getCode())){
                cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
            }else {
                cHolder.execute_status.setTextColor(context.getResources().getColor(R.color.green_a));
            }
        } else if (status.equals("7")) {
            cHolder.execute_status.setText("未执行");
            if (!TextUtils.isEmpty(centity.getCode())){
                cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
            }else {
                cHolder.execute_status.setTextColor(Color.RED);
            }
        }

        /**
         * 新增
         * 2017/10/13
         */
        if (10 < Integer.parseInt(status) && Integer.parseInt(status) < 20) {
            cHolder.execute_status.setText("暂停");
            if (!TextUtils.isEmpty(centity.getCode())){
                cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
            }else {
                cHolder.execute_status.setTextColor(Color.RED);
            }
        }

        switch (Integer.parseInt(status)) {
            /**
             * 新增
             * 2017/10/12
             */
            case 8:
                cHolder.execute_status.setText("自动执行中");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;

            case 9:
                cHolder.execute_status.setText("已执行");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;

            case 10:
                cHolder.execute_status.setText("执行失败");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;

            case 20:
                cHolder.execute_status.setText("接收超时");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;

            case 21:
                cHolder.execute_status.setText("执行异常");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;

            case 22:
                cHolder.execute_status.setText("异常解除");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;

            case 23:
                cHolder.execute_status.setText("跳过");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;

            case 24:
                cHolder.execute_status.setText("跳过");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;

            case 25:
                cHolder.execute_status.setText("执行超时");
                if (!TextUtils.isEmpty(centity.getCode())){
                    cHolder.execute_status.setTextColor(Color.parseColor(centity.getCode()));
                }else {
                    cHolder.execute_status.setTextColor(Color.RED);
                }
                break;
        }
        //2018.7.13新增，预案处于待启动或已启动或已授权的情况下，更改流程状态显示为流程未启动
        if(groupItem.getState().equals("0") || groupItem.getState().equals("2") || groupItem.getState().equals("1"))
            cHolder.execute_status.setText("流程未启动");
        //根据层级设置缩进
        convertView.setPadding(centity.getIndex() * 20, 0, 0, 0);
        //子预案节点
        if(centity.getNodeStepType().equals("CallActivity"))
            cHolder.child_img.setVisibility(View.VISIBLE);
        else
            cHolder.child_img.setVisibility(View.GONE);
        return convertView;
        // }
    }

    class childViewHolder {
        private ImageView child_img;
        private TextView child_step;
        private TextView execute_status;

    }

    @Override
    public GroupEntity getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        if (groupList == null) {
            return null;
        }
        return groupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        if (groupList == null) {
            return 0;
        }
        return groupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final GroupEntity gentity = getGroup(groupPosition);
        GropViewHolder gHolder = null;
        if (convertView == null) {
            gHolder = new GropViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.group_planexcute, null);
            gHolder.group_palnexecute = (TextView) convertView
                    .findViewById(R.id.group_palnexecute);
            gHolder.gropimg = (ImageView) convertView
                    .findViewById(R.id.gropimg);
            gHolder.ll_operation_menu = (LinearLayout) convertView
                    .findViewById(R.id.ll_operation_menu);
            gHolder.textstate = (TextView) convertView
                    .findViewById(R.id.textstate);

            convertView.setTag(gHolder);
        } else {
            gHolder = (GropViewHolder) convertView.getTag();
        }
        gHolder.group_palnexecute.setText(gentity.getGroupname());
        String state = gentity.getState();
        // state（0.待启动 1.已启动待授权 2.已授权待流程启动3.执行中4.完成 5.强行中止 ）判断，小于3就表示流程还未启动
        int stateint = Integer.parseInt(state);
        if (stateint == 0) {
            gHolder.textstate.setText("状态：待启动");
        } else if (stateint == 1) {
            gHolder.textstate.setText("状态：已启动");
        } else if (stateint == 2) {
            gHolder.textstate.setText("状态：已授权");
        } else if (stateint == 3) {
            gHolder.textstate.setText("状态：执行中");
        } else if (stateint == 4) {
            gHolder.textstate.setText("状态：完成");
        } else if (stateint == 5) {
            gHolder.textstate.setText("状态：强行中止");
        } else if (stateint == 6) {
            gHolder.textstate.setText("状态：暂停");
        }

        gHolder.textstate.setTextColor(Color.RED);
        gHolder.ll_operation_menu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // planResType 预案来源类型 1、事件 2、演练计划
                // drillPrecautionId 演练预案id
                // planInfoId 执行预案ID
                Intent intent = new Intent(context, OperationMenuActivity.class);
                intent.putExtra("planResType", gentity.getPlanResType());
                intent.putExtra("drillPrecautionId",
                        gentity.getDrillPrecautionId());
                intent.putExtra("planInfoId", gentity.getGroup_id());
                intent.putExtra("name", gentity.getGroupname());
                intent.putExtra("tag", "1");// 列表点进去
                context.startActivity(intent);
            }
        });
        if (gentity.getcList().size() > 0) {
            if (!isExpanded) {
                gHolder.gropimg.setImageResource(R.drawable.rightlist);

            } else {
                gHolder.gropimg.setImageResource(R.drawable.expandlist);

            }
        }
        return convertView;
    }

    class GropViewHolder {
        private TextView group_palnexecute, textstate;
        private ImageView gropimg;
        private LinearLayout ll_operation_menu;

    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}
