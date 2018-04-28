package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

/**
 * 实时跟踪的实体类
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-15
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class RealTimeTrackingEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String time;
	private String status;
	private String name;
	private String step;
	private String content;
	private String overtime;
	private String predicttime;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}

	public String getPredicttime() {
		return predicttime;
	}

	public void setPredicttime(String predicttime) {
		this.predicttime = predicttime;
	}

}
