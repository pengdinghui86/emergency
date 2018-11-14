package com.dssm.esc.model.entity.message;

import java.io.Serializable;

/**
 * 历史通知实体类
 */
public class HistoryNoticeEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    /** 发送时间*/
    private String createTime;
    /** 发送方式*/
    private String sendType;
    /** 消息内容*/
    private String message;

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
