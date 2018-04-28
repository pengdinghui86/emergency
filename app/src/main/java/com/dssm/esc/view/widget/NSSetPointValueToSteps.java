package com.dssm.esc.view.widget;

import com.dssm.esc.model.entity.control.FlowChartPlanEntity.FlowChart;

import java.util.ArrayList;
import java.util.List;

public class NSSetPointValueToSteps {

	public List<NSstep> steplist;

	public int rowId;

	public int rowNum;
	public int maxLineNum;

	/**
	 * 测试用例
	 */
	public void exampleSteps(List<FlowChart> list) {
		steplist = new ArrayList<NSstep>();

		for (FlowChart chart : list) {
			String[] nextsetpids = new String[chart.getProcesslist().size()];
			for (int i = 0; i < chart.getProcesslist().size(); i++) {
				nextsetpids[i] = chart.getProcesslist().get(i).getNextid();
			}
			steplist.add(new NSstep().setStep(chart.getId(), nextsetpids,
					chart.getStatus(), chart.getBeginTime(), chart.getName(),
					chart.getEditOrderNum(), chart.getType(),
					chart.getExecutePeople(), chart.getBeginTime(),
					chart.getEndTime(),chart.getCode()));
		}

		// /*steplist.add(new NSstep().setStep(10001, new int[]{1}, 10,
		// "预案开始"));
		//
		// steplist.add(new NSstep().setStep(1, new int[]{2,3,4}, 1, "22:12"));
		//
		// steplist.add(new NSstep().setStep(2, new int[]{5,6}, 1, "15:56"));
		// steplist.add(new NSstep().setStep(3, new int[]{7}, 2, "22:12"));
		// steplist.add(new NSstep().setStep(4, new int[]{8}, 1, "15:56"));
		//
		//
		// steplist.add(new NSstep().setStep(5, new int[]{9}, 1, "22:12"));
		// steplist.add(new NSstep().setStep(6, new int[]{13}, 1, "15:56"));
		// steplist.add(new NSstep().setStep(7, new int[]{10}, 2, "22:12"));
		// steplist.add(new NSstep().setStep(8, new int[]{11,12}, 1, "15:56"));
		//
		//
		// steplist.add(new NSstep().setStep(9, new int[]{14,15}, 1, "15:56"));
		// steplist.add(new NSstep().setStep(13, new int[]{15}, 2, "22:12"));
		// steplist.add(new NSstep().setStep(10, new int[]{15}, 2, "22:12"));
		// steplist.add(new NSstep().setStep(11, new int[]{16}, 1, "15:56"));
		// steplist.add(new NSstep().setStep(12, new int[]{16}, 1, "15:56"));
		//
		//
		// steplist.add(new NSstep().setStep(14, new int[]{17}, 1, "15:56"));
		// steplist.add(new NSstep().setStep(15, new int[]{17}, 1, "15:56"));
		// steplist.add(new NSstep().setStep(16, new int[]{17}, 1, "15:56"));*/
		/* steplist.add(new NSstep().setStep(17, new int[]{22}, 1, "15:56")); */
		/*
		 * steplist.add(new NSstep().setStep(18, new int[]{22}, 1, "15:56"));
		 * steplist.add(new NSstep().setStep(19, new int[]{22}, 1, "15:56"));
		 * steplist.add(new NSstep().setStep(20, new int[]{22}, 1, "15:56"));
		 * steplist.add(new NSstep().setStep(21, new int[]{22}, 1, "15:56"));
		 */

		/*
		 * //steplist.add(new NSstep().setStep(21, new int[]{29}, 1, "15:56"));
		 * steplist.add(new NSstep().setStep(22, new int[]{29}, 1, "15:56"));
		 * steplist.add(new NSstep().setStep(23, new int[]{29}, 2, "22:12"));
		 * steplist.add(new NSstep().setStep(24, new int[]{29}, 2, "22:12"));
		 * steplist.add(new NSstep().setStep(25, new int[]{29}, 2, "22:12"));
		 * steplist.add(new NSstep().setStep(26, new int[]{29}, 2, "22:12"));
		 * steplist.add(new NSstep().setStep(27, new int[]{29}, 2, "22:12"));
		 * steplist.add(new NSstep().setStep(28, new int[]{30}, 2, "22:12"));
		 * steplist.add(new NSstep().setStep(30, new int[]{29}, 2, "22:12"));
		 */

		// steplist.add(new NSstep().setStep(17, new int[]{10002}, 1, "15:56"));
		//
		// steplist.add(new NSstep().setStep(10002, new int[]{}, 10, "预案结束"));

		getStepPointBySteps();

	}

