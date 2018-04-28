package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 驳回列表实体类
 * @author zsj
 *
 */
public class BoHuiListEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String planId;//预案编号
	private String planResId;//事件详情id
	private String	precautionId;//预案id
	
	
	/**
	 * 业务类型
	 */
	private String tradeType;
	/**
	 * 事件名称
	 */
	private String eveName;
	/**
	 * 事件ID
	 */
	private String id;
	/**
	 * 事件等级
	 */
	private String eveLevel;
	/**
	 * 事件类型
	 */
	private String eveType;
	/**
	 * 
	 */
	private String eveScenarioId;
	/**
	 * 
	 */
	private String updateTime;
	/**
	 * 
	 */
	private String state;
	/**
	 * 事件编号
	 */
	private String eveCode;
	/**
	 * 
	 */
	private String drillPlanName;
	/**
	 * 事件场景
	 */
	private String eveScenarioName;
	/**
	 * 
	 */
	private String emergType;
	/**
	 * 
	 */
	private String drillPlanId;
	private String submitterId;
	private String	submitter;
	
	private String	planResName;
	private String	planResType;
	private String	isAuthor;
	
	public String getIsAuthor() {
		return isAuthor;
	}
	public void setIsAuthor(String isAuthor) {
		this.isAuthor = isAuthor;
	}
	public String getPlanResType() {
		return planResType;
	}
	public void setPlanResType(String planResType) {
		this.planResType = planResType;
	}
	public String getPlanResName() {
		return planResName;
	}
	public void setPlanResName(String planResName) {
		this.planResName = planResName;
	}
	public String getSubmitterId() {
		return submitterId;
	}
	public void setSubmitterId(String submitterId) {
		this.submitterId = submitterId;
	}
	public String getSubmitter() {
		return submitter;
	}
	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getEveLevel() {
		return eveLevel;
	}
	public void setEveLevel(String eveLevel) {
		this.eveLevel = eveLevel;
	}
	public String getEveType() {
		return eveType;
	}
	public void setEveType(String eveType) {
		this.eveType = eveType;
	}
	public String getEveScenarioId() {
		return eveScenarioId;
	}
	public void setEveScenarioId(String eveScenarioId) {
		this.eveScenarioId = eveScenarioId;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getEveCode() {
		return eveCode;
	}
	public void setEveCode(String eveCode) {
		this.eveCode = eveCode;
	}
	public String getDrillPlanName() {
		return drillPlanName;
	}
	public void setDrillPlanName(String drillPlanName) {
		this.drillPlanName = drillPlanName;
	}
	public String getEveScenarioName() {
		return eveScenarioName;
	}
	public void setEveScenarioName(String eveScenarioName) {
		this.eveScenarioName = eveScenarioName;
	}
	public String getEmergType() {
		return emergType;
	}
	public void setEmergType(String emergType) {
		this.emergType = emergType;
	}
	public String getDrillPlanId() {
		return drillPlanId;
	}
	public void setDrillPlanId(String drillPlanId) {
		this.drillPlanId = drillPlanId;
	}
	public String getPrecautionId() {
		return precautionId;
	}
	public void setPrecautionId(String precautionId) {
		this.precautionId = precautionId;
	}
	public String getPlanResId() {
		return planResId;
	}
	public void setPlanResId(String planResId) {
		this.planResId = planResId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEveName() {
		return eveName;
	}
	public void setEveName(String eveName) {
		this.eveName = eveName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

     
}
