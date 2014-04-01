package com.cintel.frame.poi.excel.export;

import com.cintel.frame.web.page.PageInfo;
import com.cintel.frame.web.page.PagedList;

public interface ExportQueryService {
	public int totalItemCnt(Object queryConditinObj);
	//
	public PagedList pageSearch(PageInfo pageInfo, Object queryConditinObj) ;
}
