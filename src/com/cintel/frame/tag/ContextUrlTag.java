package com.cintel.frame.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.UrlUtils;


/**
 * 
 * @version      1.0
 * @author       WangShuDa
 * @date         2008-07-24
 * @description  ContextUrlTag.
 */
public class ContextUrlTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(ContextUrlTag.class);
	
	protected String var;

	public int doEndTag() throws JspException {
		try {
			String contextUrl = UrlUtils.getContextUrl(pageContext);
			//
			if(StringUtils.hasText(var)) {
				pageContext.setAttribute(var, contextUrl, PageContext.REQUEST_SCOPE);
			}
			else {
				pageContext.getOut().print(contextUrl);
			}
    		
    	}
    	catch(Exception ex) {
    		log.error("ContextUrlTag Error!", ex);
    		
    		return SKIP_BODY;
    	}
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
    	return SKIP_BODY;
    }

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}