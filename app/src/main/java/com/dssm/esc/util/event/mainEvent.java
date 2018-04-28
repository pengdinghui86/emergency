package com.dssm.esc.util.event;

/**
 * 主界面刷新消息
 * 
 * @author zsj
 * 
 */
public class mainEvent {

	private String data;

	public mainEvent(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

}
