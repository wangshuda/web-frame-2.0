package com.cintel.frame.poi.excel;

import java.beans.PropertyDescriptor;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.cintel.frame.poi.excel.hssf.CellData;
import com.cintel.frame.properties.PropertiesUtils;
import com.cintel.frame.util.DateUtils;
import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;

/**
 * 
 * @file    : WorkSheetInfo.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-8-25 wangshuda created
 */

public class WorkSheetInfo implements InitializingBean {
    private static final Log log = LogFactory.getLog(WorkSheetInfo.class);
    
    private boolean withTitle;
    
    private int rowStartIndex = 0;
    
    private int columnStartIndex = 0;
    
    private String commandClass;
    
    @SuppressWarnings("unchecked")
    private Set<ColumnInfo> columnInfoList = Collections.EMPTY_SET;

    public int getColumnCount() {
        return columnInfoList.size();
    }

    private String filterUnNumber(String str) {
        return com.cintel.frame.util.StringUtils.replaceAll(str, "[^0-9]", "");
    }
    
    
    public PoiDataObj loadPoiObjFromCellDataArr(CellData[] cellDataArr) throws Exception {
        PoiDataObj poiDataObj = new PoiDataObj();
        //
        Object resultObj = Class.forName(commandClass).newInstance();
        PropertyDescriptor propertyDescriptor = null;
        
        Object valueObj = null;
        String valueStr = null;
        String propertyName = null;
        Class propertyClass = null;
        
        int colIndex = 0;
        int rowIndex = 0;
        CellType cellType = null;
        String colParseErrMsg = "";
        //
        for(ColumnInfo columnInfo:columnInfoList) {
            valueObj = null;
            colParseErrMsg = "";
            
            //
            propertyName = columnInfo.getPropertyName();
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(resultObj, propertyName);
            
            propertyClass = propertyDescriptor.getPropertyType();
            //
            colIndex = columnInfo.getIndex();
            valueStr = cellDataArr[colIndex].getValueStr();
            rowIndex = cellDataArr[colIndex].getRowIndex();
            cellType = cellDataArr[colIndex].getType();
            //
            if(!columnInfo.isEnableBlank() && !StringUtils.hasText(valueStr)) {
                colParseErrMsg = MessageFormat.format("Error: colIndex:{0}, rowIndex:{1} is empty, but this column:{2} can not be empty.", colIndex, rowIndex, columnInfo.getTitle());
                log.error(colParseErrMsg);
                //
                //throw new ErrorReportException(ErrorInfo.newInstance("error.excel.unableEmpty", errMsg));
            }
            else if(columnInfo.isEnableBlank() && !StringUtils.hasText(valueStr)){
                valueStr = columnInfo.getNullValue();
            }

            //
            String valueStrInMap = null;
            Map<String, String> valueAndLabelMap = columnInfo.getValueAndLabelMap();
            if(valueAndLabelMap != null && !valueAndLabelMap.isEmpty()) {
                valueStr = StringUtils.trimAllWhitespace(valueStr);
                //
                valueStrInMap = valueAndLabelMap.get(valueStr);
                if(StringUtils.hasLength(valueStrInMap)) {
                    valueStr = valueStrInMap;
                }
                else if(!valueAndLabelMap.containsValue(valueStr) && !valueAndLabelMap.containsValue(valueStr)) {
                    String allAcceptKey = StringUtils.collectionToCommaDelimitedString(valueAndLabelMap.keySet());
                    String allAcceptValue = StringUtils.collectionToCommaDelimitedString(valueAndLabelMap.values());
                    //
                    colParseErrMsg = MessageFormat.format("Unexpected value {2}, only accept: {3} or {4}. Column:{0}, Row:{1}", columnInfo.getTitle(), rowIndex - 1, valueStr, allAcceptKey, allAcceptValue);
                    log.error(colParseErrMsg);
                    //
                    //throw new ErrorReportException(ErrorInfo.newInstance("error.excel.unExpectedValue", parseErrMsg));
                }
            }
            //
            if(StringUtils.hasLength(valueStr)) {
                int valueStrLen = valueStr.length();
                if(valueStrLen > columnInfo.getMaxLen()) {
                    valueStr = StringUtils.trimAllWhitespace(valueStr);
                }
            }
            //
            if(propertyClass.equals(String.class)) {
                if(CellType.Date.isSame(cellType)) {
                    valueObj = DateUtils.formatTransfer(valueStr, DateUtils.date14Format, columnInfo.getSaveFmt());
                }
                else if(CellType.Date.isSame(columnInfo.getExcelType())) {
                    valueObj = this.filterUnNumber(valueStr);
                }
                else {
                    valueObj = valueStr;
                }
            }
            else if(propertyClass.equals(int.class) || propertyClass.equals(Integer.class)) {
                if(!StringUtils.hasLength(valueStr)) {
                    valueStr = "0";
                }
                //
                try {
                    valueObj = Integer.parseInt(valueStr);
                }
                catch(Exception ex) {
                    colParseErrMsg = ex.getLocalizedMessage();
                    log.warn("valueStr is:" + valueStr, ex);
                }
            }
            else if(propertyClass.equals(double.class) || propertyClass.equals(Double.class)) {
                valueObj = Double.parseDouble(valueStr);
            }
            else {
                String unKnownMsgPtn = "Unknown the property type with name:{0} in class:{1}. Can only support: String ,int and double!";
                colParseErrMsg = MessageFormat.format(unKnownMsgPtn, propertyName, propertyClass);
                //throw new IllegalArgumentException(MessageFormat.format(message, propertyName, propertyName));
            }
            //
            
            if(StringUtils.hasLength(colParseErrMsg)) {
                poiDataObj.rptParseResult(propertyName, valueStr, colParseErrMsg);
            }
            else {
                PropertyUtils.setProperty(resultObj, propertyName, valueObj);
            }
        }
        //
        poiDataObj.setRowIndex(rowIndex);
        poiDataObj.setDataObj(resultObj);
        //
        return poiDataObj;
    }
    
