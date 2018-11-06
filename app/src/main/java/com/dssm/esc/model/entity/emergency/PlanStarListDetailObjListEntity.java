package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

public class PlanStarListDetailObjListEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String processId;//预案流程编号
	private String name;//预案名称
	private String planType;//预案类型
	private String sceneName;//场景名称

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
}
