package com.cintel.frame.net.http.auth;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-12-7 wangshuda created
 */
public class HttpAuthCtx {
	private String userName;
	
	private String userPwd;
	
	private boolean preemptive = true;
	
	private String hostAddr = AuthScope.ANY_HOST;
	
	private int hostPort = AuthScope.ANY_PORT;
	
	private List<String> authPrefsList = null;;
	
	public HttpAuthCtx () {
		authPrefsList = new ArrayList<String>(3);
		//
		authPrefsList.add(AuthPolicy.DIGEST);
		authPrefsList.add(AuthPolicy.BASIC);
		authPrefsList.add(AuthPolicy.NTLM);
	}
	
	public String getHostAddr() {
		return hostAddr;
	}

	public void setHostAddr(String hostAddr) {
		this.hostAddr = hostAddr;
	}

	public int getHostPort() {
		return hostPort;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public boolean isPreemptive() {
		return preemptive;
	}

	public void setPreemptive(boolean preemptive) {
		this.preemptive = preemptive;
	}

	public List<String> getAuthPrefsList() {
		return authPrefsList;
	}

	public void setAuthPrefsList(List<String> authPrefsList) {
		this.authPrefsList = authPrefsList;
	}
}
