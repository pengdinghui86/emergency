package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 应急管理页面的菜单实体类
 */
public class EmergencyMenuEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 菜单id
	private String name;// 菜单名称
	private int icon;// 菜单图片
	private int count;// 数量提醒
	private Class activity;// 菜单对应的activity类

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

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public Class getActivity() {
		return activity;
	}

	public void setActivity(Class activity) {
		this.activity = activity;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
