package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

import org.json.JSONArray;

/**
 * 发送通告实体类
 * @author zsj
 *
 */
public class SendNoticyEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;//接收人岗位标识	逗号隔开	
	private String sendType;//发送方式	0系统，1邮件，2短信，3 APP，逗号隔开
	private String content;//通知内容	
	private String busType;//通知类型	协同:busType='collaborNotice',
//	通告:busType='displayNotice'
	private String planInfoId;//执行编号	
	private String coorStage;//协同--阶段	
	private String sendObj;//协同—对象	
	
	
	public String getSendObj() {
		return sendObj;
	}
	public void setSendObj(String sendObj) {
		this.sendObj = sendObj;
	}
	public String getSendType() {
		return sendType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getPlanInfoId() {
		return planInfoId;
	}
	public void setPlanInfoId(String planInfoId) {
		this.planInfoId = planInfoId;
	}
	public String getCoorStage() {
		return coorStage;
	}
	public void setCoorStage(String coorStage) {
		this.coorStage = coorStage;
	}
	
	
	
}
