package com.cintel.frame.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @version $Id: WebUtils.java 37976 2013-01-14 09:00:11Z wangshuda $
 * @history 
 *          1.0.0 2010-2-8 wangshuda created
 */
public class WebUtils {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(WebUtils.class);
	
	public static Map<String, String> getParametersStartingWith(ServletRequest request, String prefix) {
		Enumeration paramNames = request.getParameterNames();
		Map<String, String> params = new HashMap<String, String>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				//
				if(values != null) {
					int iLen = values.length;
					for(int i = 0; i < iLen; i ++) {
						//
						params.put(unprefixed, values[i]);
					}
				}
			}
		}
		return params;
	}
		
	public static Object getSpringBean(HttpSession session, String beanName) {
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        Object rtnObj = null;
        if(webAppContext != null) {
            rtnObj = webAppContext.getBean(beanName);
        }
		return rtnObj;
	}
	
	public static String parseRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
