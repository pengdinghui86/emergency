package com.dssm.esc.util.event;
/**
 * 银行卡列表刷新 通信
 * @Description TODO
 * @author XiaoHuan
 * @date 2015-9-17
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class PushMessageEvent {
	/** 数据 */
	public int index;
	public PushMessageEvent(int index) {
		this.index=index;
	}
}
