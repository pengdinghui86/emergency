package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 预案启动列表事件详情实体类
 * 
 * @author zsj
 * 
 */
public class PlanStarListDetailObjEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<PlanStarListDetailObjListEntity> list = new ArrayList<>();
	private String id;// 事件编号
	private String submitter;// 事件提交人
	private String subTime;// 事件提交时间
	private String nowTime;//提交时间
	private String discoverer;// 事件发现人
	private String discoveryTime;//事件发生时间
	private String tradeType;// 行业类型名称
	private String eveLevel;// 事件等级名称
	private String emergType;// 演练类型 eveType为2时有值
	private String eveDescription;// 事件描述
	private String eveScenarioName;// 事件场景
	private String drillPlanName;// 演练详细计划名称
	private String submitterId;// 事件提交人id

	private String drillPlanId;// 演练详细计划ID
	private String eveScenarioId;// 事件场景id
	private String eveType;// 事件类型
	private String dealAdvice;// 处置建议
	private String eveCode;//事件编号
	private String eveName;// 事件名称
	private String planResName;//预案来源名称	发送通知使用
private String tradeTypeId;//业务类型ID
private String eveLevelId;//事件等级ID
//		planResType	预案来源类型	发送通知使用
//planResName	预案来源名称	发送通知使用
//planName	预案名称	发送通知使用
//planId	预案ID	发送通知使用
private String planResType;//预案来源类型	发送通知使用
private String planName;//预案名称	发送通知使用
private String planId;//预案ID	发送通知使用
private String planTypeName;//预案来源类型	发送通知使用
private  String hasStartAuth;//启动权限


	public String getHasStartAuth() {
	return hasStartAuth;
}

public void setHasStartAuth(String hasStartAuth) {
	this.hasStartAuth = hasStartAuth;
}

	public String getNowTime() {
	return nowTime;
}

public void setNowTime(String nowTime) {
	this.nowTime = nowTime;
}

	public String getPlanTypeName() {
	return planTypeName;
}

public void setPlanTypeName(String planTypeName) {
	this.planTypeName = planTypeName;
}

	public String getEveCode() {
	return eveCode;
}

public void setEveCode(String eveCode) {
	this.eveCode = eveCode;
}

	public String getPlanResType() {
	return planResType;
}

public void setPlanResType(String planResType) {
	this.planResType = planResType;
}

public String getPlanName() {
	return planName;
}

public void setPlanName(String planName) {
	this.planName = planName;
}

public String getPlanId() {
	return planId;
}

public void setPlanId(String planId) {
	this.planId = planId;
}

	public String getTradeTypeId() {
	return tradeTypeId;
}

public void setTradeTypeId(String tradeTypeId) {
	this.tradeTypeId = tradeTypeId;
}

public String getEveLevelId() {
	return eveLevelId;
}

public void setEveLevelId(String eveLevelId) {
	this.eveLevelId = eveLevelId;
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

	public ArrayList<PlanStarListDetailObjListEntity> getList() {
		return list;
	}

	public void setList(ArrayList<PlanStarListDetailObjListEntity> list) {
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEveName() {
		return eveName;
	}

	public void setEveName(String eveName) {
		this.eveName = eveName;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getSubTime() {
		return subTime;
	}

	public void setSubTime(String subTime) {
		this.subTime = subTime;
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

	public String getEmergType() {
		return emergType;
	}

	public void setEmergType(String emergType) {
		this.emergType = emergType;
	}

	public String getEveDescription() {
		return eveDescription;
	}

	public void setEveDescription(String eveDescription) {
		this.eveDescription = eveDescription;
	}

	public String getEveScenarioName() {
		return eveScenarioName;
	}

	public void setEveScenarioName(String eveScenarioName) {
		this.eveScenarioName = eveScenarioName;
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

	public String getDealAdvice() {
		return dealAdvice;
	}

	public void setDealAdvice(String dealAdvice) {
		this.dealAdvice = dealAdvice;
	}

	public String getDiscoverer() {
		return discoverer;
	}

	public void setDiscoverer(String discoverer) {
		this.discoverer = discoverer;
	}

	public String getDiscoveryTime() {
		return discoveryTime;
	}

	public void setDiscoveryTime(String discoveryTime) {
		this.discoveryTime = discoveryTime;
	}
}
