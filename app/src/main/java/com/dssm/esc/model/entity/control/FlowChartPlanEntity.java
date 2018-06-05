package com.dssm.esc.model.entity.control;

import java.io.Serializable;
import java.util.List;

/**
 * 流程图实体类
 *
 * @author Administrator
 */
public class FlowChartPlanEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 当前时间
     */
    private String curDate;
    /**
     * 预计占用时间
     */
    private String duration;
    /**
     * 状态
     */
    private String state;
    /**
     * 节点集合
     */
    private List<FlowChart> data;


    public String getCurDate() {
        return curDate;
    }


    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }


    public String getDuration() {
        return duration;
    }


    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }


    public List<FlowChart> getData() {
        return data;
    }


    public void setData(List<FlowChart> data) {
        this.data = data;
    }


    public class FlowChart implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 节点颜色值
         */
        private String code;

        /**
         * 创建时间
         */
        private String createTime;
        /**
         * 执行人类型
         * 0.无执行人 1 A角 2.B角 3.C角 4.指派
         */
        private String executePeopleType;
        /**
         * 节点类型
         * ExclusiveGateway=判断节点，UserTask=其他节点
         */
        private String nodeStepType;

        public String getParentProcessStepId() {
            return parentProcessStepId;
        }

        public void setParentProcessStepId(String parentProcessStepId) {
            this.parentProcessStepId = parentProcessStepId;
        }

        /**
         * 当前节点所属预案的编号
         */
        private String parentProcessStepId;

        public String getNodeStepType() {
            return nodeStepType;
        }

        public void setNodeStepType(String nodeStepType) {
            this.nodeStepType = nodeStepType;
        }

        /**
         * 流程节点备注
         */
        private String remark;
        /**
         * 执行人id
         */
        private String executePeopleId;
        /**
         * 保存坐标样式
         */
        private String type;
        /**
         * 结束时间
         */
        private String endTime;
        /**
         * 流程id
         */
        private String id;
        /**
         * 风格
         */
        private String style;
        /**
         * 步骤名称
         */
        private String name;
        /**
         * 执行人姓名
         */
        private String executePeople;
        /**
         * 操作手册详细表ID
         */
        private String manualDetailId;
        /**
         * 执行人B
         */
        private String executorB;
        /**
         * 预案执行id
         */
        private String planInfoId;
        /**
         * 执行人A
         */
        private String executorA;
        /**
         * 节点执行状态
         * 全部完成：1，部分完成：2，跳过：3，正在执行：4，可执行：5,准备执行：6，还未执行：7
         */
        private String status;
        /**
         * 修改时间
         */
        private String updateTime;
        /**
         * 排序号
         */
        private String orderNum;
        /**
         * 开始时间
         */
        private String beginTime;
        /**
         * 演练编号
         */
        private String drillId;
        /**
         * 完成信息
         */
        private String message;
        /**
         * 下一个点节点集合
         */
        private List<ProcessEntity> processlist;
        private String process;
        /**
         * 当他为空时不是子节点，不为空时是子节点
         */
        private String pId;
        /**
         * 事件评估id
         */
        private String planEveId;
        /**
         * 预计占用时间
         */
        private String duration;
        /**
         * 创建人
         */
        private String createUser;
        /**
         * 执行人C
         */
        private String executorC;
        /**
         * 预案id
         */
        private String precautionId;
        /**
         * 是否显示节点
         */
        private String isShow;
        /**
         * 修改人
         */
        private String updateUser;
        private String editOrderNum;

        //节点所属预案层级，多一级子预案数值加1
        private int index = 0;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getCode(){
            return code;
        }

        public void setCode(String code){
            this.code = code;
        }

        public String getEditOrderNum() {
            return editOrderNum;
        }

        public void setEditOrderNum(String editOrderNum) {
            this.editOrderNum = editOrderNum;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getExecutePeopleType() {
            return executePeopleType;
        }

        public void setExecutePeopleType(String executePeopleType) {
            this.executePeopleType = executePeopleType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getExecutePeopleId() {
            return executePeopleId;
        }

        public void setExecutePeopleId(String executePeopleId) {
            this.executePeopleId = executePeopleId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExecutePeople() {
            return executePeople;
        }

        public void setExecutePeople(String executePeople) {
            this.executePeople = executePeople;
        }

        public String getManualDetailId() {
            return manualDetailId;
        }

        public void setManualDetailId(String manualDetailId) {
            this.manualDetailId = manualDetailId;
        }

        public String getExecutorB() {
            return executorB;
        }

        public void setExecutorB(String executorB) {
            this.executorB = executorB;
        }

        public String getPlanInfoId() {
            return planInfoId;
        }

        public void setPlanInfoId(String planInfoId) {
            this.planInfoId = planInfoId;
        }

        public String getExecutorA() {
            return executorA;
        }

        public void setExecutorA(String executorA) {
            this.executorA = executorA;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getDrillId() {
            return drillId;
        }

        public void setDrillId(String drillId) {
            this.drillId = drillId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ProcessEntity> getProcesslist() {
            return processlist;
        }

        public void setProcesslist(List<ProcessEntity> processlist) {
            this.processlist = processlist;
        }

        public String getpId() {
            return pId;
        }

        public void setpId(String pId) {
            this.pId = pId;
        }

        public String getPlanEveId() {
            return planEveId;
        }

        public void setPlanEveId(String planEveId) {
            this.planEveId = planEveId;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getExecutorC() {
            return executorC;
        }

        public void setExecutorC(String executorC) {
            this.executorC = executorC;
        }

        public String getPrecautionId() {
            return precautionId;
        }

        public void setPrecautionId(String precautionId) {
            this.precautionId = precautionId;
        }

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        /*"createTime": "2015-10-09 16:39:07",
         "executePeopleType": "0",
         "remark": null,
         "executePeopleId": null,
         "type": "end",
         "endTime": null,
         "id": "sid-40A877D5-F968-4CF6-B896-BD25F51C6E94",
         "style": "",
         "name": "结束",
         "executePeople": null,
         "manualDetailId": null,
         "executorB": null,
         "planInfoId": "13fc2dd8-c3bb-4282-9b0f-d1499de22e21",
         "executorA": null,
         "status": "",
         "updateTime": null,
         "orderNum": null,
         "beginTime": null,
         "drillId": null,
         "message": null,
         "process": [ ],
         "pId": "",
         "planEveId": "64f7af42-c3bb-490d-9ecb-e6ffd1fd39d6",
         "duration": null,
         "createUser": "[U0098]李大霄",
         "executorC": null,
         "precautionId": "870c17fe-b062-4a7d-8aa5-0df43c8d4ab8",
         "isShow": 1,
         "updateUser": null*/
        public class ProcessEntity {
            String nextid;
            String nextname;

            public String getNextid() {
                return nextid;
            }

            public void setNextid(String nextid) {
                this.nextid = nextid;
            }

            public String getNextname() {
                return nextname;
            }

            public void setNextname(String nextname) {
                this.nextname = nextname;
            }

        }
    }
}
