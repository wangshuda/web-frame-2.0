package com.cintel.frame.web.app.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.DateUtils;
import com.cintel.frame.util.RandomUtils;
import com.cintel.frame.web.action.BaseDispatchAction;
import com.cintel.frame.webui.IDomainVo;

public class WebAdminAction extends BaseDispatchAction {
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(WebAdminAction.class);
	
	@SuppressWarnings("deprecation")
	@Override
	protected void doInsert(IDomainVo domainVo) {
		WebAdmin webAdmin = (WebAdmin)domainVo;
		webAdmin.setId(RandomUtils.getStreamNo20());
		//
		String createDateTime = DateUtils.getCurrentTimeString();
		webAdmin.setCreateDateTime(createDateTime);
		//
		getService().insert(webAdmin);
	}
}
