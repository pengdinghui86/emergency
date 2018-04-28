package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;

/**
 * 演练预案详情实体类
 * 
 * @author zsj
 * 
 */
public class DrillProcationDetailEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String success;
	private String message;
	private DrillProcationDetailObjEntity obj;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public DrillProcationDetailObjEntity getObj() {
		return obj;
	}
	public void setObj(DrillProcationDetailObjEntity obj) {
		this.obj = obj;
	}
	
	
}
