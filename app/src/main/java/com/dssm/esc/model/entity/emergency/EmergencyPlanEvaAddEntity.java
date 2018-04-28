package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 应急管理--添加评估实体类
 * 
 * @author zsj
 * 
 */
public class EmergencyPlanEvaAddEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 行业类型 */
	private String tradeType;
	/** 事件等级 */
	private String eveLevel;
	/** 事件描述 */
	private String eveDescription;
	/** 事件场景 */
	private String eveScenarioId;
	/** 事件场景名称 */
	private String eveScenarioName;
	/** 演练类型 */
	private String emergType;
	/** 事件名称 */
	private String eveName;
	/** 处置建议 */
	private String dealAdvice;
	/** 参考预案 可以多选，以“|”隔开 */
	private String referPlan;
	/** 其他预案 可以多选，以“|”隔开 */
	private String otherReferPlan;
	/** 分类预案 可以多选，以“|”隔开 */
	private String categoryPlan;
	/** 事件类型 1为应急，2为演练 */
	private String eveType;
	/** 演练详细计划ID eveType为2时传入 */
	private String drillPlanId;
	/** 演练详细计划名称 eveType为2时传入 */
	private String drillPlanName;
	/**演练初始计划id*/
	private String exPlanId;
	
	public String getExPlanId() {
		return exPlanId;
	}

	public void setExPlanId(String exPlanId) {
		this.exPlanId = exPlanId;
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

	public String getEveDescription() {
		return eveDescription;
	}

	public void setEveDescription(String eveDescription) {
		this.eveDescription = eveDescription;
	}

	public String getEveScenarioId() {
		return eveScenarioId;
	}

	public void setEveScenarioId(String eveScenarioId) {
		this.eveScenarioId = eveScenarioId;
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

	public String getEveName() {
		return eveName;
	}

	public void setEveName(String eveName) {
		this.eveName = eveName;
	}

	public String getDealAdvice() {
		return dealAdvice;
	}

	public void setDealAdvice(String dealAdvice) {
		this.dealAdvice = dealAdvice;
	}

	public String getReferPlan() {
		return referPlan;
	}

	public void setReferPlan(String referPlan) {
		this.referPlan = referPlan;
	}

	public String getOtherReferPlan() {
		return otherReferPlan;
	}

	public void setOtherReferPlan(String otherReferPlan) {
		this.otherReferPlan = otherReferPlan;
	}

	public String getCategoryPlan() {
		return categoryPlan;
	}

	public void setCategoryPlan(String categoryPlan) {
		this.categoryPlan = categoryPlan;
	}

	public String getEveType() {
		return eveType;
	}

	public void setEveType(String eveType) {
		this.eveType = eveType;
	}

	public String getDrillPlanId() {
		return drillPlanId;
	}

	public void setDrillPlanId(String drillPlanId) {
		this.drillPlanId = drillPlanId;
	}

	public String getDrillPlanName() {
		return drillPlanName;
	}

	public void setDrillPlanName(String drillPlanName) {
		this.drillPlanName = drillPlanName;
	}

}
