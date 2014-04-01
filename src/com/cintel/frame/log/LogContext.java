package com.cintel.frame.log;

import com.cintel.frame.webui.IDomainVo;

/**
 * 
 * @version $Id: LogContext.java 15278 2010-01-07 09:23:59Z wangshuda $
 *
 */
public interface LogContext extends IDomainVo {
    
    public void setLogDateTime(String logDateTime);
    
    public void setLogKey(String logKey);
    
    public void setLogText(String logText);
    
    public void setStreamNumber(String streamNumber);
    
    public void setRequestIp(String requestIp);
    
    public void setSessionId(String sessionId);
    
    public void setUserName(String userName);
    
    public void setAuthAreaCode(String authAreaCode);
    
    public void setUserRoleName(String userRoleName);
    
    public void setKey1(String key1);
    
    public void setKey2(String key2);
    
    public void setKey3(String key3);
    
    public void setKey4(String key4);
    
    public void setKey5(String key5);
    
    public String getLogDateTime();
    
    public String getLogKey();
    
    public String getLogText();
    
    public String getStreamNumber();
    
    public String getRequestIp();
    
    public String getSessionId();
    
    public String getUserName();
    
    public String getAuthAreaCode();
    
    public String getUserRoleName();
    
    public String getKey1();
    
    public String getKey2();
    
    public String getKey3();
    
    public String getKey4();
    
    public String getKey5();
}
