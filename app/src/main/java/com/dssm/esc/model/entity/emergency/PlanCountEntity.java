package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

public class PlanCountEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	//驳回
	private String rejectEvaCount;
	//待授权
	private String authCount;
	//待启动
	private String startCount;

	public String getRejectEvaCount() {
		return rejectEvaCount;
	}

	public void setRejectEvaCount(String rejectEvaCount) {
		this.rejectEvaCount = rejectEvaCount;
	}

	public String getAuthCount() {
		return authCount;
	}

	public void setAuthCount(String authCount) {
		this.authCount = authCount;
	}

	public String getStartCount() {
		return startCount;
	}

	public void setStartCount(String startCount) {
		this.startCount = startCount;
	}
}
