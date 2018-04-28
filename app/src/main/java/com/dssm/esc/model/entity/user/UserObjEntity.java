package com.dssm.esc.model.entity.user;

import java.io.Serializable;

public class UserObjEntity implements Serializable{
	private static final long serialVersionUID = 1L;

//	"id": "ef8ab258-41f5-4edf-9a0a-4c8fac183229", 
//    "sex": "女", 
//    "postName": "副行长", 
//    "phoneNumTwo": "0750-63232403", 
//    "email": "szliaoyq@qq163.com", 
//    "name": "阳青松", 
//    "phoneNumOne": "18680683503", 
//    "numbuer": "FB00005", 
//    "depName": "高级管理层", 
//    "orgName": "广州银行", 
//    "postFlag": "bbec1261-4597-477b-be58-859db4cbef66"
	
	private String id;
	private String sex;
	private String postName;
	private String phoneNumTwo;
	
	private String phoneNumOne;
	private String email;
	private String name;
	private String numbuer;
	
	private String depName;
	private String orgName;
	private String postFlag;
	private String userId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getPhoneNumTwo() {
		return phoneNumTwo;
	}
	public void setPhoneNumTwo(String phoneNumTwo) {
		this.phoneNumTwo = phoneNumTwo;
	}
	public String getPhoneNumOne() {
		return phoneNumOne;
	}
	public void setPhoneNumOne(String phoneNumOne) {
		this.phoneNumOne = phoneNumOne;
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
	public String getNumbuer() {
		return numbuer;
	}
	public void setNumbuer(String numbuer) {
		this.numbuer = numbuer;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getPostFlag() {
		return postFlag;
	}
	public void setPostFlag(String postFlag) {
		this.postFlag = postFlag;
	}
	
	
	
}
