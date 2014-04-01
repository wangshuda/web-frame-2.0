package com.cintel.frame.log;

/**
 * 
 * @version $Id: AbstractLogContextBean.java 15278 2010-01-07 09:23:59Z wangshuda $
 * @history 
 *          1.0.0 2010-1-7 wangshuda created
 */
public abstract class AbstractLogContextBean implements LogContext {
    private String logDateTime;
    
    private String logKey;

    private String logText;

    private String streamNumber;

    private String requestIp;

    private String userName;

    private String authAreaCode;

    private String userRoleName;
    
    private String key1;
    
    private String key2;
    
    private String key3;
    
    private String key4;
    
    private String key5;

    public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getKey3() {
		return key3;
	}

	public void setKey3(String key3) {
		this.key3 = key3;
	}

	public String getKey4() {
		return key4;
	}

	public void setKey4(String key4) {
		this.key4 = key4;
	}

	public String getKey5() {
		return key5;
	}

	public void setKey5(String key5) {
		this.key5 = key5;
	}

	public String getLogKey() {
        return logKey;
    }

    public String getLogText() {
        return logText;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public String getStreamNumber() {
        return streamNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public String getLogDateTime() {
        return logDateTime;
    }
    
    public void setLogKey(String logKey) {
        this.logKey = logKey;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public void setStreamNumber(String streamNumber) {
        this.streamNumber = streamNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public void setLogDateTime(String logDateTime) {
        this.logDateTime = logDateTime;
    }

	public String getAuthAreaCode() {
		return authAreaCode;
	}

	public void setAuthAreaCode(String authAreaCode) {
		this.authAreaCode = authAreaCode;
	}
}
