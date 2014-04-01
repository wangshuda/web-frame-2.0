package com.cintel.frame.poi.excel.hssf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

import com.cintel.frame.poi.excel.CellStyle;
import com.cintel.frame.poi.excel.CellType;
import com.cintel.frame.poi.excel.ColumnInfo;
import com.cintel.frame.poi.excel.WorkSheetInfo;
import com.cintel.frame.util.DateUtils;

/**
 * 
 * @file : WorkBookBox.java
 * @version : 1.0
 * @desc :
 * @history : 1) 2009-8-25 wangshuda created
 * 			  2) 2010-6-12 Added the methods to support the cell write for report export.
 */
@SuppressWarnings("deprecation")
public class WorkBookBox {
    private static Log log = LogFactory.getLog(WorkBookBox.class);
    private final static String _EXCEL_COL_NAME_PREFIX_ARR[] = {
    			"A", "B", "C", "D", "E", "F", "G", "H", "I","J", "K", "L", "M", "N"
    			,"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    
    private static final String _DEFAULT_SHEET_NAME = "Sheet1";
    //
    private HSSFWorkbook workBook = null;
    
    private HSSFSheet currentWorkSheet = null;
    
    private HSSFCellStyle defaultHeadStyle = null;
    private HSSFCellStyle defaultCellStyle = null;
    
    private String colIndex2Name(int colIndex) {
    	int firstCharIndex = colIndex/26;
    	int secondCharIndex = colIndex%26;
    	//
    	if(firstCharIndex == 0) {
    		return _EXCEL_COL_NAME_PREFIX_ARR[secondCharIndex];
    	}
    	else {
    		return _EXCEL_COL_NAME_PREFIX_ARR[firstCharIndex - 1] +  _EXCEL_COL_NAME_PREFIX_ARR[secondCharIndex];
    	}
    }
    
    protected HSSFCellStyle getHeadCellStyle() {
    	if(defaultHeadStyle == null) {
    		log.debug("Generated the DefaultHeadStyle");
    		defaultHeadStyle = CellStyle.loadDefaultHeadStyle().load2HssfCellStyle(workBook);
    	}
        return defaultHeadStyle;
    }
    
    protected HSSFCellStyle getContentCellStyle() {
    	if(defaultCellStyle == null) {
    		log.debug("Generated the DefaultCellStyle");
    		defaultCellStyle = CellStyle.loadDefaultCellStyle().load2HssfCellStyle(workBook);
    	}
        return defaultCellStyle;
    }
    // --------------------- construct functions. ---------------------
    public WorkBookBox() {
        workBook = new HSSFWorkbook();
        currentWorkSheet = workBook.createSheet(_DEFAULT_SHEET_NAME);
    }
    
    public WorkBookBox(boolean createNewSheet) {
    	workBook = new HSSFWorkbook();
    	if(createNewSheet) {
            currentWorkSheet = workBook.createSheet(_DEFAULT_SHEET_NAME);
    	}
    }
    public WorkBookBox(String filePath) throws IOException, FileNotFoundException {
        this(new File(filePath));
    }

    public WorkBookBox(File file) throws IOException, FileNotFoundException {
        this(new FileInputStream(file));
    }

    public WorkBookBox(InputStream stream) throws IOException {
        workBook = new HSSFWorkbook(stream);
    }

    // -------------------------- writeCellData and loadCellData methods. --------------------------
    public void addNewSheet(String sheetName) {
    	currentWorkSheet = workBook.getSheet(sheetName);
    	if(currentWorkSheet == null) {
    		currentWorkSheet = workBook.createSheet(sheetName);
    	}
    }
    
    public void write2OutputStream(OutputStream outputStream) throws IOException {
    	workBook.write(outputStream);
    }
    
    @SuppressWarnings("deprecation")
    private int createMergedRegion(int startRow, int startCol, int endRow, int endCol) {
    	CellRangeAddress cellRangeAddress = new CellRangeAddress(startRow, endRow, startCol, endCol);
    	
		return currentWorkSheet.addMergedRegion(cellRangeAddress);
    }
    
    public void writeRegion(int startRow, int startCol, int endRow, int endCol, String cellText, CellStyle cellStyle) {    	
    	for(int i = startRow; i <= endRow; i ++) {
    		for(int j = startCol; j <= endCol; j ++) {
    			setCellStyle(i, j, cellStyle);
    		}
    	}
    	//
    	this.createMergedRegion(startRow, startCol, endRow, endCol);
    	writeCellData(startRow, startCol, cellText, cellStyle, String.class);
    }
    
    private HSSFRow loadRow(int rowIndex) {
    	HSSFRow dataRow = currentWorkSheet.getRow(rowIndex);
    	if(dataRow == null) {
    		dataRow = currentWorkSheet.createRow(rowIndex);
    	}
    	return dataRow;
    }
    
    private HSSFCell loadCell(int rowIndex, int colIndex) {
    	HSSFRow dataRow = loadRow(rowIndex);
    	//
    	HSSFCell dataCell = dataRow.getCell(colIndex);
    	if(dataCell == null) {
    		dataCell = dataRow.createCell(colIndex);
    	}
    	return dataCell;
    	
    }
    public void setCellStyle(int rowIndex, int colIndex, CellStyle cellStyle) {
    	HSSFRow dataRow = loadRow(rowIndex);
    	
    	HSSFCell dataCell = loadCell(rowIndex, colIndex);
    	//
    	if(cellStyle.getHeight() != -1) {
    		dataRow.setHeight((short)cellStyle.getHeight());
    	}    	
    	dataCell.setCellStyle(cellStyle.load2HssfCellStyle(workBook));
    }
    
    public String loadSumFormulaTxt(int startRow, int startCol, int endRow, int endCol) {
    	StringBuffer buffer = new StringBuffer("SUM(");
    	buffer.append(colIndex2Name(startCol));
    	buffer.append(startRow);
    	buffer.append(":");
    	buffer.append(colIndex2Name(endCol));
    	buffer.append(endRow);
    	buffer.append(")");
    	return buffer.toString();
    }
    
    public void writeFormulaCellData(int rowIndex, int colIndex, String formulaTxt, CellStyle cellStyle) {
    	HSSFCell dataCell = loadCell(rowIndex, colIndex);
    	dataCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
    	dataCell.setCellStyle(cellStyle.load2HssfCellStyle(workBook));
    	
    	dataCell.setCellFormula(formulaTxt);
    }
    
    public void writeCellData(int rowIndex, int colIndex, String cellText, CellStyle cellStyle) {
    	this.writeCellData(rowIndex, colIndex, cellText, cellStyle, String.class);
    }
    
    public void writeCellData(int rowIndex, int colIndex, String cellText, CellStyle cellStyle, Class cellType) {
    	HSSFCell dataCell = loadCell(rowIndex, colIndex);
    	if(cellType.equals(String.class)) {
    		
    		dataCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		dataCell.setCellValue(new HSSFRichTextString(cellText));
    	}
    	else if(cellType.equals(Integer.class) || cellType.equals(Double.class)) {
    		dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    		dataCell.setCellValue(new Double(cellText));
    	}
    	else {
    		dataCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
    		dataCell.setCellValue(new HSSFRichTextString(cellText));
    	}
    	//
    	dataCell.setCellStyle(cellStyle.load2HssfCellStyle(workBook));
    }
    
    public void writeCellData(List<Object> dataList, WorkSheetInfo workSheetInfo)  throws Exception {
    	Set<ColumnInfo> columnInfoSet = workSheetInfo.getColumnInfoList();

        int rowIndex = 0;
        if(workSheetInfo.isWithTitle()) {
            HSSFRow headRow = currentWorkSheet.createRow(rowIndex++);
            
            HSSFCell headCell;
            for(ColumnInfo columnInfo:columnInfoSet) {
                headCell = headRow.createCell(columnInfo.getIndex());
                headCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                headCell.setCellStyle(this.getHeadCellStyle());
                headCell.setCellValue(new HSSFRichTextString(columnInfo.getTitle()));
            }
            //
            if(log.isDebugEnabled()) {
                log.debug("Write head title ok!");
            }
        }

        //
        String valueStr = null;
        HSSFRow dataRow = null;
        HSSFCell dataCell = null;
        
        Object valueObj = null;
        for(Object dataObj:dataList) {
            dataRow = currentWorkSheet.createRow(rowIndex++);
            for(ColumnInfo columnInfo:columnInfoSet) {
                dataCell = dataRow.createCell(columnInfo.getIndex());
                //
                valueObj = PropertyUtils.getProperty(dataObj, columnInfo.getPropertyName());
                if(valueObj != null) {
                    valueStr = String.valueOf(valueObj);
                }
                else {
                    valueStr = "";
                }
                //
                if(CellType.Numeric.isSame(columnInfo.getExcelType())) {
                    dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    //
                    Integer valueNum = null; 
                    try {
                        valueNum = Integer.valueOf(valueStr.trim());
                    }
                    catch(Exception ex) {
                        valueNum = null;
                    }
                    //
                    if(valueNum != null) {
                        dataCell.setCellValue(valueNum);
                    }
                    else {
                        dataCell.setCellValue(valueStr);
                    }
                }
                else {
                    dataCell.setCellType(HSSFCell.CELL_TYPE_STRING); 
                    dataCell.setCellValue(new HSSFRichTextString(valueStr));//
                }
                //
                dataCell = null;
            }
            if(log.isDebugEnabled() && rowIndex%500 == 0) {
                log.debug("Write rowIndex:" + rowIndex + " ok!");
            }
            //
            dataRow = null;
            
        }
    }
    /**
     * writeCellData
     * 
     * @param dataList
     * @param workSheetInfo
     * @param outputStream
     * @throws Exception
     */
    public void writeCellData(List<Object> dataList, WorkSheetInfo workSheetInfo, OutputStream outputStream) throws Exception {
        this.writeCellData(dataList, workSheetInfo);
        //
        workBook.write(outputStream);
        //
        if(log.isDebugEnabled()) {
            log.debug("Write into outputStream ok!");
        }
    }
    
    /**
     * loadCellData
     * 
     * @return
     */
    public List<CellData[]> loadCellData(int columnCount) {
        return this.loadCellData(columnCount, 0);
    }
    
    public List<CellData[]> loadCellData(int columnCount, int rowStartIndex) {
        return this.loadCellData(0, columnCount, rowStartIndex);
    }
    
    
    /**
     * loadCellData
     * @param workSheetIndex
     * @return
     */
    public List<CellData[]> loadCellData(int workSheetIndex, int columnCount, int rowStartIndex) {
        List<CellData[]> cellDataList = new LinkedList<CellData[]>();
        HSSFSheet workSheet = workBook.getSheetAt(workSheetIndex);
        
        CellData[] cellDataArr = null;
        int rowCount = workSheet.getLastRowNum();
        HSSFCell cell = null;
        HSSFRow row  = null;
        //
        for (int rowIndex = rowStartIndex; rowIndex <= rowCount; rowIndex++) {
            row = workSheet.getRow(rowIndex);
            if (row != null) {
                cellDataArr = new CellData[columnCount];
                //
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    cell = row.getCell(columnIndex);
                    if(cell == null) {
                    	cellDataArr[columnIndex] = new CellData();
                    	//
                        continue;
                    }
                    cellDataArr[columnIndex] = this.buildCellData(cell);
                }
                //
                cellDataList.add(cellDataArr);
            }
        }        
        return cellDataList;
    }
    
