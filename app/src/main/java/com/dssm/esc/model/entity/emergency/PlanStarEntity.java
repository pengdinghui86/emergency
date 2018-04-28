package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;


/**
 * 预案启动实体类
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co., Ltd. Inc. All rights reserved.
 */
public class PlanStarEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String startState;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartState() {
		return startState;
	}
	public void setStartState(String startState) {
		this.startState = startState;
	}
	

}
