package com.dssm.esc.view.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.view.activity.AssignmentActivity;

import java.util.HashMap;
import java.util.List;


/**
 * 应急小组，可扩展listview的适配器（子带checkbox,单选）
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-12
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class ExpanListCheckboxAdapter extends BaseExpandableListAdapter {
	private AssignmentActivity context = null;
	/** 父list显示组 */
	private static List<GroupEntity> groupList;
	private static HashMap<String, Boolean> statusHashMap;

	public ExpanListCheckboxAdapter(AssignmentActivity context,
			HashMap<String, Boolean> statusHashMap, List<GroupEntity> groupList) {
		this.context = context;
		this.statusHashMap = statusHashMap;
		this.groupList = groupList;
		setCheck();
	}

	public static void setCheck() {
		if (groupList != null) {
			for (int i = 0; i < groupList.size(); i++) {// 初始时,让所有的子选项均未被选中
				for (int a = 0; a < groupList.get(i).getcList().size(); a++) {
					String idString = groupList.get(i).getcList().get(a)
							.getOnlyId()
							+ groupList.get(i).getcList().get(a).getChild_id();
					statusHashMap.put(idString, false);
				}
			}
		}
	}

	@Override
	public ChildEntity getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		final GroupEntity groupItem = groupList.get(groupPosition);
		if (groupItem == null || groupItem.getcList() == null
				|| groupItem.getcList().isEmpty()) {
			return null;
		}
		return groupItem.getcList().get(childPosition);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		final GroupEntity groupItem = groupList.get(groupPosition);
		if (groupItem == null || groupItem.getcList() == null
				|| groupItem.getcList().isEmpty()) {
			return 0;
		}
		return groupItem.getcList().size();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	// 点击事件发生后:先执行事件监听,然后调用此getChildView()
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {// 返回子项组件
		final ChildEntity centity = getChild(groupPosition, childPosition);
		// ChildViewHolder viewHolder =null;
		convertView = LayoutInflater.from(context).inflate(
				R.layout.child_checkbox_sigin, null);
		ImageView head = (ImageView) convertView.findViewById(R.id.iv_head);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView zhizhe = (TextView) convertView.findViewById(R.id.zhizhe);
		TextView phonenumber = (TextView) convertView
				.findViewById(R.id.phonenumber);
		TextView signintv = (TextView) convertView.findViewById(R.id.signin);
		CheckBox child_checkbox = (CheckBox) convertView
				.findViewById(R.id.child_chexkbox);
//		TextView textView2 = (TextView) convertView.findViewById(R.id.tv);
//		RelativeLayout child_ll = (RelativeLayout) convertView
//				.findViewById(R.id.child_ll);
		/*
		 * if (convertView == null) {// 第一次的时候convertView是空,所以要生成convertView
		 * viewHolder= new ChildViewHolder(); convertView =
		 * LayoutInflater.from(context).inflate( R.layout.child_checkbox_sigin,
		 * null); viewHolder.name = (TextView)
		 * convertView.findViewById(R.id.name); viewHolder.zhizhe = (TextView)
		 * convertView.findViewById(R.id.zhizhe); viewHolder.phonenumber =
		 * (TextView) convertView .findViewById(R.id.phonenumber);
		 * viewHolder.signintv = (TextView)
		 * convertView.findViewById(R.id.signin); viewHolder.child_checkbox =
		 * (CheckBox) convertView .findViewById(R.id.child_chexkbox);
		 * viewHolder.textView2 = (TextView) convertView .findViewById(R.id.tv);
		 * viewHolder.child_ll = (RelativeLayout) convertView
		 * .findViewById(R.id.child_ll);
		 * 
		 * convertView.setTag(viewHolder); }else { viewHolder =
		 * (ChildViewHolder) convertView.getTag(); }
		 */
		if(centity.getSex()== null ? false : centity.getSex().equals("女"))
			head.setImageResource(R.drawable.woman);
		else
			head.setImageResource(R.drawable.man);
		name.setText(centity.getName());
		zhizhe.setText(centity.getZhiwei());
		phonenumber.setText(centity.getPhoneNumber());

		String sign = centity.getSignin();
		final String child_id = centity.getChild_id();
		final String onlyId = centity.getOnlyId();
//		textView2.setText(onlyId + child_id);
//		textView2.setVisibility(View.GONE);
		if (sign.equals("0")) {// 未签到
			sign = "未签到";
			signintv.setTextColor(Color.RED);
			// viewHolder.child_checkbox.setClickable(false);
			child_checkbox.setVisibility(View.INVISIBLE);
		} else if (sign.equals("1")) {// 已签到
			sign = "已签到";
			child_checkbox.setVisibility(View.VISIBLE);
			signintv.setTextColor(Color.GREEN);
			// child_checkbox.setClickable(true);
			Boolean nowStatus = statusHashMap.get(onlyId + child_id);// 当前状态
			child_checkbox.setChecked(nowStatus);
		}
		// int gropposition =groupPosition;

		signintv.setText(sign);
		child_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					int x = groupPosition;
					int y = childPosition;

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stuch
						Log.i("id", getChild(x, y).getOnlyId()
								+ getChild(x, y).getChild_id());

						Log.i("===", arg1 + "");
						setCheck();

						statusHashMap.put(getChild(x, y).getOnlyId()
								+ getChild(x, y).getChild_id(), arg1);
						Log.i("应急选的id", getChild(x, y).getChild_id());
						Log.i("应急选的name", getChild(x, y).getName());
						ExpanListCheckboxAdapter.this.notifyDataSetChanged();
						ListviewCheckboxAdapter.setchek();
					}
				});

		return convertView;
	}

	class ChildViewHolder {
		private CheckBox child_checkbox;
		private TextView name, zhizhe, phonenumber, textView2, signintv;
		private RelativeLayout child_ll;
	}

	@Override
	public GroupEntity getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		if (groupList == null) {
			return null;
		}
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if (groupList == null) {
			return 0;
		}
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	// public TextView buildTextView() { // 自定义方法，建立文本.用于显示组
	// AbsListView.LayoutParams param = new AbsListView.LayoutParams(
	// ViewGroup.LayoutParams.FILL_PARENT, 60); // 指定布局参数
	// TextView textView = new TextView(this.context); // 创建TextView
	// textView.setLayoutParams(param); // 设置布局参数
	// textView.setTextSize(14.0f); // 设置文字大小
	// textView.setGravity(Gravity.LEFT); // 左对齐
	// textView.setPadding(40, 8, 3, 3); // 间距
	// return textView; // 返回组件
	// }
	// @Override
	// public View getGroupView(int groupPosition, boolean isExpanded,
	// View convertView, ViewGroup parent) {// 取得组显示组件
	// TextView textView = buildTextView(); // 建立组件
	// textView.setText(this.getGroup(groupPosition).getGroupname()); // 设置文字
	// return textView;
	// }

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupEntity gentity = getGroup(groupPosition);
		GropViewHolder gHolder = null;
		if (convertView == null) {
			gHolder = new GropViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.group_sigin, null);
			gHolder.group_tv = (TextView) convertView
					.findViewById(R.id.group_tv);
			convertView.setTag(gHolder);
		} else {
			gHolder = (GropViewHolder) convertView.getTag();
		}
		gHolder.group_tv.setText(gentity.getGroupname());

		return convertView;
	}

	class GropViewHolder {
		private TextView group_tv;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void notifyDataSetChanged() {// 通知数据发生变化
		super.notifyDataSetChanged();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}