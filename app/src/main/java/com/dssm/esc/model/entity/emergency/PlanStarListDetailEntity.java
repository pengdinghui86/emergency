package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;

/**
 * 预案启动事件详情
 * 
 * @author zsj
 * 
 */
public class PlanStarListDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String success;
	private PlanStarListDetailObjEntity obj;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public PlanStarListDetailObjEntity getObj() {
		return obj;
	}
	public void setObj(PlanStarListDetailObjEntity obj) {
		this.obj = obj;
	}
	
}
