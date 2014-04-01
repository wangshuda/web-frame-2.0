package com.cintel.frame.auth.login.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;

/**
 *
 * @file    : SysSessionAttributeDisposerImpl.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-7-3 wangshuda created
 */
public class SysSessionAttributeDisposerImpl implements SysSessionAttributeDisposer {

    private static Log log = LogFactory.getLog(SysSessionAttributeDisposerImpl.class);
    
    private Map<String, SessionAttributeHandler> sessionAttributeHandlerMap = new HashMap<String, SessionAttributeHandler>();
    
    private boolean mustGrantedInMap = false;
    
    public void setMustGrantedInMap(boolean mustGrantedInMap) {
        this.mustGrantedInMap = mustGrantedInMap;
    }

    public void setSessionAttributeHandlerMap(Map<String, SessionAttributeHandler> sessionAttributeHandlerMap) {
        this.sessionAttributeHandlerMap = sessionAttributeHandlerMap;
    }

    // ------------- methods declare in SysSessionAttributeDisposer ---------------
    public void attributeAdded(HttpSessionBindingEvent event) {
        String attributeName = event.getName();
        //
        log.info("attributeAdded:" + attributeName);
        
        if(sessionAttributeHandlerMap.containsKey(attributeName)) {
            SessionAttributeHandler handler = sessionAttributeHandlerMap.get(attributeName);
            //
            if(handler != null) {
                handler.attributeAdded(event);
            }
        }
        else if(mustGrantedInMap) {
            throw new ErrorReportException(new ErrorInfo("error.noGrantedSessionKey", attributeName));
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent event) {
        String attributeName = event.getName();
        //
        log.info("attributeRemoved:" + attributeName);
        //
        if(sessionAttributeHandlerMap.containsKey(attributeName)) {
            SessionAttributeHandler handler = sessionAttributeHandlerMap.get(attributeName);
            //
            if(handler != null) {
                handler.attributeRemoved(event);
            }
        }
        else if(mustGrantedInMap) {
            throw new ErrorReportException(new ErrorInfo("error.noGrantedSessionKey", attributeName));
        }
    }

    public void attributeReplaced(HttpSessionBindingEvent event) {
        String attributeName = event.getName();
        //
        log.info("attributeReplaced:" + attributeName);
        if(sessionAttributeHandlerMap.containsKey(attributeName)) {
            SessionAttributeHandler handler = sessionAttributeHandlerMap.get(attributeName);
            //
            if(handler != null) {
                handler.attributeReplaced(event);
            }
        }
        else if(mustGrantedInMap) {
            throw new ErrorReportException(new ErrorInfo("error.noGrantedSessionKey", attributeName));
        }
    }

}
