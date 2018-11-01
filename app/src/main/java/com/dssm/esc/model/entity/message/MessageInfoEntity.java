package com.dssm.esc.model.entity.message;

import com.dssm.esc.model.database.DataBaseFields;

import java.io.Serializable;


/**
 * 信息实体类
 * @author zsj
 *
 */
public class MessageInfoEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 本地数据库记录id */
	private String id;
	/** 消息id（服务器上消息唯一Id） */
	private String messageId;
	/** 时间 */
	private String time;
	/** 消息 */
	private String message;
	private String senderId;
	private String sender;
	private String receiverId;
	private String receiver;
	private String modelFlag;
	//事件名称
	private String eveName;
	//预案名称
	private String planName;
	//预案类型，1=应急，2=演练
	private String eveType;

	public String getEveType() {
		return eveType;
	}

	public void setEveType(String eveType) {
		this.eveType = eveType;
	}

	public String getEveName() {
		return eveName;
	}

	public void setEveName(String eveName) {
		this.eveName = eveName;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getModelFlag() {
		return modelFlag;
	}
	public void setModelFlag(String modelFlag) {
		this.modelFlag = modelFlag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

	/** 获取数据库字段头 */
	public String[] getDataBaseTitles() {
		String[] titles = {
				// 列名
				DataBaseFields.MESSAGEID, // 消息id
				DataBaseFields.MESSAGE, // 消息
				DataBaseFields.TIME, // 时间
				DataBaseFields.EVENTNAME, // 事件名称
				DataBaseFields.EVENTTYPE, // 预案类型
				DataBaseFields.SENDER, // 发送人
				DataBaseFields.MODELFLAG, // 状态标识
				DataBaseFields.PLANNAME // 预案名称
		};
		return titles;
	}

	/** 获取用于数据库存储的所有数据 */
	public String[] getDataBaseValues() {
		String[] values = {
				// 数据
				messageId, // 消息id
				message, // 消息
				time, //时间
				eveName, //事件名称
				eveType, //预案类型
				sender, //发送人
				modelFlag, //状态标识
				planName //预案名称
		};

		return values;
	}

}
