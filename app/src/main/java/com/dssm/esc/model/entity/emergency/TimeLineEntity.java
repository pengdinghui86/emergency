package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

public class TimeLineEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String event;
	private String time;
	private String overtime;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}

}
