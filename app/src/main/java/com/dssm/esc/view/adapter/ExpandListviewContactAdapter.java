package com.dssm.esc.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;

import java.util.List;


/**
 * 通讯录，可扩展listview的适配器（不带checkbox）
 *
 */
public class ExpandListviewContactAdapter extends BaseExpandableListAdapter {
private	List<GroupEntity> groupList;
private	Context context;
	/** 1,应急通知接收详情;2,应急小组签到情况 */
private	String tag;
	public ExpandListviewContactAdapter(List<GroupEntity> groupList,
                                        Context context, String tag) {
		this.groupList = groupList;
		this.context = context;
		this.tag = tag;
		
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param dataList
	 */
	public void updateListView(List<GroupEntity> dataList,String tag) {
		this.groupList = dataList;
		notifyDataSetChanged();
		this.tag=tag;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.child_contact,
					null);
			cHolder.head = (ImageView) convertView.findViewById(R.id.child_contact_iv_head);
			cHolder.name = (TextView) convertView.findViewById(R.id.child_contact_tv_name);
			cHolder.sign_in = (TextView) convertView.findViewById(R.id.child_contact_tv_sign_in);
			convertView.setTag(cHolder);
		} else {
			cHolder = (childViewHolder) convertView.getTag();
		}
		if(centity.getSex()==null?false:centity.getSex().equals("女"))
			cHolder.head.setImageResource(R.drawable.woman_online);
		else
			cHolder.head.setImageResource(R.drawable.man_online);
		cHolder.name.setText(centity.getName());
		String signin = centity.getSignin();
		String notice = centity.getNoticeState();
		if (tag.equals("1")) {//接收情况
//			0:未通知 1：已通知
			if (notice.equals("1")) {//已通知
				notice="已接收";
				cHolder.sign_in.setTextColor(Color.GREEN);
				cHolder.sign_in.setTextSize(15);
			}else  {//未通知
				notice="未接收";
				cHolder.sign_in.setTextColor(Color.RED);
				cHolder.sign_in.setTextSize(15);
			}
			cHolder.sign_in.setText(notice);

		}else if (tag.equals("2")) {//签到情况
//			0:未签到 1：已签到

			if (signin.equals("1")) {//已签到
				signin="已签到";
				cHolder.sign_in.setTextColor(Color.GREEN);
				cHolder.sign_in.setTextSize(14);
			}else if (signin.equals("0")) {//未签到
				signin="未签到";
				cHolder.sign_in.setTextColor(Color.RED);
				cHolder.sign_in.setTextSize(14);
			}
			cHolder.sign_in.setText(signin);
		}
		
		return convertView;
	}

	class childViewHolder {
		private ImageView head;
		private TextView name;
		private TextView sign_in;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.group_contact,
					null);
			gHolder.group_tv = (TextView) convertView
					.findViewById(R.id.group_contact_tv_department);
			gHolder.group_img = (ImageView) convertView
					.findViewById(R.id.group_contact_iv_arrow);
			
			convertView.setTag(gHolder);
		} else {
			gHolder = (GropViewHolder) convertView.getTag();
		}
		gHolder.group_tv.setText(gentity.getGroupname());
		if (!isExpanded) {
			gHolder.group_img.setImageResource(R.drawable.arrow_right);
		} else {
			gHolder.group_img.setImageResource(R.drawable.arrow_down);
		}
		return convertView;
	}

	class GropViewHolder {
		private TextView group_tv;
		private ImageView group_img;
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
	
	
}
