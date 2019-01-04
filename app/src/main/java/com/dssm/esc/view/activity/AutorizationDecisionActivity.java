package com.dssm.esc.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl.ControlServiceImplBackValueListenser;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplListListenser;
import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.BaseActivity.onInitNetListener;
import com.dssm.esc.view.adapter.DismissValuationListviewAdapter;
import com.dssm.esc.view.adapter.PlanAdapter;
import com.dssm.esc.view.widget.AutoListView;
import com.dssm.esc.view.widget.AutoListView.OnLoadListener;
import com.dssm.esc.view.widget.AutoListView.OnRefreshListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 决策授权界面
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_dismissvaluation)
public class AutorizationDecisionActivity extends BaseActivity implements
        OnRefreshListener, OnLoadListener, onInitNetListener {
    /**
     * 标题
     */
    @ViewInject(R.id.tv_actionbar_title)
    private TextView title;
    /**
     * 返回按钮
     */
    @ViewInject(R.id.iv_actionbar_back)
    private ImageView back;
    /**
     * ListView
     */
    @ViewInject(R.id.listview)
    AutoListView listView;
    /**
     * 适配器
     */
    private DismissValuationListviewAdapter adapter;
    private PlanAdapter padapter;
    /**
     * 数据源
     */
    private List<BoHuiListEntity> list = new ArrayList<BoHuiListEntity>();
    private List<PlanEntity> plist = new ArrayList<PlanEntity>();
    /**
     * 当前页面
     */
    private int i = 1;
    /** 0，指挥与展示 1，应急；2，演练 */
    // private String tag;
    /**
     * 0，已授权1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示
     */
    private String tags;
    private String signState = "";// 签到状态0:未签到 1：已签到
    private Intent intent = null;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            /** 接收子集合 */
            if (tags.equals("5")) {
                List<PlanEntity> result = (List<PlanEntity>) msg.obj;
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        /** 总集合清理 */
                        plist.clear();
                        /** 总集合添加 */
                        plist.addAll(result);
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        plist.addAll(result);
                        break;
                }
                listView.setResultSize(result.size(), i);
                padapter.notifyDataSetChanged();
            } else {
                List<BoHuiListEntity> result = (List<BoHuiListEntity>) msg.obj;
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        list.clear();
                        list.addAll(result);
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        list.addAll(result);
                        break;
                }
                listView.setResultSize(result.size(), i);
                adapter.notifyDataSetChanged();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 与驳回事件用同一个布局，都是个简单的列表
