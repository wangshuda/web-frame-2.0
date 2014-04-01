package com.cintel.frame.auth.context;

public interface SecurityContext {
	String SECURITY_CONTEXT_KEY = "com.cintel.frame.auth.context.SecurityContext";
	public Object getObject();
	public void setObject(Object object);
}
