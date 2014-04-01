/**
 * 
 */
package com.cintel.frame.auth.role.func;

import com.cintel.frame.auth.func.FuncItemImpl;

/**
 * @file    : FuncItemWithUsedInfo.java
 * @author  : WangShuDa
 * @date    : 2008-9-23
 * @corp    : CINtel
 * @version : 1.0
 */
public class FuncItemWithUsedInfo extends FuncItemImpl {
    private static final long serialVersionUID = 3717882756612514925L;
    
    private int roleUsedFlag;

	public int getRoleUsedFlag() {
		return roleUsedFlag;
	}

	public void setRoleUsedFlag(int roleUsedFlag) {
		this.roleUsedFlag = roleUsedFlag;
	}
}
