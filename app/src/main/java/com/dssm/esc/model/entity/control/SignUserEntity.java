package com.dssm.esc.model.entity.control;

import java.io.Serializable;
import java.util.List;
/**
 * 5.2.5	应急通知接收及人员签到情况详情
 * @author Administrator
 *
 */
public class SignUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 已签到总人数
	 */
	private String totalSignNum;
	/**
	 * 需签到总人数
	 */
	private String totalNeedSignNum;
	/**
	 * 未签到总人数
	 */
	private String totalNoSignNum;
	/**
	 * 接收通知统计列表
	 */
	private List<Notice> noticeList;
	/**
	 * 签到统计列表
	 */
	private List<Sign>signList;
	
	
	/*"totalSignNum": 7, 
    "totalNeedSignNum": 249, 
    "totalNoSignNum": 242, */
	
	
	public String getTotalSignNum() {
		return totalSignNum;
	}

	public void setTotalSignNum(String totalSignNum) {
		this.totalSignNum = totalSignNum;
	}

	public String getTotalNeedSignNum() {
		return totalNeedSignNum;
	}

	public void setTotalNeedSignNum(String totalNeedSignNum) {
		this.totalNeedSignNum = totalNeedSignNum;
	}

	public String getTotalNoSignNum() {
		return totalNoSignNum;
	}

	public void setTotalNoSignNum(String totalNoSignNum) {
		this.totalNoSignNum = totalNoSignNum;
	}

	public List<Notice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<Notice> noticeList) {
		this.noticeList = noticeList;
	}

	public List<Sign> getSignList() {
		return signList;
	}

	public void setSignList(List<Sign> signList) {
		this.signList = signList;
	}

	public class Notice implements Serializable{
		
		private static final long serialVersionUID = 1L;
	/**
	 * 需通知人数
	 */
	private String needNoticeNum;
	/**
	 * 已接收通知人数
	 */
	private String noticeNum;
	/**
	 * 应急小组ID
	 */
	private String emergTeamId;
	/**
	 * 应急小组名称
	 */
	private String emergTeam;
	/* "needNoticeNum": 98, 
     "noticeNum": 3, 
     "emergTeamId": "81151632-5548-4d04-aca1-2ea4f339cb08", 
     "emergTeam": "业务恢复执行小组"*/
	public String getNeedNoticeNum() {
		return needNoticeNum;
	}
	public void setNeedNoticeNum(String needNoticeNum) {
		this.needNoticeNum = needNoticeNum;
	}
	public String getNoticeNum() {
		return noticeNum;
	}
	public void setNoticeNum(String noticeNum) {
		this.noticeNum = noticeNum;
	}
	public String getEmergTeamId() {
		return emergTeamId;
	}
	public void setEmergTeamId(String emergTeamId) {
		this.emergTeamId = emergTeamId;
	}
	public String getEmergTeam() {
		return emergTeam;
	}
	public void setEmergTeam(String emergTeam) {
		this.emergTeam = emergTeam;
	}
	}
	
	public class Sign implements Serializable{

		private static final long serialVersionUID = 1L;
		/**
		 * 需签到人数
		 */
		private String needSignNum;
		/**
		 * 已签到人数
		 */
		private String signNum;
		/**
		 * 应急小组ID
		 */
		private String emergTeamId;
		/**
		 * 应急小组名称
		 */
		private String emergTeam;
		/* "needSignNum": 98, 
         "signNum": 3, 
         "emergTeamId": "81151632-5548-4d04-aca1-2ea4f339cb08", 
         "emergTeam": "业务恢复执行小组"*/
		public String getNeedSignNum() {
			return needSignNum;
		}
		public void setNeedSignNum(String needSignNum) {
			this.needSignNum = needSignNum;
		}
		public String getSignNum() {
			return signNum;
		}
		public void setSignNum(String signNum) {
			this.signNum = signNum;
		}
		public String getEmergTeamId() {
			return emergTeamId;
		}
		public void setEmergTeamId(String emergTeamId) {
			this.emergTeamId = emergTeamId;
		}
		public String getEmergTeam() {
			return emergTeam;
		}
		public void setEmergTeam(String emergTeam) {
			this.emergTeam = emergTeam;
		}
		
	}
}
