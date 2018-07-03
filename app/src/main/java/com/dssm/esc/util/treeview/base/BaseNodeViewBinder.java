package com.dssm.esc.util.treeview.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.TreeView;

public abstract class BaseNodeViewBinder extends RecyclerView.ViewHolder {
    /**
     * This reference of TreeView make BaseNodeViewBinder has the ability
     * to expand node or select node.
     */
    protected TreeView treeView;

    public BaseNodeViewBinder(View itemView) {
        super(itemView);
    }

    public void setTreeView(TreeView treeView) {
        this.treeView = treeView;
    }

    /**
     * Get node item layout id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * Bind your data to view,you can get the data from treeNode by getValue()
     *
     * @param treeNode Node data
     */
    public abstract void bindView(TreeNode treeNode, String tag);

    /**
     * if you do not want toggle the node when click whole item view,then you can assign a view to
     * trigger the toggle action
     *
     * @return The assigned view id to trigger expand or collapse.
     */
    public int getToggleTriggerViewId() {
        return 0;
    }

    /**
     * Callback when a toggle action happened (only by clicked)
     *
     * @param treeNode The toggled node
     * @param expand   Expanded or collapsed
     */
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        //empty
    }
}
