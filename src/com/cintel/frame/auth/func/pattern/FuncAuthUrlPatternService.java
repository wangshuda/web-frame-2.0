package com.cintel.frame.auth.func.pattern;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.webui.DomainService;
import com.cintel.frame.webui.IDomainDao;

public class FuncAuthUrlPatternService extends DomainService implements IFuncAuthUrlPatternService{
	private IDomainDao funcItemDao;
	
	public FuncItem getFuncItemInfo(String funcId) {
		return (FuncItem)funcItemDao.get("getFuncInfo", funcId);
	}

	public void setFuncItemDao(IDomainDao funcItemDao) {
		this.funcItemDao = funcItemDao;
	}
}
