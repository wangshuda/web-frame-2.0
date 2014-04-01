package com.cintel.frame.auth.role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.web.action.BaseDispatchAction;
import com.cintel.frame.web.action.JsonOption;

/**
 * @version : $Id: RoleInfoAction.java 15770 2010-01-14 06:24:41Z wangshuda $
 */
@SuppressWarnings("unchecked")
public class RoleInfoAction extends BaseDispatchAction {
	@SuppressWarnings("unused")
	protected final Log log = LogFactory.getLog(this.getClass());
	//
	private IRoleInfoService roleInfoService;

	private List<RoleContext> allRoleItemsList = Collections.EMPTY_LIST;
	
	private boolean loadRoleInfoFromSpring = true;
	
	public void setRoleInfoService(IRoleInfoService roleInfoService) {
		this.roleInfoService = roleInfoService;
	}
	
	@Override
	public IRoleInfoService getService() {
		return this.roleInfoService;
	}
	
	/**
	 * 
	 * @method: insert
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward insert(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		DynaActionForm domainForm = (DynaActionForm)form;
		RoleInfoImpl roleInfo = (RoleInfoImpl)domainForm.get(commandStr);
		this.getService().insertRole(roleInfo);
		return mapping.findForward(actionForwardKey.getAfterInsert());
	}
	
	/**
	 * 
	 * @method: updateRoleFuncItems
	 * @return: ActionForward
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateRoleFuncItems(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleId = request.getParameter("roleId");
		String chkFuncIdList[] = request.getParameterValues("chkFuncId");
		//
		this.getService().updateRoleFuncItems(roleId, chkFuncIdList);
		//
		return mapping.findForward(actionForwardKey.getAfterInsert());
	}
	
	@Override
	public ActionForward getGsonOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(!loadRoleInfoFromSpring) {
			return super.getGsonOption(mapping, form, request, response);
		}
		else {
			Map<String ,String> conditionMap = (Map<String ,String>)(super.getJsonConditions(form, request));
			
			//<!--  here roleLevel and roleGroup can not rename. see ibatis.xml in RoleType.listJsonOption  -->
			List<JsonOption> optionList = new ArrayList<JsonOption>(allRoleItemsList.size());
			
			String roleLevelStr = conditionMap.get("roleLevel");
			String roleGroupStr = conditionMap.get("roleGroup");
			
			int roleLevel = Integer.parseInt((StringUtils.hasText(roleLevelStr) ? roleLevelStr: "-1"));
			
			int roleGroup = Integer.parseInt((StringUtils.hasText(roleGroupStr) ? roleGroupStr: "-1"));
			
			for(RoleContext roleContext:allRoleItemsList) {
				if(roleContext.getRoleLevel() > roleLevel 
						&& (roleGroup == -1 || roleGroup == roleContext.getRoleGroup())) {
					optionList.add(new JsonOption(roleContext.getRoleId(), roleContext.getTitle()));
				}
			}
			//
			this.writeWithGson(optionList, response);
		}
		return null;
	}

	public IRoleInfoService getRoleInfoService() {
		return roleInfoService;
	}

	public void setLoadRoleInfoFromSpring(boolean loadRoleInfoFromSpring) {
		this.loadRoleInfoFromSpring = loadRoleInfoFromSpring;
	}

	public void setAllRoleItemsList(List<RoleContext> allRoleItemsList) {
		this.allRoleItemsList = allRoleItemsList;
	}
	
}
