package com.cintel.frame.properties.db;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cintel.frame.properties.MessageLoader;
import com.cintel.frame.web.action.BaseDispatchAction;

 /**
 * @file : $Id: DbPropertyAction.java 13450 2009-12-17 00:30:18Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
*/
public class DbPropertyAction extends BaseDispatchAction {
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(DbPropertyAction.class);
	//
	
    private MessageLoader dbMessageLoader;
    
    public ActionForward reload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        dbMessageLoader.loadProperties();
        //
        return mapping.findForward(this.getActionForwardKey().getSuccess());
    }
}
