package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 人员指派实体类
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-12
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class PersonAssignmentEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String stepname;
	private String personname;
	private String persontask;

	public String getStepname() {
		return stepname;
	}

	public void setStepname(String stepname) {
		this.stepname = stepname;
	}

	public String getPersonname() {
		return personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname;
	}

	public String getPersontask() {
		return persontask;
	}

	public void setPersontask(String persontask) {
		this.persontask = persontask;
	}

}
