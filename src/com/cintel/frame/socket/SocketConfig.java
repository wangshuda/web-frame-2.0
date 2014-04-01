package com.cintel.frame.socket;

public class SocketConfig {

	private String serverIpAddr;

	private int serverPort;
	
	private int clientPort = -1;
	
	private int soTimeout = 60 * 1000;

	private boolean autoFlush = true;

	private boolean autoReconncet = true;

	public SocketConfig() {
		
	}
	public SocketConfig(String serverIpAddr, int serverPort) {
		this.serverIpAddr = serverIpAddr;
		this.serverPort = serverPort;
	}
	
	public boolean isAutoFlush() {
		return autoFlush;
	}

	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}

	public boolean isAutoReconncet() {
		return autoReconncet;
	}

	public void setAutoReconncet(boolean autoReconncet) {
		this.autoReconncet = autoReconncet;
	}
	
	public String getServerIpAddr() {
		return serverIpAddr;
	}

	public void setServerIpAddr(String serverIpAddr) {
		this.serverIpAddr = serverIpAddr;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getClientPort() {
		return clientPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
}
