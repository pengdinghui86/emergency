package com.dssm.esc.view.widget;

import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
import com.dssm.esc.model.entity.control.FlowChartPlanEntity.FlowChart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NSSetPointValueToSteps{

	public List<NSstep> steplist = new ArrayList<>();

	public List<FlowChart> subFlowChart = new ArrayList<>();

	public int rowId;

	public int rowNum;
	public int maxLineNum;

	/**
	 * 测试用例
	 */
	public void exampleSteps(List<FlowChart> list, String parentId) {
		List<FlowChart> temp = new ArrayList<>(list);
		Collections.sort(temp, new Comparator<FlowChartPlanEntity.FlowChart>() {
			@Override
			public int compare(FlowChartPlanEntity.FlowChart o1, FlowChartPlanEntity.FlowChart o2) {
				if (o1.getEditOrderNum() == null || "".equals(o1.getEditOrderNum()))
					o1.setEditOrderNum("0");
				if (o2.getEditOrderNum() == null || "".equals(o2.getEditOrderNum()))
					o2.setEditOrderNum("0");
				if(Integer.parseInt(o1.getEditOrderNum()) > Integer.parseInt(o2.getEditOrderNum())) {
					return 1;
				}
				else if(Integer.parseInt(o1.getEditOrderNum()) < Integer.parseInt(o2.getEditOrderNum())) {
					return -1;
				}
				else {
					return 0;
				}
			}
		});
		if(steplist != null)
			steplist.clear();
		else
			steplist = new ArrayList<>();
		if(subFlowChart != null)
			subFlowChart.clear();
		else
			subFlowChart = new ArrayList<>();

		for (FlowChart chart : temp) {
			if(chart.getParentProcessStepId().equals(parentId)) {
				String[] nextsetpids = new String[chart.getProcesslist().size()];
				for (int i = 0; i < chart.getProcesslist().size(); i++) {
					nextsetpids[i] = chart.getProcesslist().get(i).getNextid();
				}
				steplist.add(new NSstep().setStep(chart.getId(), nextsetpids,
						chart.getStatus(), chart.getBeginTime(), chart.getName(),
						chart.getEditOrderNum(), chart.getType(), chart.getNodeStepType(),
						chart.getExecutePeople(), chart.getBeginTime(),
						chart.getEndTime(), chart.getCode()));
			}
			else
				subFlowChart.add(chart);
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
		initStepValues2();
	}

	/**
	 * 找总共多少行
	 * 
	 * @param steplist
	 * @return
	 */
	public int findRowNumber(List<NSstep> steplist) {
		List<NSstep> list = findLastStepId(steplist);
//		List<NSstep> list = findFirstStepId(steplist);
		if (list != null && list.size() > 0) {

			return getRowNumber(list);
		} else {
			return 0;
		}

	}

	/**
	 * 查找第一级的点集合
	 *
	 * @param steplist
	 * @return
	 */
	public List<NSstep> findFirstStepId(List<NSstep> steplist) {

		List<NSstep> list = new ArrayList<NSstep>();
		for (NSstep sstep : steplist) {
			if (sstep.isTheFirstStep()) {
				list.add(sstep);
			}
		}
		return list;
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
//		List<NSstep> list = findNextStepsId(steplist);
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

	private void initStepValues2() {

		for (int i = 1; i <= rowNum; i++) {
			int j = 0;
			int k = 0;
			float startY = 0f;
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

					List<NSstep> nextSteps = getNextRowNextSteps(currentStep, i);
					int nextStepCount = nextSteps.size();
					if(nextStepCount > 1) {
						float y = (nextSteps.get(0).y + nextSteps.get(nextStepCount - 1).y) / 2;
						if(y < startY + 1f / (maxLineNum + 1))
							y = startY + 1f / (maxLineNum + 1);
						if (y < currentStep.y) {
							currentStep.y = y;
						}
					}
					startY = currentStep.y;
				}
			}
		}
	}

	/**
	 * 获取下一行的子节点列表
	 */
	private List<NSstep> getNextRowNextSteps(NSstep step, int i) {
		List<NSstep> nSsteps = new ArrayList<>();
		for(NSstep nextStep : steplist) {
			for (String nextStepId : step.nextStepIds) {
				if(nextStepId.equals(nextStep.stepId) && nextStep.lineId == i - 1) {
					nSsteps.add(nextStep);
					break;
				}
			}
		}
		return nSsteps;
	}

	/**
	 * 优化节点的位置
	 */
	public void proveStepPosition() {
		for (int row = rowNum - 1; row >= 1; row--) {
			List<NSstep> currentSteps = findAllCurrentNodes(row);

			if(currentSteps.size() > 1) {

				for(int p = 1; p < currentSteps.size(); p++) {
					int j;
					NSstep parentNode = findLeftParentNode(currentSteps.get(p));
					NSstep temp = currentSteps.get(p);

					for (j = p; j > 0; j--) {
						NSstep parentNode1 = findLeftParentNode(currentSteps.get(j - 1));
						if (parentNode.y < parentNode1.y)
							//后移
							currentSteps.get(j).y = currentSteps.get(j - 1).y;
						else
							break;
					}
					currentSteps.get(j).y = temp.y;//插入到合适的位置
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

	/**
	 * 根据当前级点集合查找下一级点集合
	 *
	 * @param currentStepList
	 * @return
	 */
	private List<NSstep> findNextStepsId(List<NSstep> currentStepList) {
		this.rowId++;
		for (NSstep currentStep : currentStepList) {
			currentStep.x = this.rowId;
			currentStep.lineId = this.rowId;
		}

		if (this.maxLineNum < currentStepList.size()) {
			this.maxLineNum = currentStepList.size();
		}
		List<NSstep> nextList = new ArrayList<NSstep>();
		for (NSstep oneStep : steplist) {
			for (NSstep currentStep : currentStepList) {
				int count = 0;
				for(String id : currentStep.nextStepIds) {
					if (oneStep.stepId.equals(id)) {
						nextList.add(oneStep);
						count = 1;
						break;
					}
				}
				if(count == 1)
					break;
			}
		}
		return nextList;
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