    public Object loadObjFromCellDataArr(CellData[] cellDataArr) throws Exception {
        Object resultObj = Class.forName(commandClass).newInstance();
        PropertyDescriptor propertyDescriptor = null;
        
        Object valueObj = null;
        String valueStr = null;
        String propertyName = null;
        Class propertyClass = null;
        
        int colIndex = 0;
        int rowIndex = 0;
        CellType cellType = null;
        
        for(ColumnInfo columnInfo:columnInfoList) {
            propertyName = columnInfo.getPropertyName();
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(resultObj, propertyName);
            
            propertyClass = propertyDescriptor.getPropertyType();
            //
            colIndex = columnInfo.getIndex();
            valueStr = cellDataArr[colIndex].getValueStr();
            rowIndex = cellDataArr[colIndex].getRowIndex();
            cellType = cellDataArr[colIndex].getType();
            //
            if(!columnInfo.isEnableBlank() && !StringUtils.hasText(valueStr)) {
                String errMsg = MessageFormat.format("Error: colIndex:{0}, rowIndex:{1} is empty, but this column:{2} can not be empty.", colIndex, rowIndex, columnInfo.getTitle());
                log.error(errMsg);
            	//
                throw new ErrorReportException(ErrorInfo.newInstance("error.excel.unableEmpty", errMsg));
            }
            else if(columnInfo.isEnableBlank() && !StringUtils.hasText(valueStr)){
            	valueStr = columnInfo.getNullValue();
            }

            //
            String valueStrInMap = null;
            Map<String, String> valueAndLabelMap = columnInfo.getValueAndLabelMap();
            if(valueAndLabelMap != null && !valueAndLabelMap.isEmpty()) {
                valueStrInMap = valueAndLabelMap.get(valueStr);
                //
                if(StringUtils.hasLength(valueStrInMap)) {
                    valueStr = valueStrInMap;
                }
                else if(!valueAndLabelMap.containsKey(valueStr) && !valueAndLabelMap.containsValue(valueStr)) {
                    String errorCode = "error.excel.unExpectedValue";
                    String defaultMsg = "Error: Column:{0}, Row:{1} is unexpected value {2}, only accept: Key:{3} or Value:{4}.";
                    String errMsgInBundle = PropertiesUtils.getMessage(errorCode, defaultMsg) ;
                    //
                    String allAcceptKey = StringUtils.collectionToCommaDelimitedString(valueAndLabelMap.keySet());
                    String allAcceptValue = StringUtils.collectionToCommaDelimitedString(valueAndLabelMap.values());
                    //
                    String errMsg = MessageFormat.format(errMsgInBundle, columnInfo.getTitle(), rowIndex, valueStr, allAcceptKey, allAcceptValue);
                    log.error(errMsg);
                    //
                    throw new ErrorReportException(errorCode, errMsg);
                }
            }
            //
            if(StringUtils.hasLength(valueStr)) {
                int valueStrLen = valueStr.length();
                if(valueStrLen > columnInfo.getMaxLen()) {
                    valueStr = StringUtils.trimAllWhitespace(valueStr);
                }
            }
            //
            if(propertyClass.equals(String.class)) {
                if(CellType.Date.isSame(cellType)) {
                    valueObj = DateUtils.formatTransfer(valueStr, DateUtils.date14Format, columnInfo.getSaveFmt());
                }
                else if(CellType.Date.isSame(columnInfo.getExcelType())) {
                    valueObj = this.filterUnNumber(valueStr);
                }
                else {
                    valueObj = valueStr;
                }
            }
            else if(propertyClass.equals(int.class) || propertyClass.equals(Integer.class)) {
                if(!StringUtils.hasLength(valueStr)) {
                    valueStr = "0";
                }
                //
                valueObj = Integer.parseInt(valueStr);
            }
            else if(propertyClass.equals(double.class) || propertyClass.equals(Double.class)) {
                valueObj = Double.parseDouble(valueStr);
            }
            else {
                String message = "Unknown the property type with name:{0} in class:{1}. Can only support: String ,int and double!";
                throw new IllegalArgumentException(MessageFormat.format(message, propertyName, propertyName));
            }
            //
            PropertyUtils.setProperty(resultObj, propertyName, valueObj);
        }
        return resultObj;
    }


    public void afterPropertiesSet() throws Exception {
        this.checkConfig();
    }

    public void checkConfig() throws Exception {
        Assert.notNull(commandClass);
        Assert.notNull(columnInfoList);
        //
        Object resultObj = Class.forName(commandClass).newInstance();
        for(ColumnInfo columnInfo:columnInfoList) {
            PropertyUtils.getProperty(resultObj, columnInfo.getPropertyName());
        }
    }
    
    public Set<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
    }

    public void setColumnInfoList(Set<ColumnInfo> columnInfoList) {
        this.columnInfoList = columnInfoList;
    }

    public boolean isWithTitle() {
        return withTitle;
    }

    public void setWithTitle(boolean withTitle) {
        this.withTitle = withTitle;
    }


    public String getCommandClass() {
        return commandClass;
    }


    public void setCommandClass(String commandClass) {
        this.commandClass = commandClass;
    }

	public int getRowStartIndex() {
		return rowStartIndex;
	}

	public void setRowStartIndex(int rowStartIndex) {
		this.rowStartIndex = rowStartIndex;
	}

	public int getColumnStartIndex() {
		return columnStartIndex;
	}

	public void setColumnStartIndex(int columnStartIndex) {
		this.columnStartIndex = columnStartIndex;
	}
    
}
