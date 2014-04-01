package com.cintel.frame.auth.user;

import java.io.Serializable;
import java.util.List;

public interface GrantedAuthority extends Serializable {
	public String getAuthority();
    
	public RoleContext getRoleContext();
	
	public boolean checkFuncItem(String key);
	public List<? extends FuncItem> getFuncItemList();
	public void setFuncItemList(List<? extends FuncItem> funcItemList);
}
