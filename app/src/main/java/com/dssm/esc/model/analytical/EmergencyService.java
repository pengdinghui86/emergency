package com.dssm.esc.model.analytical;


import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.EmergencyPlanEvaAddEntity;
import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjEntity;
import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
import com.dssm.esc.model.entity.emergency.SendNoticyEntity;

public interface EmergencyService {
	/**
	 * 获取业务类型和事件等级
	 * 
	 * @param tag
	 *            1，业务类型；2，事件等级
	 * @param listenser
	 */
	void getBusinessType(int tag, EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 获取事件场景
	 * 
	 * @param listenser
	 */
	void getEventScene(EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 获取预案名称
	 * 
	 * @param tags
	 *            1,默认；2，其他；3，总预案
	 * @param id
	 *            事件场景id
	 * @param listenser
	 */
	void getPlanName(int tags, String id,
					 EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 添加评估
	 * 
	 * @param tag
	 *            1,应急2,演练
	 * @param addEntity
	 * @param listenser
	 */
	void addEmergencyPlanevent(String tag, EmergencyPlanEvaAddEntity addEntity,
							   EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 获取演练计划表
	 * 
	 * @param listenser
	 */
	void getDrillProjectName(EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 获取演练计划详情
	 * 
	 * @param detailPlanId
	 * @param listenser
	 */
	void getDrillProjectNameDetail(String detailPlanId, String drillPlanName,
								   EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 获取事件列表
	 * 
	 * @param listenser
	 */
	void getPlanStarList(EmergencyServiceImpl.EmergencySeviceImplListListenser listenser, String status);
	/**
	 * 获取事件已启动列表
	 * 
	 * @param listenser
	 */
	void getStarList(EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 获取启动事件详情
	 * 
	 * @param id
	 * @param listenser
	 */
	void getPlanStarListDetail(String id,
							   EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 预案驳回
	 * 
	 * @param planEveId
	 * @param planEveName
	 * @param submitterId
	 * @param eveType
	 * @param listenser
	 */
	void planStarBohui(String planEveId, String planEveName,
					   String submitterId, String eveType,
					   EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 预案启动
	 * 
	 * @param id
	 * @param usePlan
	 * @param detailObjEntity
	 * @param listenser
	 */
	void planStar(String id, String usePlan,
				  PlanStarListDetailObjEntity detailObjEntity,
				  EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 获取驳回事件列表
	 * 
	 * @param listenser
	 */
	void getBoHuiList(EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 获取事件评估信息
	 * 
	 * @param id
	 * @param listenser
	 */
	void getEventInfo(String id, EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 重新评估
	 * 
	 * @param entity
	 * @param listenser
	 */
	void reValuationEvent(GetProjectEveInfoEntity entity,
						  EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 事件删除
	 * 
	 * @param id
	 * @param listenser
	 */
	void deleteEvent(String id,
					 EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 列表展示
	 * 
	 * @param a
	 *            :1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示
	 * @param listenser
	 */
	void getAuthlist(int a, EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 预案详情
	 * 
	 * @param id
	 * @param listenser
	 */
	void getPlanDetail(String id, EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 预案中止
	 * 
	 * @param suspandEntity
	 */
	void planSuspand(PlanSuspandEntity suspandEntity,
					 EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 预案授权
	 * 
	 * @param id
	 * @param planAuthOpition
	 * @param listenser
	 */
	void planAuth(String id, String planAuthOpition, String planName,
				  String planResName, String planResType, String planId, String planStarterId, String submitterId,
				  EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 签到详情
	 * 
	 * @param planInfoId
	 * @param listenser
	 */
	void getSignEmergencyInfo(String planInfoId,
							  EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 判断用户是否签到
	 * 
	 * @param planInfoId
	 * @param listenser
	 */
	void checkEmergencySign(String planInfoId,
							EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 签到
	 * 
	 * @param planInfoId
	 * @param listenser
	 */
	void signIn(String planInfoId,
				EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 预案步骤
	 * 
	 * @param planInfoId
	 * @param listenser
	 */
	void getPlanProcessList(String planInfoId,
							EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);

	/**
	 * 人员指派
	 * 
	 * @param id
	 * @param planInfoId
	 * @param executePeopleId
	 * @param executePeople
	 * @param listenser
	 */
	void assign(String id, String planInfoId, String executePeopleId,
				String executePeople,
				EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 执行列表
	 * 
	 * @param listListenser
	 */
	void getPlanExecute(String planInfoId, EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser);

	/**
	 * 待执行步骤
	 * 

	 * @param planInfoId
	 * @param listListenser
	 */
	void getPerformPlanExcute(String planInfoId,
							  EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser);

	/**
	 * 任务执行
	 * 
	 * @param id
	 * @param planInfoId
	 * @param listenser
	 */
	void getBeginPlan(String id, String planInfoId,
					  EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 任务切换
	 * 
	 * @param id
	 * @param planInfoId
	 * @param status
	 * @param message
	 * @param nodeStepType
	 * @param branch
	 * @param listenser
	 */
	void swichOver(String id, String planInfoId, String status, String message,String nodeStepType,String branch,
				   EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 发送通知
	 * 
	 * @param noticyEntity
	 * @param listenser
	 */
	void sendNotice(SendNoticyEntity noticyEntity,
					EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser);

	/**
	 * 预案小组列表
	 * 
	 * @param planInfoId
	 * @param precautionId
	 * @param listenser
	 */
	void getEmergencyGropData(String planInfoId, String precautionId,
							  EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);
	
	/**
	 * 根据对象和阶段获取对应的配置内容
	 * @param precautionId 预案id
	 * @param type 发送对象
	 * @param stage 阶段
	 * @param listenser
	 */
	void getNotiConfigContent(String precautionId, String type,
							  String stage,
							  EmergencyServiceImpl.EmergencySeviceImplListListenser listenser);
	/**
	 * 4.2.8预案中止（+）
	 * @param entity
	 * @param listListenser
	 */
	void suspand(PlanSuspandEntity entity, EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listListenser);
}