	public void getStepPointBySteps() {
		rowNum = findRowNumber(steplist);
		initStepVlaus();

	}

	/**
	 * 找总共多少行
	 * 
	 * @param steplist
	 * @return
	 */
	public int findRowNumber(List<NSstep> steplist) {
		List<NSstep> list = findLastStepId(steplist);
		if (list != null && list.size() > 0) {

			return getRowNumber(list);
		} else {
			return 0;
		}

	}

	/**
	 * 查找最后一级的点集合
	 * 
	 * @param steplist
	 * @return
	 */
	public List<NSstep> findLastStepId(List<NSstep> steplist) {

		List<NSstep> list = new ArrayList<NSstep>();
		for (NSstep sstep : steplist) {
			if (sstep.isTheLastStep()) {
				list.add(sstep);
			}
		}
		return list;
	}

	/**
	 * 总共查询到级数
	 * 
	 * @param steplist
	 * @return
	 */
	private int getRowNumber(List<NSstep> steplist) {
		List<NSstep> list = findParentStepsId(steplist);
		if (list != null && list.size() > 0)
			return 1 + getRowNumber(list);
		else
			return 1;

	}

	/**
	 * 根据当前级点集合查找上一级点集合
	 * 
	 * @param currentsteplist
	 * @return
	 */
	private List<NSstep> findParentStepsId(List<NSstep> currentsteplist) {
		this.rowId++;

		for (NSstep currentstep : currentsteplist) {
			currentstep.x = this.rowId;
			currentstep.lineId = this.rowId;
		}

		if (this.maxLineNum < currentsteplist.size()) {
			this.maxLineNum = currentsteplist.size();
		}
		List<NSstep> parentlist = new ArrayList<NSstep>();

		for (NSstep onestep : steplist) {

			for (NSstep currentstep : currentsteplist) {
				if (onestep.isParentStep(currentstep)) {
					parentlist.add(onestep);
					break;
				}
			}
		}

		return parentlist;
	}

	private void initStepVlaus() {
		// TODO Auto-generated method stub
		for (int i = 1; i <= rowNum; i++) {
			int j = 0;
			int k = 0;
			for (NSstep currentstep : steplist) {
				if (currentstep.lineId == i) {
					j++;
				}
			}
			for (NSstep currentstep : steplist) {
				if (currentstep.lineId == i) {
					k++;
					currentstep.x = (1 - currentstep.x / (rowNum + 1));
					currentstep.y = k / (j + 1.f);
				}
			}
		}

	}

	/*
	 * steplist.add(new NSstep().setStep(10001, new int[]{1}, 10, "预案开始"));
	 * 
	 * 
	 * steplist.add(new NSstep().setStep(1, new int[]{2,3,4}, 1, "22:12"));
	 * 
	 * steplist.add(new NSstep().setStep(2, new int[]{5,6}, 1, "15:56"));
	 * steplist.add(new NSstep().setStep(3, new int[]{7}, 2, "22:12"));
	 * steplist.add(new NSstep().setStep(4, new int[]{8}, 1, "15:56"));
	 * 
	 * 
	 * steplist.add(new NSstep().setStep(5, new int[]{9}, 1, "22:12"));
	 * steplist.add(new NSstep().setStep(6, new int[]{13}, 1, "15:56"));
	 * steplist.add(new NSstep().setStep(7, new int[]{10}, 2, "22:12"));
	 * steplist.add(new NSstep().setStep(8, new int[]{11,12}, 1, "15:56"));
	 * 
	 * 
	 * steplist.add(new NSstep().setStep(9, new int[]{15}, 1, "15:56"));
	 * steplist.add(new NSstep().setStep(13, new int[]{15}, 2, "22:12"));
	 * steplist.add(new NSstep().setStep(10, new int[]{15}, 2, "22:12"));
	 * steplist.add(new NSstep().setStep(11, new int[]{14}, 1, "15:56"));
	 * steplist.add(new NSstep().setStep(12, new int[]{16}, 1, "15:56"));
	 * 
	 * steplist.add(new NSstep().setStep(14, new int[]{17}, 1, "15:56"));
	 * steplist.add(new NSstep().setStep(15, new int[]{17}, 1, "15:56"));
	 * steplist.add(new NSstep().setStep(16, new int[]{17}, 2, "22:12"));
	 * 
	 * steplist.add(new NSstep().setStep(17, new int[]{10002}, 1, "15:56"));
	 * 
	 * steplist.add(new NSstep().setStep(10002, new int[]{}, 10, "预案结束"));
	 */
}
