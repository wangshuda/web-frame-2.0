package com.cintel.frame.poi.excel;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.poi.excel.hssf.CellData;
import com.cintel.frame.poi.excel.hssf.WorkBookBox;
import org.springframework.util.StringUtils;
import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;

public class ExcelServiceImpl extends AbstractExcelService implements ExcelService {
    private final Log log = LogFactory.getLog(ExcelServiceImpl.class);
    //
	private String delimitedChar = ",";

    public void doMultiSheetExport(WorkBookBox workBookBox, String newSheetName, List<Object> dataList) throws Exception {
    	workBookBox.addNewSheet(newSheetName);
        workBookBox.writeCellData(dataList, workSheetInfo);
    }
    
    public void doExport(List<Object> dataList, OutputStream outputStream) throws Exception {
        WorkBookBox workBookBox = new WorkBookBox();
        workBookBox.writeCellData(dataList, workSheetInfo, outputStream);
    }
    
   
    @SuppressWarnings("unchecked")
    public List<Object> doImport(InputStream inputStream) throws Exception {
        List<Object> resultList = Collections.EMPTY_LIST;
        
        WorkBookBox workBookBox = new WorkBookBox(inputStream);
      
        List<CellData[]> cellDataList = workBookBox.loadCellData(workSheetInfo.getColumnCount(), workSheetInfo.getRowStartIndex());
        if(cellDataList != null && !cellDataList.isEmpty()) {
            
            if(workSheetInfo.isWithTitle()) {
                int titleIndex = 0;
                this.doTitleCheck(cellDataList.get(titleIndex));
                //
                cellDataList.remove(titleIndex);
            }
            //
            Object dataObj = null;
            if(cellDataList != null && !cellDataList.isEmpty()) {            	
                resultList = new ArrayList<Object>(cellDataList.size());
            }
            //
            int index = 0;
            int errCnt = 0;
            StringBuffer errorMsg = new StringBuffer();
            for(CellData[] cellDataArr : cellDataList){
            	try{
            		index ++;
            		dataObj = workSheetInfo.loadObjFromCellDataArr(cellDataArr);
                    resultList.add(dataObj);
            	}
                catch(ErrorReportException ex){
                    throw ex;
                } 
                catch(Exception ex){
            		errorMsg.append(MessageFormat.format("Parse error occured at row {0}", index));
                    //
                    if(errCnt++ == 0) {
                        log.error(errorMsg.toString(), ex);
                    }
            	}                
            }
            if(errCnt > 0){
    			throw new ErrorReportException(ErrorInfo.newInstance("error.failedLoad", errorMsg.toString()));
    		}
        }
        //
        return resultList;
    }
    

	public void doExport(List<Object> dataList, BufferedWriter bufferedWriter, int targetPage) throws Exception {
		int columnCnt = workSheetInfo.getColumnCount();
		String[] dataArr = new String[columnCnt];
		
		Set<ColumnInfo> columnInfoList = workSheetInfo.getColumnInfoList();
		if(workSheetInfo.isWithTitle() && targetPage == 0) {
			for(ColumnInfo columnInfo:columnInfoList) {
				dataArr[columnInfo.getIndex()] = columnInfo.getTitle();
			}	
			bufferedWriter.write(StringUtils.arrayToDelimitedString(dataArr, delimitedChar));	
			bufferedWriter.newLine();
		}	
        Object valueObj = null;
		for(Object dataObj:dataList) {
			for(ColumnInfo columnInfo:columnInfoList) {
                valueObj = PropertyUtils.getProperty(dataObj, columnInfo.getPropertyName());
                   
                if(valueObj != null) {
                	StringBuffer sb = new StringBuffer();
                	sb.append("\"").append(String.valueOf(valueObj)).append("\"");
                    //dataArr[columnInfo.getIndex()] = String.valueOf(valueObj);
                    dataArr[columnInfo.getIndex()] = sb.toString();
                }
                else {
                    dataArr[columnInfo.getIndex()] = "";
                }
			}	
			bufferedWriter.write(StringUtils.arrayToDelimitedString(dataArr, delimitedChar));	
			bufferedWriter.newLine();
		}
		bufferedWriter.flush();		
	}	
	
	public String getDelimitedChar() {
		return delimitedChar;
	}

	public void setDelimitedChar(String delimitedChar) {
		this.delimitedChar = delimitedChar;
	}

}
