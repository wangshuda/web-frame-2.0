package com.cintel.frame.auth.role.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.web.action.BaseDispatchAction;
import com.cintel.frame.web.action.JsonOption;

/**
 * @version : $Id: RoleTypeAction.java 15770 2010-01-14 06:24:41Z wangshuda $
 */
@SuppressWarnings("unchecked")
public class RoleTypeAction extends BaseDispatchAction {
	
	private List<RoleContext> allRoleItemsList = Collections.EMPTY_LIST;
	
	private boolean loadRoleTypeFromSpring = true;
	
	@SuppressWarnings("unchecked")
	@Override
	public ActionForward getGsonOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(!loadRoleTypeFromSpring) {
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
						|| roleGroup == -1 || roleGroup == roleContext.getRoleGroup()) {
					optionList.add(new JsonOption(roleContext.getRoleId(), roleContext.getTitle()));
				}
			}
			//
			this.writeWithGson(optionList, response);
		}
		return null;
	}

	public List<RoleContext> getAllRoleItemsList() {
		return allRoleItemsList;
	}

	public void setAllRoleItemsList(List<RoleContext> allRoleItemsList) {
		this.allRoleItemsList = allRoleItemsList;
	}

	public boolean isLoadRoleTypeFromSpring() {
		return loadRoleTypeFromSpring;
	}

	public void setLoadRoleTypeFromSpring(boolean loadRoleTypeFromSpring) {
		this.loadRoleTypeFromSpring = loadRoleTypeFromSpring;
	}

}
