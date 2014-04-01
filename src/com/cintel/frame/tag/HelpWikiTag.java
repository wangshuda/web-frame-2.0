package com.cintel.frame.tag;

import java.io.IOException;

import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.UrlUtils;


/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-9-5 wangshuda created
 */
public class HelpWikiTag extends TagSupport{
    private static final Log log = LogFactory.getLog(HelpWikiTag.class);
            
	private static final long serialVersionUID = 376170897904865069L;

	
	private String helpImg = "";
	
	private String helpKey = "";
	
	private String relativeHelpLink = "";
	
	private String helpLink = "";
	

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	public int doStartTag() throws JspException {
		 try {
			 StringBuffer helpWikiContext = new StringBuffer();
		
			 String i18nLocal = pageContext.getRequest().getLocale().toString();
		 
			 ResourceBundle rb = ResourceBundle.getBundle("messages.zh_CN.menu-message", pageContext.getRequest().getLocale());
			 if ("en_US".equals(i18nLocal)) {
				 rb = ResourceBundle.getBundle("messages.en_US.menu-message", pageContext.getRequest().getLocale());                  
			 }
         
			 this.helpImg = rb.getString("sys.springMenu.helpImg");
			 this.relativeHelpLink = rb.getString("sys.springMenu.relativeHelpLink");
        
			 if(!StringUtils.hasText(helpLink)) {
				 String contextPath = UrlUtils.getContextPath(pageContext);                    	
				 this.helpLink = "cinPromptWin.helpWin('" + contextPath + relativeHelpLink + helpKey + "')";
			 }
		
			 helpWikiContext.append("<img src='" + helpImg + "' style='cursor:pointer' onclick=\"" + helpLink + "\"/>");
			 
			 pageContext.getOut().print(helpWikiContext.toString());
        }
         catch (IOException ex) {
			log.error("", ex);
		}  
          
		return EVAL_BODY_INCLUDE;
	}

	public String getHelpKey() {
		return helpKey;
	}

	public void setHelpKey(String helpKey) {
		this.helpKey = helpKey;
	}

	public String getHelpLink() {
		return helpLink;
	}

	public void setHelpLink(String helpLink) {
		this.helpLink = helpLink;
	}

}
