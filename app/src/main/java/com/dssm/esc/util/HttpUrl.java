package com.dssm.esc.util;

import com.easemob.chatuidemo.DemoApplication;

/**
 * 各个接口的URL
 *
 * @author zsj
 */
public class HttpUrl {
    //测试环境
//    private static final String  = "http://192.168.1.203:7001/bcm/app/";
//    private static final String _VERSION = "http://192.168.1.203:7001/bcm/";
    //云服务器
//    private static final String  = "http://www.1sc-china.com/bcm/app/";
//    private static final String _VERSION = "http://www.1sc-china.com/bcm/app/bcm/";
    //http://192.168.1.203:7001/bcm/app/
//    public static String  = DemoApplication.getInstance().getUrl();
    /**
     * 登陆
     */
    public final static String LOGIN =  "user/login";
    /**
     * 用户重新登录
     */
    public final static String RELOGIN =  "user/againLogin";
    /**
     * 角色选择
     */
    public final static String LOGINROLE =  "user/loginRole";
    /**
     * 根据用户id获取用户姓名
     */
    public final static String GETUSERNAMEBYID =  "user/findById?id=";
    /**
     * 1.1.6用户权限控制
     */
    public final static String GETMENUPOWER =  "user/getMenuPower";

    // --------------------------------------应急管理-------------------------------------------------
    /**
     * 添加评估（应急）
     */
    public final static String EMERGENCY_PLANEVA_ADD = 
             "emergency/planEva/add";
    /**
     * 添加评估(演练)
     */
    public final static String DRILL_ADD =  "emergency/planEva/drill/add";
    /**
     * 业务类型
     */
    public final static String GET_BUSINESSTYPE = 
             "param/getParamValues?paramCode=businessType";
    /**
     * 事件等级
     */
    public final static String GET_EVENTLEVEL = 
            "param/getParamValues?paramCode=eventLevel";
    /**
     * 事件场景
     */
    public final static String GET_EVENTSCENCE_FIELDS =  "param/fields";
    /**
     * 预案名称(默认)
     */
    public final static String GET_PREBTSCENARIOID = 
             "emergency/planEva/getPreByScenarioId";
    /**
     * 预案名称(其他类型)
     */
    public final static String GET_OTEHERPREBTSCENARIOID = 
             "precaution/otherSceneDetailList";
    /**
     * 预案名称(总预案)
     */
    public final static String GET_CATEGORYPREBTSCENARIOID = 
             "precaution/categoryPrecautionList";

    /**
     * 演练计划表
     */
    public final static String GET_DRILLPROJECTNAME = 
            "emergency/planEva/getDrillProjectName";
    /**
     * 演练计划详情
     */
    public final static String DRILLPROJECTNAMEDETAIL = 
           "emergency/planEva/getDrillPrecautionDetail";
    /**
     * 预案启动列表
     */
    public final static String GETPLANSTARLIST = 
             "emergency/planEva/getStartList?state=0";
    /**
     * 预案启动详情
     */
    public final static String PLANSTARDETAIL = 
             "emergency/planEva/getEveInfoById";

    /**
     * 预案启动
     */
    public final static String PLANSTARD =  "emergency/planInfo/start";

    /**
     * 预案驳回
     */
    public final static String PLANSTARD_BOHUI = 
            "emergency/planEva/reject";
    /**
     * 驳回列表
     */
    public final static String GET_BOHUILIST = 
             "emergency/planEva/getRejectEvaName";
    /**
     * 获取事件评估信息
     */
    public final static String GET_REJECTEVENTINFO = 
           "emergency/planEva/getRejectEvaInfo?id=";
    /**
     * 重新评估
     */
    public final static String RE_VALUTEEVENT =  "emergency/planEva/reva";
    /**
     * 事件删除
     */
    public final static String DELETEEVENT =  "emergency/planEva/delete";
    /**
     * 授权决策列表
     */
    public final static String GETAUTHLIST = 
             "emergency/planInfo/getToAuthList";
    /**
     * 预案详情
     */
    public final static String GETPLANDETAIL = 
             "emergency/planInfo/getDetailById?id=";
    /**
     * 预案中止
     */
    public final static String PLANSUSPAND =  "emergency/planInfo/suspend";
    /**
     * 预案授权
     */
    public final static String PLANAUTH =  "emergency/planInfo/auth";
    /**
     * 签到详情
     */
    public final static String GETSIGNUSERINFO = 
            "emergency/personSign/getSignUserInfo?id=";

    /**
     * 判断用户是否签到
     */
    public final static String CHECKPERSONSIGN = 
           "emergency/personSign/checkPersonSign";
    /**
     * 签到
     */
    public final static String SIGNIN =  "emergency/personSign/sign";
    /**
     * 预案步骤列表
     */
    public final static String GETQUERYPROCESSLIST = 
            "emergency/planPerform/queryProcessList?planInfoId=";
    /**
     * 人员指派
     */
    public final static String ASSIGN =  "emergency/personSign/assign";

    /**
     * 预案执行列表
     */
    // public final static String GETWEITPERFROMPRELIST = 
    // + "emergency/planInfo/getPrecautionByPlanRes";
    public final static String GETWEITPERFROMPRELIST = 
             "emergency/planPerform/getPlanPerformByUser";

    /**
     * 待执行步骤
     */
    public final static String GETPERFORMBYEXECUTEPEOPLEID = 
             "emergency/planPerform/getPerformByExecutePeopleId";

