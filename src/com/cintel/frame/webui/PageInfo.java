package com.cintel.frame.webui;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.properties.TableProperties;

/**
 * 
 * replace the class with:com.cintel.frame.tag.PageInfo;
 * change the BigInteger to int and added an new constructor.
 *
 */
@Deprecated
public class PageInfo {

	public final static String _PAGE_INFO_PARA_NAME = "pageInfo";
	
	private BigInteger totalCount;

	private BigInteger targetPage;

	private BigInteger pageSize;

	public BigInteger getPageSize() {
		return pageSize;
	}

	public void setPageSize(BigInteger pageSize) {
		this.pageSize = pageSize;
	}

	public BigInteger getTargetPage() {
		return targetPage;
	}

	
	public void setTargetPage(BigInteger targetPage) {
		this.targetPage = targetPage;
	}

	public BigInteger getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(BigInteger totalCount) {
		this.totalCount = totalCount;
	}

	public boolean isFirstPage() {
		return (targetPage.intValue() == 1);
	}
	
	public PageInfo (HttpServletRequest request, BigInteger defaultPageSize) {
		TableProperties properties = TableProperties.getInstance(request);
		
		String targetPageStr = request.getParameter(properties.getPaginationPageNumberParam());
		if(targetPageStr == null) {
			targetPage = new BigInteger("1");
		}
		else {
			targetPage = new BigInteger(targetPageStr);
		}
		//
		String pageSizeStr = request.getParameter(properties.getPageSizeParameterName());
		if(pageSizeStr == null) {
			pageSize = defaultPageSize;
		}
		else {
			pageSize = new BigInteger(pageSizeStr);
		}
		//
		String totalCountStr = request.getParameter(properties.getPageTotalCountParaName());
		if(totalCountStr != null) {
			totalCount = new BigInteger(totalCountStr);
		}

	}
}
