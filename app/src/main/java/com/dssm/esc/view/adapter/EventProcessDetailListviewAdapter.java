package com.dssm.esc.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.util.Utils;

import java.util.List;


/**
 * 事件流程详情适配器
 */
public class EventProcessDetailListviewAdapter extends BaseAdapter {

	private List<ProgressDetailEntity.EvenDetail> list;
	private Context context;
	private String timeNow;

	public EventProcessDetailListviewAdapter(Context mContext, List<ProgressDetailEntity.EvenDetail> data) {
		this.context = mContext;
		this.list = data;
	}

	public void update(List<ProgressDetailEntity.EvenDetail> data, String timeNow)
	{
		this.list = data;
		this.timeNow = timeNow;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public ProgressDetailEntity.EvenDetail getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ProgressDetailEntity.EvenDetail entity = list.get(position);
		ViewHolder mhHolder = new ViewHolder();
		if (position % 2 == 0) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_event_process_detail_right, null);
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_event_process_detail_left, null);
		}
		mhHolder.name = (TextView) convertView.findViewById(R.id.event_process_detail_tv_name);
		mhHolder.time = (TextView) convertView.findViewById(R.id.event_process_detail_tv_time);
		mhHolder.overtime = (TextView) convertView.findViewById(R.id.event_process_detail_tv_overtime);
		mhHolder.imageView = (ImageView) convertView.findViewById(R.id.event_process_detail_iv_process);
		mhHolder.v_top = (View) convertView.findViewById(R.id.event_process_detail_v_top);
		mhHolder.v_bottom = (View) convertView.findViewById(R.id.event_process_detail_v_bottom);

		mhHolder.name.setText(entity.getProgressName());
		if (!entity.getFinishTime().equals("")) {
			mhHolder.overtime.setText(Utils.getInstance().setTtimeline(entity.getFinishTime(), entity.getStartTime()));
		}else{
			mhHolder.overtime.setText(Utils.getInstance().setTtimeline(timeNow, entity.getStartTime()));
		}
		if (position == 0) {
			mhHolder.v_top.setVisibility(View.INVISIBLE);
			mhHolder.v_bottom.setVisibility(View.VISIBLE);
			mhHolder.imageView.setImageResource(R.drawable.event_process_evaluation);

		} else if (position > 0 && !entity.getProgress().equals("eveClose")) {
			mhHolder.v_top.setVisibility(View.VISIBLE);
			mhHolder.v_bottom.setVisibility(View.VISIBLE);
			if(entity.getProgress().equals("planStart"))
				mhHolder.imageView.setImageResource(R.drawable.event_process_plan_start);
			if(entity.getProgress().equals("planAuth"))
				mhHolder.imageView.setImageResource(R.drawable.event_process_plan_authorize);
			if(entity.getProgress().equals("personSign"))
				mhHolder.imageView.setImageResource(R.drawable.event_process_sign_assign);
			if(entity.getProgress().equals("planPerform"))
				mhHolder.imageView.setImageResource(R.drawable.event_process_plan_execute);

		} else if (entity.getProgress().equals("eveClose")) {
			mhHolder.v_top.setVisibility(View.VISIBLE);
			mhHolder.v_bottom.setVisibility(View.INVISIBLE);
			mhHolder.imageView.setImageResource(R.drawable.event_process_event_close);
		}
		if (entity.getState().equals("0")) {
			mhHolder.time.setText("未开始");
			mhHolder.imageView.setBackgroundResource(R.drawable.circle_unstart_bg);
		}else if (entity.getState().equals("1")) {
			mhHolder.time.setText("执行中");
			mhHolder.imageView.setBackgroundResource(R.drawable.circle_execute_bg);
		}else if (entity.getState().equals("2")) {
			mhHolder.time.setText("已完成" + (entity.getFinishTime() == null || "null".equals(entity.getFinishTime()) ? "" : entity.getFinishTime()));
			mhHolder.imageView.setBackgroundResource(R.drawable.circle_complete_bg);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView time;
		private TextView overtime;
		private ImageView imageView;
		private View v_top;
		private View v_bottom;
		private TextView name;
	}

}
