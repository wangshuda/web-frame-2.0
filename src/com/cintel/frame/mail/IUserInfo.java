package com.cintel.frame.mail;

/**
 * Title: IUserInfo
 * 
 * @version 1.0
 * @author  wangshuda
 * @date    2006-7-26
 * 
 */
public interface IUserInfo  {

	String getAccountName();

	String getKeyStr();

	int getRecvServerPort();

	String getRecvServerAddr();

	int getRecvNeedSslFlag();

	int getSendServerPort();

	String getSendServerAddr();

	int getSendNeedSslFlag();

	String getAccountPwd();
	
}
