package com.dssm.esc.model.analytical;


import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.entity.control.SignUserEntity;

public interface ControlSevice {
	/**
	 * 获取展示事件列表
	 */
	void getEvalist(ControlServiceImpl.ControlServiceImplBackValueListenser<?> listenser);

	/**
	 * 5.2.1 待展示预案列表
	 * 
	 * @param listenser
	 */
	void getPlanlist(ControlServiceImpl.ControlServiceImplBackValueListenser<?> listenser);

	/**
	 * 5.2.2 预案终止
	 * 
	 * @param entity
	 * @param listenser
	 */
	void stopPlan(PlanEntity entity, String planSuspendOpition,
				  ControlServiceImpl.ControlServiceImplBackValueListenser<?> listenser);

	/**
	 * 5.2.3 流程启动
	 * 
	 * @param listenser
	 */
	void starPlan(String id, ControlServiceImpl.ControlServiceImplBackValueListenser<?> listenser);

	/**
	 * 5.2.4 流程图
	 * 
	 * @param listenser
	 */
	void flowChartPlan(String planInfoId,
					   ControlServiceImpl.ControlServiceImplBackValueListenser<FlowChartPlanEntity> listenser);

	/**
	 * 实时跟踪
	 * 
	 * @param planInfoId
	 * @param listenser
	 */
	void queryProcessTrack(String planInfoId,
						   ControlServiceImpl.ControlServiceImplBackValueListenser<FlowChartPlanEntity> listenser);

	/**
	 * 5.2.5 应急通知接收及人员签到情况详情
	 * 
	 * @param listenser
	 */
	void getNoticeAndSignList(String planInfoId,
							  ControlServiceImpl.ControlServiceImplBackValueListenser<SignUserEntity> listenser);

	/**
	 * 5.1.2 展示事件流程
	 * 
	 * @param listenser
	 */
	void getProgressDetail(String id,
						   ControlServiceImpl.ControlServiceImplBackValueListenser<ProgressDetailEntity> listenser);

	/**
	 * 5.2.6应急通知接收及人员签到详情
	 * 
	 * @param planInfoId
	 * @param listenser
	 */
	void getSignDetailInfo(String planInfoId,
						   ControlServiceImpl.ControlServiceImplBackValueListenser<?> listenser);
	/**
	 * 跳过此步骤
	 * @param id
	 * @param planInfoId
	 * @param listenser
	 */
	void jumpplan(String id, String planInfoId, ControlServiceImpl.ControlServiceImplBackValueListenser<?> listenser);


	void jumpplan2(String id, String planInfoId,String stopOrStart, ControlServiceImpl.ControlServiceImplBackValueListenser<?> listenser);

    /**
     * 暂停和开启
     * 2017/10/13
     */
    void pauseplan(String id, String planInfoId,String stopOrStart, ControlServiceImpl.ControlServiceImplBackValueListenser<?> listenser);

}
