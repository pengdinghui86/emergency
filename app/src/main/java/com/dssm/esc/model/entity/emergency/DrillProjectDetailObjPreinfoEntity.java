package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

public class DrillProjectDetailObjPreinfoEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String precautionId;
	private String precautionName;
//	 "precautionType": "309d577a-a1fd-4cb2-8073-4ecc337aa6e1", 
//     "detailPlanId": "f1f47aa7-ac9f-447b-b3b9-67fb1706caa5", 
//     "precautionName": "IT恢复预案", 
//     "drillPrecautionId": "cf75ee73-2c95-4632-af6f-6bc713810efc", 
//     "precautionId": "9d707d56-f456-4f6a-a02d-d84b84debc1f"
	
	private String detailPlanId;
	private String precautionType;
	private String drillPrecautionId;
	
	public String getDetailPlanId() {
		return detailPlanId;
	}
	public void setDetailPlanId(String detailPlanId) {
		this.detailPlanId = detailPlanId;
	}
	public String getPrecautionType() {
		return precautionType;
	}
	public void setPrecautionType(String precautionType) {
		this.precautionType = precautionType;
	}
	public String getDrillPrecautionId() {
		return drillPrecautionId;
	}
	public void setDrillPrecautionId(String drillPrecautionId) {
		this.drillPrecautionId = drillPrecautionId;
	}
	public String getPrecautionId() {
		return precautionId;
	}
	public void setPrecautionId(String precautionId) {
		this.precautionId = precautionId;
	}
	public String getPrecautionName() {
		return precautionName;
	}
	public void setPrecautionName(String precautionName) {
		this.precautionName = precautionName;
	}
	

}
