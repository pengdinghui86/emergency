package com.dssm.esc.util.treeview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.dssm.esc.util.treeview.base.BaseNodeViewBinder;
import com.dssm.esc.util.treeview.base.BaseNodeViewFactory;
import com.dssm.esc.util.treeview.base.CheckableNodeViewBinder;
import com.dssm.esc.util.treeview.helper.TreeHelper;

import java.util.ArrayList;
import java.util.List;

public class TreeViewAdapter extends RecyclerView.Adapter {

    private Context context;
    /**
     * Tree root.
     */
    private TreeNode root;
    /**
     * The current data set of Adapter,which means excluding the collapsed nodes.
     */
    private List<TreeNode> expandedNodeList;
    /**
     * The binder factory.A binder provide the layoutId which needed in method
     * <code>onCreateViewHolder</code> and the way how to render ViewHolder.
     */
    private BaseNodeViewFactory baseNodeViewFactory;
    //1：接收情况，2：签到情况，3：CheckBox单选模式，""：CheckBox多选模式
    private String tag;
    /**
     * This parameter make no sense just for avoiding IllegalArgumentException of ViewHolder's
     * constructor.
     */
    private View EMPTY_PARAMETER;

    private TreeView treeView;

    public TreeViewAdapter(Context context, TreeNode root,
                           @NonNull BaseNodeViewFactory baseNodeViewFactory, String tag) {
        this.context = context;
        this.root = root;
        this.baseNodeViewFactory = baseNodeViewFactory;
        this.tag = tag;

        this.EMPTY_PARAMETER = new View(context);
        this.expandedNodeList = new ArrayList<>();

        buildExpandedNodeList();
    }

    private void buildExpandedNodeList() {
        expandedNodeList.clear();

        for (TreeNode child : root.getChildren()) {
            insertNode(expandedNodeList, child);
        }
    }

