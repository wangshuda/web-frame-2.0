package com.cintel.frame.log;

/**
 * @file : $Id: LogContextBean.java 15278 2010-01-07 09:23:59Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
 */
public class LogContextBean extends AbstractLogContextBean {

    private String streamNumber;

    private String logKey;

    private String logText;

    private int operateType;
    
    private int operateResult;
    
    private String logDateTime;

    private String userName;

    private String userRoleKey;

    private String userRoleName;

    private String authAreaCode;
    
    private String requestIp;
    
    private String sessionId;
    
    private String key1;
    
    private String key2;
    
    private String key3;
    
    private String key4;
    
    private String key5;

    public String getOid() {
        return String.valueOf(streamNumber);
    }

    public void setOid(String oid) {
        if (oid == null) {
            return;
        }
        String oidStr[] = oid.split(",");
        streamNumber = oidStr[0];
    }

    public String getStreamNumber() {
        return streamNumber;
    }

    public void setStreamNumber(String streamNumber) {
        this.streamNumber = streamNumber;
    }

    public String getLogKey() {
        return logKey;
    }

    public void setLogKey(String logKey) {
        this.logKey = logKey;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public String getLogDateTime() {
        return logDateTime;
    }

    public void setLogDateTime(String logDateTime) {
        this.logDateTime = logDateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRoleKey() {
        return userRoleKey;
    }

    public void setUserRoleKey(String userRoleKey) {
        this.userRoleKey = userRoleKey;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	public int getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(int operateResult) {
		this.operateResult = operateResult;
	}

	public String getAuthAreaCode() {
		return authAreaCode;
	}

	public void setAuthAreaCode(String authAreaCode) {
		this.authAreaCode = authAreaCode;
	}
}