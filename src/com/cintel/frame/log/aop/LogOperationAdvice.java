package com.cintel.frame.log.aop;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;

import com.cintel.frame.log.LogHandler;
import com.cintel.frame.log.holder.LogParametersHolder;
import com.cintel.frame.properties.PropertiesUtils;
import com.cintel.frame.util.StringUtils;

/**
 * 
 * @file    : LogOperationAdvice.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-8-21 wangshuda created
 */
public class LogOperationAdvice implements AfterReturningAdvice  {

    private static Log log = LogFactory.getLog(LogOperationAdvice.class);
    
    private LogHandler logHandler;
    
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        if(log.isDebugEnabled()) {
            log.debug("Call Method" + method.getName());
        }
        //
        try {
            Method implMethod = target.getClass().getMethod( method.getName(), method.getParameterTypes());
            
            //
            LogOperation logOperationAnnotation = implMethod.getAnnotation(LogOperation.class);
            //
            String key = logOperationAnnotation.key();
            if(!(StringUtils.hasText(key))) {
                key = implMethod.getName();
            }
            //
            Object[] logParameters = LogParametersHolder.load();
            
            String text = logOperationAnnotation.text();
            if(!StringUtils.hasText(text)) {
                text = PropertiesUtils.getMessage(key);
            }
            //
            if(logParameters != null) {
                text = MessageFormat.format(text, logParameters);
            }
            //
            logHandler.log(key, text);
        }
        catch(Throwable ex) {
            log.warn("Call LogOperationAdvice error!", ex);
        }
        //
        LogParametersHolder.clear();
    }

    public LogHandler getLogHandler() {
        return logHandler;
    }

    public void setLogHandler(LogHandler logHandler) {
        this.logHandler = logHandler;
    }
}
