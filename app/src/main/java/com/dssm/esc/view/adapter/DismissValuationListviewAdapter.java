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
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;

import java.util.List;


/**
 * 驳回事件适配器
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-10
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class DismissValuationListviewAdapter extends BaseAdapter {

	private List<BoHuiListEntity> arraylist;
	private Context context;
	/** 0，已授权1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示,6,事件流程列表7,驳回事件列表 */
	private String tags;

	public DismissValuationListviewAdapter(Context context,
			List<BoHuiListEntity> list, String tags) {
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
	public BoHuiListEntity getItem(int position) {
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
		BoHuiListEntity entity = arraylist.get(position);
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
			mhHolder.img = (ImageView) convertView.findViewById(R.id.img);

			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolder) convertView.getTag();
		}
		if (tags.equals("0")||tags.equals("1")||tags.equals("4")||tags.equals("2")||tags.equals("3")) {
			mhHolder.img.setVisibility(View.VISIBLE);
		} else {
			mhHolder.img.setVisibility(View.GONE);
		}
		if (tags.equals("6") || tags.equals("7")) {// 事件列表
			mhHolder.dismissv_item_tv.setText(arraylist.get(position)
					.getEveName());
			mhHolder.evecodetv.setText(entity.getEveCode());
			mhHolder.tradeTypetv.setText(entity.getTradeType());
//			mhHolder.eveLeveltv.setText(entity.getEveLevel());
			// 0:，初始状态；1，待预案评估；2，执行中；3，结束；-1，驳回评估
			if (!entity.getState().equals("null")
					&& !entity.getState().equals("")) {
				String status = "";
				switch (Integer.parseInt(entity.getState())) {
				case 0:
					status = "初始状态";
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
				/**
				 * 添加执行完成状态
				 * 2017.10.18
				 */
				case 4:
					status = "已完成";
					break;

				/**
				 * 添加暂停状态
				 * 2017.10.12
				 */
				case 6:
					status = "暂停";
					break;

				case -1:
					status = "驳回评估";
					break;

				}
				mhHolder.statetv.setTextColor(Color.RED);
				mhHolder.statetv.setText(status);
			}
		} else if (tags.equals("0") || tags.equals("1") || tags.equals("2")
				|| tags.equals("3")) {// 预案列表
			mhHolder.dismissv_item_tv.setText(entity.getEveName());
			String planResType = entity.getPlanResType();
			if (planResType.equals("1")) {
				mhHolder.evecodetv.setTextColor(context.getResources().getColor(R.color.control_center_emergency_type));
				mhHolder.evecodetv.setText(context.getResources().getString(R.string.control_center_emergency_txt));
			} else if (planResType.equals("2")) {
				mhHolder.evecodetv.setTextColor(context.getResources().getColor(R.color.control_center_drill_type));
				mhHolder.evecodetv.setText(context.getResources().getString(R.string.control_center_drill_txt));
			}
			mhHolder.tradeTypetv.setText( entity.getPlanResName());
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
				 * 2017.10.12
				 */
				case 6:
					status = "暂停";
					break;
				}
				mhHolder.statetv.setTextColor(Color.RED);
				mhHolder.statetv.setText(status);
			}
		} else if (tags.equals("4")) {
			mhHolder.dismissv_item_tv.setText(entity.getEveName());
			String planResType = entity.getPlanResType();
			if (planResType.equals("1")) {
				mhHolder.evecodetv.setText("应急");
			} else if (planResType.equals("2")) {
				mhHolder.evecodetv.setText("演练");
			}
			mhHolder.tradeTypetv.setText(entity.getPlanResName());
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
				mhHolder.statetv.setTextColor(Color.RED);
				mhHolder.statetv.setText(status);
			}
		}

		return convertView;
	}

	class ViewHolder {
		private TextView dismissv_item_tv;
		private TextView evecodetv;
		private TextView tradeTypetv;
		private TextView statetv;
		private ImageView img;
	}

}
