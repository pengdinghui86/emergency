package com.dssm.esc.model.entity.message;

/**
 * 消息描述预案状态的枚举类
 */
public enum MessageStatusEnum {
	emergencyNotice("1", "应急通知"),
	noticeAnnouncement("2", "通知公告"),
	collaborationAnnouncement("3", "协同与通告"),
	eventEvaluation("5", "事件评估"),  //事件已评估待启动
	planStarted("6", "预案启动"),   //预案已启动待签到
	personSignIn("7", "人员签到"),//预案已启动待就位并签到
	planAuthorize("71", "预案待授权"),//预案已启动待授权
	planExecute("9", "预案执行"),//预案执行请准备下一步任务
	planAuthorized("10", "决策授权通过"),//预案授权通过通知相关人员
	PlanUnAuthorized("11", "决策授权未通过"),//预案授权未通过通知相关人员
	procedureStarted("12", "流程启动"),//流程启动通知相关人员
	procedureStop("13", "流程中止"),//流程中止通知相关人员
	procedureEnd("14", "流程结束"),//流程结束通知相关人员
	emergencyCommunication("15", "应急通信录"),
	eventReject("8", "事件驳回");  //事件被驳回待重新评估

	private String id;
	private String title;

	MessageStatusEnum(String id, String title) {
		this.id = id;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

}
