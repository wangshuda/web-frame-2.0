package com.cintel.frame.auth.login;

import com.cintel.frame.auth.user.UserContext;
import com.cintel.frame.auth.user.UserContextImpl;

public class DefaultUserContext extends UserContextImpl implements UserContext {
    private static final long serialVersionUID = -1556074673939859298L;

    private String userPwd;

	private String userName;

    private String areaCode;
    
    public DefaultUserContext(String userName, String userPwd) {
		this.userName = userName;
		this.userPwd = userPwd;
	}

    public DefaultUserContext(String userName, String userPwd, String areaCode) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.areaCode = areaCode;
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

	public String getUserPassword() {
		return this.userPwd;
	}
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
