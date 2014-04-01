package com.cintel.frame.auth.login.session;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.context.SecurityContext;
import com.cintel.frame.auth.login.session.db.SessionUserInfo;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.util.DateUtils;
import com.cintel.frame.webui.IDomainService;

public class AuthInfoSessionAttributeHandler implements SessionAttributeHandler {
    private static Log log = LogFactory.getLog(AuthInfoSessionAttributeHandler.class);
    
    private final static int _DEL_ON_SESSION_INVALIDATE = 0;
    
    private final static int _UPDATE_ON_SESSION_INVALIDATE = 1;
    
    private int sessionInvalidateMode = _DEL_ON_SESSION_INVALIDATE;
    
    private boolean registerSessionUserInDb = true;
    
    private IDomainService sessionUserInfoService;
    //
    
    private void assertKey(String attributeName) {
        if(!(SecurityContext.SECURITY_CONTEXT_KEY.equals(attributeName))) {
            String errorInfo = "Can not access key[" + attributeName + "]. Can only access the key:" + SecurityContext.SECURITY_CONTEXT_KEY;
            log.error(errorInfo);
            //
            throw new UnsupportedOperationException(errorInfo);
        } 
    }
    
    public void attributeAdded(HttpSessionBindingEvent event) {
        this.assertKey(event.getName());
        HttpSession session = event.getSession();
        //
        if(registerSessionUserInDb) {
            UserDetails userDetails = (UserDetails)(event.getValue());
            //
            SessionUserInfo sessionUserInfo = new SessionUserInfo();
            sessionUserInfo.setUserName(userDetails.getUserNumber());
            sessionUserInfo.setRoleTitle(userDetails.getDisplayRoleContext().getTitle());
            sessionUserInfo.setLoginIp(userDetails.getLoginRequestIp());
            if(userDetails.getUserContext() != null) {
            	sessionUserInfo.setAuthAreaCode(userDetails.getUserContext().getAreaCode());
            }
            //
            sessionUserInfo.setLoginDateTime(DateUtils.getCurrentTimeString());
            sessionUserInfo.setSessionCreatedTime(session.getCreationTime());
            //
            sessionUserInfo.setSessionId(session.getId());
            sessionUserInfo.setWebAppName(session.getServletContext().getServletContextName());
            //
            sessionUserInfoService.insert(sessionUserInfo);
        } 
    }

    public void attributeRemoved(HttpSessionBindingEvent event) {
        HttpSession session = event.getSession();
        //
        if(sessionInvalidateMode == _DEL_ON_SESSION_INVALIDATE && registerSessionUserInDb) {
            sessionUserInfoService.delete("deleteWithId", session.getId());
        }
        else if (sessionInvalidateMode == _UPDATE_ON_SESSION_INVALIDATE && registerSessionUserInDb) {
        	SessionUserInfo sessionUserInfo = new SessionUserInfo();
        	sessionUserInfo.setSessionId(session.getId());
        	//
        	sessionUserInfo.setInvalidateTime(DateUtils.getCurrentTimeString());
        	sessionUserInfo.setLastRequestTime(DateUtils.getDate14FromDate(new Date(event.getSession().getLastAccessedTime())));
        	//
        	sessionUserInfoService.update("updateInvalidateTime", sessionUserInfo);
        }
    }

    public void attributeReplaced(HttpSessionBindingEvent event) {
        HttpSession session = event.getSession();
        if(sessionInvalidateMode == _DEL_ON_SESSION_INVALIDATE && registerSessionUserInDb) {
            sessionUserInfoService.delete("deleteWithId", session.getId());
        }
        //
        this.attributeAdded(event);
    }

    //
    public IDomainService getSessionUserInfoService() {
        return sessionUserInfoService;
    }

    public void setSessionUserInfoService(IDomainService sessionUserInfoService) {
        this.sessionUserInfoService = sessionUserInfoService;
    }

    public void setSessionInvalidateMode(int sessionInvalidateMode) {
        this.sessionInvalidateMode = sessionInvalidateMode;
    }

    public void setRegisterSessionUserInDb(boolean registerSessionUserInDb) {
        this.registerSessionUserInDb = registerSessionUserInDb;
    }

}
