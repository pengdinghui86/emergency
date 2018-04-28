package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;

/**
 * 签到详情实体类
 * 
 * @author zsj
 * 
 */
public class UserSignInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String emergTeam;// 组名
	private List<TeamUserListEntity> teamUserList;

	public String getEmergTeam() {
		return emergTeam;
	}

	public void setEmergTeam(String emergTeam) {
		this.emergTeam = emergTeam;
	}

	public List<TeamUserListEntity> getTeamUserList() {
		return teamUserList;
	}

	public void setTeamUserList(List<TeamUserListEntity> teamUserList) {
		this.teamUserList = teamUserList;
	}

}
