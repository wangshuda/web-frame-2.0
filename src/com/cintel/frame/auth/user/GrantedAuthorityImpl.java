
package com.cintel.frame.auth.user;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @file    : GrantedAuthorityImpl.java
 * @author  : WangShuDa
 * @date    : 2009-2-25
 * @corp    : CINtel
 * @version : 1.0
 */
public class GrantedAuthorityImpl implements GrantedAuthority, Serializable {
    //~ Instance fields ================================================================================================

    private static final long serialVersionUID = 1L;
   
	private String role;

	private RoleContext roleContext;
	
	private List<? extends FuncItem> funcItemList = null;
	
	//~ Constructors ================================================================================================
	public GrantedAuthorityImpl() {
	}

	@SuppressWarnings("unchecked")
	public GrantedAuthorityImpl(String role) {
		this(role, Collections.EMPTY_LIST);
	}
	
	@Deprecated
	public GrantedAuthorityImpl(String role, List<? extends FuncItem> funcItemList)  {
		this.role = role;
		//
		this.funcItemList = funcItemList;
	}
	
	@SuppressWarnings("unchecked")
	public GrantedAuthorityImpl(RoleContext roleContext) {
		this(roleContext, Collections.EMPTY_LIST);
	}
	
	public GrantedAuthorityImpl(RoleContext roleContext, List<? extends FuncItem> funcItemList)  {
		this.roleContext = roleContext;
		this.role = roleContext.getKey();
		//
		this.funcItemList = funcItemList;
	}
	//
	
	public List<? extends FuncItem> getFuncItemList() {
		return funcItemList;
	}

	public void setFuncItemList(List<? extends FuncItem> funcItemList) {
		this.funcItemList = funcItemList;
	}

	public boolean checkFuncItem(String key) {
		if(key == null) {
			return false;
		}
		FuncItem funcItem = null;
		for(int i = 0; i < funcItemList.size(); i ++) {
			funcItem = funcItemList.get(i);
			if(key.equalsIgnoreCase(funcItem.getKey())) {
				return true;
			}
		}
		return false;
	}

    //~ Methods ========================================================================================================

    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return obj.equals(this.role);
        }

        if (obj instanceof GrantedAuthority) {
            GrantedAuthority attr = (GrantedAuthority) obj;

            return this.role.equals(attr.getAuthority());
        }

        return false;
    }

    public String getAuthority() {
        return this.role;
    }

    public int hashCode() {
        return this.role.hashCode();
    }

    public String toString() {
        return this.role;
    }

	public RoleContext getRoleContext() {
		return roleContext;
	}    
}
