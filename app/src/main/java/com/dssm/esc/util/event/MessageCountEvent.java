package com.dssm.esc.util.event;
/**
 * 唯独消息刷新tab显示小红点
 * @Description TODO
 * @author XiaoHuan
 * @date 2015-10-18
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class MessageCountEvent {
  public String count1;
  public String count2;
  public String count3;
  public String count4;

public MessageCountEvent(String count1, String count2, String count3, String count4 ) {
	this.count1=count1;
	this.count2=count2;
	this.count3=count3;
	this.count4=count4;
}
}
