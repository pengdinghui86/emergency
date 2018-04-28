package com.dssm.esc.model.entity.control;

import java.io.Serializable;
/**
 * 预案实体类
 * @author Administrator
 *
 */
public class PlanEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 编号
	 */
	private String id;
	/**
	 * 预案名称
	 */
	private String planName;
	/**
	 * 事件名称
	 */
	private String planResName;
	public String getPlanResName() {
		return planResName;
	}
	public void setPlanResName(String planResName) {
		this.planResName = planResName;
	}
//	planResType	预案来源类型	中止时发送通知使用
//	tradeTypeId	业务类型ID	中止时发送通知使用
//	eveLevelId	事件等级ID	中止时发送通知使用
//	planStarterId	预案启动人	中止时发送通知使用
//	planAuthorId	预案授权人	中止时发送通知使用
//	submitterId	事件提交人	中止时发送通知使用
	private String  planResType;
	private String  tradeTypeId;
	private String  eveLevelId;
	private String  planStarterId;
	private String  planAuthorId;
	private String  submitterId;
	private String	state;
	private String	drillPrecautionId;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDrillPrecautionId() {
		return drillPrecautionId;
	}
	public void setDrillPrecautionId(String drillPrecautionId) {
		this.drillPrecautionId = drillPrecautionId;
	}
	public String getPlanResType() {
		return planResType;
	}
	public void setPlanResType(String planResType) {
		this.planResType = planResType;
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
	/**
	 * 预案id
	 */
	private String planId;
	/**
	 * 预案事件id
	 */
	private String planResId;
	//"id":"13fc2dd8-c3bb-4282-9b0f-d1499de22e21","planName":"应急响应预案","planResName":" ","planId":"870c17fe-b062-4a7d-8aa5-0df43c8d4ab8","planResId":"64f7af42-c3bb-490d-9ecb-e6ffd1fd39d6"
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getPlanResId() {
		return planResId;
	}
	public void setPlanResId(String planResId) {
		this.planResId = planResId;
	}
	
}
