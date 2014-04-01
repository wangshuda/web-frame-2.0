package com.cintel.frame.web.action;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.beans.BeanUtils;

import com.cintel.frame.poi.excel.export.ExportService;
import com.cintel.frame.poi.excel.export.FileType;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.WebUtils;
import com.cintel.frame.web.page.PageInfo;
import com.cintel.frame.web.page.PageResult;
import com.cintel.frame.web.page.PagedList;
import com.cintel.frame.webui.IDomainService;
import com.cintel.frame.webui.IDomainVo;

/**
 * @file : BaseDispatchAction.java
 * @author : WangShuDa
 * @date : 2009-1-14
 * @corp : CINtel
 * @version : $Revision: 39676 $
 */
public class BaseDispatchAction extends BaseAction {
	protected Log log = LogFactory.getLog(this.getClass());
	
	protected static String _ACTION_FORWARD_NAME_IN_REQUEST = "actionForward";
	protected static String commandStr = "command";

	private Class commandClass = null;

	private IDomainService service = null;
	
	private ExportService exportService;

	// ----------------------------- set/get methods ------------------------
	public void setService(IDomainService service) {
		this.service = service;
	}

	/**
     * If a new sub service which extends from IDomainService is delcared in the
     * sub class, the method of getService should be overrided, and return the
     * new sub service.
     * 
     * @method: getService
     */
	public IDomainService getService() {
		return this.service;
	}

	//
	public Class getCommandClass() {
		return commandClass;
	}

	public void setCommandClass(Class commandClass) {
		this.commandClass = commandClass;
	}
	

	/**
	 * @method: forward
	 */
	public ActionForward forward(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String forwardName = request.getParameter("forwardName");
		Map parameterMap = WebUtils.getParametersStartingWith(request, "");
		request.setAttribute(commandStr, parameterMap);
		if (forwardName == null) {
			forwardName = actionForwardKey.getSuccess();
		}
		return mapping.findForward(forwardName);
	}
	
	// ---- getJsonConditions ,getSearchConditions--------
	// the next method group is designed to provide the conditions object.
	// --------------------------------------------------------------------
	/**
     * @param form
     * @param request
     * @return
     * @throws Exception
     */
	protected Object getJsonConditions(ActionForm form, HttpServletRequest request) throws Exception {
		return this.organizeSearchConditions(form, request);
	}

	/**
     * @method:getSearchConditions
     */
	protected Object getSearchConditions(ActionForm form, HttpServletRequest request) {
		return null;
	}

	/**
     * @method: getParameterMap
     * @desc:   organize the request parameters with the fixed prefix "command."
     *          , save their value and key without the prefix in map and return the map
     * 
     */
	@SuppressWarnings("unchecked")
    protected Map<String, String> getParameterMap(ActionForm form, HttpServletRequest request) {
		Map<String, String> map = WebUtils.getParametersStartingWith(request, commandStr + ".");
        //
        Object obj = request.getAttribute(commandStr);
        if(obj instanceof Map) {
            map.putAll((Map)obj);
        }
        //
        return map;
	}
	
	@SuppressWarnings("unchecked")
	protected Map organizeSearchConditions(ActionForm form, HttpServletRequest request) {
		Map parameterMap = this.getParameterMap(form, request);
		Object searchConditions = this.getSearchConditions(form, request);
		if (searchConditions != null) {
			parameterMap.putAll((Map) searchConditions);
		}

		return parameterMap;
	}

	// ---------------getGsonOption ,search, read ------------------------
	// the next method group is designed for reading.
	// -----------------------------------------------------------------------
	/**
     * @method: getGsonOption
     */
	@SuppressWarnings("unchecked")
	public ActionForward getGsonOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		Object conditionsObj = this.getJsonConditions(form, request);
		List<JsonOption> optionList = this.getService().getJsonOption(conditionsObj);
		//
		String needEmpty = request.getParameter("needEmpty");
		if ("1".equalsIgnoreCase(needEmpty) && optionList != null && optionList.size() > 0) {
			if (optionList == null) {
				optionList = new ArrayList();
			}
			optionList.add(0, new JsonOption());
		}

		this.writeWithGson(optionList, response);

