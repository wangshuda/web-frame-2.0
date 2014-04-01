package com.cintel.frame.log;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @file    : LogPersisterLog4jImpl.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-9-14 wangshuda created
 */
public class LogPersisterLog4jImpl implements LogPersister {
    private static Log log = LogFactory.getLog(LogPersisterLog4jImpl.class);
    //
    private String logTextWithUserCtx = "StreamNumber:{0}\tKey:{1}\tText:{2}\tUserName:{3}\tIP:{4}\t";
    
    public void logCtx(LogContext logContext) {
        this.logCtxBean((AbstractLogContextBean)logContext);
    }

    public void logCtxBean(AbstractLogContextBean logContext) {
        String log4JText = null;
        log4JText = MessageFormat.format(logTextWithUserCtx, 
                logContext.getStreamNumber(), logContext.getLogKey(), logContext.getLogText(), logContext.getUserName(), logContext.getRequestIp());
        //
        if(log.isInfoEnabled()) {
            log.info(log4JText);
        }
    }

}
