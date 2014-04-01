package com.cintel.frame.ws.axis.auth;

import org.apache.axis.MessageContext;

import com.cintel.frame.auth.user.UserContext;

/**
 * 
 * @version $Id: WsRequestContext.java 16685 2010-02-08 01:34:21Z wangshuda $
 * @history 
 *          1.0.0 2009-12-31 wangshuda created
 */
public class WsRequestContext implements UserContext {
	private static final long serialVersionUID = 4050360082959258885L;
	//
	private MessageContext msgContext = null;
	
	private String remoteAddr = null;

	public MessageContext getMsgContext() {
		return msgContext;
	}

	public void setMsgContext(MessageContext msgContext) {
		this.msgContext = msgContext;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	//
	public String getUserName() {
		return msgContext.getUsername();
	}

	public String getAreaCode() {
		return null;
	}

	public String getUserPassword() {
		return msgContext.getPassword();
	}
}
