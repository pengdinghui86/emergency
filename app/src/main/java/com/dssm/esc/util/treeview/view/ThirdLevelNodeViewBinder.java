package com.dssm.esc.util.treeview.view;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.util.DisplayUtils;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.base.CheckableNodeViewBinder;

public class ThirdLevelNodeViewBinder extends CheckableNodeViewBinder {
    LinearLayout ll_content;
    ImageView head;
    TextView name;
    TextView sigin_tv;
    CheckBox checkBox;


    public ThirdLevelNodeViewBinder(View itemView) {
        super(itemView);
        ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
        head = (ImageView) itemView.findViewById(R.id.child_contact_checkbox_iv_head);
        name = (TextView) itemView.findViewById(R.id.child_contact_checkbox_tv_name);
        sigin_tv = (TextView) itemView.findViewById(R.id.child_contact_tv_sign_in);
        checkBox = (CheckBox) itemView.findViewById(R.id.child_contact_cb);
    }

    @Override
    public int getCheckableViewId() {
        return R.id.child_contact_cb;
    }

    @Override
    public int getLayoutId() {
        return R.layout.third_tree_view;
    }

    @Override
    public void bindView(TreeNode treeNode, String tag) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ll_content.getLayoutParams());
        lp.setMargins(DisplayUtils.dp2px(32), 0, 0, 0);
        ll_content.setLayoutParams(lp);
        ChildEntity centity = (ChildEntity) treeNode.getValue();
        if(centity.getSex()==null?false:centity.getSex().equals("女"))
            head.setImageResource(R.drawable.woman_online);
        else
            head.setImageResource(R.drawable.man_online);
        name.setText(centity.getName());
        name.setText(centity.getName());
        sigin_tv.setVisibility(View.VISIBLE);
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
        else if (tag.equals("4")) {
            //checkbox只能单选
            sigin_tv.setVisibility(View.VISIBLE);
            if (signin.equals("1")) {//已签到
                sigin_tv.setTextColor(Color.GREEN);
                signin = "已签到";
            } else if (signin.equals("0")) {//未签到
                signin = "未签到";
                sigin_tv.setTextColor(Color.RED);
            }
            sigin_tv.setTextSize(14);
            checkBox.setVisibility(View.VISIBLE);
            sigin_tv.setText(signin);
        }
        else {
            sigin_tv.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);

        }
    }
}
