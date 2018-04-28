package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 业务类型和事件等级实体类
 * 
 * @author zsj
 * 
 */
public class BusinessTypeEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;// 业务类型、事件等级，事件场景
	
	private String signState;//状态
	private String role;//角色
	private boolean isSelect;
	
	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSignState() {
		return signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
