package com.dssm.esc.util.treeview.view;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.util.DisplayUtils;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.base.CheckableNodeViewBinder;

public class ThirdLevelNodeViewBinder extends CheckableNodeViewBinder {
    ImageView head;
    TextView name;
    TextView zhizhe;
    TextView phonenumber;
    TextView sigin_tv;
    CheckBox checkBox;


    public ThirdLevelNodeViewBinder(View itemView) {
        super(itemView);
        head = (ImageView) itemView.findViewById(R.id.iv_head);
        name = (TextView) itemView.findViewById(R.id.name);
        zhizhe = (TextView) itemView.findViewById(R.id.zhizhe);
        phonenumber = (TextView) itemView
                .findViewById(R.id.phonenumber);
        sigin_tv = (TextView) itemView.findViewById(R.id.sigin_tv);
        checkBox = (CheckBox) itemView.findViewById(R.id.child_checkbox);
        itemView.setPadding(DisplayUtils.dp2px(32),0,0,0);

    }

    @Override
    public int getCheckableViewId() {
        return R.id.child_checkbox;
    }

    @Override
    public int getLayoutId() {
        return R.layout.third_sigin;
    }

    @Override
    public void bindView(TreeNode treeNode, String tag) {
        ChildEntity centity = (ChildEntity) treeNode.getValue();
        if(centity.getSex() == null ? false : centity.getSex().equals("女"))
            head.setImageResource(R.drawable.woman);
        else
            head.setImageResource(R.drawable.man);
        name.setText(centity.getName());
        zhizhe.setText(centity.getZhiwei());
        phonenumber.setText(centity.getPhoneNumber());
        String signin = centity.getSignin();
        String notice= centity.getNoticeState();
        if (tag.equals("1")) {//接收情况
//			0:未通知 1：已通知
            sigin_tv.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.GONE);
            if (notice.equals("1")) {//已通知
                notice="已接收";
                sigin_tv.setTextColor(Color.GREEN);
                sigin_tv.setTextSize(15);
            }else  {//未通知
                notice="未接收";
                sigin_tv.setTextColor(Color.RED);
                sigin_tv.setTextSize(15);
            }
            sigin_tv.setText(notice);

        }else if (tag.equals("2")) {//签到情况
//			0:未签到 1：已签到
            sigin_tv.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.GONE);
            if (signin.equals("1")) {//已签到
                signin = "已签到";
                sigin_tv.setTextColor(Color.GREEN);
                sigin_tv.setTextSize(14);
            } else if (signin.equals("0")) {//未签到
                signin = "未签到";
                sigin_tv.setTextColor(Color.RED);
                sigin_tv.setTextSize(14);
            }
            sigin_tv.setText(signin);
        }
        else if (tag.equals("3")) {
            //checkbox只能单选
            sigin_tv.setVisibility(View.VISIBLE);
            if (signin.equals("1")) {//已签到
                signin = "已签到";
                sigin_tv.setTextColor(Color.GREEN);
                sigin_tv.setTextSize(14);
                checkBox.setVisibility(View.VISIBLE);
            } else if (signin.equals("0")) {//未签到
                signin = "未签到";
                sigin_tv.setTextColor(Color.RED);
                sigin_tv.setTextSize(14);
                checkBox.setVisibility(View.INVISIBLE);
            }
            sigin_tv.setText(signin);
        }
        else {
            sigin_tv.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);

        }
    }
}
