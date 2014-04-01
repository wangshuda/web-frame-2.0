package com.cintel.frame.auth.func.pattern;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.webui.IDomainService;

public interface IFuncAuthUrlPatternService extends IDomainService {
	public FuncItem getFuncItemInfo(String funcId);
}
