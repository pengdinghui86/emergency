package com.dssm.esc.model.entity.user;

import java.io.Serializable;
import java.util.List;

import android.R.string;

/**
 * 用户信息实体类
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-21
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;

	private String success;
	private String message;
	private List<UserLoginObjEntity> obj;
	private String objString;
	private UserAttributesEntity attributes;

	public String getObjString() {
		return objString;
	}

	public void setObjString(String objString) {
		this.objString = objString;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<UserLoginObjEntity> getObj() {
		return obj;
	}

	public void setObj(List<UserLoginObjEntity> obj) {
		this.obj = obj;
	}

	public UserAttributesEntity getAttributes() {
		return attributes;
	}

	public void setAttributes(UserAttributesEntity attributes) {
		this.attributes = attributes;
	}

}
