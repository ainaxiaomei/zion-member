package com.snail.zion.member.web.domain;

public class Error {
	
	public static final String SOURCE_SHIRO = "shiro";
	public static final String SOURCE_SNAILCLOUD = "snailcloud";
	public static final String SOURCE_LMS = "lms";
	
	private String source;
	
	private String code = "-1";
	
	private String message;
	
    
	public static Error instance (){
		return new Error();
	}
	
	public Error source(String source){
		this.source = source;
		return this;
	}
	
	public Error code(String code){
		this.code = code;
		return this;
	}
	
	public Error message(String message){
		this.message = message;
		return this;
	}
	
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
		return "Error [source=" + source + ", code=" + code + ", message=" + message + "]";
	}
	
	
	

}
