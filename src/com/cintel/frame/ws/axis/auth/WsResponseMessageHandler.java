package com.cintel.frame.ws.axis.auth;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.context.SecurityContextHolder;

/**
 * 
 * @version $Id: WsResponseMessageHandler.java 42658 2014-03-21 14:56:24Z wangshuda $
 * @history 
 *          1.0.0 2009-12-31 wangshuda created
 */
public class WsResponseMessageHandler extends BasicHandler {
	private static final long serialVersionUID = -536957249755792523L;
	//
	private static final Log log = LogFactory.getLog(WsResponseMessageHandler.class);
	//
	public void invoke(MessageContext wsMsgCtx) throws AxisFault {
		if(log.isDebugEnabled()) {
			log.debug(wsMsgCtx.getResponseMessage().getSOAPPartAsString());
		}
		//
		this.clearSecurityContextHolder();
	}
	
	@Override
	public void onFault(MessageContext wsMsgCtx) {
		this.clearSecurityContextHolder();
    }
	
	private void clearSecurityContextHolder() {
        SecurityContextHolder.clearContext();
	}
}