//        setContentView(R.layout.activity_dismissvaluation);
        View findViewById = findViewById(R.id.dismissvaluation);
        findViewById.setFitsSystemWindows(true);

        Intent intent = getIntent();
        // tag = intent.getStringExtra("tag");
        tags = intent.getStringExtra("tags");
        initview();

    }

    /***
     * 打开EventBus开关
     */
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 刷新界面
     *
     * @param data
     */
    public void onEvent(mainEvent data) {
        if (data.getData().equals("r")) {

            initData();
        }
    }


    private void initview() {
        // TODO Auto-generated method stub
        back.setVisibility(View.VISIBLE);

        // if (tag.equals("1")) {
        if (tags.equals("0")) {
            title.setText("已授权");
        } else if (tags.equals("1")) {
            title.setText("待授权");
        } else if (tags.equals("2")) {
            title.setText("人员签到");
        } else if (tags.equals("3")) {
            title.setText("人员指派");
        } else if (tags.equals("4")) {
            title.setText("协同通告");
        } else if (tags.equals("5")) {
            title.setText("指挥与展示");
        }
        if (tags.equals("5")) {

            padapter = new PlanAdapter(AutorizationDecisionActivity.this, plist);
            listView.setAdapter(padapter);
        } else {
            adapter = new DismissValuationListviewAdapter(
                    AutorizationDecisionActivity.this, list, tags);
            listView.setAdapter(adapter);
        }
        initData();
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listvitemclick();
//		setNetListener(this);
    }

    private void initData() {
        loadData(AutoListView.REFRESH);
    }

    /**
     * 从网络获取的总的list
     */
    private List<BoHuiListEntity> allList = new ArrayList<BoHuiListEntity>();
    /**
     * 从网络获取的总的list
     */
    private List<PlanEntity> allList2 = new ArrayList<PlanEntity>();
    /**
     * 每次显示20条
     */
    private int num = 20;//

    private void loadData(final int what) {
        if (tags.equals("0")) {// 已授权
            if (what == 0) {// 刷新和第一次加载
                getAuthList(0);
            } else if (what == 1) {// 加载更多
                getLoadData();
            }
        } else if (tags.equals("1")) {// 代授权
            if (what == 0) {// 刷新和第一次加载
                getAuthList(1);
            } else if (what == 1) {// 加载更多
                getLoadData();
            }

        } else if (tags.equals("2")) {// 代签到
            if (what == 0) {// 刷新和第一次加载
                getAuthList(2);
            } else if (what == 1) {// 加载更多
                getLoadData();
            }

        } else if (tags.equals("3")) {// 待指派
            if (what == 0) {// 刷新和第一次加载
                getAuthList(3);
            } else if (what == 1) {// 加载更多
                getLoadData();
            }

        } else if (tags.equals("4")) {// 协同通告
            if (what == 0) {// 刷新和第一次加载
                getAuthList(4);
            } else if (what == 1) {// 加载更多
                getLoadData();
            }

        } else if (tags.equals("5")) {// 指挥与展示
            if (what == 0) {// 刷新和第一次加载
                getControlData();
            } else if (what == 1) {// 加载更多
                List<PlanEntity> datalist2;
                if ((num + 20) <= allList2.size()) {
                    datalist2 = allList2.subList(num, num + 20);
                    num += 20;
                } else {
                    datalist2 = allList2.subList(num, allList2.size());
                }
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = datalist2;
                handler.sendMessage(message);
            }

        }

    }

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        loadData(AutoListView.REFRESH);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        list.clear();
    }

    private EmergencySeviceImplBackBooleanListenser listener = new EmergencySeviceImplBackBooleanListenser() {

        @Override
        public void setEmergencySeviceImplListenser(
                Boolean backflag, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            String str = null;
            if (backflag) {
                str = "已签到";
                signState = "1";
                Log.i("signState", signState);
                intent.putExtra("signState", "1");
                startActivity(intent);
            } else if (backflag == false) {
                str = "未签到";
                signState = "0";
                Log.i("signState", signState);
                intent.putExtra("signState", "0");
                startActivity(intent);
            } else if (stRerror != null) {

                str = stRerror;
                ToastUtil.showLongToast(
                        AutorizationDecisionActivity.this, str);
            } else if (Exceptionerror != null) {

                str = Const.NETWORKERROR;
                ToastUtil.showLongToast(
                        AutorizationDecisionActivity.this, str);
            }
        }
    };

    /**
     * 判断用户是否签到
     */
    private void checkUserSignin(int position) {
        Control.getinstance().getEmergencyService().checkEmergencySign(list.get(position - 1).getId(), listener);
    }

    /**
     * listview的item监听
     */
    private void listvitemclick() {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                if (tags.equals("5")) {
                    if (position > 0 && position <= plist.size()) {
                        // 指挥与展示
                        intent = new Intent(AutorizationDecisionActivity.this,
                                ControlActivity.class);
                        intent.putExtra("PlanTreeEntity", padapter.getItem(position - 1));
                        startActivity(intent);
                    }
                } else {
                    if (position > 0 && position <= list.size()) {
                        if (tags.equals("0")) {
                            intent = new Intent(
                                    AutorizationDecisionActivity.this,
                                    PlanSuspandDetilActivity.class);
                            // intent.putExtra("tag", tag);
                            intent.putExtra("id", list.get(position - 1)
                                    .getId());
                            intent.putExtra("isAuthor", list.get(position - 1)
                                    .getIsAuthor());

                            intent.putExtra("stop", "1");// 已授权的预案
                            startActivity(intent);
                        } else if (tags.equals("1")) {

                            intent = new Intent(
                                    AutorizationDecisionActivity.this,
                                    AutorizateDecDetailActivity.class);
                            // intent.putExtra("tag", tag);
                            intent.putExtra("id", list.get(position - 1)
                                    .getId());
                            intent.putExtra("planId", list.get(position - 1)
                                    .getPlanId());
                            intent.putExtra("planResId", list.get(position - 1)
                                    .getPlanResId());
                            intent.putExtra("isAuthor", list.get(position - 1)
                                    .getIsAuthor());
                            startActivity(intent);
                        } else if (tags.equals("2")) {
                            intent = new Intent(
                                    AutorizationDecisionActivity.this,
                                    SignInActivity.class);
                            // intent.putExtra("tag", tag);
                            intent.putExtra("id", list.get(position - 1)
                                    .getId());
                            intent.putExtra("planId", list.get(position - 1)
                                    .getPlanId());

                            checkUserSignin(position);
                        } else if (tags.equals("3")) {
                            intent = new Intent(
                                    AutorizationDecisionActivity.this,
                                    PersonnelAssignmentActivity.class);
                            // intent.putExtra("tag", tag);
                            intent.putExtra("id", list.get(position - 1)
                                    .getId());
                            startActivity(intent);
                        } else if (tags.equals("4")) {
                            intent = new Intent(
                                    AutorizationDecisionActivity.this,
                                    SendCollaborativeActivity.class);
                            // intent.putExtra("tag", tag);
                            intent.putExtra("id", list.get(position - 1)
                                    .getId());
                            intent.putExtra("name", list.get(position - 1)
                                    .getEveName());
                            intent.putExtra("precautionId",
                                    list.get(position - 1).getPrecautionId());
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private EmergencySeviceImplListListenser listListener = new EmergencySeviceImplListListenser() {
        @Override
        public void setEmergencySeviceImplListListenser(Object object,
                String stRerror, String Exceptionerror) {
            // TODO Auto-generated method stub
            List<BoHuiListEntity> dataList = null;
            Message message = handler.obtainMessage();
            if (object != null) {
                dataList = (List<BoHuiListEntity>) object;
                Log.i("决策授权列表的长度", dataList.size() + "");

            } else if (stRerror != null) {
                dataList = new ArrayList<BoHuiListEntity>();

            } else if (Exceptionerror != null) {
                dataList = new ArrayList<BoHuiListEntity>();
                ToastUtil.showToast(AutorizationDecisionActivity.this,
                        Const.NETWORKERROR);
            }
            if (dataList.size() > 20) {// 如果超过20条，则分页
                List<BoHuiListEntity> subList = dataList.subList(0, 20);
                message.obj = subList;
            } else {
                message.obj = dataList;
            }
            message.what = 0;
            handler.sendMessage(message);
            allList = dataList;
            Utils.getInstance().hideProgressDialog();
        }
    };

    /**
     * 获取授权列表
     */
    private void getAuthList(int tag) {
		Utils.getInstance().showProgressDialog(AutorizationDecisionActivity.this, "", Const.LOAD_MESSAGE);
        Control.getinstance().getEmergencyService().getAuthlist(tag, listListener);
    }

    /**
     * 获取加载更多的数据
     */
    private void getLoadData() {
        // 本地做分页，加载20条以后的数据，默认每20条分一页
        Log.i("list测试长度", allList.size() + "");
        Log.i("num", num + "");
        List<BoHuiListEntity> datalist2;
        if ((num + 20) <= allList.size()) {
            datalist2 = allList.subList(num, num + 20);
            num += 20;
        } else {
            datalist2 = allList.subList(num, allList.size());
        }
        Message message = handler.obtainMessage();
        message.what = 1;
        message.obj = datalist2;
        handler.sendMessage(message);
    }

    private ControlServiceImplBackValueListenser<List<PlanEntity>> controlServiceImplBackValueListenser = new ControlServiceImplBackValueListenser<List<PlanEntity>>() {

        @Override
        public void setControlServiceImplListenser(
                List<PlanEntity> backValue, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            Message message = handler.obtainMessage();
            message.what = 0;
            List<PlanEntity> dataList = null;
            if (backValue != null) {
                dataList = backValue;

            } else if (stRerror != null) {
                dataList = new ArrayList<PlanEntity>();

            } else if (Exceptionerror != null) {
                dataList = new ArrayList<PlanEntity>();
                ToastUtil.showToast(AutorizationDecisionActivity.this,
                        Const.NETWORKERROR);
            }
            if (dataList.size() > 20) {// 如果超过20条，则分页
                List<PlanEntity> subList = dataList.subList(0, 20);
                message.obj = subList;
            } else {
                message.obj = dataList;
            }
            allList2 = dataList;
            handler.sendMessage(message);
//				if (Utils.getInstance().progressDialog.isShowing()) {
//					Utils.getInstance().hideProgressDialog();
//				}
        }
    };

    /**
     * 获取指挥与展示的数据
     */
    private void getControlData() {
//		Utils.getInstance().showProgressDialog(AutorizationDecisionActivity.this, "", Const.LOAD_MESSAGE);
        Control.getinstance().getControlSevice().getPlanlist(controlServiceImplBackValueListenser);
    }

    @Override
    public void initNetData() {
        // TODO Auto-generated method stub
        initData();
    }
}
