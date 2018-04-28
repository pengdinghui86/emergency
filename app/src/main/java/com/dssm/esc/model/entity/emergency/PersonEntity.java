package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

public class PersonEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String receiverPhone;

	private String receiverEmail;

	private String receiver;

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
