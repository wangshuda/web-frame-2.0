
package com.cintel.frame.auth.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import org.springframework.web.util.ExpressionEvaluationUtils;

import com.cintel.frame.auth.context.SecurityContext;

import com.cintel.frame.auth.user.GrantedAuthority;
import com.cintel.frame.auth.user.GrantedAuthorityImpl;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.util.MyAntPathMatcher;

import java.util.Arrays;
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


public class AuthorizeRoleTag extends TagSupport {
	private static final long serialVersionUID = -2306184371414745881L;
	//
	@SuppressWarnings("unused")
	private final static Log log = LogFactory.getLog(AuthorizeRoleTag.class);
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

        final Collection granted = this.loadPrincipalAuthorities(pageContext);
        //
        final String evaledIfAnyGranted = ExpressionEvaluationUtils.evaluateString("ifAnyGranted", ifAnyGranted, pageContext);

        if ((null != evaledIfAnyGranted) && !"".equals(evaledIfAnyGranted)) {
            Set grantedCopy = this.retainAll(granted, parseAuthoritiesString(evaledIfAnyGranted));
            //
            if (grantedCopy.isEmpty()) {
                return this.reportTagRtn(Tag.SKIP_BODY);
            }
        }
        
        final String evaledIfNotGranted = ExpressionEvaluationUtils.evaluateString("ifNotGranted", ifNotGranted, pageContext);

        if ((null != evaledIfNotGranted) && !"".equals(evaledIfNotGranted)) {
            Set grantedCopy = this.retainAll(granted, parseAuthoritiesString(evaledIfNotGranted));

            if (!grantedCopy.isEmpty()) {
                return this.reportTagRtn(Tag.SKIP_BODY);
            }
        }

        final String evaledIfAllGranted = ExpressionEvaluationUtils.evaluateString("ifAllGranted", ifAllGranted, pageContext);

        if ((null != evaledIfAllGranted) && !"".equals(evaledIfAllGranted)) {
            if (!granted.containsAll(parseAuthoritiesString(evaledIfAllGranted))) {
                return this.reportTagRtn(Tag.SKIP_BODY);
            }
        }



        return this.reportTagRtn(Tag.EVAL_BODY_INCLUDE);
    }

    @SuppressWarnings("unchecked")
    private Set authoritiesToRoles(Collection c) {
        Set target = new HashSet();

        for (Iterator iterator = c.iterator(); iterator.hasNext();) {
            GrantedAuthority authority = (GrantedAuthority) iterator.next();

            if (null == authority.getAuthority()) {
                throw new IllegalArgumentException(
                    "Cannot process GrantedAuthority objects which return null from getAuthority() - attempting to process "
                    + authority.toString());
            }
            //
            target.add(authority.getAuthority());
        }

        return target;
    }
    
    private Collection loadPrincipalAuthorities(PageContext pageContext) {
    	UserDetails userDetails = null;
		HttpSession session = pageContext.getSession();
		userDetails = (UserDetails)session.getAttribute(SecurityContext.SECURITY_CONTEXT_KEY);
        if (null == userDetails) {
            return Collections.EMPTY_LIST;
        }

        if ((null == userDetails.getAuthorities()) || (userDetails.getAuthorities().length < 1)) {
            return Collections.EMPTY_LIST;
        }
        //
        Collection granted = Arrays.asList(userDetails.getAuthorities());
        return granted;
    }

    @SuppressWarnings("unchecked")
	private Set parseAuthoritiesString(String authorizationsString) {
        final Set requiredAuthorities = new HashSet();
        final String[] authorities = StringUtils.commaDelimitedListToStringArray(authorizationsString);

        for (int i = 0; i < authorities.length; i++) {
            String authority = authorities[i];
            //log.debug(authority);
            // Remove the role's whitespace characters without depending on JDK 1.4+
            // Includes space, tab, new line, carriage return and form feed.
            String role = authority.trim(); // trim, don't use spaces, as per SEC-378
            role = StringUtils.replace(role, "\t", "");
            role = StringUtils.replace(role, "\r", "");
            role = StringUtils.replace(role, "\n", "");
            role = StringUtils.replace(role, "\f", "");

            requiredAuthorities.add(new GrantedAuthorityImpl(role));
        }
       
        return requiredAuthorities;
    }
    
    private boolean roleIsMatch(String theRole, String otherRole) {
        boolean isMatchRole = false;
        if(StringUtils.hasLength(theRole) && StringUtils.hasLength(otherRole)) {
            isMatchRole = theRole.equalsIgnoreCase(otherRole);
            //
            if(!isMatchRole) {
                MyAntPathMatcher pathMatcher = new MyAntPathMatcher();
                
                isMatchRole = pathMatcher.matchIgnoreCase(theRole, otherRole) || pathMatcher.matchIgnoreCase(otherRole, theRole);
            }
        }
        //
        return isMatchRole;
    }
    
    /**
     * support match with "**"
     * 
     * @param origSet
     * @param otherSet
     * @return
     */
    private Set retainAllMatch(final Set origSet, final Set otherSet) {
        Set<String> newSet = new HashSet<String> ();        
        boolean findMatch = false;
        for (Iterator iterator = origSet.iterator(); iterator.hasNext();) {
            String origRole = (String)iterator.next();
            //
            findMatch = false;
            for (Iterator grantedIterator = otherSet.iterator(); grantedIterator.hasNext();) {
                String authorityRole = (String)grantedIterator.next();
                //
                findMatch = this.roleIsMatch(origRole, authorityRole);
                //
                if(findMatch) {
                    newSet.add(origRole);
                }
            }
        }

        return newSet;
    }
        
    @SuppressWarnings("unchecked")
	private Set retainAll(final Collection grantedRoleSet, final Set requiredRoleSet) {
        //
        Set orgiGrantedRoles = this.authoritiesToRoles(grantedRoleSet);
        Set requiredRoles = this.authoritiesToRoles(requiredRoleSet);
        //
        Set grantedRoles = this.retainAllMatch(orgiGrantedRoles, requiredRoles);
        //
        //grantedRoles.retainAll(requiredRoles);

        //
        return this.rolesToAuthorities(grantedRoles, grantedRoleSet);
    }

    @SuppressWarnings("unchecked")
	private Set rolesToAuthorities(Set grantedRoles, Collection granted) {
        boolean authMatch = false;
        Set target = new HashSet();
        //        
        for (Iterator iterator = grantedRoles.iterator(); iterator.hasNext();) {
            String role = (String)iterator.next();
 
            for (Iterator grantedIterator = granted.iterator(); grantedIterator.hasNext();) {
                GrantedAuthority authority = (GrantedAuthority) grantedIterator.next();
                //
                
                authMatch = this.roleIsMatch(role, authority.getAuthority());
                //if (authority.getAuthority().equals(role)) {
                if(authMatch) {
                    target.add(authority);
                    break;
                }
            }
        }
        //
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
