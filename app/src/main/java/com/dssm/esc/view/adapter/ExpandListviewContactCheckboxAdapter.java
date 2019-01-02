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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandListviewContactCheckboxAdapter extends BaseExpandableListAdapter {
	List<GroupEntity> groupList;
	Context context;
	public ArrayList<String> checkedChildren = new ArrayList<String>();
	public Map<String, Integer> groupCheckedStateMap = new HashMap<String, Integer>();
	public List<String> selectId = new ArrayList<String>();
	public List<String> selectName = new ArrayList<String>();
	public ExpandListviewContactCheckboxAdapter(List<GroupEntity> groupList,
                                                Context context) {
		this.groupList = groupList;
		this.context = context;
		int groupCount = getGroupCount();
		for (int groupPosition = 0; groupPosition < groupCount; groupPosition++) {
			try {
				GroupEntity groupItem = groupList.get(groupPosition);
				if (groupItem == null || groupItem.getcList() == null
						|| groupItem.getcList().isEmpty()) {
					groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
					continue;
				}
				groupCheckedStateMap.put(groupItem.getGroup_id(), 3);

			} catch (Exception e) {

			}
		}
	}

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
					R.layout.child_contact_checkbox, null);
			cHolder.head = (ImageView) convertView.findViewById(R.id.child_contact_checkbox_iv_head);
			cHolder.name = (TextView) convertView.findViewById(R.id.child_contact_checkbox_tv_name);
			cHolder.child_checkbox = (CheckBox) convertView
					.findViewById(R.id.child_contact_cb);
			convertView.setTag(cHolder);
		} else {
			cHolder = (childViewHolder) convertView.getTag();
		}
		if(centity.getSex()==null?false:centity.getSex().equals("女"))
			cHolder.head.setImageResource(R.drawable.woman_online);
		else
			cHolder.head.setImageResource(R.drawable.man_online);
		cHolder.name.setText(centity.getName());
		
		final String childrenId = centity.getChild_id();
		final String onlyId=centity.getOnlyId();
		final String pid=centity.getpId();
        final String name = centity.getName();
		cHolder.child_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							centity.setIsChecked(isChecked);
							if (!checkedChildren.contains(pid+onlyId+childrenId)) {
								selectId.add(childrenId);
								selectName.add(name);
								checkedChildren.add(pid+onlyId+childrenId);
							}
						} else {
							selectId.remove(childrenId);
                            selectName.remove(name);
							checkedChildren.remove(pid+onlyId+childrenId);
							centity.setIsChecked(isChecked);
						}
						setGroupItemCheckedState(groupList.get(groupPosition));

						ExpandListviewContactCheckboxAdapter.this
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
		private ImageView head;
		private TextView name;
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
		GroupViewHolder gHolder = null;
		if (convertView == null) {
			gHolder = new GroupViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.group_contact_checkbox, null);
			gHolder.group_contact_rl = (RelativeLayout) convertView
					.findViewById(R.id.group_contact_checkbox_rl);
			gHolder.group_tv = (TextView) convertView
					.findViewById(R.id.group_contact_checkbox_tv_department);
			gHolder.groupCBImg = (ImageView) convertView
					.findViewById(R.id.group_contact_checkbox_iv_checkbox);
			gHolder.groupCBLayout = (LinearLayout) convertView
					.findViewById(R.id.cb_layout);
			gHolder.group_img = (ImageView) convertView
					.findViewById(R.id.group_contact_checkbox_iv_arrow);
			convertView.setTag(gHolder);
		} else {
			gHolder = (GroupViewHolder) convertView.getTag();
		}
		gHolder.group_tv.setText(gentity.getGroupname() + "（" + gentity.getcList().size() + "）");
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
			gHolder.group_contact_rl.setBackgroundResource(R.color.white);
			gHolder.group_img.animate().rotation(0).setDuration(200).start();
			gHolder.group_tv.setPadding(8, 20, 0, 20);
			gHolder.group_tv.setTextSize(16);
			gHolder.group_tv.setTextColor(context.getResources().getColor(R.color.colorWeFontBlack));
		} else {
			gHolder.group_contact_rl.setBackgroundResource(R.color.colorWeWindowGrayBackground);
			gHolder.group_img.animate().rotation(90).setDuration(200).start();
			gHolder.group_tv.setPadding(8, 8, 0, 8);
			gHolder.group_tv.setTextSize(13);
			gHolder.group_tv.setTextColor(context.getResources().getColor(R.color.colorWeFontGray_9));
		}
		return convertView;
	}

	final static class GroupViewHolder {
		private TextView group_tv;
		ImageView groupCBImg;
		LinearLayout groupCBLayout;
		private ImageView group_img;
		private RelativeLayout group_contact_rl;
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

			for (ChildEntity childrenItem : childrenItems) {
				String holderKey = childrenItem.getChild_id();
				String holderKey1 = childrenItem.getOnlyId();
				String holderKey2 = childrenItem.getpId();
				String name = childrenItem.getName();
				if (checked) {
					if (!checkedChildren.contains(holderKey2+holderKey1+holderKey)) {
						checkedChildren.add(holderKey2+holderKey1+holderKey);
						selectId.add(holderKey);
						selectName.add(name);
					}
				} else {
					selectId.remove(holderKey);
					selectName.remove(name);
					checkedChildren.remove(holderKey2+holderKey1+holderKey);
				}
			}

			ExpandListviewContactCheckboxAdapter.this.notifyDataSetChanged();
		}
	}

	public ArrayList<String> getCheckedRecords()
	{
		return checkedChildren;
	}
	public List<String> getCheckedChildren()
	{
		return Utils.getInstance().removeDuplicate(selectId);
	}
	public List<String> getCheckedChildrenName()
	{
		return Utils.getInstance().removeDuplicate(selectName);
	}
	public void clearSelectedItems()
	{
		checkedChildren.clear();
		selectId.clear();
	}
}
