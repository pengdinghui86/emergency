package com.dssm.esc.model.jsonparser;

/**
 * 数据加载完毕之后回调此接口
 * 
 * @author Administrator
 * 
 */
public interface OnDataCompleterListener {
	public void onEmergencyParserComplete(Object object, String error);

}
