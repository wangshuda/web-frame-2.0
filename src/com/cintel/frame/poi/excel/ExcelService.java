package com.cintel.frame.poi.excel;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.cintel.frame.poi.excel.hssf.WorkBookBox;

public interface ExcelService {
    /**
     * doImport
     * @param inputStream
     * @return
     * @throws Exception
     */
    public List<Object> doImport(InputStream inputStream) throws Exception;
    
    /**
     * doExport
     * 
     * @param dataList
     * @param outputStream
     * @throws Exception
     */
    public void doExport(List<Object> dataList, OutputStream outputStream) throws Exception;
    
    /**
     * 
     * @param workBookBox
     * @param newSheetName
     * @param dataList
     * @param outputStream
     * @throws Exception
     */
    public void doMultiSheetExport(WorkBookBox workBookBox, String newSheetName, List<Object> dataList) throws Exception;
    
    public void doExport(List<Object> dataList, BufferedWriter bufferedWriter, int targetPage) throws Exception;
}
