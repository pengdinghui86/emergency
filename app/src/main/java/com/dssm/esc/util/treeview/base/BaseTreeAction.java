package com.dssm.esc.util.treeview.base;

import com.dssm.esc.util.treeview.TreeNode;

import java.util.List;

public interface BaseTreeAction {
    void expandAll();

    void expandNode(TreeNode treeNode);

    void expandLevel(int level);

    void collapseAll();

    void collapseNode(TreeNode treeNode);

    void collapseLevel(int level);

    void toggleNode(TreeNode treeNode);

    void deleteNode(TreeNode node);

    void addNode(TreeNode parent, TreeNode treeNode);

    List<TreeNode> getAllNodes();
    // 1.add node at position
    // 2.add slide delete or other operations
}
