package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;

/**
 * 演练预案详情obj实体类
 * 
 * @author zsj
 * 
 */
public class DrillProcationDetailObjEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String exPlanId;
	private String emergType;
private String 	drillPlanName;
private String  drillPlanId;
private String  precautionList;//演练预案名称+场景名称列表
private String  referPlan;//演练预案编号，提交评估时传回给服务器
private String  referProcess;//演练预案+场景编号，提交评估时传回给服务器

	public String getPrecautionList() {
		return precautionList;
	}

	public void setPrecautionList(String precautionList) {
		this.precautionList = precautionList;
	}

	public String getReferPlan() {
		return referPlan;
	}

	public void setReferPlan(String referPlan) {
		this.referPlan = referPlan;
	}

	public String getReferProcess() {
		return referProcess;
	}

	public void setReferProcess(String referProcess) {
		this.referProcess = referProcess;
	}

	public String getDrillPlanId() {
	return drillPlanId;
}
public void setDrillPlanId(String drillPlanId) {
	this.drillPlanId = drillPlanId;
}
	public String getDrillPlanName() {
		return drillPlanName;
	}
	public void setDrillPlanName(String drillPlanName) {
		this.drillPlanName = drillPlanName;
	}
	private List<DrillProjectDetailObjPreinfoEntity> preInfo;
	public String getExPlanId() {
		return exPlanId;
	}
	public void setExPlanId(String exPlanId) {
		this.exPlanId = exPlanId;
	}
	public String getEmergType() {
		return emergType;
	}
	public void setEmergType(String emergType) {
		this.emergType = emergType;
	}
	public List<DrillProjectDetailObjPreinfoEntity> getPreInfo() {
		return preInfo;
	}
	public void setPreInfo(List<DrillProjectDetailObjPreinfoEntity> preInfo) {
		this.preInfo = preInfo;
	}
	

}
