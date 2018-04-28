package com.dssm.esc.model.entity.user;

import java.io.Serializable;

/**
 * 权限中的menu
 * @author zsj
 *
 */
public class MenuEntity implements Serializable{
	private static final long serialVersionUID = 1L;

//	 "name": "事件评估", 
//     "mark": "SJPG", 
//     "code": "040302"
	private String name;
	private String mark;
	private String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
