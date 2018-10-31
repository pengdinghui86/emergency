package com.dssm.esc.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.PlanProcessEntity;
import com.dssm.esc.status.RealTimeTrackingStatus;
import com.dssm.esc.view.activity.AssignmentActivity;

import java.util.ArrayList;


/**
 * 人员指派的适配器
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-12
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class PersonAssignListvAdapter extends BaseAdapter {

	private ArrayList<PlanProcessEntity> arraylist;
	private Context context;
private String planInfoId;
	public PersonAssignListvAdapter(String planInfoId,Context context,
			ArrayList<PlanProcessEntity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arraylist = list;
		this.planInfoId = planInfoId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public PlanProcessEntity getItem(int position) {
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

		final PlanProcessEntity entity = getItem(position);
		final ViewHolder mhHolder;
		if (convertView == null) {
			mhHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_person_assign, null);

			mhHolder.stepname = (TextView) convertView
					.findViewById(R.id.stepname);
			mhHolder.whodone_title = (TextView) convertView
					.findViewById(R.id.tv);
			mhHolder.whodone_name = (TextView) convertView
					.findViewById(R.id.whodone_name);
			mhHolder.who_task = (TextView) convertView
					.findViewById(R.id.who_task);
			mhHolder.assign = (TextView) convertView.findViewById(R.id.assign);
			mhHolder.numbertv = (TextView) convertView.findViewById(R.id.numbertv);
			mhHolder.v_top = (View) convertView.findViewById(R.id.item_list_view_person_assign_v_top);
			mhHolder.v_bottom = (View) convertView.findViewById(R.id.item_list_view_person_assign_v_bottom);
			mhHolder.iv = (ImageView) convertView.findViewById(R.id.item_list_view_person_assign_iv);
			mhHolder.v_split = (View) convertView.findViewById(R.id.item_list_view_person_assign_v_split);
			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolder) convertView.getTag();
		}

		mhHolder.stepname.setText(entity.getName());

		if(entity.getNodeStepType().equals("CallActivity")) {
			mhHolder.assign.setVisibility(View.INVISIBLE);
			mhHolder.who_task.setVisibility(View.GONE);
			mhHolder.whodone_name.setVisibility(View.GONE);
			mhHolder.whodone_title.setVisibility(View.GONE);
			mhHolder.stepname.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
			if(entity.getOrderNum() != null && !("").equals(entity.getOrderNum()))
				mhHolder.numbertv.setText(entity.getParentProcessNumber() + entity.getOrderNum());
			else if(entity.getEditOrderNum() != null && !("").equals(entity.getEditOrderNum()))
				mhHolder.numbertv.setText(entity.getParentProcessNumber() + entity.getEditOrderNum());
		}
		else {
			if(entity.getStatus().equals(RealTimeTrackingStatus.PREPARATION) || entity.getStatus().equals(RealTimeTrackingStatus.BEFORE))
				mhHolder.assign.setVisibility(View.VISIBLE);
			else
				mhHolder.assign.setVisibility(View.INVISIBLE);
			mhHolder.who_task.setVisibility(View.VISIBLE);
			mhHolder.whodone_name.setVisibility(View.VISIBLE);
			mhHolder.whodone_title.setVisibility(View.VISIBLE);
			mhHolder.stepname.setGravity(Gravity.LEFT|Gravity.TOP);
			mhHolder.assign.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//mhHolder.assign.setBackgroundResource(R.drawable.btbg_gray);
					Intent intent = new Intent(context, AssignmentActivity.class);
					intent.putExtra("id", entity.getId());//// 流程步骤id
					intent.putExtra("planInfoId", planInfoId);
					intent.putExtra("entity", entity);
					context.startActivity(intent);
				}
			});
			String stepname = entity.getExecutePeople();
			String state = entity.getExecutePeopleType();
			if(entity.getOrderNum() != null && !("").equals(entity.getOrderNum()))
				mhHolder.numbertv.setText(entity.getParentProcessNumber() + entity.getOrderNum());
			else if(entity.getEditOrderNum() != null && !("").equals(entity.getEditOrderNum()))
				mhHolder.numbertv.setText(entity.getParentProcessNumber() + entity.getEditOrderNum());
			if (state.equals("0")) {
				stepname="无执行人";
				mhHolder.who_task.setVisibility(View.GONE);

			}else if (state.equals("1")) {
				mhHolder.who_task.setText("("+"A角"+")");
				mhHolder.who_task.setVisibility(View.VISIBLE);
			}else if (state.equals("2")) {
				mhHolder.who_task.setText("("+"B角"+")");
				mhHolder.who_task.setVisibility(View.VISIBLE);
			}
			else if (state.equals("3")) {
				mhHolder.who_task.setText("("+"C角"+")");
				mhHolder.who_task.setVisibility(View.VISIBLE);
			}
			else if (state.equals("4")) {
				mhHolder.who_task.setText("("+"被指派"+")");
				mhHolder.who_task.setVisibility(View.VISIBLE);
			}else {
				stepname="未知";
				mhHolder.who_task.setVisibility(View.GONE);
			}
			mhHolder.whodone_name.setText(stepname);
		}
		if(arraylist.get(position).getParentProcessStepId() == null || "".equals(arraylist.get(position).getParentProcessStepId()))
			mhHolder.iv.setVisibility(View.VISIBLE);
		else
			mhHolder.iv.setVisibility(View.GONE);
		if(position == 0)
		{
			mhHolder.v_top.setVisibility(View.INVISIBLE);
			mhHolder.v_bottom.setVisibility(View.VISIBLE);
			if(arraylist.size() > position + 1)
            {
                if(arraylist.get(position + 1).getParentProcessStepId() == null || "".equals(arraylist.get(position + 1).getParentProcessStepId()))
                    mhHolder.v_split.setVisibility(View.VISIBLE);
                else
                    mhHolder.v_split.setVisibility(View.GONE);
            }
		}
		else if(position == arraylist.size() - 1)
		{
			mhHolder.v_top.setVisibility(View.VISIBLE);
			mhHolder.v_bottom.setVisibility(View.INVISIBLE);
            mhHolder.v_split.setVisibility(View.GONE);
		}
		else
		{
			mhHolder.v_top.setVisibility(View.VISIBLE);
			mhHolder.v_bottom.setVisibility(View.VISIBLE);
            if(arraylist.size() > position + 1)
            {
                if(arraylist.get(position + 1).getParentProcessStepId() == null || "".equals(arraylist.get(position + 1).getParentProcessStepId()))
                    mhHolder.v_split.setVisibility(View.VISIBLE);
                else
                    mhHolder.v_split.setVisibility(View.GONE);
            }
		}
		return convertView;
	}

	public class ViewHolder {
		private TextView stepname;// 步骤名
		private TextView whodone_title;// 执行人文本
		private TextView whodone_name;// 执行人名字
		private TextView who_task;// 执行人任务
		private TextView assign;// 指派
		private TextView numbertv;
		private View v_top;
		private View v_bottom;
		private ImageView iv;
		private View v_split;

	}

}
