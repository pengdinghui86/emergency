package com.dssm.esc.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.entity.emergency.EmergencyMenuEntity;
import com.dssm.esc.util.Utils;

import java.util.List;

public class EventProcessRecyclerViewAdapter extends RecyclerView.Adapter<EventProcessRecyclerViewAdapter.ViewHolder>
{
    private List<ProgressDetailEntity.EvenDetail> list;
    private Context context;
    private String timeNow;
    public EventProcessRecyclerViewAdapter(Context mContext, List<EmergencyMenuEntity> datats)
    {
        this.context = context;
        this.list = list;
        this.timeNow=timeNow;
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View view;
        if (i == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_event_process_detail_right,null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_event_process_detail_left,null);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        ProgressDetailEntity.EvenDetail entity = list.get(position);
        viewHolder.name.setText(entity.getProgressName());
        if (!entity.getFinishTime().equals("")) {
            viewHolder.overtime.setText(Utils.getInstance().setTtimeline(entity.getFinishTime(), entity.getStartTime()));
        }else{
            viewHolder.overtime.setText(Utils.getInstance().setTtimeline(timeNow, entity.getStartTime()));
        }
        if (position == 0) {
            viewHolder.v_top.setVisibility(View.INVISIBLE);
            viewHolder.v_bottom.setVisibility(View.VISIBLE);
            viewHolder.imageView.setImageResource(R.drawable.event_process_evaluation);

        } else if (position > 0 && !entity.getProgress().equals("eveClose")) {
            viewHolder.v_top.setVisibility(View.VISIBLE);
            viewHolder.v_bottom.setVisibility(View.VISIBLE);

        } else if (entity.getProgress().equals("eveClose")) {
            viewHolder.v_top.setVisibility(View.VISIBLE);
            viewHolder.v_bottom.setVisibility(View.INVISIBLE);
            viewHolder.imageView.setImageResource(R.drawable.event_process_event_close);
        }
        if (entity.getState().equals("0")) {
            viewHolder.time.setText("未开始");
            viewHolder.imageView.setBackgroundResource(R.drawable.circle_unstart_bg);
        }else if (entity.getState().equals("1")) {
            viewHolder.time.setText("执行中");
            viewHolder.imageView.setBackgroundResource(R.drawable.circle_execute_bg);
        }else if (entity.getState().equals("2")) {
            viewHolder.time.setText("已完成"+(entity.getFinishTime()));
            viewHolder.imageView.setBackgroundResource(R.drawable.circle_complete_bg);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView time;
        private TextView overtime;
        private ImageView imageView;
        private View v_top;
        private View v_bottom;
        private TextView name;
        public ViewHolder(View arg0)
        {
            super(arg0);
            name = (TextView) arg0.findViewById(R.id.event_process_detail_tv_name);
            time = (TextView) arg0.findViewById(R.id.event_process_detail_tv_time);
            overtime = (TextView) arg0.findViewById(R.id.event_process_detail_tv_overtime);
            imageView = (ImageView) arg0.findViewById(R.id.event_process_detail_iv_process);
            v_top = (View) arg0.findViewById(R.id.event_process_detail_v_top);
            v_bottom = (View) arg0.findViewById(R.id.event_process_detail_v_bottom);

        }

    }
}
