package com.dssm.esc.model.entity.control;

import java.io.Serializable;

/**
 *事件流程
 */
public class EventProgressEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    /**
     * 流程步骤编号
     */
    private String id;
    /**
     * 流程内容
     */
    private String content;
    /**
     * 事件等级名称
     */
    private String eveLevelName;

	/**
	 * 步骤执行人名称
	 */
	private String operatorName;

	/**
	 * 执行时间
	 */
	private String operationTime;

	/**
	 * 步骤类型
	 */
	private String stepType;

	/**
	 * 步骤名称
	 */
	private String step;

	/**
	 * 事件发生时间
	 */
	private String discoveryTime;

	/**
	 * 启动的预案名称
	 */
	private String planName;

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getDiscoveryTime() {
		return discoveryTime;
	}

	public void setDiscoveryTime(String discoveryTime) {
		this.discoveryTime = discoveryTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEveLevelName() {
		return eveLevelName;
	}

	public void setEveLevelName(String eveLevelName) {
		this.eveLevelName = eveLevelName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}
}
