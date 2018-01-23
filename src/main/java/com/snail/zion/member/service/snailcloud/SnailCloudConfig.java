package com.snail.zion.member.service.snailcloud;

import javax.annotation.PostConstruct;

public class SnailCloudConfig {

	private String host;

	private String port;

	private String applyPlayTokenPort;

	private String httpUrl;
	
	@PostConstruct
	public void setHttpUrl() {
		this.httpUrl = "http://" + this.host + ":" + this.port ;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getApplyPlayTokenPort() {
		return applyPlayTokenPort;
	}

	public void setApplyPlayTokenPort(String applyPlayTokenPort) {
		this.applyPlayTokenPort = applyPlayTokenPort;
	}

	public String httpUrl() {
		return this.httpUrl;
	}

}
