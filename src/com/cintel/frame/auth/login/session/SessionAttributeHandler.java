package com.cintel.frame.auth.login.session;

import javax.servlet.http.HttpSessionBindingEvent;

/**
 * 
 * @file    : SessionAttributeHandler.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-7-3 wangshuda created
 */
public interface SessionAttributeHandler {

    public void attributeAdded(HttpSessionBindingEvent event);
    
    public void attributeRemoved(HttpSessionBindingEvent event);
    
    public void attributeReplaced(HttpSessionBindingEvent event);
}
