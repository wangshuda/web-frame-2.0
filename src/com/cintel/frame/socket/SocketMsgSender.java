package com.cintel.frame.socket;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * 
 * @file    : SocketMsgSender.java
 * @author  : WangShuDa
 * @date    : 2008-9-26
 * @corp    : CINtel
 * @version : 1.0
 */
public interface SocketMsgSender {
	public String post(String requestStr);
	
	public String post(String requestStr, boolean existResponse);
	
	public void post(String requestStr, OutputStream response);
	
	public void write(String requestStr, boolean endWithNullChar);
	
	public void read(OutputStream response);
	
	public InputStream getInputStream();
	public OutputStream getOutputStream();
	
}
