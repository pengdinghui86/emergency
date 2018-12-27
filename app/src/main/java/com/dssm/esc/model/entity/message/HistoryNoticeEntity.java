package com.dssm.esc.model.entity.message;

import java.io.Serializable;

/**
 * 历史通知实体类
 */
public class HistoryNoticeEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    /** 消息编号*/
    private String id;
    /** 发送时间*/
    private String createTime;
    /** 发送方式，多个用,隔开，0系统，1邮件，2短信，3APP*/
    private String sendType;
    /** 消息内容*/
    private String message;
    /** 消息发送人*/
    private String sender;
    /** 消息发送人编号*/
    private String senderId;
    /** 消息创建人*/
    private String createUser;
    /** 消息接收人，多个用、隔开*/
    private String receiver;
    /** 消息接收人编号，多个用,隔开*/
    private String receiverId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
