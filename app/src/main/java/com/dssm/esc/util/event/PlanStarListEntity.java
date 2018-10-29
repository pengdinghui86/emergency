package com.dssm.esc.util.event;

import java.io.Serializable;

/**
 * 预案启动实体类
 * @author zsj
 *
 */
public class PlanStarListEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id ;
	private String eveName;
	private String state;
//	id	预案ID	
//	suspendType	中止类型	启动时中止，类型为null
//	planSuspendOpition	中止原因	
//	planName	预案名称	发送通知使用
//	planResName	预案来源名称	发送通知使用
//	planResType	预案来源类型	发送通知使用
//	planId	预案ID	发送通知使用
//	tradeTypeId	业务类型ID	发送通知使用
//	eveLevelId	事件等级ID	发送通知使用
//	planStarterId	预案启动人	发送通知使用
//	planAuthorId	预案授权人	发送通知使用
//	submitterId	事件提交人	发送通知使用
	private String suspendType;
	private String planSuspendOpition;
	private String planName;
	private String planResName;
	private String planResType;
	private String planId;
	private String tradeTypeId;
	
	private String eveLevelId;
	private String planStarterId;
	private String planAuthorId;
	private String submitterId;
	
	private String eveLevel;
	private String tradeType;
	private String	eveCode;
	private String	eveType;
	private String isStarter;
	//数据类型，0=事件，1=预案
	private int dataType;

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getIsStarter() {
		return isStarter;
	}
	public void setIsStarter(String isStarter) {
		this.isStarter = isStarter;
	}
	public String getEveType() {
		return eveType;
	}
	public void setEveType(String eveType) {
		this.eveType = eveType;
	}
	public String getEveCode() {
		return eveCode;
	}
	public void setEveCode(String eveCode) {
		this.eveCode = eveCode;
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
	public String getSuspendType() {
		return suspendType;
	}
	public void setSuspendType(String suspendType) {
		this.suspendType = suspendType;
	}
	public String getPlanSuspendOpition() {
		return planSuspendOpition;
	}
	public void setPlanSuspendOpition(String planSuspendOpition) {
		this.planSuspendOpition = planSuspendOpition;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanResName() {
		return planResName;
	}
	public void setPlanResName(String planResName) {
		this.planResName = planResName;
	}
	public String getPlanResType() {
		return planResType;
	}
	public void setPlanResType(String planResType) {
		this.planResType = planResType;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	

}
