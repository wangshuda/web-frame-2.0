package com.cintel.frame.chart.dataset;

import java.util.Collections;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;


/**
 * 
 * @file    : MyCategoryDataset.java
 * @author  : WangShuDa
 * @date    : 2008-10-15
 * @corp    : CINtel
 * @version : 1.0
 */

public class MyCategoryDataset extends DefaultCategoryDataset{
	private static final long serialVersionUID = 1L;
	//
    @SuppressWarnings("unchecked")
	private List<RowKeyInfo> rowKeysInfoList = Collections.EMPTY_LIST;

	public List<RowKeyInfo> getRowKeysInfoList() {
		return rowKeysInfoList;
	}

	public void setRowKeysInfoList(List<RowKeyInfo> rowKeysInfoList) {
		this.rowKeysInfoList = rowKeysInfoList;
	}

}
