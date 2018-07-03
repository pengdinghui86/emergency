package com.dssm.esc.util.treeview.animator;


import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

public class TreeItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        super.animateAdd(holder);
        ViewCompat.setAlpha(holder.itemView, 1);
        return true;
    }
}
