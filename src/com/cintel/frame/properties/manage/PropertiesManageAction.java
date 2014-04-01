package com.cintel.frame.properties.manage;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cintel.frame.util.FileUtils;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.WebUtils;
import com.cintel.frame.web.action.BaseAction;

public class PropertiesManageAction extends BaseAction {
	private String preifixForKeyName = "cinProKey.";
	
	private PropertiesManager propertiesManager;

	public PropertiesManager getPropertiesManager() {
		return propertiesManager;
	}

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}
	
	public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileCtxKeyInSpring = request.getParameter("fileCtxKeyInSpring");
		
		Collection<PropertiesFileCtx> propertiesFileCtxCollection;
		if(StringUtils.hasText(fileCtxKeyInSpring)) {
			propertiesFileCtxCollection = new ArrayList<PropertiesFileCtx>(1);
			PropertiesFileCtx propertiesFileCtx = propertiesManager.getPropertiesFileCtx(fileCtxKeyInSpring);
			propertiesFileCtxCollection.add(propertiesFileCtx);
			
		}
		else {
			propertiesFileCtxCollection = propertiesManager.getPropertiesFileCtxCollection();
		}
		//
		request.setAttribute("list", propertiesFileCtxCollection);
		request.setAttribute("fileCtxKeyInSpring", fileCtxKeyInSpring);
		return mapping.findForward(actionForwardKey.getSearch());
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> requestParaMap = WebUtils.getParametersStartingWith(request, preifixForKeyName);
		
		String fileCtxKeyInSpring = request.getParameter("fileCtxKeyInSpring");
		if(StringUtils.hasText(fileCtxKeyInSpring)) {
			propertiesManager.save(fileCtxKeyInSpring, requestParaMap);
		}
		else {
			propertiesManager.save(requestParaMap);
		}
		//
		return mapping.findForward(actionForwardKey.getSuccess());
	}

	/**
	 * 
	 * @method: readConfig
	 * @return: ActionForward
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward readConfig(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String location = request.getParameter("location");
		String fileContent = FileUtils.copyToString(new FileReader(new File(location)));
		
		request.setAttribute("fileContent", fileContent);
		request.setAttribute("location", location);
		//
    	return mapping.findForward("readConfig");
	}

	/**
	 * 
	 * @method: saveConfig
	 * @return: ActionForward
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveConfig(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appName = request.getParameter("appName");
		String fileContent = request.getParameter("fileContent");
		String location = request.getParameter("location");
		//
		FileUtils.copy(fileContent.getBytes(), new File(location));
		
		request.setAttribute("appName", appName);
		//
    	return mapping.findForward(actionForwardKey.getSuccess());
	}
	
	public String getPreifixForKeyName() {
		return preifixForKeyName;
	}

	public void setPreifixForKeyName(String preifixForKeyName) {
		this.preifixForKeyName = preifixForKeyName;
	}
}
