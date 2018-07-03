package com.dssm.esc.util.treeview.view;

import android.view.View;

import com.dssm.esc.util.treeview.base.BaseNodeViewBinder;
import com.dssm.esc.util.treeview.base.BaseNodeViewFactory;

public class MyNodeViewFactory extends BaseNodeViewFactory {

    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {
        switch (level) {
            case 0:
                return new FirstLevelNodeViewBinder(view);
            case 1:
                return new SecondLevelNodeViewBinder(view);
            case 2:
                return new ThirdLevelNodeViewBinder(view);
            default:
                return null;
        }
    }
}
