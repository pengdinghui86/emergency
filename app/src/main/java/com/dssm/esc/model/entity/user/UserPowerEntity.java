package com.dssm.esc.model.entity.user;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限的实体类
 * 
 * @author zsj
 * 
 */
public class UserPowerEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	List<MenuEntity> menu;
	List<ButtonEntity> btns;

	public List<MenuEntity> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuEntity> menu) {
		this.menu = menu;
	}

	public List<ButtonEntity> getBtns() {
		return btns;
	}

	public void setBtns(List<ButtonEntity> btns) {
		this.btns = btns;
	}

	
}
