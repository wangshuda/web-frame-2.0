package com.cintel.frame.log;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cintel.frame.log.LogHandler;

public class LogHandlerLoader implements ApplicationContextAware {

    private String logHandlerImplBeanName = "LogHandler";
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LogHandler logHandler = (LogHandler)(applicationContext.getBean(logHandlerImplBeanName));
        LogHandlerFactory.setInstance(logHandler);
    }

    public String getLogHandlerImplBeanName() {
        return logHandlerImplBeanName;
    }

    public void setLogHandlerImplBeanName(String logHandlerImplBeanName) {
        this.logHandlerImplBeanName = logHandlerImplBeanName;
    }
}