    private void insertNode(List<TreeNode> nodeList, TreeNode treeNode) {
        nodeList.add(treeNode);

        if (!treeNode.hasChild()) {
            return;
        }
        if (treeNode.isExpanded()) {
            for (TreeNode child : treeNode.getChildren()) {
                insertNode(nodeList, child);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return expandedNodeList.get(position).getLevel();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int level) {
        View view = LayoutInflater.from(context).inflate(baseNodeViewFactory
                .getNodeViewBinder(EMPTY_PARAMETER, level).getLayoutId(), parent, false);

        BaseNodeViewBinder nodeViewBinder = baseNodeViewFactory.getNodeViewBinder(view, level);
        nodeViewBinder.setTreeView(treeView);
        return nodeViewBinder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final View nodeView = holder.itemView;
        final TreeNode treeNode = expandedNodeList.get(position);
        final BaseNodeViewBinder viewBinder = (BaseNodeViewBinder) holder;

        if (viewBinder.getToggleTriggerViewId() != 0) {
            View triggerToggleView = nodeView.findViewById(viewBinder.getToggleTriggerViewId());

            if (triggerToggleView != null) {
                triggerToggleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNodeToggled(treeNode);
                        viewBinder.onNodeToggled(treeNode, treeNode.isExpanded());
                    }
                });
            }
        } else if (treeNode.isItemClickEnable()) {
            nodeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNodeToggled(treeNode);
                    viewBinder.onNodeToggled(treeNode, treeNode.isExpanded());
                }
            });
        }

        if (viewBinder instanceof CheckableNodeViewBinder) {
            final View view = nodeView
                    .findViewById(((CheckableNodeViewBinder) viewBinder).getCheckableViewId());

            if (view != null && view instanceof CheckBox) {
                final CheckBox checkableView = (CheckBox) view;
                checkableView.setChecked(treeNode.isSelected());

                checkableView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean checked = checkableView.isChecked();
                        selectNode(checked, treeNode);
                        ((CheckableNodeViewBinder) viewBinder).onNodeSelectedChanged(treeNode, checked);
                    }
                });
            } else {
//                throw new ClassCastException("The getCheckableViewId() " +
//                        "must return a CheckBox's id");
            }
        }

        viewBinder.bindView(treeNode, tag);
    }

    public void selectNode(boolean checked, TreeNode treeNode) {
        //单选模式
        if("3".equals(tag) || "4".equals(tag)) {
            if(checked)
                selectChildren(root, false);
            treeNode.setSelected(checked);
            treeView.refreshTreeView();
        }
        else {
            treeNode.setSelected(checked);
            selectChildren(treeNode, checked);
            selectParentIfNeed(treeNode, checked);
        }
    }

    private void selectChildren(TreeNode treeNode, boolean checked) {
        List<TreeNode> impactedChildren = TreeHelper.selectNodeAndChild(treeNode, checked);
        int index = expandedNodeList.indexOf(treeNode);
        if (index != -1 && impactedChildren.size() > 0) {
            notifyItemRangeChanged(index, impactedChildren.size() + 1);
        }
    }

    private void selectParentIfNeed(TreeNode treeNode, boolean checked) {
        List<TreeNode> impactedParents = TreeHelper.selectParentIfNeedWhenNodeSelected(treeNode, checked);
        if (impactedParents.size() > 0) {
            for (TreeNode parent : impactedParents) {
                int position = expandedNodeList.indexOf(parent);
                if (position != -1) notifyItemChanged(position);
            }
        }
    }

    private void onNodeToggled(TreeNode treeNode) {
        treeNode.setExpanded(!treeNode.isExpanded());

        if (treeNode.isExpanded()) {
            expandNode(treeNode);
        } else {
            collapseNode(treeNode);
        }
    }

    @Override
    public int getItemCount() {
        return expandedNodeList == null ? 0 : expandedNodeList.size();
    }

    /**
     * Refresh all,this operation is only used for refreshing list when a large of nodes have
     * changed value or structure because it take much calculation.
     */
    public void refreshView() {
        buildExpandedNodeList();
        notifyDataSetChanged();
    }

    /**
     * Insert a node list after index.
     *
     * @param index         the index before new addition nodes's first position
     * @param additionNodes nodes to add
     */
    private void insertNodesAtIndex(int index, List<TreeNode> additionNodes) {
        if (index < 0 || index > expandedNodeList.size() - 1 || additionNodes == null) {
            return;
        }
        expandedNodeList.addAll(index + 1, additionNodes);
//        notifyItemRangeInserted(index + 1, additionNodes.size());
        notifyItemRangeChanged(index + 1, expandedNodeList.size() - 1);
    }

    /**
     * Remove a node list after index.
     *
     * @param index        the index before the removedNodes nodes's first position
     * @param removedNodes nodes to remove
     */
    private void removeNodesAtIndex(int index, List<TreeNode> removedNodes) {
        if (index < 0 || index > expandedNodeList.size() - 1 || removedNodes == null) {
            return;
        }
        expandedNodeList.removeAll(removedNodes);
        notifyItemRangeRemoved(index + 1, removedNodes.size());
    }

    /**
     * Expand node. This operation will keep the structure of children(not expand children)
     *
     * @param treeNode
     */
    public void expandNode(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        List<TreeNode> additionNodes = TreeHelper.expandNode(treeNode, false);
        int index = expandedNodeList.indexOf(treeNode);

        insertNodesAtIndex(index, additionNodes);
    }


    /**
     * Collapse node. This operation will keep the structure of children(not collapse children)
     *
     * @param treeNode
     */
    public void collapseNode(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        List<TreeNode> removedNodes = TreeHelper.collapseNode(treeNode, false);
        int index = expandedNodeList.indexOf(treeNode);

        removeNodesAtIndex(index, removedNodes);
    }

    /**
     * Delete a node from list.This operation will also delete its children.
     *
     * @param node
     */
    public void deleteNode(TreeNode node) {
        if (node == null || node.getParent() == null) {
            return;
        }
        List<TreeNode> allNodes = TreeHelper.getAllNodes(root);
        if (allNodes.indexOf(node) != -1) {
            node.getParent().removeChild(node);
        }

        //remove children form list before delete
        collapseNode(node);

        int index = expandedNodeList.indexOf(node);
        if (index != -1) {
            expandedNodeList.remove(node);
        }
        notifyItemRemoved(index);
    }

    public void setTreeView(TreeView treeView) {
        this.treeView = treeView;
    }
}
