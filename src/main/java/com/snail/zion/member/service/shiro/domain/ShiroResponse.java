package com.snail.zion.member.service.shiro.domain;

public class ShiroResponse {
	
	public static final String CODE_SUCCESS = "0";
	
	private String code ;
	
	private String message ;
	
	private ShiroData data;

	public ShiroData getData() {
		return data;
	}

	public void setData(ShiroData data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ShiroResponse [code=" + code + ", message=" + message + ", data=" + data + "]";
	}
	
    
	
	
}
