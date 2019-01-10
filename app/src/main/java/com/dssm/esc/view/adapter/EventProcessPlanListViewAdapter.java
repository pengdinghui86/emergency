package com.dssm.esc.view.adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dssm.esc.R;
import com.dssm.esc.util.event.PlanStarListEntity;
import java.util.List;

/**
 * 事件流程页面预案列表适配器
 */
public class EventProcessPlanListViewAdapter extends BaseAdapter {

	private List<PlanStarListEntity> list;
	private Context context;

	public EventProcessPlanListViewAdapter(Context mContext, List<PlanStarListEntity> data) {
		this.context = mContext;
		this.list = data;
	}

	public void update(List<PlanStarListEntity> data)
	{
		this.list = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public PlanStarListEntity getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PlanStarListEntity entity = list.get(position);
		ViewHolder mhHolder = new ViewHolder();
		convertView = LayoutInflater.from(context).inflate(
				R.layout.item_listview_plan, null);
		mhHolder.name = (TextView) convertView.findViewById(R.id.plan_listview_tv_plan_name);
		mhHolder.status = (TextView) convertView.findViewById(R.id.plan_listview_tv_plan_status);
		mhHolder.status.setTextColor(Color.RED);
		mhHolder.name.setText(entity.getPlanName() + "-" + entity.getSceneName());
		// （0.待启动 1.已启动 2.已授权 3.流程启动 4.完成 5.强行中止）
		if (!"null".equals(entity.getState())
				&& !"".equals(entity.getState())) {
			String status = "";
			switch (Integer.parseInt(entity.getState())) {
				case 0:
					status = "待启动";
					mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
					break;
				case 1:
					status = "已启动";
					mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_green));
					break;
				case 2:
					status = "已授权";
					mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_blue));
					break;
				case 3:
					status = "执行中";
					mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_yellow));
					break;
				case 4:
					status = "完成";
					mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_blue));
					break;
				case 5:
					status = "强行中止";
					mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_red));
					break;

				/**
				 * 添加暂停状态
				 * 2017.10.12
				 */
				case 6:
					status = "暂停";
					mhHolder.status.setTextColor(context.getResources().getColor(R.color.color_state_gray));
					break;
			}
			mhHolder.status.setText(status);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView name;
		private TextView status;
	}

}
