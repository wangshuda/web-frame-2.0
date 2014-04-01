package com.cintel.frame.poi.excel;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * 
 * @file : ColumnInfo.java
 * @version : 1.0
 * @desc :
 * @history : 1) 2009-8-25 wangshuda created
 */
@SuppressWarnings("unchecked")
public class ColumnInfo {
    private static Log log = LogFactory.getLog(ColumnInfo.class);

    //
    private int index;

    private String propertyName;

    private String title;

    private CellType excelType;

    private String javaType;

    private String nullValue;

    private boolean enableBlank = false;

    private String saveFmt;

    private int maxLen;

    private Map<String, String> valueAndLabelMap = Collections.EMPTY_MAP;

    public CellType getExcelType() {
        return excelType;
    }

    //
    public boolean equals(Object other) {
        if (other == null || !(other instanceof ColumnInfo)) {
            return false;
        }
        //
        ColumnInfo otherColumnInfo = (ColumnInfo) other;
        // if the propertyName or title equal, the method will return true.
        if (otherColumnInfo.getIndex() == this.index
                || (StringUtils.hasText(otherColumnInfo.getPropertyName()) && StringUtils.hasText(this.propertyName))
                && this.propertyName.equals(otherColumnInfo.getPropertyName())
                || (StringUtils.hasText(otherColumnInfo.getTitle()) && StringUtils.hasText(this.title))
                && this.title.equals(otherColumnInfo.getTitle())) {
            return true;
        }
        //
        return false;

    }

    public void setExcelType(String excelType) {
        try {
            this.excelType = CellType.valueOf(excelType);
        }
        catch (IllegalArgumentException ex) {
            log
                    .error(
                            "Enable excelType: Numeric, String, Formula, Blank, Error! See com.cintel.frame.poi.excel.CellType",
                            ex);
            //
            throw ex;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getNullValue() {
        return nullValue;
    }

    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setExcelType(CellType excelType) {
        this.excelType = excelType;
    }

    public boolean isEnableBlank() {
        return enableBlank;
    }

    public void setEnableBlank(boolean enableBlank) {
        this.enableBlank = enableBlank;
    }

    public String getSaveFmt() {
        return saveFmt;
    }

    public void setSaveFmt(String saveFmt) {
        this.saveFmt = saveFmt;
    }

    public Map<String, String> getValueAndLabelMap() {
        return valueAndLabelMap;
    }

    public void setValueAndLabelMap(Map<String, String> valueAndLabelMap) {
        this.valueAndLabelMap = valueAndLabelMap;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
    }
}
