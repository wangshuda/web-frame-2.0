package com.cintel.frame.mail;

/**
 * Title: MailServerInfo
 * 
 * @version 1.0
 * @author  wangshuda
 * @date   2006-11-18
 */
public class MailServerInfo {
	private String address;
	
	private int port;
	
	private int needSslFlag = 1;// 1: unnecessary , 2:necessary
	
	public MailServerInfo() {
	}
	
	public MailServerInfo(String address, int port, int needSslFlag) {
		setAddress(address);
		setPort(port);
		setNeedSslFlag(needSslFlag);
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setNeedSslFlag(int needSslFlag) {
		this.needSslFlag = needSslFlag;
	}
	
	public int getNeedSslFlag() {
		return needSslFlag;
	}
	
}
