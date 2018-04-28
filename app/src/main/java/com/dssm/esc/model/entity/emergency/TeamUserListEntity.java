package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 签到组中成员实体类
 * @author zsj
 *
 */
public class TeamUserListEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String emergTeam;
	private String postName;
	private String roleTypeName;
	private String userName;
	private String telphone;
	private String signState;// 0:未签到 1：已签到

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmergTeam() {
		return emergTeam;
	}

	public void setEmergTeam(String emergTeam) {
		this.emergTeam = emergTeam;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getRoleTypeName() {
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getSignState() {
		return signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

}
