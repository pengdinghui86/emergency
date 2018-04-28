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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 三级列表checkbox多选适配器
 * @author zsj
 *
 */
public class ThreeListCheckboxMulAdapter extends BaseExpandableListAdapter {
	List<GroupEntity> groupList;
	//List<ChildEntity> childList;
	Context context;
	// 已选中的子列表项
	private List<String> checkedChildren = new ArrayList<String>();
	// 父列表项的选中状态：value值为1（选中）、2（部分选中）、3（未选中）
	private Map<String, Integer> groupCheckedStateMap = new HashMap<String, Integer>();
	public ThreeListCheckboxMulAdapter(List<GroupEntity> groupList,
		 Context context) {
		this.groupList = groupList;
		//this.childList = childList;
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

				//默认设置所有的父列表项和子列表项都为选中状态
//				groupCheckedStateMap.put(groupItem.getId(), 1);
//				List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
//				for (ChildrenItem childrenItem : childrenItems) {
//					checkedChildren.add(childrenItem.getId());
//					//checkedChildren.add(childrenItem.getName());
//				}
				//默认设置所有的父列表项和子列表项都为未选中状态
				groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
				/*List<ChildEntity> childrenItems = groupItem.getcList();
				for (ChildEntity childrenItem : childrenItems) {
					checkedChildren.add("");
					//checkedChildren.add(childrenItem.getName());
				}*/
			} catch (Exception e) {

			}
		}
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
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
		if (groupItem== null || groupItem.getcList()==null
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
			convertView = LayoutInflater.from(context).inflate(R.layout.child_checkbox,
					null);
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

						setGroupItemCheckedState(groupList.get(groupPosition));

						ThreeListCheckboxMulAdapter.this.notifyDataSetChanged();
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
		private TextView name;
		private TextView zhizhe;
		private TextView phonenumber;
		CheckBox child_checkbox;
	}

	@Override
	public GroupEntity getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		if (groupList==null) {
			return null;
		}
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if (groupList==null) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.group_checkbox,
					null);
			gHolder.group_tv = (TextView) convertView
					.findViewById(R.id.group_tv);
			
			gHolder.groupCBImg = (ImageView) convertView.findViewById(R.id.group_cb_img);
			gHolder.groupCBLayout = (LinearLayout) convertView.findViewById(R.id.cb_layout);
			convertView.setTag(gHolder);
		} else {
			gHolder = (GropViewHolder) convertView.getTag();
		}
		gHolder.group_tv.setText(gentity.getGroupname());
		gHolder.groupCBLayout.setOnClickListener(new GroupCBLayoutOnClickListener(gentity));
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
		return convertView;
	}

	final static class GropViewHolder {
		private TextView group_tv;
		ImageView groupCBImg;
		LinearLayout groupCBLayout;
	//	private CheckBox group_checkBox;
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
	private void setGroupItemCheckedState(GroupEntity groupItem){
		List<ChildEntity> childrenItems = groupItem.getcList();
		if (childrenItems==null || childrenItems.isEmpty()) {
			groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
			return;
		}
		
		int  checkedCount = 0;
		for (ChildEntity childrenItem : childrenItems) {
			if (checkedChildren.contains(childrenItem.getChild_id())) {
				checkedCount ++;
			}
		}
		int state = 1;
		if (checkedCount==0) {
			state = 3;
		}else if (checkedCount==childrenItems.size()) {
			state = 1;
		}else {
			state = 2;
		}
		
		groupCheckedStateMap.put(groupItem.getGroup_id(), state);
	}
	public class GroupCBLayoutOnClickListener implements OnClickListener{

		private GroupEntity groupItem;
		
		public GroupCBLayoutOnClickListener(GroupEntity groupItem){
			this.groupItem = groupItem;
		}
		
		@Override
		public void onClick(View v) {
			List<ChildEntity> childrenItems = groupItem.getcList();
			if (childrenItems==null || childrenItems.isEmpty()) {
				groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
				return;
			}
			int  checkedCount = 0;
			for (ChildEntity childrenItem : childrenItems) {
				if (checkedChildren.contains(childrenItem.getChild_id())) {
					checkedCount ++;
				}
			}
			
			boolean checked = false;
			if (checkedCount==childrenItems.size()) {
				checked = false;
				groupCheckedStateMap.put(groupItem.getGroup_id(), 3);
			}else{
				checked = true;
				groupCheckedStateMap.put(groupItem.getGroup_id(), 1);
			}
			
			for (ChildEntity childrenItem : childrenItems) {
				String holderKey = childrenItem.getChild_id();
				if (checked) {
					if (!checkedChildren.contains(holderKey)) {
						checkedChildren.add(holderKey);
					}
				}else {
					checkedChildren.remove(holderKey);
				}
			}
			
			ThreeListCheckboxMulAdapter.this.notifyDataSetChanged();
		}
	}


	public List<String> getCheckedRecords() {
		return checkedChildren;
	}


	public List<String> getCheckedChildren() {
		return checkedChildren;
	}
	
}
