package com.cintel.frame.tag;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.StringUtils;

/**
 * 
 * @version $Id: HostName2Ip.java 34286 2012-05-22 02:11:53Z wangshuda $
 * @history 
 *          1.0.0 2010-3-1 wangshuda created
 */
public class HostName2Ip extends TagSupport {
	private static final long serialVersionUID = 6294032789411906953L;

	private Log log = LogFactory.getLog(HostName2Ip.class);
	
	private static final String _LOCAL_HOST_NAME = "localhost";
	
	protected String hostName;
	
	protected String defaultHostName = _LOCAL_HOST_NAME;
	
	protected String var;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	//
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	public int doStartTag() throws JspException {
		if(!StringUtils.hasText(hostName)) {
			hostName = defaultHostName; 
		}
		//
		try {
			InetAddress netAddr = InetAddress.getByName(hostName);
			
			String ipAddr = netAddr.getHostAddress();
			if(StringUtils.hasText(var)) {
				pageContext.setAttribute(var, ipAddr, PageContext.REQUEST_SCOPE);
			}
			else {
                pageContext.getOut().print(ipAddr);  
            }
		}
		catch (UnknownHostException ex) {
			log.error(new StringBuilder("Unknown Host:").append(hostName), ex);
		}
		catch (IOException ex) {
			log.error("Write to pageContext error!", ex);
		}
		//
		return EVAL_BODY_INCLUDE;
	}

	public String getDefaultHostName() {
		return defaultHostName;
	}

	public void setDefaultHostName(String defaultHostName) {
		this.defaultHostName = defaultHostName;
	}
}
