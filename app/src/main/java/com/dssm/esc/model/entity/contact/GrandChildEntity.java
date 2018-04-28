package com.dssm.esc.model.entity.contact;

import java.io.Serializable;

/**
 * 第三级列表的子实体类
 * @author zsj
 *
 */
public class GrandChildEntity implements Serializable{
	private static final long serialVersionUID = 1L;
//	 "id": "20c8a30d-f831-484c-8456-b0bbde2e4845", 
//     "parentId": "12c467f6-7a16-4749-8ebf-7b652bfa8b84", 
//     "postName": "副行长", 
//     "email": "564529541@qq.com", 
//     "name": "李佳", 
//     "phoneNumOne": "0755-63232480", 
//     "phoneNumtwo": "0750-63232402", 
//     "postFlag": "24e0f831-eb7d-49f7-947e-4c97e6140a3e", 
//     "entityName": "李佳
	private String id;
	private String postName;
	private String email;
	private String name;
	private String phoneNumOne;
	private String phoneNumtwo;
	private String entityName;
	private String postFlag;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumOne() {
		return phoneNumOne;
	}
	public void setPhoneNumOne(String phoneNumOne) {
		this.phoneNumOne = phoneNumOne;
	}
	public String getPhoneNumtwo() {
		return phoneNumtwo;
	}
	public void setPhoneNumtwo(String phoneNumtwo) {
		this.phoneNumtwo = phoneNumtwo;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getPostFlag() {
		return postFlag;
	}
	public void setPostFlag(String postFlag) {
		this.postFlag = postFlag;
	}
	
	
}
