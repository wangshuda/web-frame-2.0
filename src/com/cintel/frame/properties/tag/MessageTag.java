package com.cintel.frame.properties.tag;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.standard.tag.common.core.Util;
import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;

import com.cintel.frame.properties.MessageLoader;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.WebUtils;

/**
 * 
 * @file    : MessageTag.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-7-4 wangshuda created
 */
public class MessageTag extends org.apache.taglibs.standard.tag.rt.fmt.MessageTag {
    private static final long serialVersionUID = -5911777845430386588L;
    
    private static Log log = LogFactory.getLog(MessageTag.class);
    //
    public static final String _LOAD_FROM_DB = "database";
    
    public static final String _LOAD_FROM_PRO = "properties";
    
    public static final String _LOAD_FROM_BUNDLE = "bundle";
    
    private MessageLoader dbMessageLoader = null;
    
    private MessageLoader proMessageLoader = null;
    
    /**
     * enable value: bundle, properties, database. bundle is default.
     */
    private String loaderMode = _LOAD_FROM_BUNDLE;
   
    private String defaultValue = "";
    
    //
    private List<Object> params = new ArrayList<Object>();
    private String var;
    private int scope = PageContext.REQUEST_SCOPE;;
    
    
    @Override
    public void addParam(Object arg) {
    	if(log.isDebugEnabled()) {
    		log.debug("arg = " + arg);
    	}
    	//
        params.add(arg);
    }
    
    private String getKey() {
        String key = null;
        if(keySpecified) {
            key = keyAttrValue;
        }
        else if(bodyContent != null && bodyContent.getString() != null) {
            key = bodyContent.getString().trim();
        }
        //
        return key;
    }
    
    private String formatMessage(String message) {
    	String formatedMsg = message;
        if(!params.isEmpty()) {
            formatedMsg = MessageFormat.format(message, params.toArray());
        }
        //
        if(log.isDebugEnabled()) {
        	log.debug(formatedMsg);
        }
        //
        return formatedMsg.toString();
    }
    
    private static void setResponseLocale(PageContext pc, Locale locale) {
        ServletResponse response = pc.getResponse();
        response.setLocale(locale);
        if(pc.getSession() != null)
            try {
                pc.setAttribute("javax.servlet.jsp.jstl.fmt.request.charset", response.getCharacterEncoding(), 3);
            }
            catch(IllegalStateException ex) { }
    }
    
    private String loadBundleMessage(String key) {
        String prefix = null;
        LocalizationContext locCtxt = null;
        //
        if(!bundleSpecified) {
            javax.servlet.jsp.tagext.Tag t = findAncestorWithClass(this, org.apache.taglibs.standard.tag.common.fmt.BundleSupport.class);
            if(t != null) {
                BundleSupport parent = (BundleSupport)t;
                locCtxt = parent.getLocalizationContext();
                prefix = parent.getPrefix();
            }
            else {
                locCtxt = BundleSupport.getLocalizationContext(pageContext);
            }
        }
        else {
            locCtxt = bundleAttrValue;
            if(locCtxt.getLocale() != null) {
                setResponseLocale(pageContext, locCtxt.getLocale());
            }
        }
        //
        String message = null;
        //
        if(locCtxt != null) {
            ResourceBundle bundle = locCtxt.getResourceBundle();
            if(bundle != null) {
                if(prefix != null) {
                    key = prefix + key;
                }
                //
                try {
                    message = bundle.getString(key);
                }
                catch(MissingResourceException ex) {
                    log.warn("Can not find the message in the bundle with the key:" + key);
                }
            }
        }
        return message;
    }
    
    private void outputMessage(String message) throws JspTagException {
        if(var != null) {
            pageContext.setAttribute(var, message, scope);
        }
        else {
            try {
                pageContext.getOut().print(message);
            }
            catch(IOException ex) {
                throw new JspTagException(ex.toString(), ex);
            }
        }
    }
    
    @Override
    public int doStartTag() throws JspException {
    	this.params.clear();
    	//
    	return super.doStartTag();
    }
    
    @Override
    public int doEndTag() throws JspException {
        String key = getKey();
        String message = null;
        //
        if(_LOAD_FROM_DB.equalsIgnoreCase(loaderMode) || !StringUtils.hasText(message)) {
            if(dbMessageLoader == null) {
                dbMessageLoader = (MessageLoader)WebUtils.getSpringBean(pageContext.getSession(), "DbMessageLoader");
            }
            //
            if(dbMessageLoader == null) {
                log.warn("Can not find the MessageLoader named DbMessageLoader in spring declaration!");
            }
            else {
                message = dbMessageLoader.getMessage(key);
                //
                log.info("dbMessageLoader load " + key + "=" + message);
            }
        }
        //
        if(_LOAD_FROM_PRO.equalsIgnoreCase(loaderMode) || !StringUtils.hasText(message)) {
            if(proMessageLoader == null) {
                proMessageLoader = (MessageLoader)WebUtils.getSpringBean(pageContext.getSession(), "ProMessageLoader");
            }
            //
            if(proMessageLoader == null) {
                log.warn("Can not find the MessageLoader named ProMessageLoader in spring declaration!");
            }
            else {
                message = proMessageLoader.getMessage(key);
                //
                log.info("proMessageLoader load " + key + "=" + message);
            }
        }
        
        //
        if(_LOAD_FROM_BUNDLE.equalsIgnoreCase(loaderMode) || !StringUtils.hasText(message)) {
            message = this.loadBundleMessage(key);
            //
            log.info("loadBundleMessage load " + key + "=" + message);
        }
        //
        if(!StringUtils.hasText(message)) {
            message = defaultValue;
            //throw new JspException("Enable attribute value for loaderMode:bundle, properties, database");
        }
        //
        String formatedMessage = this.formatMessage(message);
        this.outputMessage(formatedMessage);
        //
        return EVAL_PAGE;
    }


    public String getLoaderMode() {
        return loaderMode;
    }


    public void setLoaderMode(String loaderMode) {
        this.loaderMode = loaderMode;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void setScope(String scope){
        this.scope = Util.getScope(scope);
    }

    @Override
    public void setVar(String var) {
        this.var = var;
    }
    
    // ---------------------- get/set methods ----------------------

}
