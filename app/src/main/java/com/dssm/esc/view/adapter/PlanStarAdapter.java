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
import com.dssm.esc.util.event.PlanStarListEntity;

import java.util.List;


/**
 * （预案启动）适配器
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class PlanStarAdapter extends BaseAdapter {

	private List<PlanStarListEntity> arraylist;
	private Context context;
	private String tags;

	public PlanStarAdapter(Context context, List<PlanStarListEntity> list,
			String tags) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arraylist = list;
		this.tags = tags;
	}

	private void setList(List<PlanStarListEntity> list, String tags) {
		this.arraylist = arraylist;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PlanStarListEntity entity = arraylist.get(position);
		ViewHolder mhHolder = null;
		if (convertView == null) {
			mhHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_planstar, null);

			mhHolder.name = (TextView) convertView.findViewById(R.id.name);
			mhHolder.statetv = (TextView) convertView
					.findViewById(R.id.statetv);
			mhHolder.evecodetv = (TextView) convertView
					.findViewById(R.id.evecodetv);
			mhHolder.tradeTypetv = (TextView) convertView
					.findViewById(R.id.tradeTypetv);
			mhHolder.eveLeveltv = (TextView) convertView
					.findViewById(R.id.eveLeveltv);
			mhHolder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolder) convertView.getTag();
		}

		if (tags.equals("1")) {// 待启动事件列表
			mhHolder.name.setText(entity.getEveName());
			mhHolder.evecodetv.setText(entity.getEveCode());
			mhHolder.tradeTypetv.setText(entity.getTradeType());
			mhHolder.eveLeveltv.setText(entity.getEveLevel());
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

				case -1:
					status = "驳回评估";
					break;

				}
				mhHolder.statetv.setTextColor(Color.RED);
				mhHolder.statetv.setText(status);
				mhHolder.img.setVisibility(View.GONE);
			}
		} else if (tags.equals("2")) {// 已启动元列表
			mhHolder.name.setText(entity.getPlanName());
			String planResType = entity.getPlanResType();
			if (planResType.equals("1")) {
				mhHolder.evecodetv.setText("应急");
			} else if (planResType.equals("2")) {
				mhHolder.evecodetv.setText("演练");
			}
			mhHolder.eveLeveltv.setVisibility(View.GONE);
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
				mhHolder.img.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	class ViewHolder {
		private TextView name;
		private TextView evecodetv;
		private TextView tradeTypetv;
		private TextView eveLeveltv;
		private TextView statetv;
		private ImageView img;

	}

}
