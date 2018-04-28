package com.dssm.esc.model.entity.user;

import java.io.Serializable;

/**
 * 权限中的button
 * @author zsj
 *
 */
public class ButtonEntity implements Serializable{
	private static final long serialVersionUID = 1L;
//	"btnName": "启动中止", 
//    "menuMark": "ZHKZ", 
//    "btnMark": "BTN_QDZZ", 
//    "menuName": "指挥控制"
	private String btnName;
	private String menuMark;
	private String btnMark;
	private String menuName;
	public String getBtnName() {
		return btnName;
	}
	public void setBtnName(String btnName) {
		this.btnName = btnName;
	}
	public String getMenuMark() {
		return menuMark;
	}
	public void setMenuMark(String menuMark) {
		this.menuMark = menuMark;
	}
	public String getBtnMark() {
		return btnMark;
	}
	public void setBtnMark(String btnMark) {
		this.btnMark = btnMark;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
}
