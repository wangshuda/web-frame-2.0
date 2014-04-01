package com.cintel.frame.poi.excel;

/**
 * 
 * @file    : CellType.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-8-25 wangshuda created
 * @see  org.apache.poi.hssf.usermodel.HSSFCell
 *       Enable excelType: Numeric, String, Formula, Blank, Error!
 */
public enum CellType {
    Numeric("Numeric")
    ,String("String")
    ,Formula("Formula")
    ,Blank("Blank")
    ,Boolean("Boolean")
    ,Date("Date")
    ,Error("Error")
    ;
     
    @SuppressWarnings("unused")
    private final String cellType;
    
    private CellType(String value) {
        this.cellType = value;
    }
    
    public String strValue() {
        return this.cellType;
    }
    
    public boolean isSame(CellType otherType) {
        return (otherType != null && this.cellType.equals(otherType.strValue()));
    }
    
}
