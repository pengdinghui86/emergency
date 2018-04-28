package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 应急小组实体类
 * 
 * @author zsj
 * 
 */
public class EmergencyGropListDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String pId;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	// "id": "97ead109-11c2-4e54-b9eb-01b8ffb32166",
	// "pId": "",
	// "name": "IT恢复执行小组"
}
