package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 预案详情实体类
 * @author zsj
 *
 */
public class PlanDetailObjEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String planId;
	private String planName;
	private String planStarter;
	private String planStartTime;
	private String planType;
	private String planTypeName;
	private String summary;
	private String planStartOpition;
	
	private String eveName;
	private String sceneName;
	private String eveDescription;
	
	
	private String planResName;
	private String tradeTypeId;
	private String eveLevelId;
	private String planStarterId;
	private String planAuthorId;
	private String submitterId;
	//planResName	预案来源名称	发送通知使用
//	tradeTypeId	业务类型ID	发送通知使用
//	eveLevelId	事件等级ID	发送通知使用
//	planStarterId	预案启动人	发送通知使用
//	planAuthorId	预案授权人	发送通知使用
//	submitterId	事件提交人	发送通知使用
	
	private String	planResType;//	演练计划类型	1、事件 2、演练计划
	private String	drillPrecautionId	;//演练计划id	
	private String	emergType;
	
	
	private String	planAuthor;
	private String	planAuthTime;
	private String	planAuthOpition;
	private String nowTime;
	private String state;
	private String subTime;
	private String 	eveType;
	
	public String getEveType() {
		return eveType;
	}
	public void setEveType(String eveType) {
		this.eveType = eveType;
	}
	public String getSubTime() {
		return subTime;
	}
	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}
	public String getNowTime() {
		return nowTime;
	}
	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPlanAuthor() {
		return planAuthor;
	}
	public void setPlanAuthor(String planAuthor) {
		this.planAuthor = planAuthor;
	}
	public String getPlanAuthTime() {
		return planAuthTime;
	}
	public void setPlanAuthTime(String planAuthTime) {
		this.planAuthTime = planAuthTime;
	}
	public String getPlanAuthOpition() {
		return planAuthOpition;
	}
	public void setPlanAuthOpition(String planAuthOpition) {
		this.planAuthOpition = planAuthOpition;
	}
	public String getPlanTypeName() {
		return planTypeName;
	}
	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}
	public String getEmergType() {
		return emergType;
	}
	public void setEmergType(String emergType) {
		this.emergType = emergType;
	}
	public String getPlanResName() {
		return planResName;
	}
	public void setPlanResName(String planResName) {
		this.planResName = planResName;
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
	public String getPlanStarterId() {
		return planStarterId;
	}
	public void setPlanStarterId(String planStarterId) {
		this.planStarterId = planStarterId;
	}
	public String getPlanAuthorId() {
		return planAuthorId;
	}
	public void setPlanAuthorId(String planAuthorId) {
		this.planAuthorId = planAuthorId;
	}
	public String getSubmitterId() {
		return submitterId;
	}
	public void setSubmitterId(String submitterId) {
		this.submitterId = submitterId;
	}
	public String getPlanResType() {
		return planResType;
	}
	public void setPlanResType(String planResType) {
		this.planResType = planResType;
	}
	public String getDrillPrecautionId() {
		return drillPrecautionId;
	}
	public void setDrillPrecautionId(String drillPrecautionId) {
		this.drillPrecautionId = drillPrecautionId;
	}
	public String getEveName() {
		return eveName;
	}
	public void setEveName(String eveName) {
		this.eveName = eveName;
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public String getEveDescription() {
		return eveDescription;
	}
	public void setEveDescription(String eveDescription) {
		this.eveDescription = eveDescription;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanStarter() {
		return planStarter;
	}
	public void setPlanStarter(String planStarter) {
		this.planStarter = planStarter;
	}
	public String getPlanStartTime() {
		return planStartTime;
	}
	public void setPlanStartTime(String planStartTime) {
		this.planStartTime = planStartTime;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPlanStartOpition() {
		return planStartOpition;
	}
	public void setPlanStartOpition(String planStartOpition) {
		this.planStartOpition = planStartOpition;
	}
	
}
