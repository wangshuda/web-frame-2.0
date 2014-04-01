package com.cintel.frame.mail;

import com.cintel.frame.encrypt.DesDigest;

/**
 * Title: MailAccountInfo
 * 
 * @version 1.0
 * @author  wangshuda
 * @date   2006-11-18
 */
public class MailAccountInfo {
	
	private MailServerInfo recvServer = new MailServerInfo();
	
	private MailServerInfo sendServer = new MailServerInfo();
	
	private String accountName;
	
	private String accountPwd;
	
	public MailAccountInfo() {
		
	}
	
	public MailAccountInfo(IUserInfo userInfo) {
		setAccountName(userInfo.getAccountName());
		//
		try {
			
			//decrypt the userPin of userInfo
			String keyStr = userInfo.getKeyStr();
			DesDigest desDigest = new DesDigest(keyStr);
	        //
			setAccountPwd(desDigest.getRawPwd(userInfo.getAccountPwd()));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		//
		MailServerInfo recvServer = new MailServerInfo();
		recvServer.setPort(userInfo.getRecvServerPort());
		recvServer.setAddress(userInfo.getRecvServerAddr());
		recvServer.setNeedSslFlag(userInfo.getRecvNeedSslFlag());
		//
		MailServerInfo sendServer = new MailServerInfo();
		sendServer.setPort(userInfo.getSendServerPort());
		sendServer.setAddress(userInfo.getSendServerAddr());
		sendServer.setNeedSslFlag(userInfo.getSendNeedSslFlag());
		
		setRecvServer(recvServer);
		setSendServer(sendServer);
	}
	
	public void setRecvServer(MailServerInfo recvServer) {
		this.recvServer = recvServer;
	}
	public MailServerInfo getRecvServer() {
		return recvServer;
	}

	public void setSendServer(MailServerInfo sendServer) {
		this.sendServer = sendServer;
	}
	public MailServerInfo getSendServer() {
		return sendServer;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}
	
	public String getAccountPwd() {
		return accountPwd;
	}
}
