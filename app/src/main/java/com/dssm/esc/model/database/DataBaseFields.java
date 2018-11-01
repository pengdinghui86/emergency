package com.dssm.esc.model.database;

/**
 * 数据库操作字段
 * 
 * @Description
 * @author 綦巍
 * @date 2015-4-28
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class DataBaseFields {
	// *******************************公用字段*****************************//
	/** 倒叙查询 */
	public static final String DESC = "desc";
	
	/**
	 * 信息数据表
	 */
	public static final String TB_MESSAGE = "tb_message_";
	/** 本地数据库自增长Id */
	public static final String ID = "id";
	/** 消息唯一id */
	public static final String MESSAGEID = "messageId";
	/** 时间 */
	public static final String TIME = "time";
	/**内容 */
	public static final String MESSAGE = "message";
	/**事件名称 */
	public static final String EVENTNAME = "eventName";
	public static final String EVENTTYPE = "eventType";
	/**预案名称 */
	public static final String PLANNAME = "planName";
	/**发送人 */
	public static final String SENDER = "sender";
	/**状态标识 */
	public static final String MODELFLAG = "modelFlag";
	/** json通用字段 */
	public static final String TB_JSON = "tb_json";
	public static final String FIELD_JSON = "field_json";
	public static final String FIELD_TYPE = "field_type";
	/** 是否接受推送消息 */
	public static final String IS_ALLOW_PUSH = "is_allow_push";
	/** 是否允许推荐消息 */
	public static final String IS_ALLOW_RECOMMEND = "is_allow_recommend";
	/** 邮箱 */
	public static final String EMAIL = "email";

	

}
