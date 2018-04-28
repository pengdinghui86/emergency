package com.dssm.esc.model.jsonparser.emergency;

import java.io.Serializable;

/**
 * 预案执行实体类
 * @author zsj
 *
 */
public class PrecautionByPlanResEntity implements Serializable{

	private static final long serialVersionUID = 1L;
//	 "id": "13fc2dd8-c3bb-4282-9b0f-d1499de22e21", 
//     "planName": "应急响应预案", 
//     "planId": "870c17fe-b062-4a7d-8aa5-0df43c8d4ab8", 
//     "planResId": "64f7af42-c3bb-490d-9ecb-e6ffd1fd39d6"
	private String id;
	private String planName;
	private String planId;
	private String planResId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanResId() {
		return planResId;
	}
	public void setPlanResId(String planResId) {
		this.planResId = planResId;
	}
	
}
