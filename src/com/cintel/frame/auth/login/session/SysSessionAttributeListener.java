package com.cintel.frame.auth.login.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.WebUtils;

/**
 * 
 * @file    : SysSessionAttributeListener.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-7-3 wangshuda created
 */
public class SysSessionAttributeListener implements HttpSessionAttributeListener {
    
    private static Log log = LogFactory.getLog(SysSessionAttributeListener.class);
    
    private final static String _DEFAULT_SYS_SESSION_ATTR_DISPOSER_NAME = "SysSessionAttributeDisposer";
    
    private SysSessionAttributeDisposer disposer = null;
    
    private String disposerNameInSpring = null;
    
    public void attributeAdded(HttpSessionBindingEvent event) {
        if(!StringUtils.hasText(this.disposerNameInSpring)) {
            HttpSession session = event.getSession();
            //
            this.disposerNameInSpring = session.getServletContext().getInitParameter("SessionAttributeDisposerNameInSpring");
            //
            if(!StringUtils.hasText(this.disposerNameInSpring)) {
                this.disposerNameInSpring = _DEFAULT_SYS_SESSION_ATTR_DISPOSER_NAME;
            }
            //
            log.info(this.disposerNameInSpring);
            this.disposer = (SysSessionAttributeDisposer)(WebUtils.getSpringBean(session, this.disposerNameInSpring));
        }
        //
        disposer.attributeAdded(event);
    }

    public void attributeRemoved(HttpSessionBindingEvent event) {
        if(disposer != null) {
            disposer.attributeRemoved(event);
        }
        else {
            log.warn("calling the attributeRemoved[" + event.getName() + "] without disposer!");
        }
    }

    public void attributeReplaced(HttpSessionBindingEvent event) {
        if(disposer != null) {
            disposer.attributeReplaced(event);
        }
        else {
            log.warn("calling the attributeRemoved[" + event.getName() + "] without disposer!");
        }
    }

}
