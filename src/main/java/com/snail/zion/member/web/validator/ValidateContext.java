package com.snail.zion.member.web.validator;

public interface ValidateContext {
	
	<T> T getParam(Class<T> type);
	
	Object getParam();
}
