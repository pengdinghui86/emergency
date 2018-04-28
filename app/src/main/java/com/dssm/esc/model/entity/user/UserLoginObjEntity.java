package com.dssm.esc.model.entity.user;

import java.io.Serializable;

/**
 * 用户登录保存信息实体类
 * @author zsj
 *
 */
public class UserLoginObjEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private	String roleId;
	private String roleName;
	
	private String roleCode;
	
	
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
