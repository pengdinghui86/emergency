package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.ExpandListvPlanExecuteAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 预案执行界面
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-12
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_plan_execute)
public class PlanExecutionActivity extends BaseActivity implements
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
     * 可扩展ListView
     */
    @ViewInject(R.id.plan_excute_expandablelist)
    private ExpandableListView expandableList;
    /**
     * 可扩展ListView 适配器
     */
    private ExpandListvPlanExecuteAdapter adapter;
    /**
     * 父list显示组
     */
    private List<GroupEntity> groupList = new ArrayList<GroupEntity>();
    /**
     * 子list显示人
     */
    private List<ChildEntity> childList = new ArrayList<ChildEntity>();
    private String id = "";
    private String planInfoId = "";
    /**
     * 暂无数据
     */
    @ViewInject(R.id.pemptytv)
    private TextView emptytv;
    /**
     * 刷新
     */
    @ViewInject(R.id.id_swipe_ly)
    private SwipeRefreshLayout mSwipeLayout;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        List<GroupEntity> result = (List<GroupEntity>) msg.obj;
                        groupList.addAll(result);
                        adapter = new ExpandListvPlanExecuteAdapter(groupList,
                                PlanExecutionActivity.this);
                        expandableList.setAdapter(adapter);
                        for (int i = 0; i < adapter.getGroupCount(); i++) {
                            expandableList.expandGroup(i);
                        }
                        if (result.size() == 0) {
                            mSwipeLayout.setVisibility(View.GONE);
                            expandableList.setVisibility(View.GONE);
                            emptytv.setVisibility(View.VISIBLE);
                        } else {
                            mSwipeLayout.setVisibility(View.VISIBLE);
                            expandableList.setVisibility(View.VISIBLE);
                            emptytv.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e) {
                        String error = e.toString();
                    }
                    //mSwipeLayout.setRefreshing(false);
                    break;
                case 1:
                    List<GroupEntity> result1 = (List<GroupEntity>) msg.obj;
                    groupList.clear();
                    groupList.addAll(result1);
                    if(adapter == null) {
                        adapter = new ExpandListvPlanExecuteAdapter(groupList,
                                PlanExecutionActivity.this);
                        expandableList.setAdapter(adapter);
                    }
                    else
                        adapter.notifyDataSetChanged();
                    mSwipeLayout.setRefreshing(false);
                    if (result1.size() == 0) {
                        mSwipeLayout.setVisibility(View.GONE);
                        expandableList.setVisibility(View.GONE);
                        emptytv.setVisibility(View.VISIBLE);
                    } else {
                        mSwipeLayout.setVisibility(View.VISIBLE);
                        expandableList.setVisibility(View.VISIBLE);
                        emptytv.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_plan_execute);
        View findViewById = findViewById(R.id.plan_execute);
        findViewById.setFitsSystemWindows(true);
        // tag = getIntent().getStringExtra("tag");
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

            initListData();
        }
    }

    private void addIndex(List<ChildEntity> childEntities) {
        for(ChildEntity childEntity : childEntities) {
            int index = 0;
            String parentId = childEntity.getParentProcessStepId();
            while (parentId != null && !"".equals(parentId)) {
                index++;
                int i = 0;
                for (ChildEntity childEntity1 : childEntities) {
                    if(childEntity1.getChild_id().equals(parentId)) {
                        parentId = childEntity1.getParentProcessStepId();
                        i++;
                        break;
                    }
                }
                if(i == 0)
                    break;
            }
            childEntity.setIndex(index);
        }
    }

    private void reSort(List<ChildEntity> childEntities, String parentId, int index) {
        for(ChildEntity childEntity : childEntities) {
            if(childEntity.getIndex() == index
                    && parentId.equals(childEntity.getParentProcessStepId())) {
                childList.add(childEntity);
                if("CallActivity".equals(childEntity.getNodeStepType())) {
                    reSort(childEntities, childEntity.getChild_id(), index + 1);
                }
            }
        }
    }

    private void initView() {
        back.setVisibility(View.VISIBLE);
        // if (tag.equals("1")) {
        title.setText("预案执行");

        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        expandableList.setGroupIndicator(null);
        initListData();
        mSwipeLayout.setOnRefreshListener(this);
        expandableList.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // TODO Auto-generated method stub
                if (adapter.getChildrenCount(groupPosition) == 0) {
                    ToastUtil.showToast(PlanExecutionActivity.this, "没有您要执行的步骤");
                }
                return false; //默认为false，设为true时，点击事件不会展开Group
            }
        });
        expandableList.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                ChildEntity child = adapter.getChild(groupPosition,
                        childPosition);
                //子预案节点
                if(child.getNodeStepType().equals("CallActivity"))
                    return true;
                GroupEntity groupEntity = adapter.getGroup(groupPosition);
                // if (child.getStatus().equals("立即执行")) {
                Intent intent = new Intent(PlanExecutionActivity.this,
                        PlanExecutionDetailActivity.class);
                intent.putExtra("entity", child);
                intent.putExtra("groupEntity", groupEntity);
                startActivity(intent);
                // }
                return true;
            }
        });
