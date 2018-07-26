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
	private String nodeStepType;//节点类型
	private String parentProcessStepId;//父节点步骤id
	private String parentProcessNumber;//父节点序号
	private String editOrderNum;//插入新增节点后的修正序号
	private String orderNum;//序号
	private String customNum;//自定义序号,用于本地排序
	private int index;//节点的层级

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getCustomNum() {
		return customNum;
	}

	public void setCustomNum(String customNum) {
		this.customNum = customNum;
	}

	public String getEditOrderNum() {
		return editOrderNum;
	}

	public void setEditOrderNum(String editOrderNum) {
		this.editOrderNum = editOrderNum;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getNodeStepType() {
		return nodeStepType;
	}

	public void setNodeStepType(String nodeStepType) {
		this.nodeStepType = nodeStepType;
	}

	public String getParentProcessStepId() {
		return parentProcessStepId;
	}

	public void setParentProcessStepId(String parentProcessStepId) {
		this.parentProcessStepId = parentProcessStepId;
	}

	public String getParentProcessNumber() {
		return parentProcessNumber;
	}

	public void setParentProcessNumber(String parentProcessNumber) {
		this.parentProcessNumber = parentProcessNumber;
	}

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
