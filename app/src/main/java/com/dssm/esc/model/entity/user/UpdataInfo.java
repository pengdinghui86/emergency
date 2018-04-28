package com.dssm.esc.model.entity.user;

import java.io.Serializable;
/**
 * 检测版本更新的实体类
 * @author zsj
 *
 */
public class UpdataInfo implements Serializable {

	private static final long serialVersionUID = 1L;

private String version;
private String url;//apk下载地址
private String description;
private String url_server;//接口地址

public String getUrl_server() {
	return url_server;
}
public void setUrl_server(String url_server) {
	this.url_server = url_server;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}



}
