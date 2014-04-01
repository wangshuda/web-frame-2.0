package com.cintel.frame.auth.login.session.db;

import com.cintel.frame.webui.IDomainVo;

/**
 * @file : $Id: SessionUserInfo.java 15278 2010-01-07 09:23:59Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
 */
public class SessionUserInfo implements IDomainVo {	
    private String sessionId;

	private long sessionCreatedTime;
	
    private String userName;

    private String authAreaCode;
    
    private String roleTitle;

    private String loginIp;

    private String webAppName;
    
    private String loginDateTime;

    private String invalidateTime;

    private String lastRequestTime;

    private String lastRequestUrl;

    public String getOid() {
        return String.valueOf(sessionId);
    }

    public void setOid(String oid) {
        if (oid == null) {
            return;
        }
        String oidStr[] = oid.split(",");
        sessionId = oidStr[0];
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(String loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    public String getInvalidateTime() {
        return invalidateTime;
    }

    public void setInvalidateTime(String invalidateTime) {
        this.invalidateTime = invalidateTime;
    }

    public String getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(String lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public String getLastRequestUrl() {
        return lastRequestUrl;
    }

    public void setLastRequestUrl(String lastRequestUrl) {
        this.lastRequestUrl = lastRequestUrl;
    }

	public long getSessionCreatedTime() {
		return sessionCreatedTime;
	}

	public void setSessionCreatedTime(long sessionCreatedTime) {
		this.sessionCreatedTime = sessionCreatedTime;
	}

	public String getWebAppName() {
		return webAppName;
	}

	public void setWebAppName(String webAppName) {
		this.webAppName = webAppName;
	}

	public String getAuthAreaCode() {
		return authAreaCode;
	}

	public void setAuthAreaCode(String authAreaCode) {
		this.authAreaCode = authAreaCode;
	}


}