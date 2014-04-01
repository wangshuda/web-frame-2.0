package com.cintel.frame.poi.excel;

import java.util.HashMap;
import java.util.Map;

public class PoiDataObj {
    
    private boolean okData = true;
    
    private int rowIndex;
    
    private Object dataObj;
    
    private Map<String, PoiParseFieldCtx> parseResultMap = new HashMap<String, PoiParseFieldCtx>();
        
    
    public void rptParseResult(String fieldName, String fieldValue, String parseRptMsg) {
        this.parseResultMap.put(fieldName, new PoiParseFieldCtx(fieldValue, parseRptMsg));
        //
        okData = false;
    }
    
    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public boolean isOkData() {
        return okData;
    }

    public void setOkData(boolean okData) {
        this.okData = okData;
    }

    public Map<String, PoiParseFieldCtx> getParseResultMap() {
        return parseResultMap;
    }

    public void setParseResultMap(Map<String, PoiParseFieldCtx> parseResultMap) {
        this.parseResultMap = parseResultMap;
    }

}
