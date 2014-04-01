package com.cintel.frame.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @file    : SocketClientImpl.java
 * @author  : WangShuDa
 * @date    : 2008-12-30
 * @corp    : CINtel
 * @version : 2.0
 */
public class SocketClientImpl implements SocketClient{
	protected Log log = LogFactory.getLog(SocketClientImpl.class);
	//
	protected Socket socket = null;
	protected InputStream inputStream = null;
	protected OutputStream outputStream = null;

	//
	private SocketConfig socketConfig = null;

	protected boolean isValid = true;
	
	/**
	 * 
	 * @param socketConfig
	 */
	public SocketClientImpl(SocketConfig socketConfig) {
		this.setSocketConfig(socketConfig);
	}
	
	/**
	 * connect
	 * 
	 */
	public void connect() {
		if(log.isDebugEnabled()) {
			log.debug("Trying to Connecte with " + socketConfig.getServerIpAddr() + ":" + socketConfig.getServerPort());
		}
		//
		isValid = true;
		try {
			if(socketConfig.getClientPort() == -1) {
				socket = new Socket(socketConfig.getServerIpAddr(), socketConfig.getServerPort());
			}
			else {
				InetAddress localAddress = InetAddress.getLocalHost();
				socket = new Socket(socketConfig.getServerIpAddr(), socketConfig.getServerPort(), localAddress, socketConfig.getClientPort());
			}
			
			//init the socket
			socket.setSoTimeout(socketConfig.getSoTimeout());
			socket.setKeepAlive(true);
			//
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			//
		}
		catch(Exception ex) {
			isValid = false;
			log.error("Connected failed !", ex);
		}
		//
		if(isValid && log.isDebugEnabled()) {
			log.debug("Connected with " + socketConfig.getServerIpAddr() + ":" + socketConfig.getServerPort());
		}
	}
	
	public void close() {
		isValid = false;
		//
		try {
			if(outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
			//
			if(inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			//
			if(socket != null) {
				socket.close();
				//
				socket = null;
			}
		}
		catch(Throwable ex) {
			log.error("disConnect error!", ex);
		}
		//
		if(log.isDebugEnabled()) {
			log.debug("Disconnected from " + socketConfig.getServerIpAddr() + ":" + socketConfig.getServerPort());
		}
	}

	
	/**
	 * flush
	 * 
	 * @throws IOException
	 */
	public void flush() {
		try {
			outputStream.flush();
		} 
		catch (IOException ex) {
			throw(new RuntimeSocketException(ex));
		}
	}
	
	public void write(String str, boolean endWithNullChar) {
		StringBuffer buffer = new StringBuffer(str);
		if(endWithNullChar) {
			buffer.append("\0");
		}
		//
		this.write(buffer.toString().getBytes());
	}
	
	public void write(byte[] data)  {
		//
		try {
			outputStream.write(data);
			//
			if(socketConfig.isAutoFlush()) {
				this.flush();
			}
		}
		catch(Exception ex) {
			isValid = false;
			//
			log.error("Socket write error!", ex);
		}

	}

	/**
	 * read with ByteArrayOutputStream
	 * 
	 * @method: read
	 * @return
	 * @
	 */
	public synchronized byte[] read()  {
		//
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
			this.read(out);
			return out.toByteArray();
		}
		catch(Exception ex) {
			isValid = false;
			
			log.error("Socket read failed!", ex);
			if(socketConfig.isAutoReconncet()) {
				close();
				connect();
			}
			//
			throw(new RuntimeSocketException(ex));
		}
	}
	
	private void validateInputStream() {
		if((inputStream == null && socket != null) || (socket!= null && socket.isInputShutdown())) {
			try {
				inputStream = socket.getInputStream();
			}
			catch(Exception ex) {
				throw new RuntimeSocketException(ex);
			}
		}
	}
	/**
	 * 
	 * @method: read
	 * @param out
	 * @
	 */
	public synchronized void read(OutputStream out) {
		try {
			//
			validateInputStream();
			//
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				//
				if(bytesRead > 0) {
					if(buffer[bytesRead - 1] == '\0') {
						out.write(buffer, 0, bytesRead - 1);
						break;
					}
					else {
						out.write(buffer, 0, bytesRead);
					}
				}
				else {
					break;
				}
			}
		}
		catch(Exception ex) {
			isValid = false;
			
			log.error("Socket read failed!", ex);
			if(socketConfig.isAutoReconncet()) {
				close();
				connect();
			}
			throw(new RuntimeSocketException(ex));
		}
	}
	
	/**
	 * validate
	 */
	public boolean validate() {
		if(!isValid) {
			try {
				close();
				//
				connect();
			}
			catch(Exception ex) {
				log.error("Socket client valid failed!", ex);
			}
		}
		return isValid;
	}
	
	public InputStream getInputStream() {
		//
		validateInputStream();
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setSocketConfig(SocketConfig socketConfig) {
		this.socketConfig = socketConfig;
	}
}
