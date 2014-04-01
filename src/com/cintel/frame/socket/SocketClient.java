package com.cintel.frame.socket;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @file    : SocketClient.java
 * @author  : WangShuDa
 * @date    : 2009-6-14
 * @corp    : CINtel
 * @version : 2.0
 */
public interface SocketClient {
	public static final int BUFFER_SIZE = 1024;
	
	public void connect();

	public void close();

	public void write(byte[] data) ;
	
	public void write(String str, boolean endWithNullChar);
	
	public void flush() ;
	
	/**
	 * replace it with void read(OutputStream out)
	 */
	@Deprecated
	public byte[] read() ;

	public void read(OutputStream out);
	
	public void setSocketConfig(SocketConfig socketConfig);
	
	public boolean validate();
	
	//
	public InputStream getInputStream();
	public OutputStream getOutputStream();
}
