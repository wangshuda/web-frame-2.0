package com.cintel.frame.auth.menu.init;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.func.pattern.FuncAuthUrlPattern;
import com.cintel.frame.auth.menu.FuncItemForSpring;
import com.cintel.frame.auth.menu.MenuFromSpringService;
import com.cintel.frame.auth.role.func.RoleFunc;
import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.webui.IDomainDao;

public class MenuInitService implements IMenuInitService {
	@SuppressWarnings("unused")
	protected Log log = LogFactory.getLog(this.getClass());
	//
	private MenuFromSpringService menuFromSpringService;
	
	private IDomainDao funcAuthUrlPatternDao;

	private IDomainDao funcItemDao;
	
	private IDomainDao roleFuncInfoDao;

	public void setFuncAuthUrlPatternDao(IDomainDao funcAuthUrlPatternDao) {
		this.funcAuthUrlPatternDao = funcAuthUrlPatternDao;
	}
	
	public void setFuncItemDao(IDomainDao funcItemDao) {
		this.funcItemDao = funcItemDao;
	}

	public void setRoleFuncInfoDao(IDomainDao roleFuncInfoDao) {
		this.roleFuncInfoDao = roleFuncInfoDao;
	}

	public void setMenuFromSpringService(MenuFromSpringService menuFromSpringService) {
		this.menuFromSpringService = menuFromSpringService;
	}
	
	/**
	 * 
	 * @method: insertAllFuncItems
	 * @author: WangShuDa
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void insertAllFuncItems() throws Exception {
		List<FuncItem> itsMenuList = menuFromSpringService.getAllFuncItemsList();
		
		funcItemDao.delete("deleteAll", null);
		funcAuthUrlPatternDao.delete("deleteAll", null);
		
		for(FuncItem funcItem:itsMenuList) {
			funcItemDao.insert(funcItem);
			
			List<String> authUrlPatternList = funcItem.getAuthUrlPatternList();
			if(authUrlPatternList.size() > 0) {
				for(String authUrlParrern : authUrlPatternList) {
					FuncAuthUrlPattern funcAuthUrlPattern = new FuncAuthUrlPattern();
					funcAuthUrlPattern.setAuthUrlPattern(authUrlParrern);
					funcAuthUrlPattern.setFuncId(funcItem.getId());
					funcAuthUrlPatternDao.insert(funcAuthUrlPattern);
				}
			}
			//
		}
	}

	/**
	 * initial the functions for the role type declared in spring.
	 */
	@SuppressWarnings("unchecked")
	public void insertRoleFuncItems(String roleTypeKey) throws Exception {

		List<FuncItemForSpring> itsMenuList = menuFromSpringService.getAllFuncItemsList();

		RoleFunc roleFunc = new RoleFunc();
		roleFunc.setRoleId(roleTypeKey);
		//
		roleFuncInfoDao.delete("deleteByRoleId", roleTypeKey);
		
		for(FuncItemForSpring funcItem:itsMenuList) {
			if(funcItem.doAuthenticate(roleTypeKey)) {
				roleFunc.setFuncId(String.valueOf(funcItem.getId()));
				roleFuncInfoDao.insert(roleFunc);
			}
		}
	}
}
