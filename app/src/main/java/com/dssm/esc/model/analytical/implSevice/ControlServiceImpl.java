package com.dssm.esc.model.analytical.implSevice;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.dssm.esc.model.analytical.ControlSevice;
import com.dssm.esc.model.entity.control.EventProgressEntity;
import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.entity.control.SignUserEntity;
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanTreeEntity;
import com.dssm.esc.model.jsonparser.ControlCompleterListenter;
import com.dssm.esc.model.jsonparser.control.FlowChartPlanParser;
import com.dssm.esc.model.jsonparser.control.GetEventlistParser;
import com.dssm.esc.model.jsonparser.control.GetPlanlistParser;
import com.dssm.esc.model.jsonparser.control.GetProgressDetailParser;
import com.dssm.esc.model.jsonparser.control.GetSignUserInfoDetailParser;
import com.dssm.esc.model.jsonparser.control.JumpPlan2Parser;
import com.dssm.esc.model.jsonparser.control.JumpPlanParser;
import com.dssm.esc.model.jsonparser.control.NoticeAndSignListParser;
import com.dssm.esc.model.jsonparser.control.PausePlanParser;
import com.dssm.esc.model.jsonparser.control.QueryProcessTrackParser;
import com.dssm.esc.model.jsonparser.control.StarPlanParser;
import com.dssm.esc.model.jsonparser.control.StopPlanParser;

public class ControlServiceImpl implements ControlSevice {

    private static ControlServiceImpl controlServiceImpl = null;

    /**
     * 获取controlServiceImpl对象
     *
     * @return
     */
    public synchronized static ControlServiceImpl getControlSeviceImpl() {
        // TODO Auto-generated method stub
        if (controlServiceImpl == null) {
            controlServiceImpl = new ControlServiceImpl();
        }
        return controlServiceImpl;
    }

    public interface ControlServiceImplBackValueListenser<T> {
        /**
         * 当backValue！=null时stRerror，Exceptionerror都为null；
         * 当stRerror！=null时backValue，Exceptionerror都为null；
         * 当Exceptionerror！=null时backValue，stRerror都为null； backValue回调得到值
         * stRerror回调得到接口返回错误信息 Exceptionerror回调得到接口返回异常信息
         *
         * @param stRerror
         * @param Exceptionerror
         */
        void setControlServiceImplListenser(T backValue, String stRerror,
                                            String Exceptionerror);
    }

    /**
     * 设置集合返回数据
     *
     * @param listenser
     * @param list1
     * @param error
     */
    private void setContorlListListenser(
            ControlServiceImplBackValueListenser<List<?>> listenser,
            List<?> list1, String error) {
        List<?> list = null;
        String Exceptionerror = null;
        if (list1 != null) {
            list = list1;

        } else {
            Exceptionerror = error;
        }
        Log.i("ContorlList", "ContorlList" + list);
        listenser.setControlServiceImplListenser(list, null, Exceptionerror);
    }

    /**
     * 设置Boolean值返回数据
     *
     * @param listenser
     * @param map
     * @param error
     */
    private void setContorlBooleanListenser(
            ControlServiceImplBackValueListenser<Boolean> listenser,
            Map<String, String> map, String error) {
        boolean flag = false;
        String stRerror = null;
        String Exceptionerror = null;
        if (map != null) {
            if (map.containsKey("success") && map.containsKey("message")) {
                if (map.get("success").equals("true")) {
                    flag = true;

                } else {
                    flag = false;
                    stRerror = "失败";
                }
                if(map.containsKey("message") && !map.get("message").toString().equals("null"))
                    stRerror = map.get("message").toString();
            } else {
                stRerror = "未访问到数据";
            }
        } else {
            Exceptionerror = error;
        }
        Log.i("stopPlan", "stopPlan" + flag);
        listenser
                .setControlServiceImplListenser(flag, stRerror, Exceptionerror);
    }

