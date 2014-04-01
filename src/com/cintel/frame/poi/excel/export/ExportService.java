package com.cintel.frame.poi.excel.export;

import java.io.OutputStream;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-11-11 wangshuda created
 */
public interface ExportService {
	public String getFileName(FileType fileType);
	
	public FileType doExport(Object queryCondition, OutputStream outStream) throws Exception;
	
	public FileType doExport(String perPageSizeStr, String maxSheetCntInFileStr, String fileNamePattern, Object queryCondition, OutputStream outStream) throws Exception;
	
	public FileType buildFileType(Object queryConditinObj);
	
	public FileType buildFileType(String perPageSizeStr, String maxSheetCntInFileStr, Object queryConditinObj);
	
	public void doCsvExport(Object queryCondition, OutputStream outStream) throws Exception;
	
	public void doCsvExport(String perPageSizeStr, String fileNamePattern, Object queryCondition, OutputStream outStream) throws Exception;
}
