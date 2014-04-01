package com.cintel.frame.auth.role.init;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.cintel.frame.auth.role.RoleFromSpringService;
import com.cintel.frame.auth.role.RoleInfoImpl;
import com.cintel.frame.auth.role.type.RoleType;
import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.webui.IDomainDao;

public class RoleInitService implements IRoleInitService {
	@SuppressWarnings("unused")
	protected Log log = LogFactory.getLog(this.getClass());
	//
	private RoleFromSpringService roleFromSpringService;
	
	private IDomainDao roleInfoDao;
	
	private IDomainDao roleTypeInfoDao;
	
	@Transactional
	public void insertAllRoleItems(){
		List<RoleContext> itsRoleList = roleFromSpringService.getAllRoleItemsList();
		
		roleTypeInfoDao.delete("deleteAll", null);
		RoleType roleType = new RoleType();
		for(RoleContext roleCtx:itsRoleList) {
			roleType.setRoleKey(roleCtx.getKey());
			roleType.setRoleTitle(roleCtx.getTitle());
			roleType.setLoginUrl(roleCtx.getLoginUrl());
			roleType.setLogoutUrl(roleCtx.getLogoutUrl());
			roleType.setWelcomeUrl(roleCtx.getWelcomeUrl());
			//
			roleType.setRoleLevel(roleCtx.getRoleLevel());
			roleType.setRoleGroup(roleCtx.getRoleGroup());
			//
			roleType.setDescription(roleCtx.getDescription());
			roleTypeInfoDao.insert(roleType);
		}
		//
		roleInfoDao.delete("deleteAll", null);
		RoleInfoImpl roleInfo = new RoleInfoImpl();
		for(RoleContext roleCtx:itsRoleList) {
			roleInfo.setRoleId(roleCtx.getKey());
			roleInfo.setRoleTypeKey(roleCtx.getKey());
			roleInfo.setRoleTitle(roleCtx.getTitle());
			roleInfo.setDescription(roleCtx.getDescription());
			roleInfoDao.insert(roleInfo);
		}
		//
	}

	public IDomainDao getRoleTypeInfoDao() {
		return roleTypeInfoDao;
	}

	public void setRoleTypeInfoDao(IDomainDao roleTypeInfoDao) {
		this.roleTypeInfoDao = roleTypeInfoDao;
	}
	public IDomainDao getRoleInfoDao() {
		return roleInfoDao;
	} 
	
	public void setRoleInfoDao(IDomainDao roleInfoDao) {
		this.roleInfoDao = roleInfoDao;
	}

	public RoleFromSpringService getRoleFromSpringService() {
		return roleFromSpringService;
	}

	public void setRoleFromSpringService(RoleFromSpringService roleFromSpringService) {
		this.roleFromSpringService = roleFromSpringService;
	}

}