    /**
     * 获取展示事件列表
     */
    @Override
    public void getEvalist(
            ControlServiceImplBackValueListenser<?> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<?>> wr = new WeakReference<ControlServiceImplBackValueListenser<?>>(listenser);
        new GetEventlistParser(
                new ControlCompleterListenter<List<BoHuiListEntity>>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void controlParserComplete(
                            List<BoHuiListEntity> object, String error) {
                        // TODO Auto-generated method stub
                        if(wr.get() != null)
                            setContorlListListenser(
                                (ControlServiceImplBackValueListenser<List<?>>) wr.get(),
                                object, error);
                    }
                });
    }

    /**
     * 5.2.1 待展示预案列表
     *
     * @param listenser
     */
    @Override
    public void getPlanlist(
            ControlServiceImplBackValueListenser<?> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<?>> wr = new WeakReference<ControlServiceImplBackValueListenser<?>>(listenser);
        new GetPlanlistParser(
                new ControlCompleterListenter<List<PlanEntity>>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void controlParserComplete(List<PlanEntity> object,
                                                      String error) {
                        // TODO Auto-generated method stub
                        if(wr.get() != null)
                            setContorlListListenser(
                                (ControlServiceImplBackValueListenser<List<?>>) wr.get(),
                                object, error);
                    }
                });
    }

    /**
     * 5.2.2 预案终止
     *
     * @param entity
     * @param listenser
     */
    @Override
    public void stopPlan(PlanEntity entity, String planSuspendOpition,
                         ControlServiceImplBackValueListenser<?> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<?>> wr = new WeakReference<ControlServiceImplBackValueListenser<?>>(listenser);
        new StopPlanParser(entity, planSuspendOpition,
                new ControlCompleterListenter<Map<String, String>>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void controlParserComplete(
                            Map<String, String> object, String error) {
                        // TODO Auto-generated method stub
                        if(wr.get() != null)
                            setContorlBooleanListenser(
                                (ControlServiceImplBackValueListenser<Boolean>) wr.get(),
                                object, error);
                    }
                });

    }

    /**
     * 5.2.3 流程启动
     *
     * @param listenser
     */
    @Override
    public void starPlan(String id,
                         ControlServiceImplBackValueListenser<?> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<?>> wr = new WeakReference<ControlServiceImplBackValueListenser<?>>(listenser);
        new StarPlanParser(id,
                new ControlCompleterListenter<Map<String, String>>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void controlParserComplete(
                            Map<String, String> object, String error) {
                        // TODO Auto-generated method stub
                        if(wr.get() != null)
                            setContorlBooleanListenser(
                                (ControlServiceImplBackValueListenser<Boolean>) wr.get(),
                                object, error);
                    }
                });
    }

    /**
     * @param listenser
     */
    @Override
    public void flowChartPlan(
            String planInfoId,
            ControlServiceImplBackValueListenser<FlowChartPlanEntity> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<FlowChartPlanEntity>> wr = new WeakReference<>(listenser);

        new FlowChartPlanParser(planInfoId,
                new ControlCompleterListenter<FlowChartPlanEntity>() {

                    @Override
                    public void controlParserComplete(
                            FlowChartPlanEntity object, String error) {
                        // TODO Auto-generated method stub
                        FlowChartPlanEntity entity = null;
                        String Exceptionerror = null;
                        if (object != null) {
                            entity = object;
                        } else if (error != null) {
                            Exceptionerror = error;
                        }
                        if(wr.get() != null)
                            wr.get().setControlServiceImplListenser(entity, null,
                                Exceptionerror);
                    }
                });
    }

    /**
     * 5.2.5 应急通知接收及人员签到情况详情
     *
     * @param listenser
     */
    @Override
    public void getNoticeAndSignList(String planInfoId,
                                     ControlServiceImplBackValueListenser<SignUserEntity> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<SignUserEntity>> wr = new WeakReference<>(listenser);
        new NoticeAndSignListParser(planInfoId,
                new ControlCompleterListenter<SignUserEntity>() {

                    @Override
                    public void controlParserComplete(SignUserEntity object,
                                                      String error) {
                        // TODO Auto-generated method stub
                        SignUserEntity entity = null;
                        String Exceptionerror = null;
                        if (object != null) {
                            entity = object;
                        } else if (error != null) {
                            Exceptionerror = error;
                        }
                        if(wr.get() != null)
                            wr.get().setControlServiceImplListenser(entity, null,
                                Exceptionerror);
                    }
                });

    }

    /**
     * 5.1.2 展示事件流程
     *
     * @param listenser
     */
    @Override
    public void getProgressDetail(
            String id,
            ControlServiceImplBackValueListenser<List<EventProgressEntity>> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<List<EventProgressEntity>>> wr = new WeakReference<>(listenser);
        new GetProgressDetailParser(id,
                new ControlCompleterListenter<List<EventProgressEntity>>() {

                    @Override
                    public void controlParserComplete(
                            List<EventProgressEntity> object, String error) {
                        // TODO Auto-generated method stub
                        List<EventProgressEntity> eventProgressEntities = null;
                        String Exceptionerror = null;
                        if (object != null) {
                            eventProgressEntities = object;
                        } else if (error != null) {
                            Exceptionerror = error;
                        }
                        if(wr.get() != null)
                            wr.get().setControlServiceImplListenser(eventProgressEntities, null,
                                Exceptionerror);
                    }
                });
    }

    /**
     * 5.2.6应急通知接收及人员签到详情
     */
    @Override
    public void getSignDetailInfo(String planInfoId,
                                  ControlServiceImplBackValueListenser<?> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<?>> wr = new WeakReference<ControlServiceImplBackValueListenser<?>>(listenser);
        new GetSignUserInfoDetailParser(planInfoId,
                new ControlCompleterListenter<List<PlanTreeEntity>>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void controlParserComplete(List<PlanTreeEntity> object,
                                                      String error) {
                        // TODO Auto-generated method stub
                        if(wr.get() != null)
                            setContorlListListenser(
                                (ControlServiceImplBackValueListenser<List<?>>) wr.get(),
                                object, error);

                    }


                });

    }

    /**
     * 实时跟踪
     */
    @Override
    public void queryProcessTrack(String planInfoId,
                                  ControlServiceImplBackValueListenser<FlowChartPlanEntity> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<FlowChartPlanEntity>> wr = new WeakReference<>(listenser);
        new QueryProcessTrackParser(planInfoId, new ControlCompleterListenter<FlowChartPlanEntity>() {

            @Override
            public void controlParserComplete(FlowChartPlanEntity object,
                                              String error) {
                // TODO Auto-generated method stub
                FlowChartPlanEntity entity = null;
                String Exceptionerror = null;
                if (object != null) {
                    entity = object;
                } else if (error != null) {
                    Exceptionerror = error;
                }
                if(wr.get() != null)
                    wr.get().setControlServiceImplListenser(entity, null,
                        Exceptionerror);
            }
        });

    }

    /**
     * 跳过此步骤
     */
    @Override
    public void jumpplan(String id, String planInfoId,
                         ControlServiceImplBackValueListenser<?> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<?>> wr = new WeakReference<ControlServiceImplBackValueListenser<?>>(listenser);
        new JumpPlanParser(id, planInfoId, new ControlCompleterListenter<Map<String, String>>() {

            @Override
            public void controlParserComplete(Map<String, String> object,
                                              String error) {
                // TODO Auto-generated method stub
                if(wr.get() != null)
                    setContorlBooleanListenser(
                        (ControlServiceImplBackValueListenser<Boolean>) wr.get(),
                        object, error);
            }
        });
    }

    /**
     * 非“执行中”跳过此步骤
     */
    @Override
    public void jumpplan2(String id, String planInfoId, String stopOrStart,
                          ControlServiceImplBackValueListenser<?> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<?>> wr = new WeakReference<ControlServiceImplBackValueListenser<?>>(listenser);
        new JumpPlan2Parser(id, planInfoId, stopOrStart, new ControlCompleterListenter<Map<String, String>>() {

            @Override
            public void controlParserComplete(Map<String, String> object,
                                              String error) {
                // TODO Auto-generated method stub
                if(wr.get() != null)
                    setContorlBooleanListenser(
                        (ControlServiceImplBackValueListenser<Boolean>) wr.get(),
                        object, error);
            }
        });
    }

    @Override
    public void pauseplan(String id, String planInfoId, String stopOrStart,
                          ControlServiceImplBackValueListenser<?> listenser) {
        // TODO Auto-generated method stub
        final WeakReference<ControlServiceImplBackValueListenser<?>> wr = new WeakReference<ControlServiceImplBackValueListenser<?>>(listenser);
        new PausePlanParser(id, planInfoId, stopOrStart, new ControlCompleterListenter<Map<String, String>>() {

            @Override
            public void controlParserComplete(Map<String, String> object,
                                              String error) {
                // TODO Auto-generated method stub
                if(wr.get() != null)
                    setContorlBooleanListenser(
                        (ControlServiceImplBackValueListenser<Boolean>) wr.get(),
                        object, error);
            }
        });
    }
}
