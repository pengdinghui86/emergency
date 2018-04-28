package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 我的消息实体类
 * @Description TODO
 * @author Zsj
 * @date 2015-9-18
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co., Ltd. Inc. All rights reserved.
 */
public class MyMessageEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String position;//职位
	private String date;
	private  String time;
	private String content;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}
