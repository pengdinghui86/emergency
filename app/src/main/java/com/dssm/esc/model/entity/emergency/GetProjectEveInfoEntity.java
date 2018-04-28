package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 获取事件对应的评估信息实体类
 * 
 * @author zsj
 * 
 */
public class GetProjectEveInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;

	private String tradeTypeId;
	private String tradeTypeName;
	private String eveLevelId;
	private String eveLevelName;
	private String eveDescription;
	private String eveScenarioId;

	private String eveScenarioName;
	private String eveName;
	private String dealAdvice;
 
	
	private String referPlanIds;
	private String otherReferPlanIds;
	private String categoryPlanIds;
	private String drillPlanName;
	
	private List<Map<String, String>> referPlan;
	private List<Map<String, String>> otherReferPlan;
	private List<Map<String, String>> categoryPlan;
	private String emergType;
	private String	eveType;
	
	public String getEveType() {
		return eveType;
	}

	public void setEveType(String eveType) {
		this.eveType = eveType;
	}

	public String getEmergType() {
		return emergType;
	}

	public void setEmergType(String emergType) {
		this.emergType = emergType;
	}

	public String getReferPlanIds() {
		return referPlanIds;
	}

	public void setReferPlanIds(String referPlanIds) {
		this.referPlanIds = referPlanIds;
	}

	public String getOtherReferPlanIds() {
		return otherReferPlanIds;
	}

	public void setOtherReferPlanIds(String otherReferPlanIds) {
		this.otherReferPlanIds = otherReferPlanIds;
	}

	public String getCategoryPlanIds() {
		return categoryPlanIds;
	}

	public void setCategoryPlanIds(String categoryPlanIds) {
		this.categoryPlanIds = categoryPlanIds;
	}

	public List<Map<String, String>> getReferPlan() {
		return referPlan;
	}

	public void setReferPlan(List<Map<String, String>> referPlan) {
		this.referPlan = referPlan;
	}

	public List<Map<String, String>> getOtherReferPlan() {
		return otherReferPlan;
	}

	public void setOtherReferPlan(List<Map<String, String>> otherReferPlan) {
		this.otherReferPlan = otherReferPlan;
	}

	public List<Map<String, String>> getCategoryPlan() {
		return categoryPlan;
	}

	public void setCategoryPlan(List<Map<String, String>> categoryPlan) {
		this.categoryPlan = categoryPlan;
	}

	public String getDrillPlanName() {
		return drillPlanName;
	}

	public void setDrillPlanName(String drillPlanName) {
		this.drillPlanName = drillPlanName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTradeTypeId() {
		return tradeTypeId;
	}

	public void setTradeTypeId(String tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public String getTradeTypeName() {
		return tradeTypeName;
	}

	public void setTradeTypeName(String tradeTypeName) {
		this.tradeTypeName = tradeTypeName;
	}

	public String getEveLevelId() {
		return eveLevelId;
	}

	public void setEveLevelId(String eveLevelId) {
		this.eveLevelId = eveLevelId;
	}

	public String getEveLevelName() {
		return eveLevelName;
	}

	public void setEveLevelName(String eveLevelName) {
		this.eveLevelName = eveLevelName;
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

}