		return null;
	}

	@SuppressWarnings("unchecked")
    public ActionForward pageSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map searchConditions = this.organizeSearchConditions(form, request);
		
        PageInfo pageInfo = new PageInfo(request);
		
        PagedList pagedList = this.getService().pageSearch(pageInfo, searchConditions);
		//
		String isJsonReq = request.getParameter("isJsonReq");
		if(!StringUtils.hasText(isJsonReq)) {
			request.setAttribute(commandStr, searchConditions);
			request.setAttribute("pagedList", pagedList);
			request.setAttribute("pageInfo", pageInfo);
	        
	        if(StringUtils.hasText((String)(searchConditions.get("listForSel")))) {
	            return mapping.findForward(actionForwardKey.getListForSel());
	        }
	        else {
	            return mapping.findForward(actionForwardKey.getSearch());
	        }
		}
		else {
			this.writeWithGson(new PageResult(pageInfo, pagedList), response);

			return null;
		}
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map searchConditions = this.organizeSearchConditions(form, request);
		List list = this.doSearch(searchConditions);
		//
		String isJsonReq = request.getParameter("isJsonReq");
		if(!StringUtils.hasText(isJsonReq)) {
			request.setAttribute(commandStr, searchConditions);
			request.setAttribute("list", list);
	        
	        if(StringUtils.hasText((String)(searchConditions.get("listForSel")))) {
	            return mapping.findForward(actionForwardKey.getListForSel());
	        }
	        else {
	            return mapping.findForward(actionForwardKey.getSearch());
	        }
		}
		else {
			this.writeWithGson(list, response);

			return null;
		}
	}
	//
	public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String oid = request.getParameter("oid");
		IDomainVo domainVo = (IDomainVo) getCommandClass().newInstance();
		domainVo.setOid(oid);
		Object resultObj = this.getService().get(domainVo);
		//
		String isJsonReq = request.getParameter("isJsonReq");
		if(!StringUtils.hasText(isJsonReq)) {
			request.setAttribute(commandStr, resultObj);
			return mapping.findForward(actionForwardKey.getRead());
		}
		else {
			this.writeWithGson(resultObj, response);

			return null;
		}
	}
	
	// ---------------preEdit,insert,update, delete ------------------------
	// the next method group is designed for editing.
	// ---------------------------------------------------------------------

	/**
     * InitCommandClass the function will be called in preEdit to set the
     * default value.
     * 
     * @param domainVo
     */
	protected void initCommandClass(IDomainVo domainVo) {
	}

	/**
     * InitCommandClass
     * 
     * @param domainVo
     * @param request
     */
	protected void initCommandClass(IDomainVo domainVo, HttpServletRequest request) {
		this.initCommandClass(domainVo);
	}

	/**
     * preEdit The method will be called before create or update a record.
     */
	public ActionForward preEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String oid = request.getParameter("oid");
		IDomainVo domainVo = (IDomainVo) getCommandClass().newInstance();
		if (oid == null || "".equals(oid)) {
			this.initCommandClass(domainVo, request);
		}
		else {
			domainVo.setOid(oid);
			domainVo = this.getService().get(domainVo);
		}
		request.setAttribute(commandStr, domainVo);

		return mapping.findForward(actionForwardKey.getEdit());
	}

	//
	protected void doInsertPre(IDomainVo domainVo, ActionForm form, HttpServletRequest request) {
	}
	
	protected void doUpdatePre(IDomainVo domainVo, ActionForm form, HttpServletRequest request) {
	}
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 */
	protected ActionForward getActionForwardWithRequest(ActionMapping mapping, HttpServletRequest request, String defaultForwardKey) {
		String requestActionForwardKey = request.getParameter(_ACTION_FORWARD_NAME_IN_REQUEST);
        //
		if(StringUtils.hasText(requestActionForwardKey)) {
			return mapping.findForward(requestActionForwardKey);
		}
		else {
			return mapping.findForward(defaultForwardKey);
		}
	}
	
	/**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DynaActionForm domainForm = (DynaActionForm) form;
		IDomainVo domainVo = (IDomainVo) (domainForm.get(commandStr));
		//
		this.doInsertPre(domainVo);
		this.doInsertPre(domainVo, form, request);
		//
		this.doInsert(domainVo);
		//
		String isJsonReq = request.getParameter("isJsonReq");
		if(!StringUtils.hasText(isJsonReq)) {
			return this.getActionForwardWithRequest(mapping, request, actionForwardKey.getAfterInsert());
		}
		else {
			return null;
		}
	}

	/**
     * @method: update
     */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DynaActionForm domainForm = (DynaActionForm) form;
		IDomainVo domainVo = (IDomainVo) (domainForm.get(commandStr));
		//
		this.doUpdatePre(domainVo);
		this.doUpdatePre(domainVo, form, request);
		//
		this.doUpdate(domainVo);
		//
		String isJsonReq = request.getParameter("isJsonReq");
		if(!StringUtils.hasText(isJsonReq)) {
			return this.getActionForwardWithRequest(mapping, request, actionForwardKey.getAfterUpdate());
		}
		else {
			return null;
		}
	}

	/**
     * @method: delete
     */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		String oid = (request.getParameter("oid"));
		IDomainVo domainVo = (IDomainVo) getCommandClass().newInstance();
		domainVo.setOid(oid);
		this.doDelete(domainVo);
		//
		String isJsonReq = request.getParameter("isJsonReq");
		if(!StringUtils.hasText(isJsonReq)) {
			return mapping.findForward(actionForwardKey.getAfterDelete());
		}
		else {
			return null;
		}
	}

	/**
	 * @method: deleteList
	 */
	@SuppressWarnings("unchecked")
	public ActionForward batchDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map parameterMap = this.getParameterMap(form, request);
		//
		String selectedIdArr[] = request.getParameterValues("selectedIds");		
		if(selectedIdArr != null) {
			List<String> list = Arrays.asList(selectedIdArr);
			
			parameterMap.put("selectedIdList", list);
			//
			this.doBatchDelete(parameterMap);
		}
		//
		return mapping.findForward(actionForwardKey.getAfterDelete());
	}
	
	public ActionForward batchExport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map searchConditions = this.organizeSearchConditions(form, request);
		//
		String perSheetRowsCnt = request.getParameter("perSheetRowsCnt");
		String maxSheetCntInFile = request.getParameter("maxSheetCntInFile");
		String fileNamePattern = request.getParameter("fileNamePattern");
		
		FileType exportFileType = this.getExportService().buildFileType(perSheetRowsCnt, maxSheetCntInFile, searchConditions);
		//
		String fileName = this.getExportService().getFileName(exportFileType);
		response.reset();
		response.setHeader("Content-Disposition", "attachment;filename=\""+ fileName + "\"");
		response.setContentType(exportFileType.getContentType());
		//
		this.getExportService().doExport(perSheetRowsCnt, maxSheetCntInFile, fileNamePattern, searchConditions, response.getOutputStream());
		//
		return null;
	}
	
	public ActionForward csvBatchExport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map searchConditions = this.organizeSearchConditions(form, request);  	
	    response.reset();
		String fileName = request.getParameter("fileName");
		response.setHeader("Content-Disposition", "attachment;filename=\""+ fileName + "\"");
		response.setContentType("text/csv");
		
		this.getExportService().doCsvExport(searchConditions, response.getOutputStream());
		return null;
	}
	
	/**
	 * Check whether the entity has been existd.
	 * @author HeXiongwei
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkUnique(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		log.warn("checkUnique start.");
		boolean isExist = false;
		try {
			Map<String, String> map = this.getParameterMap(form, request);
			isExist = this.getService().isExist(map);
			
		}
		catch(Exception ex) {
			log.error("checkUnique failed", ex);
			isExist = false;
		}
		
		super.writeWithGson(isExist, response);
		//
		log.debug("is Exist:"+isExist);
		return null;
	}

	// ---------------doInsert,doUpdate,doDelete ------------------------
	// You may overload the next function for insert,update and delete
	// ------------------------------------------------------------------
	protected void doInsert(IDomainVo domainVo) {
		this.getService().insert(domainVo);
	}

	protected void doUpdate(IDomainVo domainVo) {
		this.getService().update(domainVo);
	}

	protected void doDelete(IDomainVo domainVo) {
		this.getService().delete(domainVo);
	}

	protected int doBatchDelete(Map parameterMap) {
		return this.getService().batchDelete(parameterMap);
	}

	protected List doSearch(Object parameterObj) {
		return this.getService().search(parameterObj);
	}

	// ---------------Error------------------------
	protected void saveError(ErrorInfo errorInfo) {
		errors.saveError(errorInfo);
		return;
	}
	
	/**
	 * @method: existError
	 */
	protected boolean existError() {
		return errors.existError();
	}

	/**
     * @method: reportError
     */
	protected ActionForward reportError(ActionMapping mapping, HttpServletRequest request) {
		//
		request.setAttribute("errorsInfo", errors.getErrors());
		return mapping.findForward(actionForwardKey.getError());
	}

	//------------------------- Deprecated methods --------------------------------
	/**
	 * 
	 * @method: Method for Insert/Update Pre
	 * @desc  : the extended action can imply the method to config the next oid of the domain.
	 */
	@Deprecated
	protected void doInsertPre(IDomainVo domainVo) {
	}
	@Deprecated
	protected void doUpdatePre(IDomainVo domainVo) {
	}
	/**
	 * @method: createCommand
	 */
	@Deprecated
	protected final Object createCommand() throws Exception {
		if (this.commandClass == null) {
			throw new IllegalStateException("Cannot create command without commandClass being set - "
					+ "either set commandClass or (in a form controller) override formBackingObject");
		}
		return BeanUtils.instantiateClass(this.commandClass);
	}
	
	/**
     * @method:getListConditions
     * @desc: recommand to use the search/getSearchConditions method to replace this method.
     */
	@Deprecated
	protected Object getListConditions(ActionForm form, HttpServletRequest request) throws Exception {
		return this.createCommand();
	}
	
	/**
	 * @method: list
	 * @desc: recommand to use the search/getSearchConditions method to replace this method.
	 */
	@Deprecated
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object listConditions = this.getListConditions(form, request);

		List list = this.getService().list(listConditions);
		request.setAttribute("list", list);
		return mapping.findForward(actionForwardKey.getList());
	}

	/**
     * getJsonOption : replace the method with getGsonOption
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	@Deprecated
	public ActionForward getJsonOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.getGsonOption(mapping, form, request, response);
	}
	
	public ExportService getExportService() {
		return exportService;
	}

	public void setExportService(ExportService exportService) {
		this.exportService = exportService;
	}	
}
