package com.snail.zion.member.service.shiro;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("lms.shiro")
@Component
public class ShiroConfig {
	
	private String host;
	
	private String port;
	
	@Autowired
	private Client client;

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
	
	private String uriGenerator(String str){
		String uri = "http://" + host + ":" + port + str ;
		return uri;
	}
	
	/**
	 * 
	 * shiro 登录接口
	 */
	public WebTarget login(){
		
		String uri = uriGenerator("/shiro/lmsLogin") ;
		return client.target(uri);
		
	}
	
	/**
	 * shiro修改密码
	 */
	public WebTarget modify(){

		String uri = uriGenerator("/shiro/lmsUpdate") ;
		return client.target(uri);
	}
	
	/**
	 * shiro修改信息
	 */
	public WebTarget modifyPassword(){
		
		String uri = uriGenerator("/shiro/lmsChangePassword") ;
		return client.target(uri);
	}
	
	/**
	 * shiro新增
	 */
	public WebTarget add(){
		
		String uri = uriGenerator("/shiro/lmsRegister") ;
		return client.target(uri);
	}
	
	/**
	 * shiro删除
	 */
	public WebTarget delete(){
		
		String uri = uriGenerator("/shiro/lmsDelete") ;
		return client.target(uri);
	}
	
	/**
	 * shiro获取角色列表
	 */
	public WebTarget roleList(){
		
		String uri = uriGenerator("/shiro/lms/roleList") ;
		return client.target(uri);
	}
	
	/**
	 * shiro查询用户信息
	 */
	public WebTarget userList(){
		
		String uri = uriGenerator("/shiro/lms/userList") ;
		return client.target(uri);
	}

}
