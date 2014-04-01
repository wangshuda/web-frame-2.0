package com.cintel.frame.auth;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.context.SecurityContext;
import com.cintel.frame.auth.context.SecurityContextHolder;
import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.auth.user.UserContext;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.web.app.admin.WebAdmin;

/**
 * 
 * @file    : UserInfoUtils.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-9-17 wangshuda modified: added userCtxClassIs methods.
 */
public class UserInfoUtils {
	private static Log log = LogFactory.getLog(UserInfoUtils.class);
	
	public static UserDetails getUserDetails() {
		SecurityContext context = SecurityContextHolder.getContext();
		Object object = context.getObject();
		if(object instanceof UserDetails) {
			UserDetails userDetails = (UserDetails)object;
			return userDetails;
		}
		return null;
	}
	
	public static boolean isAdmin() {
        return userCtxClassIs(WebAdmin.class);
	}
	
    public static boolean userCtxClassIs(String targetClassName) {
        UserDetails userDetails = getUserDetails();
        boolean rtnValue = false;
        if(userDetails != null) {
            UserContext userContext = userDetails.getUserContext();
            if(userContext != null) {
                rtnValue = userContext.getClass().getName().equals(targetClassName);
            }
        }
        
        return rtnValue;
    }
    
    public static boolean userCtxClassIs(Class targetClass) {
        return userCtxClassIs(targetClass.getName());
    }
    
	/**
	 * 
	 * @method: getStringPropertyValue
	 * @return: String
	 * @author: WangShuDa
	 * @param propertyName
	 */
	protected static String getStringPropertyValue(String propertyName) {
		return (String)getPropertyValue(propertyName);
	}
	
	/**
	 * 
	 * @method: getPropertyValue
	 * @return: Object
	 * @author: WangShuDa
	 * @param propertyName
	 */
	public static Object getPropertyValue(String propertyName) {
		Object propertyValue = null;
		UserDetails userDetails = getUserDetails();
		if(userDetails != null) {
			try {
				propertyValue = PropertyUtils.getProperty(userDetails, propertyName);
			}
			catch(Exception ex) {
				log.error("getPropertyValue error !", ex);
				propertyValue = null;
			}
		}
		//
		return propertyValue;
	}

	protected static void setPropertyValue(String propertyName, Object propertyValue) {
		UserDetails userDetails = getUserDetails();
		if(userDetails != null) {
			try {
				BeanUtils.setProperty(userDetails, propertyName, propertyValue);
			}
			catch(Exception ex) {
				log.error("setPropertyValue error !", ex);
				propertyValue = null;
			}
		}
	}
	
    public static Object getDynamicProperty(String key) {
        UserDetails userDetails = getUserDetails();
        Object retObj = null;
        if(userDetails != null) {
            retObj = userDetails.getDynamicPropertyMap().get(key);
        }
        else {
            log.warn("userDetails is null!");
        }
        return retObj;
    }

    public static void putDynamicProperty(String key, Object value) {
        UserDetails userDetails = getUserDetails();
        if(userDetails != null) {
            userDetails.getDynamicPropertyMap().put(key, value);
        }
        else {
            log.warn("userDetails is null!");
        }
    }
    
	public static String getUserNumber() {
		String userNumber = null;
		UserDetails userDetails = getUserDetails();
		if(userDetails != null) {
			userNumber = userDetails.getUserNumber();
		}
		//
		return userNumber;
	}
	
	public static String getUserPassword() {
		String userPassword = null;
		UserDetails userDetails = getUserDetails();
		if(userDetails != null) {
			userPassword = userDetails.getPassword();
		}
		//
		return userPassword;
	}
	
	public static UserContext getUserContext() {
		UserContext userContext = null;
		UserDetails userDetails = getUserDetails();
		if(userDetails != null) {
			userContext = userDetails.getUserContext();
		}
		//
		return userContext;
	}
	
	public static void setUserPassword(String password) {
		UserDetails userDetails = getUserDetails();
		if(userDetails != null) {
			userDetails.setPassword(password);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<FuncItem> getFuncItemsList() {
		UserDetails userDetails = getUserDetails();
		if(userDetails != null) {
			return userDetails.getFuncItemList();
		}
		return Collections.EMPTY_LIST;
	}
}
