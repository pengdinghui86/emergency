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
import com.dssm.esc.model.entity.control.EventProgressEntity;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.util.Utils;

import java.util.List;


/**
 * 事件流程详情适配器
 */
public class EventProcessDetailListviewAdapter extends BaseAdapter {

	private List<EventProgressEntity> list;
	private Context context;
	private String timeNow;

	public EventProcessDetailListviewAdapter(Context mContext, List<EventProgressEntity> data) {
		this.context = mContext;
		this.list = data;
	}

	public void update(List<EventProgressEntity> data, String timeNow)
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
	public EventProgressEntity getItem(int position) {
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
		EventProgressEntity entity = list.get(position);
		ViewHolder mhHolder = new ViewHolder();
		if (position % 2 == 0) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_event_process_detail_right, null);
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_event_process_detail_left, null);
		}
		mhHolder.name = (TextView) convertView.findViewById(R.id.event_process_detail_tv_name);
		mhHolder.content = (TextView) convertView.findViewById(R.id.event_process_detail_tv_content);
		mhHolder.time = (TextView) convertView.findViewById(R.id.event_process_detail_tv_time);
		mhHolder.overtime = (TextView) convertView.findViewById(R.id.event_process_detail_tv_overtime);
		mhHolder.imageView = (ImageView) convertView.findViewById(R.id.event_process_detail_iv_process);
		mhHolder.v_top = (View) convertView.findViewById(R.id.event_process_detail_v_top);
		mhHolder.v_bottom = (View) convertView.findViewById(R.id.event_process_detail_v_bottom);

		mhHolder.name.setText(entity.getStep());
		mhHolder.time.setText(entity.getOperationTime().split(" ")[1]);
		mhHolder.content.setText(entity.getContent());
		if (!entity.getOperationTime().equals("")) {
			mhHolder.overtime.setText(Utils.getInstance().setTtimeline(entity.getOperationTime(), entity.getDiscoveryTime()));
		}else{
			mhHolder.overtime.setText(Utils.getInstance().setTtimeline(timeNow, entity.getDiscoveryTime()));
		}
		if (position == 0) {
			mhHolder.v_top.setVisibility(View.INVISIBLE);
			mhHolder.v_bottom.setVisibility(View.VISIBLE);
		} else if (position > 0 && position < list.size() - 1) {
			mhHolder.v_top.setVisibility(View.VISIBLE);
			mhHolder.v_bottom.setVisibility(View.VISIBLE);
		} else {
			mhHolder.v_top.setVisibility(View.VISIBLE);
			mhHolder.v_bottom.setVisibility(View.INVISIBLE);
		}
		mhHolder.imageView.setBackgroundResource(R.drawable.circle_complete_bg);
		mhHolder.imageView.setImageResource(R.drawable.event_process_plan_execute);
		if("9".equals(entity.getStepType()))
		{
			mhHolder.imageView.setBackgroundResource(R.drawable.circle_unstart_bg);
			mhHolder.imageView.setImageResource(R.drawable.event_process_event_close);
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
		private TextView content;
	}

}
