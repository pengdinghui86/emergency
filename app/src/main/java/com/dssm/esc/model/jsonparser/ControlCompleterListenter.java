package com.dssm.esc.model.jsonparser;
/**
 * 数据加载完毕之后回调此接口
 * @author Administrator
 *
 * @param <T>
 */
public interface ControlCompleterListenter<T> {
	public void controlParserComplete(T object, String error);

}
