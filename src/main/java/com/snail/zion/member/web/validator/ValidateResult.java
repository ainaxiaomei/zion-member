package com.snail.zion.member.web.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidateResult {
	
	private boolean success;
	
	private List<String> messages;
	
	private int errorCode;
	
	
	
	public ValidateResult() {
		this.success = false;
		this.messages = new ArrayList<>();
		this.errorCode = -1;
	}


	public  void addMessage(String msg) {
		this.messages.add(msg);
	}

	public List<String> getMessages() {
		return messages;
	}



	public void setMessages(List<String> messages) {
		this.messages = messages;
	}



	public int getErrorCode() {
		return errorCode;
	}



	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}



	public void setSuccess(boolean success) {
		this.success = success;
	}



	public boolean isSuccess() {
		return this.success;
	}
}
