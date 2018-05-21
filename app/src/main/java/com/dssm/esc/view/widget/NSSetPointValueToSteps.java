package com.dssm.esc.view.widget;

import com.dssm.esc.model.entity.control.FlowChartPlanEntity.FlowChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		Collections.sort(steplist, new Comparator<NSstep>() {
			@Override
			public int compare(NSstep o1, NSstep o2) {
				if (o1.editOrderNum == null || "".equals(o1.editOrderNum))
					o1.editOrderNum = "0";
				if (o2.editOrderNum == null || "".equals(o2.editOrderNum))
					o2.editOrderNum = "0";
				if(Integer.parseInt(o1.editOrderNum) > Integer.parseInt(o2.editOrderNum)) {
					return 1;
				}
				else if(Integer.parseInt(o1.editOrderNum) < Integer.parseInt(o2.editOrderNum)) {
					return -1;
				}
				else {
					return 0;
				}
			}
		});
		rowNum = findRowNumber(steplist);
		initStepValues();
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
	 * @param currentStepList
	 * @return
	 */
	private List<NSstep> findParentStepsId(List<NSstep> currentStepList) {
		this.rowId++;
		for (NSstep currentStep : currentStepList) {
			currentStep.x = this.rowId;
			currentStep.lineId = this.rowId;
		}

		if (this.maxLineNum < currentStepList.size()) {
			this.maxLineNum = currentStepList.size();
		}
		List<NSstep> parentList = new ArrayList<NSstep>();

		for (NSstep oneStep : steplist) {

			for (NSstep currentStep : currentStepList) {
				if (oneStep.isParentStep(currentStep)) {
					parentList.add(oneStep);
					break;
				}
			}
		}
		return parentList;
	}

	private void initStepValues() {
		// TODO Auto-generated method stub

		for (int i = 1; i <= rowNum; i++) {
			int j = 0;
			int k = 0;
			for (NSstep currentStep : steplist) {
				if (currentStep.lineId == i) {
					j++;
				}
			}
			//计算每一个节点的绝对坐标
			for (NSstep currentStep : steplist) {
				if (currentStep.lineId == i) {
					k++;
					currentStep.stepNum = j;
					currentStep.x = (1 - currentStep.x / (rowNum + 1));
					currentStep.y = k / (j + 1f);
				}
			}
		}

	}

	/**
	 * 优化节点的位置
	 */
	public void proveStepPosition() {
		for (int row = rowNum; row >= 1; row--) {
			List<NSstep> currentSteps = findAllCurrentNodes(row);
			if(currentSteps.size() > 1) {
				for(int i = 0; i < currentSteps.size() - 1; i++) {
					NSstep parentNode1 = findLeftParentNode(currentSteps.get(i));
					NSstep parentNode2 = findLeftParentNode(currentSteps.get(i + 1));
					if(parentNode1 != null && parentNode2 != null) {
						if ((parentNode1.lineId == parentNode2.lineId && parentNode1.y > parentNode2.y)
								|| parentNode1.lineId > parentNode2.lineId) {
							float y = currentSteps.get(i).y;
							currentSteps.get(i).y = currentSteps.get(i + 1).y;
							currentSteps.get(i + 1).y = y;
						}
					}
				}

			}
		}
	}

	private List<NSstep> findAllCurrentNodes(int lineId) {
		List<NSstep> currentList = new ArrayList<>();
		for (NSstep oneStep : steplist) {
			if (oneStep.lineId == lineId) {
				currentList.add(oneStep);
			}
		}
		return currentList;
	}

	private List<NSstep> findAllParentNodes(NSstep step) {
		List<NSstep> parentList = new ArrayList<>();
		for (NSstep oneStep : steplist) {
			if (oneStep.isParentStep(step)) {
				parentList.add(oneStep);
			}
		}
		return parentList;
	}

	private NSstep findLeftParentNode(NSstep step) {
		NSstep parent = null;
		for (NSstep oneStep : steplist) {
			if (oneStep.isParentStep(step)) {
				if(parent == null)
					parent = oneStep;
				else {
					if(parent.y > oneStep.y)
						parent = oneStep;
				}
			}
		}
		return parent;
	}

	private List<NSstep> findAllNextNodes(NSstep step, int lineId) {
		List<NSstep> list = new ArrayList<>();
		for (NSstep oneStep : steplist) {
			for(String id : step.nextStepIds) {
				if (oneStep.stepId.equals(id) && lineId - 1 == oneStep.lineId) {
					list.add(oneStep);
					break;
				}
			}
		}
		return list;
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
