package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;

/**
 * 通讯录分组实体类（通讯录，签到，预案执行的父实体类）
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-8
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class GroupEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private	String group_id;//通讯录id
	private String groupname;//通讯录姓名，签到姓名，预案执行姓名
	private Boolean ischecked;//通讯录被选
	private List<ChildEntity> cList;//通讯录子集合
	private String sortLetters; // 显示数据拼音的首字母
	private String planId;
	private String planResId;
	private String	planResType;
	private String	drillPrecautionId;
	private String state;
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDrillPrecautionId() {
		return drillPrecautionId;
	}

	public void setDrillPrecautionId(String drillPrecautionId) {
		this.drillPrecautionId = drillPrecautionId;
	}

	public String getPlanResType() {
		return planResType;
	}

	public void setPlanResType(String planResType) {
		this.planResType = planResType;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanResId() {
		return planResId;
	}

	public void setPlanResId(String planResId) {
		this.planResId = planResId;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public GroupEntity() {
	}

	public GroupEntity(String group_id, String groupname,
			List<ChildEntity> cList) {
		this.group_id = group_id;
		this.groupname = groupname;
		this.cList = cList;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public List<ChildEntity> getcList() {
		return cList;
	}

	public void setcList(List<ChildEntity> cList) {
		this.cList = cList;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public Boolean getIschecked() {
		return ischecked;
	}

	public void setIschecked(Boolean ischecked) {
		this.ischecked = ischecked;
	}

}
