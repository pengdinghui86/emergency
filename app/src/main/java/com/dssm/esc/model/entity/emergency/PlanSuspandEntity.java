package com.dssm.esc.model.entity.emergency;
/**
 * 预案中止实体类
 * @author zsj
 *
 */
public class PlanSuspandEntity {
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
	

	private String id;
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
	
	
	public String getEveLevelId() {
		return eveLevelId;
	}
	public void setEveLevelId(String eveLevelId) {
		this.eveLevelId = eveLevelId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
}
