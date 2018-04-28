package com.dssm.esc.model.entity.user;

import java.io.Serializable;

public class UserPersonIdEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String success;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private UserObjEntity obj;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public UserObjEntity getObj() {
		return obj;
	}
	public void setObj(UserObjEntity obj) {
		this.obj = obj;
	}
	
}
