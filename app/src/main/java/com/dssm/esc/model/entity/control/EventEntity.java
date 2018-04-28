package com.dssm.esc.model.entity.control;

import java.io.Serializable;

/**
 * 事件实体类
 * 
 * @author zsj
 * 
 */
public class EventEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	// eveName 事件名称
	// eveType 事件类型
	// id 事件ID
	private String eveName;// 事件名称
	private String eveType;// 事件类型
	private String id;// 事件ID

	public String getEveName() {
		return eveName;
	}

	public void setEveName(String eveName) {
		this.eveName = eveName;
	}

	public String getEveType() {
		return eveType;
	}

	public void setEveType(String eveType) {
		this.eveType = eveType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
