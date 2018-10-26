package com.dssm.esc.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.view.widget.EventDragListItem;

import java.util.List;


/**
 * 侧滑操作-事件列表适配器
 */
public class EventDragListviewAdapter extends BaseAdapter {

	private List<PlanStarListEntity> arraylist;
	private Context context;
	/** 0，已授权1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示,6,事件流程列表7,驳回事件列表 */
	private String tags;
	private ItemClickListener itemOnClickListener;

	public EventDragListviewAdapter(Context context,
                                    List<PlanStarListEntity> list, String tags) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arraylist = list;
		this.tags = tags;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public PlanStarListEntity getItem(int position) {
		// TODO Auto-generated method stub
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public interface ItemClickListener
	{
		void itemOnClick();
		void hiddenItemOnClick(PlanStarListEntity entity);
	}

	public void setItemOnClickListener(ItemClickListener itemOnClickListener)
	{
		this.itemOnClickListener = itemOnClickListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final PlanStarListEntity entity = arraylist.get(position);
		ViewHolder mhHolder = null;
		EventDragListItem eventDragListItem = (EventDragListItem) convertView;
		final EventDragListItem dragListItem;
		if (eventDragListItem == null) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_event_hide_drag, parent, false);
			dragListItem = new EventDragListItem(context);
			dragListItem.setContentView(view);
			mhHolder = new ViewHolder(dragListItem);
			dragListItem.setTag(mhHolder);
		} else {
			dragListItem = eventDragListItem;
			mhHolder = (ViewHolder) dragListItem.getTag();
		}
		dragListItem.rollBack();
		dragListItem.setOnClickListener(new View.OnClickListener() {//给条目添加点击事件
			@Override
			public void onClick(View v) {
				if(itemOnClickListener != null)
					itemOnClickListener.itemOnClick();
			}
		});
//		dragListItem.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View view, MotionEvent motionEvent) {
//				dragListItem.onDragTouchEvent(motionEvent);
//				return false;
//			}
//		});

//		mhHolder.hideItem.setOnClickListener(new View.OnClickListener() {//给隐藏的布局设置点击事件 比如点击删除功能
//			@Override
//			public void onClick(View v) {
//				if(itemOnClickListener != null)
//					itemOnClickListener.hiddenItemOnClick(entity);
//			}
//		});

		if (tags.equals("1")) {// 待启动事件列表
			mhHolder.tvEventName.setText(entity.getEveName());
			mhHolder.tvState.setTextColor(Color.RED);
			mhHolder.tvTradeType.setText(entity.getTradeType());
			String planResType = entity.getEveType();
			if (planResType.equals("1")) {
				mhHolder.ivEventType.setImageResource(R.drawable.emergency_type);
			} else if (planResType.equals("2")) {
				mhHolder.ivEventType.setImageResource(R.drawable.drill_type);
			}
			mhHolder.tvEventLevel.setText(entity.getEveLevel());
			// 0:，初始状态；1，待预案评估；2，执行中；3，结束；5，启动中；-1，驳回评估
			if (!entity.getState().equals("null")
					&& !entity.getState().equals("")) {
				String status = "";
				switch (Integer.parseInt(entity.getState())) {
					case 0:
						status = "初始状态";
						mhHolder.tvState.setTextColor(Color.BLUE);
						break;
					case 1:
						status = "待预案评估";
						break;
					case 2:
						status = "执行中";
						break;
					case 3:
						status = "结束";
						break;
					case 5:
						status = "启动中";
						break;
					case -1:
						status = "驳回评估";
						break;

				}
				mhHolder.tvState.setTextColor(Color.RED);
				mhHolder.tvState.setText(status);
			}
		} else if (tags.equals("2")) {// 已启动事件列表
			mhHolder.tvEventName.setText(entity.getPlanName());
			mhHolder.tvState.setTextColor(Color.RED);
			String planResType = entity.getPlanResType();
			if (planResType.equals("1")) {
				mhHolder.ivEventType.setImageResource(R.drawable.emergency_type);
			} else if (planResType.equals("2")) {
				mhHolder.ivEventType.setImageResource(R.drawable.drill_type);
			}
			mhHolder.tvEventLevel.setVisibility(View.GONE);
			mhHolder.tvTradeType.setText(entity.getPlanResName());
			// （0.待启动 1.已启动 2.已授权 3.流程启动 4.完成 5.强行中止）
			if (!entity.getState().equals("null")
					&& !entity.getState().equals("")) {
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
					/**
					 * 添加暂停状态
					 * 2017.10.16
					 */
					case 6:
						status = "暂停";
						break;
				}
				mhHolder.tvState.setText(status);
			}
		}
		return dragListItem;
	}

	class ViewHolder {
		private TextView tvEventName;
		private TextView tvEventLevel;
		private TextView tvTradeType;
		private TextView tvState;
		private ImageView ivEventType;

		public ViewHolder(EventDragListItem convertView)
		{
			this.tvEventName = (TextView) convertView.findViewById(R.id.event_listview_tv_name);
			this.tvState = (TextView) convertView
					.findViewById(R.id.event_listview_tv_event_status);
			this.tvTradeType = (TextView) convertView
					.findViewById(R.id.event_listview_tv_sub_name);
			this.tvEventLevel = (TextView) convertView
					.findViewById(R.id.event_listview_tv_event_level);
			this.ivEventType = (ImageView) convertView
					.findViewById(R.id.event_listview_iv_type);
		}
	}

}
