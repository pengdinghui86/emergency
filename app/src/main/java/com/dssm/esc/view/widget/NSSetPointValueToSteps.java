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
				if (o1.getOrderNum() == null || "".equals(o1.getOrderNum())) {
					if(o1.getEditOrderNum() != null && !o1.getEditOrderNum().equals(""))
						o1.setOrderNum(o1.getEditOrderNum());
					else
						o1.setOrderNum("0");
				}
				if (o2.getOrderNum() == null || "".equals(o2.getOrderNum())) {
					if(o2.getEditOrderNum() != null && !o2.getEditOrderNum().equals(""))
						o2.setOrderNum(o2.getEditOrderNum());
					else
						o2.setOrderNum("0");
				}
				if(Integer.parseInt(o1.getOrderNum()) > Integer.parseInt(o2.getOrderNum())) {
					return 1;
				}
				else if(Integer.parseInt(o1.getOrderNum()) < Integer.parseInt(o2.getOrderNum())) {
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
						chart.getOrderNum(), chart.getType(), chart.getNodeStepType(),
						chart.getExecutePeople(), chart.getBeginTime(),
						chart.getEndTime(), chart.getCode()));
			}
			else
				subFlowChart.add(chart);
		}
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
//		List<NSstep> list = findLastStepId(steplist);
		List<NSstep> list = findFirstStepId(steplist);
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
//		List<NSstep> list = findParentStepsId(steplist);
		List<NSstep> list = findNextStepsId(steplist);
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
//					currentStep.x = (1 - currentStep.x / (rowNum + 1));
					currentStep.x = currentStep.x / (rowNum + 1);
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
		for (int row = 1; row < rowNum; row++) {
			List<NSstep> currentSteps = findAllCurrentNodes(row);

			if(currentSteps.size() > 1) {
				float[] values = new float[currentSteps.size()];
				int n = 0;
				for(NSstep nSstep : currentSteps) {
					values[n++] = nSstep.y;
				}
				float[] tempValues = new float[currentSteps.size()];
				tempValues[0] = values[0];
				//插入排序
				for(int i = 1; i < currentSteps.size(); i++) {
					int j = i - 1;
					while (tempValues[j] > values[i] && j >= 0) {
						tempValues[j + 1] = tempValues[j];
						j--;
					}
					tempValues[j + 1] = values[i];
				}
				List<NSstep> tempSteps = new ArrayList<>(currentSteps);
				for(int i = 1; i < currentSteps.size(); i++) {
					int j = i - 1;
					NSstep parentNode1 = findLeftParentNode(currentSteps.get(i));
					while (j >= 0) {
						NSstep parentNode = findLeftParentNode(tempSteps.get(j));
						if (parentNode.y > parentNode1.y) {
							tempSteps.set(j + 1, tempSteps.get(j));
							j--;
						}
						else
							break;
					}
					tempSteps.set(j + 1, currentSteps.get(i));
				}
				for(int i = 0; i < tempSteps.size(); i++) {
					tempSteps.get(i).y = tempValues[i];
				}
			}
		}

		//检查某节点存在跨层次的子节点且子节点的x坐标值与该节点相同的情况
		for(NSstep nSstep : steplist) {
			for (String nextStepId : nSstep.nextStepIds) {
				NSstep nextStep = getStepById(nextStepId);
				if(nSstep.y == nextStep.y && Math.abs(nSstep.lineId - nextStep.lineId) > 1) {
					changeNodePosition(nSstep);
					break;
				}
			}
		}
	}

	//当某节点存在跨层次的子节点且子节点的x坐标值与该节点相同时，将该节点与同一层的其他节点交换位置，避免连接线穿过其他节点
	private void changeNodePosition(NSstep step) {
		List<NSstep> steplist = getOtherRowSteps(step);
		if(steplist.size() == 0)
			return;
		for(NSstep nSstep : steplist) {
			int flag = 0;
			for (String nextStepId : nSstep.nextStepIds) {
				NSstep nextStep = getStepById(nextStepId);
				if(step.y == nextStep.y && Math.abs(nSstep.lineId - nextStep.lineId) > 1) {
					flag = 1;
					break;
				}
			}
			if(flag == 0) {
				float temp = step.y;
				step.y = nSstep.y;
				nSstep.y = temp;
				break;
			}
		}
	}

	/**
	 * 获取同一行的其他所有节点
	 */
	private List<NSstep> getOtherRowSteps(NSstep step) {
		List<NSstep> nSsteps = new ArrayList<>();
		for(NSstep otherStep : steplist) {
			if(!otherStep.stepId.equals(step.stepId) && otherStep.lineId == step.lineId) {
				nSsteps.add(otherStep);
			}
		}
		return nSsteps;
	}

	/**
	 * 根据id获取节点信息
	 */
	private NSstep getStepById(String stepId) {
		NSstep step = null;
		for(NSstep nSstep : steplist) {
			if(stepId.equals(nSstep.stepId)) {
				step = nSstep;
				break;
			}
		}
		return step;
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
