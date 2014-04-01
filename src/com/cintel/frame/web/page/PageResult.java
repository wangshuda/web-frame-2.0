package com.cintel.frame.web.page;

import com.google.gson.annotations.Expose;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-1-5 wangshuda created
 */
public class PageResult<T> {

	@Expose
	private PageInfo pageInfo;

	@Expose
	private PagedList<T> pagedList;
    
	public PageResult(PageInfo pageInfo, PagedList<T> pagedList) {
		this.setPagedList(pagedList);
		this.setPageInfo(pageInfo);
	}
	public PagedList getPagedList() {
		return pagedList;
	}

	public void setPagedList(PagedList<T> pagedList) {
		this.pagedList = pagedList;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
}
