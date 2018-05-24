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
 * 人员签到，可扩展listview的适配器（不带checkbox）
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co., Ltd. Inc. All rights reserved.
 */
public class ExpanListvSignInAdapter extends BaseExpandableListAdapter {
private	List<GroupEntity> groupList;
private	Context context;
	/** 1,应急通知接收详情;2,应急小组签到情况 */
private	String tag;
	public ExpanListvSignInAdapter(List<GroupEntity> groupList,
		 Context context,String tag) {
		this.groupList = groupList;
		//this.childList = childList;
		this.context = context;
		this.tag=tag;
		
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
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
			convertView = LayoutInflater.from(context).inflate(R.layout.child_sigin,
					null);
			cHolder.head = (ImageView) convertView.findViewById(R.id.iv_head);
			cHolder.name = (TextView) convertView.findViewById(R.id.name);
			cHolder.zhizhe = (TextView) convertView.findViewById(R.id.zhizhe);
			cHolder.phonenumber = (TextView) convertView
					.findViewById(R.id.phonenumber);
			cHolder.sigin_tv=(TextView) convertView.findViewById(R.id.sigin_tv);
			convertView.setTag(cHolder);
		} else {
			cHolder = (childViewHolder) convertView.getTag();
		}
		if(centity.getSex()==null?false:centity.getSex().equals("女"))
			cHolder.head.setImageResource(R.drawable.woman);
		else
			cHolder.head.setImageResource(R.drawable.man);
		cHolder.name.setText(centity.getName());
		cHolder.zhizhe.setText(centity.getZhiwei());
		cHolder.phonenumber.setText(centity.getPhoneNumber());
		String signin = centity.getSignin();
		String notice= centity.getNoticeState();
		if (tag.equals("1")) {//接收情况
//			0:未通知 1：已通知
			if (notice.equals("1")) {//已通知
				notice="已接收";
				cHolder.sigin_tv.setTextColor(Color.GREEN);
				cHolder.sigin_tv.setTextSize(15);
			}else  {//未通知
				notice="未接收";
				cHolder.sigin_tv.setTextColor(Color.RED);
				cHolder.sigin_tv.setTextSize(15);
			}
			cHolder.sigin_tv.setText(notice);

		}else if (tag.equals("2")) {//签到情况
//			0:未签到 1：已签到

			if (signin.equals("1")) {//已签到
				signin="已签到";
				cHolder.sigin_tv.setTextColor(Color.GREEN);
				cHolder.sigin_tv.setTextSize(14);
			}else if (signin.equals("0")) {//未签到
				signin="未签到";
				cHolder.sigin_tv.setTextColor(Color.RED);
				cHolder.sigin_tv.setTextSize(14);
			}
			cHolder.sigin_tv.setText(signin);
		}
		
		return convertView;
	}

	class childViewHolder {
		private ImageView head;
		private TextView name;
		private TextView zhizhe;
		private TextView phonenumber;
		private TextView sigin_tv;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.group_sigin,
					null);
			gHolder.group_tv = (TextView) convertView
					.findViewById(R.id.group_tv);
			gHolder.gropimg = (ImageView) convertView
					.findViewById(R.id.gropimg);
			
			convertView.setTag(gHolder);
		} else {
			gHolder = (GropViewHolder) convertView.getTag();
		}
		gHolder.group_tv.setText(gentity.getGroupname());
		if (!isExpanded) {
			gHolder.gropimg.setImageResource(R.drawable.rightlist);
		} else {
			gHolder.gropimg.setImageResource(R.drawable.expandlist);
		}
		return convertView;
	}

	class GropViewHolder {
		private TextView group_tv;
		private ImageView gropimg;
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
