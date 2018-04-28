package com.dssm.esc.model.entity.control;

import java.io.Serializable;
/**
 * 5.1.2	展示事件流程
 * @author Administrator
 *
 */
public class ProgressDetailEntity implements Serializable {

	
	private static final long serialVersionUID = 1L;
	/**
	 * 决策授权
	 */
    private EvenDetail planAuth;
    /**
     * 预案执行
     */
    private EvenDetail planPerform;
    /**
     * 事件关闭
     */
    private EvenDetail eveClose;
    /**
     * 人员签到与指派
     */
    private EvenDetail personSign;
    /**
     * 预案启动
     */
    private EvenDetail planStart;
    /**
     * 事件评估
     */
    private EvenDetail eveAssess;
    /**
     * 事件开始时间
     */
    private String eveStartTime;
    /**
     * 完成流程节点数
     */
    private String progressNum;	
    /**
     * 服务器现在时间
     */
    private String nowTime;	
    
	public EvenDetail getPlanAuth() {
		return planAuth;
	}

	public void setPlanAuth(EvenDetail planAuth) {
		this.planAuth = planAuth;
	}

	public EvenDetail getPlanPerform() {
		return planPerform;
	}

	public void setPlanPerform(EvenDetail planPerform) {
		this.planPerform = planPerform;
	}

	public EvenDetail getEveClose() {
		return eveClose;
	}

	public void setEveClose(EvenDetail eveClose) {
		this.eveClose = eveClose;
	}

	public EvenDetail getPersonSign() {
		return personSign;
	}

	public void setPersonSign(EvenDetail personSign) {
		this.personSign = personSign;
	}

	public EvenDetail getPlanStart() {
		return planStart;
	}

	public void setPlanStart(EvenDetail planStart) {
		this.planStart = planStart;
	}

	public EvenDetail getEveAssess() {
		return eveAssess;
	}

	public void setEveAssess(EvenDetail eveAssess) {
		this.eveAssess = eveAssess;
	}

	public String getEveStartTime() {
		return eveStartTime;
	}

	public void setEveStartTime(String eveStartTime) {
		this.eveStartTime = eveStartTime;
	}

	public String getProgressNum() {
		return progressNum;
	}

	public void setProgressNum(String progressNum) {
		this.progressNum = progressNum;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public class EvenDetail {
		/**
		 * 流程节点开始时间
		 */
		private String startTime;
		/**
		 * 流程名称
		 */
		private String progress;
		/**
		 * 状态说明
		 */
		private String stateDesc;
		/**
		 * 流程节点详情
		 */
		private String remark;
		/**
		 * 状态
		 */
		private String state;
		/**
		 * 流程节点完成时间
		 */
		private String finishTime;
		/**
		 * 流程说明
		 */
		private String progressName;
		public String getStartTime() {
			return startTime;
		}
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		public String getProgress() {
			return progress;
		}
		public void setProgress(String progress) {
			this.progress = progress;
		}
		public String getStateDesc() {
			return stateDesc;
		}
		public void setStateDesc(String stateDesc) {
			this.stateDesc = stateDesc;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getFinishTime() {
			return finishTime;
		}
		public void setFinishTime(String finishTime) {
			this.finishTime = finishTime;
		}
		public String getProgressName() {
			return progressName;
		}
		public void setProgressName(String progressName) {
			this.progressName = progressName;
		}
		
		/* "startTime": "2015-10-14 17:00:06", 
	        "progress": "eveAssess", 
	        "stateDesc": "已完成", 
	        "remark": "事件评估完成", 
	        "state": "2", 
	        "finishTime": "2015-10-14 17:00:06", 
	        "progressName": "事件评估"*/
	}
}
