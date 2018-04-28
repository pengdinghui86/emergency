package com.dssm.esc.model.entity.control;

import java.io.Serializable;

public class EvaProgressEntity implements Serializable {

	
	private static final long serialVersionUID = 1L;
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
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	
	/*"tradeType": "信用卡业务", 
    "eveLevel": "Ⅰ级（重大事件）", 
    "eveType": "1", 
    "eveScenarioId": "2c6946ba-a792-4ae4-afbe-6681d0feba99", 
    "updateTime": "2015-10-14 17:01:09", 
    "state": "0", 
    "eveCode": "SJ-YJ-1444813206699", 
    "eveName": "ggh", 
    "drillPlanName": null, 
    "id": "5b27fac1-6d18-449d-8b8a-bc1a226350b5", 
    "eveScenarioName": "基础设施类故障", 
    "emergType": null, 
    "drillPlanId": null*/
}
