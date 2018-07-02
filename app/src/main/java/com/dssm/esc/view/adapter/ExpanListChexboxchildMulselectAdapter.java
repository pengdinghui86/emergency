package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 协同通告可扩展listview适配器（子带checkbox，多选）
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-14
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class ExpanListChexboxchildMulselectAdapter extends
		BaseExpandableListAdapter {
	List<GroupEntity> groupList;
	// List<ChildEntity> childList;
	Context context;
	// 已选中的子列表项
	private List<String> checkedChildren = new ArrayList<String>();

	public ExpanListChexboxchildMulselectAdapter(List<GroupEntity> groupList,
			Context context) {
		this.groupList = groupList;
		// this.childList = childList;
		this.context = context;
		
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param dataList
	 */
	public void updateListView(List<GroupEntity> dataList) {
		this.groupList = dataList;
		notifyDataSetChanged();
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

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ChildEntity centity = getChild(groupPosition, childPosition);
		childViewHolder cHolder = null;
		if (convertView == null) {
			cHolder = new childViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.child_checkbox, null);
			cHolder.head = (ImageView) convertView.findViewById(R.id.iv_head);
			cHolder.name = (TextView) convertView.findViewById(R.id.name);
			cHolder.zhizhe = (TextView) convertView.findViewById(R.id.zhizhe);
			cHolder.phonenumber = (TextView) convertView
					.findViewById(R.id.phonenumber);
			cHolder.child_checkbox = (CheckBox) convertView
					.findViewById(R.id.child_chexkbox);
			convertView.setTag(cHolder);
		} else {
			cHolder = (childViewHolder) convertView.getTag();
		}
		if(centity.getSex()== null ? false : centity.getSex().equals("女"))
			cHolder.head.setImageResource(R.drawable.woman);
		else
			cHolder.head.setImageResource(R.drawable.man);
		cHolder.name.setText(centity.getName());
		cHolder.zhizhe.setText(centity.getZhiwei());
		cHolder.phonenumber.setText(centity.getPhoneNumber());
		final String childrenId = centity.getChild_id();
		cHolder.child_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							centity.setIsChecked(isChecked);
							if (!checkedChildren.contains(childrenId)) {
								checkedChildren.add(childrenId);
							}
						} else {
							checkedChildren.remove(childrenId);
							centity.setIsChecked(isChecked);
						}


						ExpanListChexboxchildMulselectAdapter.this
								.notifyDataSetChanged();
					}
				});

		if (checkedChildren.contains(childrenId)) {
			cHolder.child_checkbox.setChecked(true);
		} else {
			cHolder.child_checkbox.setChecked(false);
		}

		return convertView;
	}

	final static class childViewHolder {
		private ImageView head;
		private TextView name;
		private TextView zhizhe;
		private TextView phonenumber;
		CheckBox child_checkbox;
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	

	public List<String> getCheckedRecords() {
		return checkedChildren;
	}

	public List<String> getCheckedChildren() {
		return checkedChildren;
	}

}
