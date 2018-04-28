package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ͨѶ¼������չlistview�������������Ӷ���checkbox,��ѡ��
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-8
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class ExpanListChexboxMulselectAdapter extends BaseExpandableListAdapter {
	List<GroupEntity> groupList;
	// List<ChildEntity> childList;
	Context context;
	// ��ѡ�е����б���
	public ArrayList<String> checkedChildren = new ArrayList<String>();
	// ���б����ѡ��״̬��valueֵΪ1��ѡ�У���2������ѡ�У���3��δѡ�У�
	public Map<String, Integer> groupCheckedStateMap = new HashMap<String, Integer>();
	/** ��ѡ�е���Ա��id */
	public List<String> selectId = new ArrayList<String>();
	public ExpanListChexboxMulselectAdapter(List<GroupEntity> groupList,
			Context context) {
		this.groupList = groupList;
		// this.childList = childList;
		this.context = context;
		int groupCount = getGroupCount();
		for (int groupPosition = 0; groupPosition < groupCount; groupPosition++) {
			try {
				GroupEntity groupItem = groupList.get(groupPosition);
				if (groupItem == null || groupItem.getcList() == null
						|| groupItem.getcList().isEmpty()) {
					groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
//					for (int i = 0; i < groupItem.getcList().size(); i++) {
//						ChildEntity childEntity = groupItem.getcList().get(i);
//						childEntity.setIsChecked(false);
//					}
					continue;
				}

				// Ĭ���������еĸ��б�������б��Ϊѡ��״̬
				// groupCheckedStateMap.put(groupItem.getId(), 1);
				// List<ChildrenItem> childrenItems =
				// groupItem.getChildrenItems();
				// for (ChildrenItem childrenItem : childrenItems) {
				// checkedChildren.add(childrenItem.getId());
				// //checkedChildren.add(childrenItem.getName());
				// }
				// Ĭ���������еĸ��б�������б��Ϊδѡ��״̬
				groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
				/*
				 * List<ChildEntity> childrenItems = groupItem.getcList(); for
				 * (ChildEntity childrenItem : childrenItems) {
				 * checkedChildren.add("");
				 * //checkedChildren.add(childrenItem.getName()); }
				 */
			} catch (Exception e) {

			}
		}
	}

	/**
	 * ��ListView���ݷ����仯ʱ,���ô˷���������ListView
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
		cHolder.name.setText(centity.getName());
		cHolder.zhizhe.setText(centity.getZhiwei());
		cHolder.phonenumber.setText(centity.getPhoneNumber());
		
		final String childrenId = centity.getChild_id();
		final String onlyId=centity.getOnlyId();
		final String pid=centity.getpId();
		
//		Boolean isChecked = centity.getIsChecked();
//		if (isChecked) {
//			selectId.add(childrenId);
//			cHolder.child_checkbox.setChecked(isChecked);
//		}else {
//			cHolder.child_checkbox.setChecked(isChecked);
//		}
		
		cHolder.child_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							centity.setIsChecked(isChecked);
							if (!checkedChildren.contains(pid+onlyId+childrenId)) {
								selectId.add(childrenId);
								checkedChildren.add(pid+onlyId+childrenId);
							}
						} else {
							selectId.remove(childrenId);
							checkedChildren.remove(pid+onlyId+childrenId);
							centity.setIsChecked(isChecked);
						}
						setGroupItemCheckedState(groupList.get(groupPosition));

						ExpanListChexboxMulselectAdapter.this
								.notifyDataSetChanged();
					}
				});
		if (checkedChildren.contains(pid+onlyId+childrenId)) {
			cHolder.child_checkbox.setChecked(true);
		} else {
			cHolder.child_checkbox.setChecked(false);
		}
		return convertView;
	}

	final static class childViewHolder {
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
					R.layout.group_checkbox, null);
			gHolder.group_tv = (TextView) convertView
					.findViewById(R.id.group_tv);
			gHolder.groupCBImg = (ImageView) convertView
					.findViewById(R.id.group_cb_img);
			gHolder.groupCBLayout = (LinearLayout) convertView
					.findViewById(R.id.cb_layout);
			gHolder.gropimg = (ImageView) convertView
					.findViewById(R.id.gropimg);
			convertView.setTag(gHolder);
		} else {
			gHolder = (GropViewHolder) convertView.getTag();
		}
		gHolder.group_tv.setText(gentity.getGroupname());
		gHolder.groupCBLayout
				.setOnClickListener(new GroupCBLayoutOnClickListener(gentity));
		if(!groupCheckedStateMap.containsKey(gentity.getGroup_id()))
			return convertView;
		int state = groupCheckedStateMap.get(gentity.getGroup_id());
		switch (state) {
		case 1:
			gHolder.groupCBImg.setImageResource(R.drawable.ck_checked);
			gentity.setIschecked(true);
			break;
		case 2:
			gHolder.groupCBImg.setImageResource(R.drawable.ck_partial_checked);
			gentity.setIschecked(true);
			break;
		case 3:
			gHolder.groupCBImg.setImageResource(R.drawable.ck_unchecked);
			gentity.setIschecked(false);
			break;
		default:
			break;
		}
		if (!isExpanded) {
			gHolder.gropimg.setImageResource(R.drawable.rightlist);
		} else {
			gHolder.gropimg.setImageResource(R.drawable.expandlist);
		}
		return convertView;
	}

	final static class GropViewHolder {
		private TextView group_tv;
		ImageView groupCBImg;
		LinearLayout groupCBLayout;
		private ImageView gropimg;
		// private CheckBox group_checkBox;
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

	public void setGroupItemCheckedState(GroupEntity groupItem) {
		List<ChildEntity> childrenItems = groupItem.getcList();
		if (childrenItems == null || childrenItems.isEmpty()) {
			groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
			return;
		}

		int checkedCount = 0;
//		for (ChildEntity childrenItem : childrenItems) {
//			if (checkedChildren.contains(childrenItem.getChild_id())) {
//				checkedCount++;
//			}
//		}
		for (ChildEntity childrenItem : childrenItems) {
			if (checkedChildren.contains(childrenItem.getpId()+childrenItem.getOnlyId()+childrenItem.getChild_id())) {
				checkedCount++;
			}
		}
		int state = 1;
		if (checkedCount == 0) {
			state = 3;
		} else if (checkedCount == childrenItems.size()) {
			state = 1;
		} else {
			state = 2;
		}

		groupCheckedStateMap.put(groupItem.getGroup_id(), state);
	}

	public class GroupCBLayoutOnClickListener implements OnClickListener {

		private GroupEntity groupItem;

		public GroupCBLayoutOnClickListener(GroupEntity groupItem) {
			this.groupItem = groupItem;
		}

		@Override
		public void onClick(View v) {
			List<ChildEntity> childrenItems = groupItem.getcList();
			if (childrenItems == null || childrenItems.isEmpty()) {
				groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
				return;
			}
			int checkedCount = 0;
//			for (ChildEntity childrenItem : childrenItems) {
//				if (checkedChildren.contains(childrenItem.getChild_id())) {
//					checkedCount++;
//				}
//			}
			for (ChildEntity childrenItem : childrenItems) {
				if (checkedChildren.contains(childrenItem.getpId()+childrenItem.getOnlyId()+childrenItem.getChild_id())) {
					checkedCount++;
				}
			}
			boolean checked = false;
			if (checkedCount == childrenItems.size()) {
				checked = false;
				groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
			} else {
				checked = true;
				groupCheckedStateMap.put(groupItem.getGroup_id(), 1);
			}

//			for (ChildEntity childrenItem : childrenItems) {
//				String holderKey = childrenItem.getChild_id();
//				if (checked) {
//					if (!checkedChildren.contains(holderKey)) {
//						checkedChildren.add(holderKey);
//					}
//				} else {
//					checkedChildren.remove(holderKey);
//				}
//			}
			for (ChildEntity childrenItem : childrenItems) {
				String holderKey = childrenItem.getChild_id();
				String holderKey1 = childrenItem.getOnlyId();
				String holderKey2 = childrenItem.getpId();
				if (checked) {
					if (!checkedChildren.contains(holderKey2+holderKey1+holderKey)) {
						checkedChildren.add(holderKey2+holderKey1+holderKey);
						selectId.add(holderKey);
					}
				} else {
					selectId.remove(holderKey);
					checkedChildren.remove(holderKey2+holderKey1+holderKey);
				}
			}

			ExpanListChexboxMulselectAdapter.this.notifyDataSetChanged();
		}
	}

	public ArrayList<String> getCheckedRecords() {
		return checkedChildren;
	}

//	public ArrayList<String> getCheckedChildren() {
//		return checkedChildren;
//	}
	public List<String> getCheckedChildren() {
		return Utils.getInstance().removeDuplicate(selectId);
	}

	public void clearSelectedItems() {
		checkedChildren.clear();
		selectId.clear();
	}
}
