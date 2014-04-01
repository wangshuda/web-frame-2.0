package com.cintel.frame.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * 
 * @version $Id$
 * @history 
 *          1.0.0 2010-11-18 wangshuda created
 *          1.0.1 Modified the result type from string to object.
 */
public class BeanReadTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(BeanReadTag.class);
	
	private Object targetObj = null;
	
	private String propertyName;
	
	private String nullValue = "";
	
	private String var = null;
	
	//
	public void setTargetObj(Object targetObj) throws JspException {
								   // String attributeName, String expression, Class expectedType, Tag tag, PageContext pageContext
		this.targetObj = targetObj;//ExpressionEvaluatorManager.evaluate("targetObj", targetObj.toString(), Object.class, this, pageContext);
	}
	
	public int doEndTag() throws JspException {
		try {
			Object valueObj = null;
			//
            if(this.targetObj != null) {
                valueObj = PropertyUtils.getProperty(this.targetObj, this.propertyName);
            }
            else {
                log.warn("targetObj is null, so return null for propertyName:" + this.propertyName);
            }
			//
            if(var != null) {
                pageContext.setAttribute(var, valueObj);
            }
            else {
                pageContext.getOut().print(valueObj == null ? nullValue : valueObj.toString());  
            }			
		}
		catch (Exception ex) {
			log.error("BeanReadTag", ex);

			return SKIP_BODY;
		}
		return EVAL_BODY_INCLUDE;
	}
	
	public int doStartTag() throws JspException {
		return EVAL_PAGE;
	}
	// ------------------ get/set methods --------------
	public Object getTargetObj() {
		return targetObj;
	}
	
	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}


	public String getPropertyName() {
		
		return propertyName;
	}


	public void setPropertyName(String propertyName) throws JspException {
		Object propertyElObj = ExpressionEvaluatorManager.evaluate(
				"propertyName", propertyName.toString(), Object.class, this, pageContext);
		 this.propertyName = propertyElObj.toString();
	}
}
