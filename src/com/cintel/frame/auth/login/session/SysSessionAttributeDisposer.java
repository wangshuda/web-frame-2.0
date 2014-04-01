package com.cintel.frame.auth.login.session;

import javax.servlet.http.HttpSessionBindingEvent;

public interface SysSessionAttributeDisposer {
    public void attributeAdded(HttpSessionBindingEvent event);
    
    public void attributeRemoved(HttpSessionBindingEvent event);
    
    public void attributeReplaced(HttpSessionBindingEvent event);
}
