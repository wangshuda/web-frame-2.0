package com.cintel.frame.ws.axis.auth;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.context.SecurityContext;
import com.cintel.frame.auth.context.SecurityContextHolder;
import com.cintel.frame.auth.context.SecurityContextImpl;
import com.cintel.frame.auth.user.GrantedAuthority;
import com.cintel.frame.auth.user.GrantedAuthorityImpl;
import com.cintel.frame.auth.user.RoleContextImpl;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.auth.user.UserDetailsImpl;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.WebUtils;


/**
 * 
 * @version $Id: WsRequestMessageHandler.java 32334 2011-12-23 09:10:09Z anlina $
 * @history 
 *          1.0.0 2009-12-31 wangshuda created
 */
public class WsRequestMessageHandler extends BasicHandler {
	private static final long serialVersionUID = -536957249755792523L;
	//
	private static final Log log = LogFactory.getLog(WsRequestMessageHandler.class);
	
	protected static final String _WS_USER_NAME_KEY = "webWsUserName";
	protected static final String _WS_USER_PWD_KEY = "webWsUserPwd";
	
	//
	protected static final String _WS_USER_ROLE_KEY = "webWsUserRole";
	protected static final String _DEFAULT_WS_USER_ROLE = "ROLE_CC_WS_USER";
	
	protected String getWsUserName() {
		return this.loadOptConf(_WS_USER_NAME_KEY, _WS_USER_NAME_KEY);
	}
	
	//
	protected String getWsUserPwd() {
		return this.loadOptConf(_WS_USER_PWD_KEY, _WS_USER_PWD_KEY);
	}
	//
	protected String getWsUserRole() {
		return this.loadOptConf(_WS_USER_ROLE_KEY, _DEFAULT_WS_USER_ROLE);
	}

	protected String loadOptConf(String key, String defaultValue) {
		String optConf = (String)this.options.get(key);
		if(!StringUtils.hasText(optConf)) {
			optConf = defaultValue;
			//
			if(log.isInfoEnabled()) {
				log.info(new StringBuffer("Can not find the parameter with name:").append(key));
			}
			
		}
		return optConf;
	}
	

	//
	public void invoke(MessageContext wsMsgCtx) throws AxisFault {
		if(log.isDebugEnabled()) {
			log.debug(wsMsgCtx.getRequestMessage().getSOAPPartAsString());
		}
		//
		String userName = wsMsgCtx.getUsername();
		if(!StringUtils.hasText(userName)) {
			userName = this.getWsUserName();
		}
		//
		String userPwd = wsMsgCtx.getPassword();
		if(!StringUtils.hasText(userPwd)) {
			userPwd = this.getWsUserPwd();
		}
		//
		String wsRoleKeyStr = this.getWsUserRole();
		RoleContextImpl roleContext = new RoleContextImpl();
		roleContext.setKey(wsRoleKeyStr);
		roleContext.setTitle(wsRoleKeyStr);
		//
		GrantedAuthorityImpl grantedAuthorityImpl = new GrantedAuthorityImpl(roleContext);
		UserDetails userDetails = new UserDetailsImpl(userName, userPwd, new GrantedAuthority[]{grantedAuthorityImpl});
		
		//
		WsRequestContext wsRequestContext = new WsRequestContext();
		wsRequestContext.setMsgContext(wsMsgCtx);
		//
		HttpServletRequest httpRequest = (HttpServletRequest) wsMsgCtx.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		
		String requestIp = WebUtils.parseRequestIp(httpRequest);
		
		wsRequestContext.setRemoteAddr(requestIp);
		userDetails.setLoginRequestIp(requestIp);
		userDetails.setLoginSessionId(httpRequest.getSession().getId());
		//
		userDetails.setUserContext(wsRequestContext);
		
		//
		SecurityContext securityContext = new SecurityContextImpl();
		securityContext.setObject(userDetails);
        SecurityContextHolder.setContext(securityContext);
        //
        if(log.isDebugEnabled()) {
        	log.debug(new StringBuffer("Bind the message context info with the current thread:").append(Thread.currentThread().getId()));
        }
	}
}
