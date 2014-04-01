package com.cintel.frame.web.page;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class PagedList<T> {
	
	@Expose
	private List<T> pageDataList = new ArrayList<T>(0);
	
	@Expose
	@SuppressWarnings("unchecked")
	private T dataArr[] = (T[])new Object[0];
	
	@Expose
	private int totalCount = 0;
	
	public PagedList() {
	}
	
	public PagedList(int totalCount, List<T> pageDataList) {
		this.totalCount = totalCount;
		this.pageDataList = pageDataList;
	}
	
	@SuppressWarnings("unchecked")
	public PagedList (List fullDataList) {
		if(fullDataList != null) {
			this.totalCount = fullDataList.size();
			dataArr = (T[])fullDataList.toArray(new Object[fullDataList.size()]);
		}
	}
	
	public T[] getDataArr() {
		return dataArr;
	}

	public void setDataArr(T[] dataArr) {
		this.dataArr = dataArr;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getPageDataList() {
		return pageDataList;
	}

	public void setPageDataList(List<T> pageDataList) {
		this.pageDataList = pageDataList;
	}
}
