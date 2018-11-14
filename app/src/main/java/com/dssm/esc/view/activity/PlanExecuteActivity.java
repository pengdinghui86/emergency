package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.PlanExecuteListvAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 预案步骤执行列表
 */
@ContentView(R.layout.activity_plan_step_execute)
public class PlanExecuteActivity extends BaseActivity implements
        OnRefreshListener, MainActivity.onInitNetListener {
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
    /** 1，应急；2，演练 */
    // private String tag;
    /**
     * ListView
     */
    @ViewInject(R.id.plan_execute_list_view)
    private ListView listView;
    /**
     * 适配器
     */
    private PlanExecuteListvAdapter adapter;
    private List<ChildEntity> childEntities = new ArrayList<ChildEntity>();
    //重新排序时用来临时存储列表
    private List<ChildEntity> childList = new ArrayList<ChildEntity>();
    //传递过来的预案编号
    private String planInfoId = "";
    //传递过来的预案状态
    private String planStatus = "";
    /**
     * 暂无数据
     */
    @ViewInject(R.id.ll_no_data)
    private LinearLayout ll_no_data;
    /**
     * 刷新
     */
    @ViewInject(R.id.id_swipe_ly)
    private SwipeRefreshLayout mSwipeLayout;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<ChildEntity> result1 = (List<ChildEntity>) msg.obj;
            childEntities.clear();
            childEntities.addAll(result1);
            if(adapter == null) {
                adapter = new PlanExecuteListvAdapter("",PlanExecuteActivity.this, childEntities, planStatus);
                listView.setAdapter(adapter);
            }
            else
                adapter.notifyDataSetChanged();
            mSwipeLayout.setRefreshing(false);
            if (result1.size() == 0) {
                mSwipeLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
            } else {
                mSwipeLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                ll_no_data.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        View findViewById = findViewById(R.id.plan_execute);
        findViewById.setFitsSystemWindows(true);
        planInfoId = getIntent().getStringExtra("planInfoId");
        planStatus = getIntent().getStringExtra("planStatus");
        initView();

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
        if (data.getData().equals("refre")) {
            i = 1;
            initListData();
        }
    }

    private void addIndex(List<ChildEntity> childEntities) {
        for(ChildEntity childEntity : childEntities) {
            int index = 0;
            String parentOrderNum = "";
            String parentId = childEntity.getParentProcessStepId();
            while (parentId != null && !"".equals(parentId)) {
                index++;
                int i = 0;
                for (ChildEntity childEntity1 : childEntities) {
                    if(childEntity1.getChild_id().equals(parentId)) {
                        parentId = childEntity1.getParentProcessStepId();
                        String num = "";
                        if (!"null".equals(childEntity1.getOrderNumber()) &&
                                !"".equals(childEntity1.getOrderNumber())) {
                            num = childEntity1.getOrderNumber() + ".";
                        }
                        parentOrderNum = num + parentOrderNum;
                        i++;
                        break;
                    }
                }
                if(i == 0)
                    break;
            }
            childEntity.setIndex(index);
            childEntity.setParentOrderNumber(parentOrderNum);
        }
    }

    private void reSort(List<ChildEntity> childEntities, String parentId, int index) {
        for(ChildEntity childEntity : childEntities) {
            if(childEntity.getIndex() == index
                    && parentId.equals(childEntity.getParentProcessStepId())
                    && !childEntity.getNodeStepType().equals("drillNew")) {
                childList.add(childEntity);
                if("CallActivity".equals(childEntity.getNodeStepType())) {
                    reSort(childEntities, childEntity.getChild_id(), index + 1);
                }
            }
        }
    }

    private void initView() {
        back.setVisibility(View.VISIBLE);
        title.setText("预案执行");
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        initListData();
        mSwipeLayout.setOnRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChildEntity child = childEntities.get(i);
                //子预案节点
                if(child.getNodeStepType().equals("CallActivity"))
                    return;
                ChildEntity childEntity = childEntities.get(i);
                Intent intent = new Intent(PlanExecuteActivity.this,
                        PlanExecutionDetailActivity.class);
                intent.putExtra("entity", child);
                intent.putExtra("groupEntity", childEntity);
                startActivity(intent);
            }
        });
    }

    private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

        @Override
        public void setEmergencySeviceImplListListenser(Object object,
                String stRerror, String Exceptionerror) {
            // TODO Auto-generated method stub
            List<ChildEntity> dataList = null;
            if (i != 1) {

                if (object != null) {
                    dataList = (List<ChildEntity>) object;

                } else if (stRerror != null) {
                    dataList = new ArrayList<ChildEntity>();

                } else if (Exceptionerror != null) {
                    dataList = new ArrayList<ChildEntity>();
                    ToastUtil.showToast(PlanExecuteActivity.this,
                            Const.NETWORKERROR);
                }
                List<ChildEntity> result = new ArrayList<>(dataList);
                childList.clear();
                addIndex(result);
                reSort(result,"", 0);
                dataList.clear();
                dataList.addAll(childList);
                Message message = new Message();
                message.what = 0;
                message.obj = dataList;
                handler.sendMessage(message);
            } else if (i == 1) {

                if (object != null) {
                    dataList = (List<ChildEntity>) object;

                } else if (stRerror != null) {
                    dataList = new ArrayList<ChildEntity>();

                } else if (Exceptionerror != null) {
                    dataList = new ArrayList<ChildEntity>();
                    ToastUtil.showToast(PlanExecuteActivity.this,
                            Const.NETWORKERROR);
                }
                List<ChildEntity> result = new ArrayList<>(dataList);
                childList.clear();
                addIndex(result);
                reSort(result,"", 0);
                dataList.clear();
                dataList.addAll(childList);
                Message message = new Message();
                message.what = 1;
                message.obj = dataList;
                handler.sendMessage(message);
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    /**
     * 初始化数据
     */
    private void initListData() {
        Utils.getInstance().showProgressDialog(PlanExecuteActivity.this, "",
                Const.LOAD_MESSAGE);
        Control.getinstance().getEmergencyService().getPlanExecute(planInfoId, listListener);
    }

    int i;

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        i = 1;
        initListData();
    }

    @Override
    public void initNetData() {
        // TODO Auto-generated method stub
        initListData();
    }
}
