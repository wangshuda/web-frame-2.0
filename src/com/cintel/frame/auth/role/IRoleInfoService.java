package com.cintel.frame.auth.role;

import com.cintel.frame.webui.IDomainService;
import com.cintel.frame.webui.IDomainVo;

public interface IRoleInfoService extends IDomainService {

	public void insertRole(IDomainVo domainVo);
	
	public void updateRoleFuncItems(String roleId, String[] roleFuncItems);
}
