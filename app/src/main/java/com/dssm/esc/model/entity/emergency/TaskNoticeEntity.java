package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;

import android.widget.ImageView;

/**
 * 任务通知实体类
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-7
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class TaskNoticeEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 任务通知当前日期
	 */
	private	String tdate;
	/**
	 * 任务通知当前时间
	 */
	private String ttime;
	/**
	 * 任务通知内容
	 */
	private String tcontent;
	/**
	 * 系统通知当前日期
	 */
	private String xdate;
	/**
	 * 系统通知当前时间
	 */
	private String xtime;
	/**
	 * 系统通知内容
	 */
	private String xcontent;
	/**
	 * 紧急通知当前日期
	 */
	private	String jdate;
	/**
	 * 紧急通知当前时间
	 */
	private String jtime;
	/**
	 * 紧急通知内容
	 */
	private String jcontent;
	
	/**
	 * 是否收到紧急通知
	 */
	private Boolean select;
	/**
	 * 我的消息里的联系人姓名
	 */
	private 	String name;
	/**
	 * 我的消息里的联系人职位
	 */
	private String zhiwei;
	/**
	 * 我的消息当前日期
	 */
	private	String mdate;
	/**
	 * 我的消息当前时间
	 */
	private	String mtime;
	/**
	 * 我的消息内容
	 */
	private	String mcontent;
	public String getTdate() {
		return tdate;
	}
	public void setTdate(String tdate) {
		this.tdate = tdate;
	}
	public String getTtime() {
		return ttime;
	}
	public void setTtime(String ttime) {
		this.ttime = ttime;
	}
	public String getTcontent() {
		return tcontent;
	}
	public void setTcontent(String tcontent) {
		this.tcontent = tcontent;
	}
	public String getXdate() {
		return xdate;
	}
	public void setXdate(String xdate) {
		this.xdate = xdate;
	}
	public String getXtime() {
		return xtime;
	}
	public void setXtime(String xtime) {
		this.xtime = xtime;
	}
	public String getXcontent() {
		return xcontent;
	}
	public void setXcontent(String xcontent) {
		this.xcontent = xcontent;
	}
	public String getJdate() {
		return jdate;
	}
	public void setJdate(String jdate) {
		this.jdate = jdate;
	}
	public String getJtime() {
		return jtime;
	}
	public void setJtime(String jtime) {
		this.jtime = jtime;
	}
	public String getJcontent() {
		return jcontent;
	}
	public void setJcontent(String jcontent) {
		this.jcontent = jcontent;
	}
	public Boolean getSelect() {
		return select;
	}
	public void setSelect(Boolean select) {
		this.select = select;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZhiwei() {
		return zhiwei;
	}
	public void setZhiwei(String zhiwei) {
		this.zhiwei = zhiwei;
	}
	public String getMdate() {
		return mdate;
	}
	public void setMdate(String mdate) {
		this.mdate = mdate;
	}
	public String getMtime() {
		return mtime;
	}
	public void setMtime(String mtime) {
		this.mtime = mtime;
	}
	public String getMcontent() {
		return mcontent;
	}
	public void setMcontent(String mcontent) {
		this.mcontent = mcontent;
	}
	

	
}
