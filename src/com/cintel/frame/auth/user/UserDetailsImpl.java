package com.cintel.frame.auth.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * 
 * @file    : UserDetailsImpl.java
 * @version : 1.1
 * @desc    : 
 * @history : 
 *          1) 2009-07-04 wangshuda 
 *              added getDisplayRoleContext method, to return the role context for display in html page or db.
 *              added property serialVersionUID to support the interface Serializable
 *          2) 2009-09-03 wangshuda
 *             Modified the grantedKeysSet and funcItemList initial methods, only the first call will do the init.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = -2746735510528857706L;
    //
    private String userNumber;

	private String password;

	private UserContext userContext;
	
	private GrantedAuthority[] authorities;
    
    private String loginRequestIp;
    
    private String loginSessionId;
    
    private Set<String> grantedKeysSet = null;
	
    private List<FuncItem> funcItemList = null;
    
    private Map<String, Object> dynamicPropertyMap = new HashMap<String, Object>();
    
    private Set funcItemListToKeys(Set<String> target, List<? extends FuncItem> funcItemList) {
        //
        FuncItem funcItem = null;
        String funcItemKey = null;
        for(Iterator funcItemsIterator = funcItemList.iterator(); funcItemsIterator.hasNext();) {
            funcItem = (FuncItem)funcItemsIterator.next();
            //
            funcItemKey = funcItem.getKey();
            if (null == funcItemKey) {
                throw new IllegalArgumentException(
                    "Cannot process FuncItem objects which return null from getKey() - attempting to process "
                    + funcItem.toString());
            }
            //
            target.add(funcItemKey);
            List<FuncItem> subFuncItemList = funcItem.getSubItemsList();
            
            if(subFuncItemList != null && !subFuncItemList.isEmpty()) {
                funcItemListToKeys(target, subFuncItemList);
            }
        }
        return target;
    }
    
    public Set<String> getGrantedKeysSet() {
        if(grantedKeysSet == null || grantedKeysSet.isEmpty()) {
            grantedKeysSet = this.authoritiesToKeys(this.authorities);
        }
        //
        return grantedKeysSet;
    }

    private Set<String> authoritiesToKeys(GrantedAuthority[] authorities) {
        Set<String> target = new HashSet<String>();
        //
        List<FuncItem> list = this.getFuncItemList();
        
        funcItemListToKeys(target, list);
        //
        return target;
    }
    
    /**
     * Constructor function.
     */
	public UserDetailsImpl(String userNumber, String password, GrantedAuthority[] authorities) {
		this.setUserNumber(userNumber);
		this.setPassword(password);
		this.setAuthorities(authorities);
	}

	public UserDetailsImpl(String userNumber, String password, GrantedAuthority[] authorities, UserContext userContext) {
		this.setUserNumber(userNumber);
		this.setPassword(password);
		this.setAuthorities(authorities);
		//
		this.setUserContext(userContext);
	}

    /**
     * using the first authoritie in authorities to be the display context.
     */
    public RoleContext getDisplayRoleContext() {
        return authorities[0].getRoleContext();
    }
    
    /**
     * 
     * @method: setAuthorities
     * @return: void
     * @author: WangShuDa
     * @param authorities
     */
    protected void setAuthorities(GrantedAuthority[] authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority array");
        for (int i = 0; i < authorities.length; i++) {
            Assert.notNull(authorities[i],
                "Granted authority element " + i + " is null - GrantedAuthority[] cannot contain any null elements");
        }

        this.authorities = authorities;
        //
        this.grantedKeysSet = this.authoritiesToKeys(this.authorities);
    }

    @Override
    public boolean equals(Object other) {
    	if(other == null) {
    		return false;
    	}
    	//
    	if(!(other instanceof UserDetails)) {
    		return false;
    	}
    	
    	UserDetails otherUserDetails = (UserDetails)other;
    	// different user number.
    	if(!otherUserDetails.getUserNumber().equals(this.getUserNumber())) {
    		return false;
    	}
    	// different role types.
    	if(!Arrays.equals(otherUserDetails.getAuthorities(), this.getAuthorities())) {
    		return false;
    	}
    	return true;
    }
    /**
     * 
     * @method: isAnonymous
     * @author: WangShuDa
     * @return
     */
	public boolean isAnonymous() {
		if(authorities != null && authorities.length == 1 && ROLE_ANONYMOUS.equals(authorities[0].getAuthority())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @method: getFuncItemList
	 * @return: List<FuncItem>
	 * @return
	 */
	public List<FuncItem> getFuncItemList() {
        if(funcItemList == null || funcItemList.isEmpty()) {
            funcItemList = new ArrayList<FuncItem>();
            for(int i = 0; i < authorities.length; i ++) {
                funcItemList.addAll(authorities[i].getFuncItemList());
            }
        }
        //
		return funcItemList;
	}

    /**
     * 
     */
    private void generateFavoriteFuncItemList(List<FuncItem> favoriteFuncItemList, List<? extends FuncItem> funcItemList) {
        for(FuncItem funcItem:funcItemList) {
            if(funcItem.isFavorite()) {
                favoriteFuncItemList.add(funcItem);
            }
            //
            if(funcItem.isExistSubMenu()) {
                generateFavoriteFuncItemList(favoriteFuncItemList, funcItem.getSubItemsList());
            }
       }
    }
   
    public List<FuncItem> getFavoriteFuncItemList() {
        List<FuncItem> funcItemList = new LinkedList<FuncItem>();
        for(GrantedAuthority grantedAuthority:authorities) {
            generateFavoriteFuncItemList(funcItemList, grantedAuthority.getFuncItemList());
        }
        return funcItemList;
    }
	// --------------- get/set methods ---------------------
	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	
    public GrantedAuthority[] getAuthorities() {
        return authorities;
    }

    public String getLoginRequestIp() {
        return loginRequestIp;
    }

    public void setLoginRequestIp(String loginRequestIp) {
        this.loginRequestIp = loginRequestIp;
    }

    public Map<String, Object> getDynamicPropertyMap() {
        return dynamicPropertyMap;
    }

	public String getLoginSessionId() {
		return loginSessionId;
	}

	public void setLoginSessionId(String loginSessionId) {
		this.loginSessionId = loginSessionId;
	}
}
