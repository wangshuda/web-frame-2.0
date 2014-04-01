package com.cintel.frame.log;

import com.cintel.frame.webui.IDomainService;
import com.cintel.frame.webui.IDomainVo;

public class LogPersisterDbImpl implements LogPersister {

    private IDomainService webLogService;
    
    public void logCtxBean(AbstractLogContextBean logContext) {
        this.logCtx(logContext);
    }

    public IDomainService getWebLogService() {
        return webLogService;
    }

    public void setWebLogService(IDomainService webLogService) {
        this.webLogService = webLogService;
    }

    public void logCtx(LogContext logContext) {
        webLogService.insert("InsertLogContext", (IDomainVo)logContext);
    }

}
