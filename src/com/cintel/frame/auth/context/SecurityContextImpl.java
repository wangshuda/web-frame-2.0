package com.cintel.frame.auth.context;

public class SecurityContextImpl implements SecurityContext {
	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
