package com.cintel.frame.auth.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cintel.frame.auth.role.func.FuncItemWithUsedInfo;
import com.cintel.frame.auth.role.func.RoleFunc;
import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;
import com.cintel.frame.webui.DomainService;
import com.cintel.frame.webui.IDomainDao;
import com.cintel.frame.webui.IDomainVo;

/**
 * 
 * @file    : RoleInfoService.java
 * @author  : WangShuDa
 * @date    : 2008-9-22
 * @corp    : CINtel
 * @version : 1.0
 */
public class RoleInfoService extends DomainService implements IRoleInfoService {

	private IDomainDao roleFuncItemsDao;
	
	public void setRoleFuncItemsDao(IDomainDao roleFuncItemsDao) {
		this.roleFuncItemsDao = roleFuncItemsDao;
	}
	
	/**
	 * 
	 * @method: get
	 * @author: WangShuDa
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IDomainVo get(IDomainVo vo) {
		RoleInfoImpl roleInfo =  (RoleInfoImpl)dao.get(vo);
		List<FuncItemWithUsedInfo> funcItemsList = roleFuncItemsDao.list("listAllFuncItems", roleInfo.getRoleId());
		//
		for(FuncItemWithUsedInfo funcItemWithUsedInfo:funcItemsList){
			Map map = new HashMap();
			for(FuncItem subFuncItem:funcItemWithUsedInfo.getSubItemsList()){
				map.put("roleId", roleInfo.getRoleId());
				map.put("parentId", subFuncItem.getId());
				List<FuncItem> subfuncItemsList = roleFuncItemsDao.list("getSubItemsList-WithUsedInfo",map);
				subFuncItem.setSubItemsList(subfuncItemsList);
			}
		}
		roleInfo.setFuncItemList(funcItemsList);
		return roleInfo;
	}

	@Override
	public int delete(IDomainVo vo) {
		roleFuncItemsDao.delete("deleteByRoleId", vo.getOid());
		return dao.delete(vo);
		
	}
	/**
	 * 
	 * @method: insertRole
	 * @author: WangShuDa
	 * @param domainVo
	 * @return
	 */
	public void insertRole(IDomainVo domainVo) {
		RoleInfoImpl roleInfo = (RoleInfoImpl)domainVo;
		
		Integer roleNameCount = (Integer)dao.get("checkRoleCount", roleInfo);
		if(roleNameCount != null && roleNameCount > 0) {
			throw new ErrorReportException(new ErrorInfo("error.roleNameAlreadyExist"));
		}
		dao.insert(roleInfo);
	}


	/**
	 * 
	 * @method: updateRoleFuncItems
	 * @author: WangShuDa
	 * @param roleId
	 * @param chkFuncIdArr
	 * @return
	 */
	public void updateRoleFuncItems(String roleId, String[] chkFuncIdArr) {
		roleFuncItemsDao.delete("deleteByRoleId", roleId);
		
		if(chkFuncIdArr != null && chkFuncIdArr.length > 0) {
			RoleFunc roleFunc = new RoleFunc();
			roleFunc.setRoleId(roleId);
			
			for(int i = chkFuncIdArr.length; i > 0; i --) {
				roleFunc.setFuncId(chkFuncIdArr[i - 1]);
				roleFuncItemsDao.insert(roleFunc);
			}
		}
	}

}
