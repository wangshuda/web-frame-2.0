package com.cintel.frame.auth.user;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @File    : UserDetails.java
 * @Author  : WangShuDa
 * @Date    : 2008-9-2
 * @Corp    : CINtel
 * @Version : 1.0
 **/
public interface UserDetails extends Serializable {
	String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	
    public Map<String, Object> getDynamicPropertyMap();
    
    //
	public String getUserNumber();

	public String getPassword();
	
	public UserContext getUserContext();
	
    public RoleContext getDisplayRoleContext();
    
	public void setUserContext(UserContext userContext);
	
	public void setPassword(String password);
	
	public GrantedAuthority[] getAuthorities();
    
    public List<FuncItem> getFuncItemList();
    
    public List<FuncItem> getFavoriteFuncItemList();
    
    public Set<String> getGrantedKeysSet();
    
	public boolean isAnonymous();
	
    public String getLoginRequestIp();
    
    public void setLoginRequestIp(String loginRequestIp);
    //
    public String getLoginSessionId();
    
    public void setLoginSessionId(String loginSessionId);
}
