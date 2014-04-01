package com.cintel.frame.auth.tag;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.context.SecurityContext;
import com.cintel.frame.util.StringUtils;

/**
 * 
 * @file    : UserInfoTag.java
 * @author  : WangShuDa
 * @date    : 2009-6-14
 * @corp    : CINtel
 * @version : 1.2
 * @history :
 * 	1) 2009/06/14 wangshuda : replace the value from String to Object.
 */
public class UserInfoTag extends TagSupport {
	private static final long serialVersionUID = 6294032789411906953L;

	protected final Log log = LogFactory.getLog(this.getClass());

	protected String property;

	protected String var;
	
	protected String nullValue = "";
	
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		if (StringUtils.hasText(property)) {
			try {
				Object userDetails = null;
				HttpSession session = pageContext.getSession();
				userDetails = session.getAttribute(SecurityContext.SECURITY_CONTEXT_KEY);
				//
				Object value = null;
				if(userDetails != null) {
					try {
						value = PropertyUtils.getProperty(userDetails, property);
					}
					catch(NoSuchMethodException ex) {
						log.warn("Can not find the property with name:" + property + " in the userDetails saved in the session.");
					}
					catch(NestedNullException ex) {
						log.warn("Can not find the property with name:" + property + " in the userDetails saved in the session.");
					}
				}
				//
				if(value == null && nullValue != null) {
					value = nullValue;
				}
				//
				if(StringUtils.hasText(var)) {
					pageContext.setAttribute(var, value, PageContext.REQUEST_SCOPE);
				}
				else {
					pageContext.getOut().print(value);
				}
				
			} catch (Exception ex) {
				log.error("UserInfoTag Error !", ex);

				return SKIP_BODY;
			}
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

}