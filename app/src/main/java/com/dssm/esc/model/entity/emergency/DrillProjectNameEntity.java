package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 演练计划实体类
 * 
 * @author zsj
 * 
 */
public class DrillProjectNameEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 演练计划id
	private String name;// 演练计划名称
	private String initialPlanId;// 演练初始计划id

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitialPlanId() {
		return initialPlanId;
	}

	public void setInitialPlanId(String initialPlanId) {
		this.initialPlanId = initialPlanId;
	}

}
