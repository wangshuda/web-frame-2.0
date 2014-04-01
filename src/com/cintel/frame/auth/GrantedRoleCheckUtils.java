package com.cintel.frame.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.cintel.frame.auth.user.GrantedAuthority;
import com.cintel.frame.auth.user.GrantedAuthorityImpl;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.util.MyAntPathMatcher;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-9-14 wangshuda created
 */
public class GrantedRoleCheckUtils {
    @SuppressWarnings("unchecked")
    private static Set authoritiesToRoles(Collection c) {
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
    
    private static Collection loadPrincipalAuthorities(UserDetails userDetails) {
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
    private static Set parseAuthoritiesString(String authorizationsString) {
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
    
    private static boolean roleIsMatch(String theRole, String otherRole) {
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
    private static Set retainAllMatch(final Set origSet, final Set otherSet) {
        Set<String> newSet = new HashSet<String> ();        
        boolean findMatch = false;
        for (Iterator iterator = origSet.iterator(); iterator.hasNext();) {
            String origRole = (String)iterator.next();
            //
            findMatch = false;
            for (Iterator grantedIterator = otherSet.iterator(); grantedIterator.hasNext();) {
                String authorityRole = (String)grantedIterator.next();
                //
                findMatch = roleIsMatch(origRole, authorityRole);
                //
                if(findMatch) {
                    newSet.add(origRole);
                }
            }
        }

        return newSet;
    }
        
    @SuppressWarnings("unchecked")
    private static Set retainAll(final Collection grantedRoleSet, final Set requiredRoleSet) {
        //
        Set orgiGrantedRoles = authoritiesToRoles(grantedRoleSet);
        Set requiredRoles = authoritiesToRoles(requiredRoleSet);
        //
        Set grantedRoles = retainAllMatch(orgiGrantedRoles, requiredRoles);
        //
        //grantedRoles.retainAll(requiredRoles);

        //
        return rolesToAuthorities(grantedRoles, grantedRoleSet);
    }

    @SuppressWarnings("unchecked")
    private static Set rolesToAuthorities(Set grantedRoles, Collection granted) {
        boolean authMatch = false;
        Set target = new HashSet();
        //        
        for (Iterator iterator = grantedRoles.iterator(); iterator.hasNext();) {
            String role = (String)iterator.next();
 
            for (Iterator grantedIterator = granted.iterator(); grantedIterator.hasNext();) {
                GrantedAuthority authority = (GrantedAuthority) grantedIterator.next();
                //
                
                authMatch = roleIsMatch(role, authority.getAuthority());
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
    
    
    @SuppressWarnings("unchecked")
    public static boolean grantedCheck(String grantedType, String grantedStr4Check, UserDetails userDetails) {
        boolean rtnValue = false;
        final Collection granted = loadPrincipalAuthorities(userDetails);
        
        if(GrantedCheckTypeEnum.ifAnyGranted.sameTo(grantedType))  {
            Set grantedCopy = retainAll(granted, parseAuthoritiesString(grantedStr4Check));
            
            rtnValue = !grantedCopy.isEmpty();
        }
        else if(GrantedCheckTypeEnum.ifAllGranted.sameTo(grantedType))  {
            rtnValue = granted.containsAll(parseAuthoritiesString(grantedStr4Check));
        }
        else if(GrantedCheckTypeEnum.ifNotGranted.sameTo(grantedType)) {
            Set grantedCopy = retainAll(granted, parseAuthoritiesString(grantedStr4Check));
            
            rtnValue = grantedCopy.isEmpty();
        }
        //
        return rtnValue;
    }
}
