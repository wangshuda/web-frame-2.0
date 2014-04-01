package com.cintel.frame.web.app.help;

import com.cintel.frame.web.action.BaseDispatchAction;
import com.cintel.frame.webui.IDomainService;


 /**
 * @file : $Id$
 * @corp : CINtel
 * @version : 1.0
*/
public class WebHelpAction extends BaseDispatchAction {
	private IWebHelpService extendService;

	@Override
	public IDomainService getService() {		
		return extendService;
	}

	public IWebHelpService getExtendService() {
		return extendService;
	}

	public void setExtendService(IWebHelpService extendService) {
		this.extendService = extendService;
	}

	
}
