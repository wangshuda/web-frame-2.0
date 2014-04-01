
package com.cintel.frame.auth.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import com.cintel.frame.auth.context.SecurityContext;
import com.cintel.frame.auth.user.UserDetails;


public class AuthorizeFuncTag extends TagSupport {
	private static final long serialVersionUID = -2306184371414745881L;
	//
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(AuthorizeFuncTag.class);
    //~ Instance fields ================================================================================================
	
	private String ifAllGranted = "";
    private String ifAnyGranted = "";
    private String ifNotGranted = "";

    private String var;
    
    //~ Methods ========================================================================================================
    private int reportTagRtn(int tagRtn) {
        if(!StringUtils.hasLength(var)) {
            return tagRtn;
        }
        else {
            boolean grantedChkRtn = (tagRtn != Tag.SKIP_BODY);
            pageContext.setAttribute(var, grantedChkRtn, PageContext.REQUEST_SCOPE);
            //
            return Tag.SKIP_BODY;
        }
    }
    
    @SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
    	if(!StringUtils.hasText(ifAllGranted) && !StringUtils.hasText(ifAnyGranted) && !StringUtils.hasText(ifNotGranted)) {
    		return this.reportTagRtn(Tag.SKIP_BODY);
    	}
    	//
        final Collection grantedKeys = this.getGrandedKeys(pageContext);
        //
        final String evaledIfAnyGranted = ExpressionEvaluationUtils.evaluateString("ifAnyGranted", ifAnyGranted, pageContext);

        if ((null != evaledIfAnyGranted) && !"".equals(evaledIfAnyGranted)) {
            Set grantedCopy = retainAll(grantedKeys, parseKeysStr2Set(evaledIfAnyGranted));

            if (grantedCopy.isEmpty()) {
                return this.reportTagRtn(Tag.SKIP_BODY);
            }
        }
        //
        final String evaledIfNotGranted = ExpressionEvaluationUtils.evaluateString("ifNotGranted", ifNotGranted, pageContext);
        if ((null != evaledIfNotGranted) && !"".equals(evaledIfNotGranted)) {
            Set grantedCopy = retainAll(grantedKeys, parseKeysStr2Set(evaledIfNotGranted));

            if (!grantedCopy.isEmpty()) {
                return this.reportTagRtn(Tag.SKIP_BODY);
            }
        }
        //
        final String evaledIfAllGranted = ExpressionEvaluationUtils.evaluateString("ifAllGranted", ifAllGranted, pageContext);

        if ((null != evaledIfAllGranted) && !"".equals(evaledIfAllGranted)) {
            if (!grantedKeys.containsAll(parseKeysStr2Set(evaledIfAllGranted))) {
                return this.reportTagRtn(Tag.SKIP_BODY);
            }
        }
        //
        return this.reportTagRtn(Tag.EVAL_BODY_INCLUDE);
    }    
    
    private Collection getGrandedKeys(PageContext pageContext) {
    	UserDetails userDetails = null;
		HttpSession session = pageContext.getSession();
		userDetails = (UserDetails)session.getAttribute(SecurityContext.SECURITY_CONTEXT_KEY);
        if (null == userDetails) {
            return Collections.EMPTY_LIST;
        }

        if ((null == userDetails.getAuthorities()) || (userDetails.getAuthorities().length < 1)) {
            return Collections.EMPTY_LIST;
        }
        
        Collection granted = userDetails.getGrantedKeysSet();
        return granted;
    }

    @SuppressWarnings("unchecked")
	private Set parseKeysStr2Set(String keysString) {
        final Set requiredKeys = new HashSet();
        final String[] keys = StringUtils.commaDelimitedListToStringArray(keysString);

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i].trim();

            key = StringUtils.replace(key, "\t", "");
            key = StringUtils.replace(key, "\r", "");
            key = StringUtils.replace(key, "\n", "");
            key = StringUtils.replace(key, "\f", "");

            requiredKeys.add(key);
        }

        return requiredKeys;
    }


    @SuppressWarnings("unchecked")
	private Set retainAll(final Collection granted, final Set requiredKeys) {
        Set target = new HashSet();
        //
        for (Iterator iterator = requiredKeys.iterator(); iterator.hasNext();) {
            String requiredKey = (String) iterator.next();
            //
            for (Iterator grantedKeysIterator = granted.iterator(); grantedKeysIterator.hasNext();) {
                String grantedKey = (String) grantedKeysIterator.next();
                //
                if (grantedKey.equals(requiredKey)) {
                    target.add(grantedKey);
                    break;
                }
            }
        }

        return target;
    }

    public String getIfAllGranted() {
        return ifAllGranted;
    }

    public String getIfAnyGranted() {
        return ifAnyGranted;
    }

    public String getIfNotGranted() {
        return ifNotGranted;
    }

    public void setIfAllGranted(String ifAllGranted) throws JspException {
        this.ifAllGranted = ifAllGranted;
    }

    public void setIfAnyGranted(String ifAnyGranted) throws JspException {
        this.ifAnyGranted = ifAnyGranted;
    }

    public void setIfNotGranted(String ifNotGranted) throws JspException {
        this.ifNotGranted = ifNotGranted;
    }
    
    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
