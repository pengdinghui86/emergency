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
