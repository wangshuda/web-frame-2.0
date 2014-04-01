package com.cintel.frame.web.app.install;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cintel.frame.util.StringUtils;
import com.cintel.frame.web.action.BaseAction;
import com.cintel.frame.web.action.JsonOption;
import com.cintel.frame.web.app.install.sql.EntitySQLContext;

 /**
 * @file : $Id: InstallAction.java 13450 2009-12-17 00:30:18Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
*/
public class InstallAction extends BaseAction {
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(InstallAction.class);
	//
	private InstallService installService = null;

	public ActionForward getSqlMapClientNameGsonOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Set<String> keySet = installService.getDbContextMap().keySet();
		List<JsonOption> optionList = new LinkedList<JsonOption>();
		for(String sqlMapName:keySet) {
			optionList.add(new JsonOption(sqlMapName, sqlMapName));
		}
		this.writeWithGson(optionList, response);
		return null;
	}
	
	//
	@SuppressWarnings("unchecked")
	public ActionForward listSqlContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");
		String sqlMapClientName = request.getParameter("sqlMapClientName");
		//
		Map<String, Map<String, EntitySQLContext>> dbContextMap = this.getInstallService().getDbContextMap();
		//
		if(!StringUtils.hasText(sqlMapClientName)) {
			List<String> sqlMapNameList = new LinkedList<String>();
			sqlMapNameList.addAll(dbContextMap.keySet());
			//
			sqlMapClientName = sqlMapNameList.get(0);
		}
		Collection<EntitySQLContext> allsqlCtxCollection = dbContextMap.get(sqlMapClientName).values();
		List<EntitySQLContext> resultList = new LinkedList<EntitySQLContext>();
		if(!StringUtils.hasText(type)) {
			resultList.addAll(allsqlCtxCollection);
		}
		else {
			for(EntitySQLContext sqlCtx:allsqlCtxCollection) {
				if(type.equals(sqlCtx.getType())) {
					resultList.add(sqlCtx);
				}
			}
		}
		//
		Collections.sort(resultList);
		//
		request.setAttribute("list", resultList);
		request.setAttribute("type", type);
		request.setAttribute("sqlMapClientName", sqlMapClientName);
		return mapping.findForward("listSqlContext");
	}

	public ActionForward executeCreateSql(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] entityNameArr = request.getParameterValues("entityNames");
		String sqlMapClientName = request.getParameter("sqlMapClientName");
		//
		List<SqlExecuteResult> sqlExecuteResultList = this.getInstallService().executeCreateSql(sqlMapClientName, entityNameArr);;

		request.setAttribute("list", sqlExecuteResultList);
		//
		return mapping.findForward("executeSqlResult");
	}

	public ActionForward executeReCreateSql(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] entityNameArr = request.getParameterValues("entityNames");
		String sqlMapClientName = request.getParameter("sqlMapClientName");
		//
		List<SqlExecuteResult> sqlExecuteResultList = this.getInstallService().executeReCreateSql(sqlMapClientName, entityNameArr);;

		request.setAttribute("list", sqlExecuteResultList);
		//
		return mapping.findForward("executeSqlResult");
	}
	
	public ActionForward executeDropSql(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] entityNameArr = request.getParameterValues("entityNames");
		String sqlMapClientName = request.getParameter("sqlMapClientName");
		//
		List<SqlExecuteResult> sqlExecuteResultList = this.getInstallService().executeDropSql(sqlMapClientName, entityNameArr);;

		request.setAttribute("list", sqlExecuteResultList);
		//
		return mapping.findForward("executeSqlResult");
	}
	
	public ActionForward executeCheckSql(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] entityNameArr = request.getParameterValues("entityNames");
		String sqlMapClientName = request.getParameter("sqlMapClientName");
		//
		List<SqlExecuteResult> sqlExecuteResultList = this.getInstallService().executeCheckSql(sqlMapClientName, entityNameArr);;

		request.setAttribute("list", sqlExecuteResultList);
		//
		return mapping.findForward("executeSqlResult");
	}
	// ------------------ get/set methods ----------------------
	public InstallService getInstallService() {
		return installService;
	}


	public void setInstallService(InstallService installService) {
		this.installService = installService;
	}
	
}
