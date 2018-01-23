package com.snail.zion.member.service.shiro.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.snail.zion.member.web.domain.User;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ShiroData {
	
	private List<String> roles;
	
	private List<String> permissions;
	
	private String organization;
	
	private String token;
	
	private List<User> userList;
	
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "ShiroData [roles=" + roles + ", permissions=" + permissions + ", organization=" + organization
				+ ", token=" + token + "]";
	}
	
	
	
	
	
}
