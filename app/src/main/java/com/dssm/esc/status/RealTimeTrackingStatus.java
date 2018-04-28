package com.dssm.esc.status;

/**
 * 2018/4/24.
 * 预案流程节点执行状态
 */

public class RealTimeTrackingStatus {
    public static String COMPLETE = "1"; //"全部完成"
    public static String PORTION = "2"; //部分完成"
    public static String IGNORE = "3"; //跳过"
    public static String EXECUTING = "4"; //"执行中"
    public static String EXECUTABLE = "5"; //"可执行"
    public static String PREPARATION = "6"; //"准备执行"
    public static String BEFORE = "7"; //"未执行"
    public static String AUTO_EXECUTING = "8"; //"自动执行中"
    public static String EXECUTE_SUCCESS = "9"; //"已执行"
    public static String EXECUTE_FAULT = "10"; //"执行失败"

    /**
     * 执行暂停 10---20 为暂停状态
     */

    /**
     * 执行异常-超时（因为超时所以触发的异常）
     * */
    public static String EXCEPTION_EXCEED = "20"; //"接收超时"
    /**
     *执行异常-暂停
     */
    public static String EXCEPTION_OPTION_STOP = "21"; //"执行异常"
    /**
     *执行异常-启动
     */
    public static String EXCEPTION_OPTION_START = "22"; //"异常解除"
    /**
     *执行异常-超时
     */
    public static String EXCEPTION_OPTION_TIME_OUT = "25"; //"执行超时"

    /**
     * 不执行节点
     */
    public static String NO_OPTION_CAN_START = "23"; //"跳过"
    public static String NO_OPTION_NO_CAN_START = "24"; //"跳过"
    //NoOption("27", "跳过",ColorStates.color_gray),
    /**
     * 演练中新增或合并节点  已执行 ,---该状态已被弃用，现暂时用作app端的状态以便节点颜色的展示--dt 2018-3-20
     */
    public static String NO_OPTION_CAN_DRILL_START = "26"; //"已执行"
}
