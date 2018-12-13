package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplListListenser;
import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.BaseActivity.onInitNetListener;
import com.dssm.esc.view.adapter.LeftSlidePlanAdapter;
import com.dssm.esc.view.widget.AutoListView;
import com.dssm.esc.view.widget.RefreshLinearLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员签到
 */
@ContentView(R.layout.activity_person_sign_in)
public class PersonSignInActivity extends BaseActivity implements
        onInitNetListener, LeftSlidePlanAdapter.IonSlidingViewClickListener{
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
    @ViewInject(R.id.person_sign_in_recyclerView)
    private RecyclerView mRecyclerView;
    /** 适配器 */
    private LeftSlidePlanAdapter adapter;
    /** 下拉刷新控件 */
    @ViewInject(R.id.person_sign_in_refreshLinearLayout)
    private RefreshLinearLayout refreshLinearLayout;
    /** 签到 */
    @ViewInject(R.id.person_sign_in_bt_sign)
    private Button bt_sign;
    /**
     * 数据源
     */
    private List<PlanStarListEntity> list = new ArrayList<PlanStarListEntity>();


    private String signState = "";// 签到状态0:未签到 1：已签到
    private Intent intent = null;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<PlanStarListEntity> result = (List<PlanStarListEntity>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    refreshLinearLayout.onCompleteRefresh();
                    list.clear();
                    list.addAll(result);
                    break;
                case AutoListView.LOAD:
                    refreshLinearLayout.onLoadComplete();
                    list.addAll(result);
                    break;
            }
            adapter.notifyDataSetChanged();
            refreshLinearLayout.setResultSize(result.size(), 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        View findViewById = findViewById(R.id.person_sign_in_ll);
        findViewById.setFitsSystemWindows(true);
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
        back.setVisibility(View.VISIBLE);
        title.setText("人员签到");
        adapter = new LeftSlidePlanAdapter(
                PersonSignInActivity.this, list, "7");
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        mRecyclerView.setAdapter(adapter);
        initData();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置控制Item增删的动画
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(divider);
        refreshLinearLayout.setOnRefreshListener(new RefreshLinearLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        refreshLinearLayout.setOnLoadListener(new RefreshLinearLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                loadData(AutoListView.LOAD);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(isSlideToBottom(recyclerView))
                    refreshLinearLayout.checkFooter();
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if(isSlideToBottom(recyclerView))
                    refreshLinearLayout.checkFooter();
            }
        });
        bt_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siginin();
            }
        });
    }

    /**
     * 签到
     */
    private void siginin() {
        if(list == null || list.size() == 0)
            return;
        Utils.getInstance().showProgressDialog(PersonSignInActivity.this, "",
                Const.LOAD_MESSAGE);
        Control.getinstance().getEmergencyService().signIn("", signInListener);
    }

    private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser signInListener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

        @Override
        public void setEmergencySeviceImplListenser(Boolean backflag,
                                                    String stRerror, String Exceptionerror) {
            // TODO Auto-generated method stub
            if (backflag) {
                ToastUtil.showToast(PersonSignInActivity.this, "签到成功");
                signState = "1";
                initData();
            } else if (backflag == false) {
                if(stRerror != null)
                    ToastUtil.showToast(PersonSignInActivity.this, stRerror);
                else
                    ToastUtil.showToast(PersonSignInActivity.this, "操作失败");
            } else if (stRerror != null) {

                ToastUtil.showLongToast(PersonSignInActivity.this, stRerror);
            } else if (Exceptionerror != null) {

                ToastUtil.showLongToast(PersonSignInActivity.this,
                        Const.NETWORKERROR);
            }
            Utils.getInstance().hideProgressDialog();
        }
    };

    @Override
    public void onItemClick(View view, final int position) {
        Intent intent = new Intent(this,
                GroupSigninDetail.class);
        intent.putExtra("tag", "1");
        intent.putExtra("id", list.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onFunction1BtnClick(View view, int position) {

    }

    @Override
    public void onFunction2BtnClick(View view, int position) {

    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        //computeVerticalScrollExtent()是当前屏幕显示的区域高度，
        // computeVerticalScrollOffset() 是当前屏幕之前滑过的距离，
        // 而computeVerticalScrollRange()是整个View控件的高度。
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void initData() {
        loadData(AutoListView.REFRESH);
    }

    /**
     * 从网络获取的总的list
     */
    private List<PlanStarListEntity> allList = new ArrayList<PlanStarListEntity>();
    /**
     * 从网络获取的总的list
     */
    private List<PlanEntity> allList2 = new ArrayList<PlanEntity>();
    /**
     * 每次显示20条
     */
    private int num = 20;//

    private void loadData(final int what) {
        if (what == 0) {// 刷新和第一次加载
            getAuthList(7);
        } else if (what == 1) {// 加载更多
            getLoadData();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        list.clear();
    }

    private EmergencySeviceImplListListenser listListener = new EmergencySeviceImplListListenser() {
        @Override
        public void setEmergencySeviceImplListListenser(Object object,
                String stRerror, String Exceptionerror) {
            // TODO Auto-generated method stub
            List<PlanStarListEntity> dataList = null;
            Message message = handler.obtainMessage();
            if (object != null) {
                dataList = (List<PlanStarListEntity>) object;
                Log.i("决策授权列表的长度", dataList.size() + "");

            } else if (stRerror != null) {
                dataList = new ArrayList<PlanStarListEntity>();

            } else if (Exceptionerror != null) {
                dataList = new ArrayList<PlanStarListEntity>();
                ToastUtil.showToast(PersonSignInActivity.this,
                        Const.NETWORKERROR);
            }
            if (dataList.size() > 20) {// 如果超过20条，则分页
                List<PlanStarListEntity> subList = dataList.subList(0, 20);
                message.obj = subList;
            } else {
                message.obj = dataList;
            }
            if(dataList == null || dataList.size() == 0)
                bt_sign.setVisibility(View.GONE);
            else if(dataList.get(0).isCheckSign())
            {
                bt_sign.setBackgroundResource(R.drawable.circle_unstart_bg);
                bt_sign.setVisibility(View.VISIBLE);
                bt_sign.setClickable(false);
            }
            else {
                bt_sign.setBackgroundResource(R.drawable.circle_blue_bg);
                bt_sign.setVisibility(View.VISIBLE);
                bt_sign.setClickable(true);
            }
            message.what = 0;
            handler.sendMessage(message);
            allList = dataList;
            Utils.getInstance().hideProgressDialog();
        }
    };

    /**
     * 获取列表
     */
    private void getAuthList(int tag) {
		Utils.getInstance().showProgressDialog(PersonSignInActivity.this, "", Const.LOAD_MESSAGE);
        Control.getinstance().getEmergencyService().getAuthlist(tag, listListener);
    }

    /**
     * 获取加载更多的数据
     */
    private void getLoadData() {
        // 本地做分页，加载20条以后的数据，默认每20条分一页
        Log.i("list测试长度", allList.size() + "");
        Log.i("num", num + "");
        List<PlanStarListEntity> datalist2;
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

    @Override
    public void initNetData() {
        // TODO Auto-generated method stub
        initData();
    }
}
