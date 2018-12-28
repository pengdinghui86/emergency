package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;

import android.R.string;

/**
 * 通讯录子通讯录实体类（通讯录，签到，预案执行公用的子实体类）
 */
public class ChildEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String child_id;// 通讯录的人id
    private String name;// 通讯录的姓名，签到的姓名
    private String phoneNumber;// 通讯录的电话号码，签到的电话号码
    private String zhiwei;// 通讯录的职位，签到的职位
    private String sortLetters; // 显示数据拼音的首字母（通讯录查询用）
    private String signin;// 签到的状态
    private String step;// 预案执行的步骤
    private String status;// 预案执行的完成状态（全部完成：1，部分完成：2，跳过：3，正在执行：4，可执行：5,准备执行：6，还未执行：7）
    private String emergTeam;
    private String roleTypeName;
    private String sex;

    private String precautionId;// 预案id
    private String processName;
    private String planPerformId;//后台重新生成的流程编号
    private String planInfoId;
    private String manualDetailId;// 操作手册详细内容记录id
    // "id": "233629ca-f975-443f-9d43-90c27ced6282",
    // "precautionId": "673629ca-f975-443f-9d43-90c27ced6282",
    // "processName": "打开阀门"
    // manualDetailId 操作手册详细内容记录id
    // planResType 演练计划类型 1、事件 2、演练计划
    // drillPrecautionId 演练计划id
    // name 步骤名称

    //	emergTeam	应急小组
//	emergTeamId	应急小组ID	
//	postName	岗位名称	
//	roleTypeName	职位	
//	userName	姓名	
//	telephone	联系电话	
//	email	邮箱	
//	signState	签到状态	0:未签到 1：已签到
//	noticeState	通知状态	0:未通知 1：已通知
    private String email;
    private String phoneNumOne;
    private String phoneNumTwo;
    private String postFlag;//岗位标识
    private String userId;
    private String onlyId;
    private String pId;

    //预案层级，多一层预案数值加1
    private int index;
    //预案步骤编号
    private String orderNumber;
    //预案父步骤编号
    private String parentOrderNumber;
    //预案步骤执行人名称
    private String executorName;
    //预案步骤执行人类型，0无执行人1A角2B角3C角4指派
    private String executePeopleType;
    //执行人A角
    private String executorAName;
    //执行人B角
    private String executorBName;
    //执行人C角
    private String executorCName;
    //是否需要签到才能执行步骤，0不需要1需要
    private String isSign = "1";
    private String parentProcessStepId;

    public String getExecutePeopleType() {
        return executePeopleType;
    }

    public void setExecutePeopleType(String executePeopleType) {
        this.executePeopleType = executePeopleType;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public String getExecutorAName() {
        return executorAName;
    }

    public void setExecutorAName(String executorAName) {
        this.executorAName = executorAName;
    }

    public String getExecutorBName() {
        return executorBName;
    }

    public void setExecutorBName(String executorBName) {
        this.executorBName = executorBName;
    }

    public String getExecutorCName() {
        return executorCName;
    }

    public void setExecutorCName(String executorCName) {
        this.executorCName = executorCName;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getParentOrderNumber() {
        return parentOrderNumber;
    }

    public void setParentOrderNumber(String parentOrderNumber) {
        this.parentOrderNumber = parentOrderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getParentProcessStepId() {
        return parentProcessStepId;
    }

    public void setParentProcessStepId(String parentProcessStepId) {
        this.parentProcessStepId = parentProcessStepId;
    }

    public String getPlanPerformId() {
        return planPerformId;
    }

    public void setPlanPerformId(String planPerformId) {
        this.planPerformId = planPerformId;
    }

    /**
     * 新增颜色节点
     * 2017/10/13
     */
    private String code; //新增颜色节点

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getOnlyId() {
        return onlyId;
    }

    public void setOnlyId(String onlyId) {
        this.onlyId = onlyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(String postFlag) {
        this.postFlag = postFlag;
    }

    private String signState;//signState	签到状态	0:未签到 1：已签到

    private String noticeState;//通知状态	0:未通知 1：已通知

    public String getNoticeState() {
        return noticeState;
    }

    public void setNoticeState(String noticeState) {
        this.noticeState = noticeState;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumOne() {
        return phoneNumOne;
    }

    public void setPhoneNumOne(String phoneNumOne) {
        this.phoneNumOne = phoneNumOne;
    }

    public String getPhoneNumTwo() {
        return phoneNumTwo;
    }

    public void setPhoneNumTwo(String phoneNumTwo) {
        this.phoneNumTwo = phoneNumTwo;
    }

    public String getSignState() {
        return signState;
    }

    public void setSignState(String signState) {
        this.signState = signState;
    }

    public String getRoleTypeName() {
        return roleTypeName;
    }

    public String getManualDetailId() {
        return manualDetailId;
    }

    public void setManualDetailId(String manualDetailId) {
        this.manualDetailId = manualDetailId;
    }

    public String getPlanInfoId() {
        return planInfoId;
    }

    public void setPlanInfoId(String planInfoId) {
        this.planInfoId = planInfoId;
    }

    public String getPrecautionId() {
        return precautionId;
    }

    public void setPrecautionId(String precautionId) {
        this.precautionId = precautionId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setRoleTypeName(String roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    public String getEmergTeam() {
        return emergTeam;
    }

    public void setEmergTeam(String emergTeam) {
        this.emergTeam = emergTeam;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignin() {
        return signin;
    }

    public void setSignin(String signin) {
        this.signin = signin;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public ChildEntity() {
    }

    public ChildEntity(String child_id, String name) {
        this.child_id = child_id;
        this.name = name;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getZhiwei() {
        return zhiwei;
    }

    public void setZhiwei(String zhiwei) {
        this.zhiwei = zhiwei;
    }

    Boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    /**
     * 判断节点
     * 2018/5/2
     */
    private String nodeStepType;//节点类型

    private List<BusinessTypeEntity> branches;//判断节点分支选项

    public String getNodeStepType() {
        return nodeStepType;
    }

    public void setNodeStepType(String nodeStepType) {
        this.nodeStepType = nodeStepType;
    }

    public List<BusinessTypeEntity> getBranches() {
        return branches;
    }

    public void setBranches(List<BusinessTypeEntity> branches) {
        this.branches = branches;
    }
}
