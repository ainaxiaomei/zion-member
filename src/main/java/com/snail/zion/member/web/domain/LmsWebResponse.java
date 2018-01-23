package com.snail.zion.member.web.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LmsWebResponse {
	
	public static final int SERVER_ERROR = 500;
	
	public static final int SUCCESS = 200;
	
	private int status;
	
	private String message;
	
	private Data data;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private Error error;

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public LmsWebResponse(int status, String message, Data data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public LmsWebResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	
	
	public LmsWebResponse() {
		
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

   
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LmsWebResponse [status=" + status + ", message=" + message + ", data=" + data + ", error=" + error
				+ "]";
	}

	
	
	
}
