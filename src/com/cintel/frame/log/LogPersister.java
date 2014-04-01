package com.cintel.frame.log;

/**
 * 
 * @file    : LogPersister.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-9-14 wangshuda created
 */
public interface LogPersister {

    /**
     * logCtx
     * @param logContext
     */
    public void logCtx(LogContext logContext);
    
    /**
     * logCtxBean
     * @param logCtxBean
     */
    public void logCtxBean(AbstractLogContextBean logCtxBean);
}
