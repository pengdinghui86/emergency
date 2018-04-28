package com.dssm.esc.view.fragment;

import android.support.v4.app.Fragment;

/**
 * 懒加载
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-6
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public abstract class LazyFragment extends Fragment {
	protected boolean isVisible;

	/**
	 * 在这里实现Fragment数据的缓加载.
	 * 
	 * @param isVisibleToUser
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
		}
	}

	protected void onVisible() {
		lazyLoad();
	}

	protected abstract void lazyLoad();
}