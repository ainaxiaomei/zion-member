package com.snail.zion.member.web.validator;

public class StandardValidateContext implements ValidateContext{
	
	private Object param;
	
	public StandardValidateContext(Object param) {
		super();
		this.param = param;
	}

	@Override
	public <T> T getParam(Class<T> type) {
		
		return type.cast(param);
	}

	@Override
	public Object getParam() {
		return param;
	}


}