//		setNetListener(this);
    }

    private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

        @Override
        public void setEmergencySeviceImplListListenser(Object object,
                String stRerror, String Exceptionerror) {
            // TODO Auto-generated method stub
            List<GroupEntity> dataList = null;
            if (i != 1) {

                if (object != null) {
                    dataList = (List<GroupEntity>) object;

                } else if (stRerror != null) {
                    dataList = new ArrayList<GroupEntity>();

                } else if (Exceptionerror != null) {
                    dataList = new ArrayList<GroupEntity>();
                    ToastUtil.showToast(PlanExecutionActivity.this,
                            Const.NETWORKERROR + ":" + Exceptionerror);
                }
                List<GroupEntity> result = new ArrayList<>(dataList);
                for (GroupEntity groupEntity : result) {
                    childList.clear();
                    addIndex(groupEntity.getcList());
                    reSort(groupEntity.getcList(), "", 0);
                    List<ChildEntity> temp = new ArrayList<>(childList);
                    groupEntity.setcList(temp);
                }
                dataList.clear();
                dataList.addAll(result);
                Message message = new Message();
                message.what = 0;
                message.obj = dataList;
                handler.sendMessage(message);
            } else if (i == 1) {

                if (object != null) {
                    dataList = (List<GroupEntity>) object;

                } else if (stRerror != null) {
                    dataList = new ArrayList<GroupEntity>();

                } else if (Exceptionerror != null) {
                    dataList = new ArrayList<GroupEntity>();
                    ToastUtil.showToast(PlanExecutionActivity.this,
                            Const.NETWORKERROR + ":" + Exceptionerror);
                }
                List<GroupEntity> result = new ArrayList<>(dataList);
                for (GroupEntity groupEntity : result) {
                    childList.clear();
                    addIndex(groupEntity.getcList());
                    reSort(groupEntity.getcList(), "", 0);
                    List<ChildEntity> temp = new ArrayList<>(childList);
                    groupEntity.setcList(temp);
                }
                dataList.clear();
                dataList.addAll(result);
                Message message = new Message();
                message.what = 1;
                message.obj = dataList;
                handler.sendMessage(message);
            }
//				if (Utils.getInstance().progressDialog.isShowing()) {
            Utils.getInstance().hideProgressDialog();
//				}
        }
    };

    /**
     * 初始化数据
     *
     * @version 1.0
     * @createTime 2015-9-8,下午8:38:59
     * @updateTime 2015-9-8,下午8:38:59
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void initListData() {
        Utils.getInstance().showProgressDialog(PlanExecutionActivity.this, "",
                Const.LOAD_MESSAGE);
        Control.getinstance().getEmergencyService().getPlanExecute(listListener);
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
