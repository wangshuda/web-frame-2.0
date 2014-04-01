package com.cintel.frame.log;

import com.cintel.frame.log.LogHandler;

public class LogHandlerFactory {
    private static LogHandler instance = null;
    
    private LogHandlerFactory() {
    }
    
    public static LogHandler getInstance() {
        return instance;
    }
    
    public static void setInstance(LogHandler logPersister) {
        instance = logPersister;
    }
}
