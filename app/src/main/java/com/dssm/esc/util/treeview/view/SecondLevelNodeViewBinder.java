package com.dssm.esc.util.treeview.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.DisplayUtils;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.base.CheckableNodeViewBinder;
import com.easemob.chatuidemo.DemoApplication;

public class SecondLevelNodeViewBinder extends CheckableNodeViewBinder {

    TextView textView;
    ImageView imageView;
    LinearLayout ll_cb;
    RelativeLayout rl_content;
    RelativeLayout rl;

    public SecondLevelNodeViewBinder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.group_contact_checkbox_tv_department);
        imageView = (ImageView) itemView.findViewById(R.id.group_contact_checkbox_iv_arrow);
        ll_cb = (LinearLayout) itemView.findViewById(R.id.cb_layout);
        rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
        rl = (RelativeLayout) itemView.findViewById(R.id.group_contact_checkbox_rl);
    }

    @Override
    public int getCheckableViewId() {
        return R.id.group_contact_cb;
    }

    @Override
    public int getLayoutId() {
        return R.layout.first_tree_view;
    }

    @Override
    public void bindView(final TreeNode treeNode, String tag) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(rl_content.getLayoutParams());
        lp.setMargins(DisplayUtils.dp2px(16), 0, 0, 0);
        rl_content.setLayoutParams(lp);
        textView.setText(treeNode.getValue().toString());
        imageView.setRotation(treeNode.isExpanded() ? 90 : 0);
        if(tag.equals(""))
            ll_cb.setVisibility(View.VISIBLE);
        else
            ll_cb.setVisibility(View.GONE);
    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        if (expand) {
            rl.setBackgroundResource(R.color.colorWeWindowGrayBackground);
            textView.setPadding(8, 8, 0, 8);
            textView.setTextSize(13);
            textView.setTextColor(DemoApplication.getInstance().getResources().getColor(R.color.colorWeFontGray_9));
            imageView.animate().rotation(90).setDuration(200).start();
        } else {
            rl.setBackgroundResource(R.color.white);
            textView.setPadding(8, 20, 0, 20);
            textView.setTextSize(16);
            textView.setTextColor(DemoApplication.getInstance().getResources().getColor(R.color.colorWeFontBlack));
            imageView.animate().rotation(0).setDuration(200).start();
        }
    }
}