    /**
     * 任务执行
     */
    public final static String BEGINEXECUTEPLAN = 
            "emergency/planPerform/beginExecutePlan";

    /**
     * 任务切换
     */

    public final static String SWITHOVER = 
             "emergency/planPerform/switchover";
    /**
     * 操作手册(步骤)
     */
    public final static String SHOWOPENMANUAL = 
             "emergency/planPerform/showOperManual";
    /**
     * 操作手册(列表)
     */
    public final static String SHOWALLOPENMANUAL = 
            "emergency/planPerform/showAllOperManual";
    /**
     * 发送通告
     */
    public final static String SENDNOTICE = 
             "emergency/collaborAndNotices/sendNotice";
    /**
     * 待签到预案列表
     */
    public final static String GETPERSONPLAN = 
             "emergency/planInfo/getPersonPlan";
    /**
     * 预案列表(协同通告)
     */
    public final static String GETBYSTATELIST =
             "emergency/planInfo/getByStateList?state=1,2,3";
    /**
     * 待指派列表
     */
    public final static String GETASSIGNLISTBYSTATE = 
            "emergency/planInfo/getByStateList?state=1,2";
    /**
     * 预案小组应急列表
     */
    public final static String GETEMERGENCYGROPDATA = 
             "emergency/personSign/getmergencyGroupData?planInfoId=";
    /**
     * 根据对象和阶段获取对应的配置内容
     */
    public final static String GETNOTICONFIGCONTENT = 
             "emergency/personSign/getNotiConfigContent";

    /**
     * 4.2.6预案启动列表（+）
     */
    public final static String GETSTARTLIST = 
             "emergency/planInfo/startList";
    /**
     * 4.2.8预案中止（+）
     */
    public final static String SUSPAND =  "emergency/planInfo/suspend";
    /**
     * 4.3.6预案授权列表（+）
     */
    public final static String AUTHLIST =  "emergency/planInfo/authList";

    // ------------------------------------应急通讯录--------------------------------------
    /**
     * 3.1.1获取应急通讯录
     */
    public final static String GETEMECONTACTLIST = 
             "contact/emeContact/emeContactList";
    /**
     * 3.1.2发送消息
     */
    public final static String SENDMESSAGE =  "contact/notice/sendNotice";

    /**
     * 3.2.1获取应急通知配置
     */
    public final static String GETNOTICELIST =  "contact/notice/list";
    /**
     * 3.2.2发送应急通知
     */
    public final static String SENDNOTICECONTACT = 
             "contact/notice/sendNotice";

    // ------------------------------------消息--------------------------------------

    /**
     * 1.1.1查询所有消息（第一次查询）
     */
    public final static String GETFIRSTALLMESSAGES = 
             "message/getAllAppMessages";
    /**
     * 1.1.2根据消息类型查询消息(刷新)
     */
    public final static String GETMESSAGE =  "message/getAppMessages";

    /**
     * 1.1.3根据消息类型批量确认未读消息
     */
    public final static String CONFREMSG =  "message/confirMsgsByMsgType";

    // ------------------------------------指挥中心--------------------------------------
    /**
     * 5.1.1 展示事件列表
     */
    public final static String GET_EVEN_LIST = 
            "emergency/planEva/getEvaProgressList";
    /**
     * 5.1.2 展示事件流程
     */
    public final static String PROGRESS_DETAIL = 
            "emergency/planEva/getProgressDetail";
    /**
     * 5.2.1 待展示预案列表
     */
    public final static String GET_PLAN_LIST = 
            "emergency/planInfo/getPrecautionByPlanRes";

    /**
     * 5.2.2 预案终止
     */
    public final static String STOP_PLAN =  "emergency/planInfo/suspend";
    /**
     * 5.2.3 流程启动
     */
    public final static String STAR_PLAN =  "emergency/planInfo/flowStart";
    /**
     * 5.2.4 流程图
     */
    public final static String FLOW_CHART_PLAN = 
             "emergency/planPerform/queryProcess";
    /**
     * 实时跟踪列表
     */
    public final static String QUERYPROCESSTRACK = 
             "emergency/planPerform/queryProcessTrack";

    /**
     * 5.2.5 应急通知接收及人员签到情况详情
     */
    public final static String SIGN_USER_LIST_COUNT = 
             "emergency/personSign/getSignUserCount";

    /**
     * 5.2.6应急通知接收及人员签到详情
     */
    public final static String SIGN_USER_LIST_COUNT_DETAIL = 
             "emergency/personSign/getSignUserInfo?id=";
    // -------------------------------------查询------------------------------
    /**
     * 6.2.1预案查询
     */
    public final static String OTHERSCENDETAILLIST = 
             "precaution/otherSceneDetailList?auditState=2&name=";
    /**
     * 查询执行人联系方式
     */
    public  static String QUERYUSERBYEXECUTEPEOPELID = 
             "emergency/planPerform/queryUserByExecutePeopleId?executePeopleId=";
    /**
     * 版本更新
     */
    public  static String VERSIONUPDATE =  "bcm/model/app/version.xml";
    /**
     * 跳过执行中的步骤
     */
    public final static String JUMPPLAN = 
            "emergency/planPerform/switchover";


    /**
     * 跳过非“执行中”的步骤
     */
    public final static String JUMPPLAN2 = "emergency/planPerform/planPerformNoOptionOrClear";


    /**
     * 暂停和开启
     * 2017/10/13
     */
    public final static String PAUSEPLAN = "emergency/planPerform/planPerformStopOrStart";



}
