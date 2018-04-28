package com.dssm.esc.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.control.PlanEntity;

import java.util.List;

/**
 * 5.2.1	待展示预案列表适配器
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-10
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
public class PlanAdapter extends BaseAdapter {

    private List<PlanEntity> arraylist;
    private Context context;

    public PlanAdapter(Context context,
                       List<PlanEntity> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.arraylist = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arraylist.size();
    }

    @Override
    public PlanEntity getItem(int position) {
        // TODO Auto-generated method stub
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        PlanEntity entity = getItem(position);
        ViewHolder mhHolder = null;
        if (convertView == null) {
            mhHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_listview_command_display, null);

            mhHolder.dismissv_item_tv = (TextView) convertView
                    .findViewById(R.id.dismissv_item_tv);
            mhHolder.evecodetv = (TextView) convertView
                    .findViewById(R.id.evecodetv);
            mhHolder.tradeTypetv = (TextView) convertView
                    .findViewById(R.id.tradeTypetv);
            mhHolder.statetv = (TextView) convertView
                    .findViewById(R.id.statetv);
            convertView.setTag(mhHolder);
        } else {
            mhHolder = (ViewHolder) convertView.getTag();
        }
        mhHolder.dismissv_item_tv.setText(entity.getPlanName());
        String planResType = entity.getPlanResType();
        if (planResType.equals("1")) {
            mhHolder.evecodetv.setTextColor(context.getResources().getColor(R.color.control_center_emergency_type));
            mhHolder.evecodetv.setText(context.getResources().getString(R.string.control_center_emergency_txt));
        } else if (planResType.equals("2")) {
            mhHolder.evecodetv.setTextColor(context.getResources().getColor(R.color.control_center_drill_type));
            mhHolder.evecodetv.setText(context.getResources().getString(R.string.control_center_drill_txt));
        }
        mhHolder.tradeTypetv.setText(entity.getPlanResName());
        // （0.待启动 1.已启动 2.已授权 3.执行中 4.完成 5.强行中止 6.暂停）
        if (!entity.getState().equals("null") && !entity.getState().equals("")) {
            String status = "";
            switch (Integer.parseInt(entity.getState())) {
                case 0:
                    status = "待启动";
                    break;
                case 1:
                    status = "已启动";
                    break;
                case 2:
                    status = "已授权";
                    break;
                case 3:
                    status = "执行中";

                    break;
                case 4:
                    status = "完成";
                    break;
                case 5:
                    status = "强行中止";
                    break;
                case 6:
                    status = "暂停";
                    break;

            }
            mhHolder.statetv.setTextColor(Color.RED);
            mhHolder.statetv.setText(status);
        }


        return convertView;
    }

    class ViewHolder {
        private TextView dismissv_item_tv;
        private TextView evecodetv;
        private TextView tradeTypetv;
        private TextView statetv;

    }

}
