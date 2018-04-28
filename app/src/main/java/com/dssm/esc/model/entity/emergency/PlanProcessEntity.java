package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 预案步骤实体类
 * 
 * @author zsj
 * 
 */
public class PlanProcessEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 流程步骤id
	private String planInfoId;// 预案执行id
	private String name;// 步骤节点名称
	private String executePeople;// 执行人
	private String executePeopleType;// 执行人状态
	private String executorA;
	private String executorB;
	private String executorC;
	private String executorAName;
	private String executorBName;
	private String executorCName;
	private String signStateA;//签到状态1，已签到，0，未签到
	
	private String signStateB;//签到状态1，已签到，0，未签到
	private String signStateC;//签到状态1，已签到，0，未签到
	
	public String getSignStateB() {
		return signStateB;
	}

	public void setSignStateB(String signStateB) {
		this.signStateB = signStateB;
	}

	public String getSignStateC() {
		return signStateC;
	}

	public void setSignStateC(String signStateC) {
		this.signStateC = signStateC;
	}

	public String getSignStateA() {
		return signStateA;
	}

	public void setSignStateA(String signStateA) {
		this.signStateA = signStateA;
	}

	public String getExecutorAName() {
		return executorAName;
	}

	public void setExecutorAName(String executorAName) {
		this.executorAName = executorAName;
	}

	public String getExecutorBName() {
		return executorBName;
	}

	public void setExecutorBName(String executorBName) {
		this.executorBName = executorBName;
	}

	public String getExecutorCName() {
		return executorCName;
	}

	public void setExecutorCName(String executorCName) {
		this.executorCName = executorCName;
	}

	public String getExecutorA() {
		return executorA;
	}

	public void setExecutorA(String executorA) {
		this.executorA = executorA;
	}

	public String getExecutorB() {
		return executorB;
	}

	public void setExecutorB(String executorB) {
		this.executorB = executorB;
	}

	public String getExecutorC() {
		return executorC;
	}

	public void setExecutorC(String executorC) {
		this.executorC = executorC;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanInfoId() {
		return planInfoId;
	}

	public void setPlanInfoId(String planInfoId) {
		this.planInfoId = planInfoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExecutePeople() {
		return executePeople;
	}

	public void setExecutePeople(String executePeople) {
		this.executePeople = executePeople;
	}

	public String getExecutePeopleType() {
		return executePeopleType;
	}

	public void setExecutePeopleType(String executePeopleType) {
		this.executePeopleType = executePeopleType;
	}

}
