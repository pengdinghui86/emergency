package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.dssm.esc.R;
import com.dssm.esc.view.activity.SendMessageActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录
 */
public class AdrressListFragment extends BaseFragment implements
        OnClickListener {
    /**
     * 应急通讯录碎片
     */
    private AdressListContactFragment adressListContactFragment;
    /**
     * 应急通知碎片
     */
    private AdressListNotificationFragment adressListNotificationFragment;
    /**
     * 用于区分消息 0,应急通讯录；1，应急通知
     */
    private int tag = 0;
    private Context context;
    private ImageView send;
    private RadioButton rb_failsafe;
    private RadioButton rb_show;
    /**
     * 被选中的人员的id
     */
    public List<String> selectId = new ArrayList<String>();

    @SuppressLint("ValidFragment")
    public AdrressListFragment(Context context) {
        this.context = context;

    }

    public AdrressListFragment() {

    }

    @Override
    protected View getViews() {
        return view_Parent = LayoutInflater.from(context).inflate(
                R.layout.fragment_addresslist_new, null);
    }

    @Override
    protected void findViews() {
        rb_failsafe = (RadioButton) view_Parent.findViewById(R.id.rb_failsafe);
        rb_show = (RadioButton) view_Parent.findViewById(R.id.rb_show);
        send = (ImageView) view_Parent.findViewById(R.id.addresslist_iv_send);

    }

    @Override
    protected void widgetListener() {
        // TODO Auto-generated method stub
        rb_failsafe.setOnClickListener(this);
        rb_show.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        // 初始化，默认加载任务通知界面
        switchView(tag);
    }

    @Override
    public void initGetData() {
        // TODO Auto-generated method stub

    }


    /**
     * 选择界面
     */
    public void switchView(int position) {
        // 获取Fragment的操作对象
        FragmentTransaction transaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:// 添加 并展示 应急通讯录 碎片
                if (adressListContactFragment == null) {
                    adressListContactFragment = new AdressListContactFragment(
                            context);
                    transaction.add(R.id.view_addresslist,
                            adressListContactFragment);
                    // failsafeAdressListFragment.initGetData();
                } else {
                    transaction.show(adressListContactFragment);
                }
                break;
            case 1:// 添加 并展示 应急通知 碎片
                if (adressListNotificationFragment == null) {
                    adressListNotificationFragment = new AdressListNotificationFragment(
                            context);
                    transaction.add(R.id.view_addresslist, adressListNotificationFragment);
                    // systemToastFragment.initGetData();
                } else {
                    transaction.show(adressListNotificationFragment);
                    // }
                    break;

                }
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 隐藏所有Fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (adressListContactFragment != null) {
            transaction.hide(adressListContactFragment);
            // messageFragment.onPause();
        }
        if (adressListNotificationFragment != null) {
            transaction.hide(adressListNotificationFragment);
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rb_failsafe:
                tag = 0;
                switchView(tag);
                break;
            case R.id.rb_show:
                tag = 1;
                switchView(tag);
                break;
            case R.id.addresslist_iv_send://选择联系人
                Intent intent = new Intent(getActivity(),
                        SendMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tag", tag + "");// 应急通讯录碎片
                bundle.putSerializable("selectId", (Serializable) selectId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    /***
     * 打开EventBus开关
     */
    protected boolean useEventBus() {
        return false;
    }
}
