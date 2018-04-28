package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

public class PlanDetailEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String success;
	private PlanDetailObjEntity obj;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public PlanDetailObjEntity getObj() {
		return obj;
	}
	public void setObj(PlanDetailObjEntity obj) {
		this.obj = obj;
	}
	
}
