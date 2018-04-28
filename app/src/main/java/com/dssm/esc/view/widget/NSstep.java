package com.dssm.esc.view.widget;

import android.graphics.Point;

public class NSstep {
	/**
	 * 点的id type为空并且id不是已sid开头的为新建节点
	 */
	public String stepId;
	/**
	 * 下一个点的数组
	 */
	public String nextsetpids[];
	/**
	 * 状态id
	 */
	public String statusId;
	/**
	 * 相对坐标位置
	 */
	public float x, y;
	/**
	 * 中心点（划线）
	 */
	public Point point;
	/**
	 * 行号
	 */
	public int lineId;
	/**
	 * 时长
	 */
	public String timenote;

	/**
	 * 步骤名称
	 */
	public String name;
	/**
	 * 流程节点序号
	 */
	public String editOrderNum;
	/**
	 * 类型
	 */
	public String type;
	/**
	 * 执行人
	 */
	public String executePeople;
	/**
	 * 开始时间
	 */
	public String beginTime;
	/**
	 * 接收时间
	 */
	public String endTime;

	/**
	 * 流程节点颜色
	 */
	public String color;


	public NSstep setStep(String stepId, String[] nextsetpids, String name,
			String editOrderNum, String type, String executePeople,
			String beginTime, String endTime,String color) {

		return setStep(stepId, nextsetpids, "0", name, editOrderNum, type,
				executePeople, beginTime, endTime,color);
	}

	public NSstep setStep(String stepId, String[] nextsetpids, String statusId,
			String name, String editOrderNum, String type,
			String executePeople, String beginTime, String endTime,String color) {

		return setStep(stepId, nextsetpids, statusId, "0001", name,
				editOrderNum, type, executePeople, beginTime, endTime,color);
	}

	public NSstep setStep(String stepId, String[] nextsetpids, String statusId,
			String timenote, String name, String editOrderNum, String type,
			String executePeople, String beginTime, String endTime,String color) {
		this.stepId = stepId;
		this.nextsetpids = nextsetpids;
		this.statusId = statusId;
		this.timenote = timenote;
		this.name = name;
		this.editOrderNum = editOrderNum;
		this.type = type;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.executePeople = executePeople;
		this.color = color;
		return this;
	}

	/**
	 * 是否没有子级
	 * 
	 * @return
	 */
	public boolean isTheLastStep() {
		// TODO Auto-generated method stub
		if (this.nextsetpids.length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前点是否为currentstep父节点
	 * 
	 * @param currentstep
	 */
	public boolean isParentStep(NSstep currentstep) {
		// TODO Auto-generated method stub
		for (String i : nextsetpids) {
			if (i.equals(currentstep.stepId)) {
				return true;
			}
		}
		return false;
	}

}
