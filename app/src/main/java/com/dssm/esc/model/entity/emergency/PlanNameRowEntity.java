package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

import android.R.string;

/**
 * 预案名称的row实体类
 * @author zsj
 *
 */
public class PlanNameRowEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String summary;
	private String hasStartAuth;
	private boolean isSelect;
	
	
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public String getHasStartAuth() {
		return hasStartAuth;
	}
	public void setHasStartAuth(String hasStartAuth) {
		this.hasStartAuth = hasStartAuth;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
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
