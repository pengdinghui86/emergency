package com.dssm.esc.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.view.activity.AssignmentActivity;

import java.util.List;


/**
 * 预案执行步骤适配器
 */
public class PlanExecuteListvAdapter extends BaseAdapter {

	private List<ChildEntity> arraylist;
	private Context context;
	private String planInfoId;
	private String planStatus;
	public PlanExecuteListvAdapter(String planInfoId, Context context,
                                   List<ChildEntity> list, String planStatus) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arraylist = list;
		this.planInfoId = planInfoId;
		this.planStatus = planStatus;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public ChildEntity getItem(int position) {
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

		final ChildEntity entity = getItem(position);
		final ViewHolder mhHolder;
		if (convertView == null) {
			mhHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_plan_execute, null);

			mhHolder.stepname = (TextView) convertView
					.findViewById(R.id.stepname);
			mhHolder.whodone_title = (TextView) convertView
					.findViewById(R.id.tv);
			mhHolder.whodone_name = (TextView) convertView
					.findViewById(R.id.whodone_name);
			mhHolder.status = (TextView) convertView
					.findViewById(R.id.item_plan_execute_tv_status);
			mhHolder.execute = (TextView) convertView.findViewById(R.id.item_plan_execute_tv_execute);
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
			mhHolder.execute.setVisibility(View.INVISIBLE);
			mhHolder.status.setVisibility(View.GONE);
			mhHolder.whodone_name.setVisibility(View.GONE);
			mhHolder.whodone_title.setVisibility(View.GONE);
			mhHolder.stepname.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
			if(entity.getOrderNumber() != null && !("").equals(entity.getOrderNumber()))
				mhHolder.numbertv.setText(entity.getParentOrderNumber() + entity.getOrderNumber());
		}
		else {
			mhHolder.status.setVisibility(View.VISIBLE);
			mhHolder.whodone_name.setVisibility(View.VISIBLE);
			mhHolder.whodone_title.setVisibility(View.VISIBLE);
			mhHolder.stepname.setGravity(Gravity.LEFT|Gravity.TOP);
			mhHolder.execute.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//mhHolder.assign.setBackgroundResource(R.drawable.btbg_gray);
					Intent intent = new Intent(context, AssignmentActivity.class);
					intent.putExtra("id", entity.getChild_id());//// 流程步骤id
					intent.putExtra("planInfoId", planInfoId);
					intent.putExtra("entity", entity);
					context.startActivity(intent);
				}
			});
			String stepname = entity.getExecutorName();
			String state = entity.getStatus();
			if(entity.getOrderNumber() != null && !("").equals(entity.getOrderNumber()))
				mhHolder.numbertv.setText(entity.getParentOrderNumber() + entity.getOrderNumber());
			if (state.equals("0")) {
				stepname="无执行人";
			}
			mhHolder.whodone_name.setText(stepname);
		}
		if(entity.getParentProcessStepId() == null || "".equals(entity.getParentProcessStepId()))
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

		mhHolder.stepname.setText(entity.getProcessName());
		// （全部完成：1，部分完成：2，跳过：3，正在执行：4，可执行：5,准备执行：6，还未执行：7）
		String status = entity.getStatus();

		if (status.equals("1") || status.equals("2") || status.equals("3")) {//
			mhHolder.status.setText("已执行");
			/**
			 * 新增颜色节点
			 * 2017/10/13
			 */
			if (!TextUtils.isEmpty(entity.getCode())){
				mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
			}else {
				mhHolder.status.setTextColor(context.getResources().getColor(R.color.green_b));
			}
		} else if (status.equals("4")) {
			mhHolder.status.setText("执行中");// 切换按钮
			if (!TextUtils.isEmpty(entity.getCode())){
				mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
			}else {
				mhHolder.status.setTextColor(context.getResources().getColor(R.color.yellow_dot));
			}
		} else if (status.equals("5")) {
			mhHolder.status.setText("可执行");// 有执行按钮
			if (!TextUtils.isEmpty(entity.getCode())){
				mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
			}else {
				mhHolder.status.setTextColor(context.getResources().getColor(R.color.blue_dot));
			}
		} else if (status.equals("6")) {
			mhHolder.status.setText("准备执行");
			if (!TextUtils.isEmpty(entity.getCode())){
				mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
			}else {
				mhHolder.status.setTextColor(context.getResources().getColor(R.color.green_a));
			}
		} else if (status.equals("7")) {
			mhHolder.status.setText("未执行");
			if (!TextUtils.isEmpty(entity.getCode())){
				mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
			}else {
				mhHolder.status.setTextColor(Color.RED);
			}
		}

		/**
		 * 新增
		 * 2017/10/13
		 */
		if (10 < Integer.parseInt(status) && Integer.parseInt(status) < 20) {
			mhHolder.status.setText("暂停");
			if (!TextUtils.isEmpty(entity.getCode())){
				mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
			}else {
				mhHolder.status.setTextColor(Color.RED);
			}
		}

		switch (Integer.parseInt(status)) {
			/**
			 * 新增
			 * 2017/10/12
			 */
			case 8:
				mhHolder.status.setText("自动执行中");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;

			case 9:
				mhHolder.status.setText("已执行");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;

			case 10:
				mhHolder.status.setText("执行失败");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;

			case 20:
				mhHolder.status.setText("接收超时");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;

			case 21:
				mhHolder.status.setText("执行异常");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;

			case 22:
				mhHolder.status.setText("异常解除");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;

			case 23:
				mhHolder.status.setText("跳过");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;

			case 24:
				mhHolder.status.setText("跳过");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;

			case 25:
				mhHolder.status.setText("执行超时");
				if (!TextUtils.isEmpty(entity.getCode())){
					mhHolder.status.setTextColor(Color.parseColor(entity.getCode()));
				}else {
					mhHolder.status.setTextColor(Color.RED);
				}
				break;
		}
		//2018.7.13新增，预案处于待启动或已启动或已授权的情况下，更改流程状态显示为流程未启动
		if(planStatus.equals("0") || planStatus.equals("2") || planStatus.equals("1"))
			mhHolder.status.setText("流程未启动");

		//子预案节点
		if(entity.getNodeStepType().equals("CallActivity"))
			mhHolder.iv.setVisibility(View.VISIBLE);
		else
			mhHolder.iv.setVisibility(View.GONE);

		return convertView;
	}

	public class ViewHolder {
		private TextView stepname;// 步骤名
		private TextView whodone_title;// 执行人文本
		private TextView whodone_name;// 执行人名字
		private TextView status;// 步骤状态
		private TextView execute;// 执行按钮
		private TextView numbertv;
		private View v_top;
		private View v_bottom;
		private ImageView iv;
		private View v_split;

	}

}
