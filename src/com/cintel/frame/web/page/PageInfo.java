package com.cintel.frame.web.page;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.properties.TableProperties;
import org.springframework.util.Assert;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-6-4 wangshuda created
 */
public class PageInfo {
	public final static String _PAGE_INFO_PARA_NAME = "pageInfo";
	
	private int totalCount = -1;

	private int targetPage = 1;

	private int pageSize;

	public int getPageCount() {
		return totalCount/pageSize + (totalCount%pageSize != 0 ? 1 : 0);
	}
	
	public boolean isFirstQuery() {
		return (totalCount == -1);
	}
	
	public int getPageStartIndex() {
		return (targetPage - 1)*pageSize;
	}
	
	public int getPageMaxCount() {
		return pageSize;
	}
	
	public int getPageSize() {
		Assert.isTrue(pageSize > 0, "pageSize can not be zero or negative!");
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTargetPage() {
		return targetPage;
	}

	
	public void setTargetPage(int targetPage) {
		Assert.isTrue(targetPage > 0, "targetPage can not be zero or negative!");
		this.targetPage = targetPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		Assert.isTrue(totalCount >= 0, "totalCount can not negative!");
		this.totalCount = totalCount;
	}

	public boolean isFirstPage() {
		return (targetPage == 1);
	}
	
	public PageInfo(int targetPage, int pageSize) {
		this.targetPage = targetPage;
		this.pageSize = pageSize;
	}
	
	public PageInfo(HttpServletRequest request) {
		this(request, 12);
	}
	
	public PageInfo (HttpServletRequest request, int defaultPageSize) {
		TableProperties properties = TableProperties.getInstance(request);
		
		String targetPageStr = request.getParameter(properties.getPaginationPageNumberParam());
		if(targetPageStr == null) {
			targetPage = 1;
		}
		else {
			targetPage = Integer.parseInt(targetPageStr);
		}
		//
		String pageSizeStr = request.getParameter(properties.getPageSizeParameterName());
		if(pageSizeStr == null) {
			pageSize = defaultPageSize;
		}
		else {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		//
		String totalCountStr = request.getParameter(properties.getPageTotalCountParaName());
		if(totalCountStr != null) {
			totalCount = Integer.parseInt(totalCountStr);
		}

	}
}
