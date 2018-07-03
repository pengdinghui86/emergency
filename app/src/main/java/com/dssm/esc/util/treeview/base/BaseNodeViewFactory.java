package com.dssm.esc.util.treeview.base;

import android.view.View;

public abstract class BaseNodeViewFactory {

    /**
     * If you want build a tree view,you must implement this factory method
     *
     * @param view  The parameter for BaseNodeViewBinder's constructor, do not use this for other
     *              purpose!
     * @param level The treeNode level
     * @return
     */
    public abstract BaseNodeViewBinder getNodeViewBinder(View view, int level);

}
