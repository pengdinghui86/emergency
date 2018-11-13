package com.dssm.esc.model.entity.control;

import java.io.Serializable;

/**
 * 接收和签到数据整合
 */
public class NoticeSignUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 接收数据
	 */
	private SignUserEntity.Notice notice;
	/**
	 * 签到数据
	 */
	private SignUserEntity.Sign sign;

	public SignUserEntity.Notice getNotice() {
		return notice;
	}

	public void setNotice(SignUserEntity.Notice notice) {
		this.notice = notice;
	}

	public SignUserEntity.Sign getSign() {
		return sign;
	}

	public void setSign(SignUserEntity.Sign sign) {
		this.sign = sign;
	}
}
