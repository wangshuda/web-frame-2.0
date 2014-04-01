package com.cintel.frame.log;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.UserInfoUtils;
import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.util.DateUtils;
import com.cintel.frame.util.RandomUtils;

/**
 * 
 * @file    : LogHandlerDefaultImpl.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-9-14 wangshuda created
 */
public class LogHandlerDefaultImpl implements LogHandler {
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(LogHandlerDefaultImpl.class);
   
    private LogPersister log4jPersister = null;

    private LogPersister logPersister;

    private LogTextLoader logTextLoader;
    
    public LogTextLoader getLogTextLoader() {
        return logTextLoader;
    }

    public void setLogTextLoader(LogTextLoader logTextLoader) {
        this.logTextLoader = logTextLoader;
    }
    
    @SuppressWarnings("deprecation")
	protected AbstractLogContextBean configCtxBean(String key, String text) {
        LogContextBean logContext = new LogContextBean();
        
        String logText = text;
        if(text.length() > 255){
        	logText = text.substring(0, 255);
        }
        
        logContext.setLogKey(key);
        //
        String lowerKey = key.toLowerCase();
        //
        if(lowerKey.endsWith("reg") || lowerKey.endsWith("insert")) {
        	logContext.setOperateType(OperateType._INSERT);
        }
        else if(lowerKey.endsWith("edit") || lowerKey.endsWith("update")) {
        	logContext.setOperateType(OperateType._UPDATE);
        }
        else if(lowerKey.endsWith("del") || lowerKey.endsWith("delete")) {
        	logContext.setOperateType(OperateType._DELETE);
        }
        else {
        	log.warn("End with: reg or insert --> INSERT; edit or update --> UPDATE; del or delete --> DELETE;");
        	log.warn("Can not judge the operate type from the log key:" + lowerKey);
        	//
        	logContext.setOperateType(OperateType._UNKONWN);
        }
        //
        logContext.setLogText(logText);
        logContext.setLogDateTime(DateUtils.getCurrentTimeString());
        logContext.setStreamNumber(RandomUtils.getStreamNo20());
        //
        UserDetails userDetails = UserInfoUtils.getUserDetails();
        
        if(userDetails != null) {
            String userName = userDetails.getUserNumber();
            logContext.setUserName(userName);
            //
            logContext.setRequestIp(userDetails.getLoginRequestIp());
            logContext.setSessionId(userDetails.getLoginSessionId());
            //
            if(userDetails.getUserContext() != null) {
            	 logContext.setAuthAreaCode(userDetails.getUserContext().getAreaCode());
            }
            //
            RoleContext roleCtx = userDetails.getDisplayRoleContext();
            String userRoleKey = roleCtx.getKey();
            
            logContext.setUserRoleKey(userRoleKey);
            logContext.setUserRoleName(roleCtx.getTitle());
        }
        return logContext;
    }
    
    //
    public void log(String key, String text) {
        AbstractLogContextBean logCtxBean = this.configCtxBean(key, text);
        if(log4jPersister != null) {
            log4jPersister.logCtxBean(logCtxBean);
        }
        logPersister.logCtx(logCtxBean);
        
    }

    public void log(String key) {
        String text = logTextLoader.getText(key);
        this.log(key, text);
    }

    public void log(String key, Object[] parameters) {
        String text = logTextLoader.getText(key);
        text = MessageFormat.format(text, parameters);
        log.info(text);
        //
        this.log(key, text);
    }
    
    // ---------------------------------------- get/set methods ----------------------------------------
    public LogPersister getLogPersister() {
        return logPersister;
    }

    public void setLogPersister(LogPersister logPersister) {
        this.logPersister = logPersister;
    }

    public LogPersister getLog4jPersister() {
        return log4jPersister;
    }

    public void setLog4jPersister(LogPersister log4jPersister) {
        this.log4jPersister = log4jPersister;
    }

}
