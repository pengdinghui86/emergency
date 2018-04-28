package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 任务通知，系统通知，任务通知实体类
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-18
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class MessageToastEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	// private String date;
	// private String time;
	// private String content;
	private String id;
	private String senderId;
	private String sender;
	private String receiverId;
	private String receiver;
	private String message;
	private String modelFlag;
	private String createTime;
	private String isSelect;

	// "id": "4f4908b4-3062-4740-a8f4-83388243dd2b",
	// "senderId": "344",
	// "senderName": "张三",
	// "receiverId": "456",
	// "receiverName": "王五",
	// "senderPostName": "技术部门经理",
	// "message": "在不在",
	// "modelFlag": "2",
	// "createTime": "2015-09-22 10:00:00"

	public String getId() {
		return id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getModelFlag() {
		return modelFlag;
	}

	public void setModelFlag(String modelFlag) {
		this.modelFlag = modelFlag;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
