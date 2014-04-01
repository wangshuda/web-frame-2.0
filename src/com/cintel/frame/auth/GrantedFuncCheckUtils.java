package com.cintel.frame.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.cintel.frame.auth.user.UserDetails;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-9-14 wangshuda created
 */
public class GrantedFuncCheckUtils {
    
    @SuppressWarnings("unchecked")
    public static boolean grantedCheck(String grantedType, String grantedStr4Check, UserDetails userDetails) {
        //
        boolean rtnValue = false;
        
        final Collection grantedKeySet = loadGrantedKeySet(userDetails);
        
        if(GrantedCheckTypeEnum.ifAnyGranted.sameTo(grantedType))  {
            Set grantedCopy = retainAll(grantedKeySet, parseKeysStr2Set(grantedStr4Check));
            rtnValue = !grantedCopy.isEmpty();
        }
        else if(GrantedCheckTypeEnum.ifAllGranted.sameTo(grantedType))  {
            rtnValue = grantedKeySet.containsAll(parseKeysStr2Set(grantedStr4Check));
        }
        else if(GrantedCheckTypeEnum.ifNotGranted.sameTo(grantedType)) {
            Set grantedCopy = retainAll(grantedKeySet, parseKeysStr2Set(grantedStr4Check));
            
            rtnValue = grantedCopy.isEmpty();
        }
        return rtnValue;
    }  
    
    private static Collection loadGrantedKeySet(UserDetails userDetails) {
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
    private static Set parseKeysStr2Set(String keysString) {
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
    private static Set retainAll(final Collection granted, final Set requiredKeys) {
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

}
