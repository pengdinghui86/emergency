package com.dssm.esc.util.treeview.base;

import com.dssm.esc.util.treeview.TreeNode;

import java.util.List;

public interface SelectableTreeAction extends BaseTreeAction {
    void selectNode(TreeNode treeNode);

    void deselectNode(TreeNode treeNode);

    void selectAll();

    void deselectAll();

    List<TreeNode> getSelectedNodes();
}
