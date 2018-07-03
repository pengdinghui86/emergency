package com.dssm.esc.util.treeview.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.DisplayUtils;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.base.CheckableNodeViewBinder;

public class SecondLevelNodeViewBinder extends CheckableNodeViewBinder {

    TextView textView;
    ImageView imageView;
    public SecondLevelNodeViewBinder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.group_tv);
        imageView = (ImageView) itemView.findViewById(R.id.gropimg);
        itemView.setPadding(DisplayUtils.dp2px(16),0,0,0);
    }

    @Override
    public int getCheckableViewId() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_sigin;
    }

    @Override
    public void bindView(final TreeNode treeNode, String tag) {
        textView.setText(treeNode.getValue().toString());
        imageView.setRotation(treeNode.isExpanded() ? 90 : 0);
    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        if (expand) {
            imageView.animate().rotation(90).setDuration(200).start();
        } else {
            imageView.animate().rotation(0).setDuration(200).start();
        }
    }
}