    private CellData buildCellData(HSSFCell cell) {
        CellData cellData = new CellData();
        //
        cellData.setRowIndex(cell.getRowIndex());
        cellData.setColumnIndex(cell.getColumnIndex());
        //
        int cellType = cell.getCellType();
        
        //
        switch (cellType) {
            case HSSFCell.CELL_TYPE_STRING: {
                cellData.setValueStr(cell.getRichStringCellValue().getString());
                cellData.setType(CellType.String);
                break;
            }
            case HSSFCell.CELL_TYPE_NUMERIC: {
                //
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    double cellNumeric = cell.getNumericCellValue();
                    Date date = HSSFDateUtil.getJavaDate(cellNumeric);
                    cellData.setValueStr(DateUtils.getDate14FromDate(date));
                    cellData.setType(CellType.Date);
                }
                else {
                    String decimalFmtTxt = "#";
                    //
                    Double doubleVal = cell.getNumericCellValue();
                    if(doubleVal - doubleVal.intValue() != 0) {
                        decimalFmtTxt = "#.###############";
                    }
                    //
                    DecimalFormat decimalFormat = new DecimalFormat(decimalFmtTxt);
                    cellData.setValueStr(decimalFormat.format(doubleVal));
                    cellData.setType(CellType.Numeric);
                }
                
                break;
            }
            case HSSFCell.CELL_TYPE_FORMULA: {
                String valueStr = "";
                try {
                    // // if the type is CELL_TYPE_FORMULA, get the string cell value directly.
                    valueStr = cell.getRichStringCellValue().getString();
                }
                catch(IllegalStateException ex) {
                    log.warn(MessageFormat.format("Unsupport cell formula type at row:{0}, column{1}. Translate it with string type.", 
                            cell.getRowIndex(), cell.getColumnIndex())); 
                }
                //
                cellData.setValueStr(valueStr); 
                // and set the cell type to be string.
                cellData.setType(CellType.String);

                break;
            }
            case HSSFCell.CELL_TYPE_BLANK: {
                cellData.setValueStr("");
                cellData.setType(CellType.Blank);
                break;
            }
            case HSSFCell.CELL_TYPE_BOOLEAN: {
                cellData.setValueStr(String.valueOf(cell.getBooleanCellValue()));
                cellData.setType(CellType.Boolean);
                break;
            }
            case HSSFCell.CELL_TYPE_ERROR: {
                cellData.setValueStr(null);
                cellData.setType(CellType.Error);
                break;
            }
            default : {
                String errorInfo = "Unkown cell type: {0} at row:{1}, column{2}.";
                log.warn(MessageFormat.format(errorInfo, cellType, cell.getRowIndex(), cell.getColumnIndex()));
            }
        }
        //
        return cellData;
    }
}
