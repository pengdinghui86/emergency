package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.util.Utils;

import java.util.List;


public class TimeLineListviewAdapter extends BaseAdapter {
	private List<ProgressDetailEntity.EvenDetail> list;
	private Context context;
	private String timeNow;
	public TimeLineListviewAdapter(Context context, List<ProgressDetailEntity.EvenDetail> list, String timeNow) {
		this.context = context;
		this.list = list;
		this.timeNow=timeNow;
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
		ProgressDetailEntity.EvenDetail entity = getItem(position);
		viewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new viewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_timeline, null);
			mHolder.event = (TextView) convertView.findViewById(R.id.event);
			mHolder.time = (TextView) convertView.findViewById(R.id.time);
			mHolder.overtime = (TextView) convertView
					.findViewById(R.id.overtime);
			mHolder.dot = (ImageView) convertView.findViewById(R.id.dot);
			mHolder.ll = (RelativeLayout) convertView.findViewById(R.id.ll);
			convertView.setTag(mHolder);
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		mHolder.event.setText(entity.getProgressName());
		if (!entity.getFinishTime().equals("")) {
			
			mHolder.overtime.setText(Utils.getInstance().setTtimeline(entity.getFinishTime(), entity.getStartTime()));
		}else{
			
			mHolder.overtime.setText(Utils.getInstance().setTtimeline(timeNow, entity.getStartTime()));
		}
		//1,2,3  完成（绿色）   4 执行中（黄色）   5,6,7 未执行（红色）
		if (position == 0) {
			if (entity.getState().equals("0")) {
				mHolder.dot.setImageResource(R.drawable.dotred1);
				mHolder.time.setText("未开始");
				mHolder.ll.setBackgroundResource(R.drawable.btbg_red);
			}else if (entity.getState().equals("1")) {
				mHolder.dot.setImageResource(R.drawable.last1);
				mHolder.time.setText("执行中");
				mHolder.ll.setBackgroundResource(R.drawable.btbg_yellow);
			}else if (entity.getState().equals("2")) {
				
				mHolder.dot.setImageResource(R.drawable.dotgreen1);
				mHolder.time.setText("已完成"+(entity.getFinishTime()));
				mHolder.ll.setBackgroundResource(R.drawable.btbg_green);
			}
			

		} else if (position>0&&!entity.getProgress().equals("eveClose")) {
			if (entity.getState().equals("0")) {
				mHolder.dot.setImageResource(R.drawable.dotred2);
				mHolder.time.setText("未开始");
				mHolder.ll.setBackgroundResource(R.drawable.btbg_red);
			}else if (entity.getState().equals("1")) {
				mHolder.dot.setImageResource(R.drawable.last2);
				mHolder.time.setText("执行中");
				mHolder.ll.setBackgroundResource(R.drawable.btbg_yellow);
			}else if (entity.getState().equals("2")) {
				
				mHolder.dot.setImageResource(R.drawable.dotgreen2);
				mHolder.time.setText("已完成"+(entity.getFinishTime()));
				mHolder.ll.setBackgroundResource(R.drawable.btbg_green);
			}
			
		} else if (entity.getProgress().equals("eveClose")) {
			if (entity.getState().equals("0")) {
				mHolder.dot.setImageResource(R.drawable.dotred3);
				mHolder.time.setText("未开始");
				mHolder.ll.setBackgroundResource(R.drawable.btbg_red);
			}else if (entity.getState().equals("1")) {
				mHolder.dot.setImageResource(R.drawable.last3);
				mHolder.time.setText("执行中");
				mHolder.ll.setBackgroundResource(R.drawable.btbg_yellow);
			}else if (entity.getState().equals("2")) {
				
				mHolder.dot.setImageResource(R.drawable.dotgreen3);
				mHolder.time.setText("已完成"+(entity.getFinishTime()));
				mHolder.ll.setBackgroundResource(R.drawable.btbg_green);
			}
		}
		return convertView;
	}

	class viewHolder {
		private TextView event;
		private TextView time;
		private TextView overtime;
		private ImageView dot;
		private RelativeLayout ll;
	}
}
