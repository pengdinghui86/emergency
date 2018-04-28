package com.dssm.esc.model.entity.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 第一次获取全部信息实体类
 * 
 * @author zsj
 * 
 */
public class FirstAllMessagesEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	// "unreadCount": 67,
	// "msgType": 1,
	// "list": [
	private String unreadCount;// 未读条数
	private String msgType;// app消息类型（1：任务通知，2：系统通知，3：我的消息，4：紧急通知）
	private ArrayList<MessageInfoEntity> list;

	public String getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(String unreadCount) {
		this.unreadCount = unreadCount;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public ArrayList<MessageInfoEntity> getList() {
		return list;
	}

	public void setList(ArrayList<MessageInfoEntity> list) {
		this.list = list;
	}

	

}
