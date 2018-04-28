package com.dssm.esc.util;

/**
 * 底部导航栏的的枚举类
 * 
 * @Description TODO
 * @author XiaoHuan
 * @date 2015-9-17
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public enum TabTypeEnum {
	message(0, "消息"), addresslist(1, "通讯录"), emenage(2, "应急管理"), control(3, "控制中心");

	private int id;
	private String title;

	TabTypeEnum(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

}
