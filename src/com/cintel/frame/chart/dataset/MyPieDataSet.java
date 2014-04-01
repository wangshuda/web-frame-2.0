package com.cintel.frame.chart.dataset;

import java.util.Collections;
import java.util.List;

import org.jfree.data.general.DefaultPieDataset;


public class MyPieDataSet extends DefaultPieDataset {
    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("unchecked")
    private List<RowKeyInfo> rowKeysInfoList = Collections.EMPTY_LIST;

    public List<RowKeyInfo> getRowKeysInfoList() {
        return rowKeysInfoList;
    }

    public void setRowKeysInfoList(List<RowKeyInfo> rowKeysInfoList) {
        this.rowKeysInfoList = rowKeysInfoList;
    }
}
