package com.cintel.frame.socket;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @file    : SocketMsgSenderImpl.java
 * @author  : WangShuDa
 * @date    : 2008-9-26
 * @corp    : CINtel
 * @version : 1.0
 */
public class SocketMsgSenderImpl implements SocketMsgSender{
	protected final Log log = LogFactory.getLog(this.getClass());
	
	protected SocketClient socketClient = null;
		
	public void write(String requestStr, boolean endWithNullChar) {
		this.getSocketClient().write(requestStr, endWithNullChar);
	}

	public void read(OutputStream response) {
		this.getSocketClient().read(response);
	}
	
	public String post(String requestStr) {
		return this.post(requestStr, true);
	}

	public String post(String requestStr, boolean existResponse) {
		this.getSocketClient().write(requestStr, true);
		//
		if(existResponse) {
			ByteArrayOutputStream out = new ByteArrayOutputStream(SocketClient.BUFFER_SIZE);
			this.getSocketClient().read(out);
			return new String(out.toByteArray());
		}
		else {
			return "";
		}
	}

	public void post(String requestStr, OutputStream response) {
		this.getSocketClient().write(requestStr, true);
		this.getSocketClient().read(response);
	}


	
	public InputStream getInputStream() {
		return this.getSocketClient().getInputStream();
	}
	
	public OutputStream getOutputStream() {
		return this.getSocketClient().getOutputStream();
	}
	
	public SocketClient getSocketClient() {
		return this.socketClient;
	}

	public void setSocketClient(SocketClient socketClient) {
		this.socketClient = socketClient;
	}

}
