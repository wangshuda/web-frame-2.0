package com.cintel.frame.poi.excel.export;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.poi.excel.ExcelService;
import com.cintel.frame.poi.excel.hssf.WorkBookBox;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.web.page.PageInfo;
import com.cintel.frame.web.page.PagedList;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-11-11 wangshuda created
 */
public class ExportServiceImpl implements ExportService {
	private static Log log = LogFactory.getLog(ExportServiceImpl.class);
	//
	private int perSheetRowsCnt = 1000;
	
	private int maxSheetCntInFile = 10;
	
	private ExcelService excelService;
	
	private ExportQueryService queryService;
	
	private String fileNamePattern;
	
	public String getFileName(FileType fileType) {
		return String.format(fileNamePattern, "All", fileType.getFileSuffix());
	}
	
	public FileType buildFileType(Object queryConditinObj) {
		return this.buildFileType(perSheetRowsCnt, maxSheetCntInFile, queryConditinObj);
	}
	
	public FileType buildFileType(String perPageSizeStr, String maxSheetCntInFileStr, Object queryConditinObj) {
		int perSheetRowsCntPara = perSheetRowsCnt;
		if(StringUtils.hasText(perPageSizeStr)) {
			perSheetRowsCntPara = Integer.valueOf(perPageSizeStr);
		}
		int maxSheetCntInFilePara = maxSheetCntInFile;
		if(StringUtils.hasText(maxSheetCntInFileStr)) {
			maxSheetCntInFilePara = Integer.valueOf(maxSheetCntInFileStr);
		}
		return this.buildFileType(perSheetRowsCntPara, maxSheetCntInFilePara, queryConditinObj);
	}
	
	private FileType buildFileType(int perSheetRowsCnt, int maxSheetCntInFile, Object queryConditinObj) {
		int totalCount = this.getQueryService().totalItemCnt(queryConditinObj);
		if(totalCount <= perSheetRowsCnt*maxSheetCntInFile) {
			return FileType.Excel;
		}
		else {
			return FileType.Zip;
		}
	}
	
	@SuppressWarnings("unchecked")
	private FileType doExport(int perSheetRowsCnt, int maxSheetCntInFile, String fileNamePattern, Object queryCondition, OutputStream outStream) throws Exception {
		//
		if(perSheetRowsCnt > 5000) {
			log.warn("Max supported rows count at SMP is 5000!!!");
		}
		//
		FileType rtnFileType = null;
		int targetPage = 1;
		PageInfo pageInfo = new PageInfo(targetPage, perSheetRowsCnt);
		//
		PagedList pagedList = this.getQueryService().pageSearch(pageInfo, queryCondition);
		int totalCount = pagedList.getTotalCount();
		pageInfo.setTotalCount(totalCount);
		//	
		List dataList = pagedList.getPageDataList();

		int pageCount = pageInfo.getPageCount();
		int excelFileCnt = pageCount/maxSheetCntInFile + (pageCount%maxSheetCntInFile == 0 ? 0 : 1);
		//
		WorkBookBox workBookBox = null;
		if(excelFileCnt == 1) {
			workBookBox = new WorkBookBox(false);
			int i = 1;
			for(targetPage = 0 ; targetPage < pageCount && i <= maxSheetCntInFile; targetPage ++) {
				pageInfo.setTargetPage(targetPage + 1);
				//
				pagedList = this.getQueryService().pageSearch(pageInfo, queryCondition);
				dataList = pagedList.getPageDataList();
				this.getExcelService().doMultiSheetExport(workBookBox, "sheet" + targetPage, dataList);
				//
				dataList.clear();
				dataList = null;
				pagedList = null;
				//
				i ++;
			}
			workBookBox.write2OutputStream(outStream);
			rtnFileType = FileType.Excel;
		}
		else {
			ZipOutputStream zipOutStream = new ZipOutputStream(outStream);
			try {
				for(int excelFileIndex = 0; excelFileIndex < excelFileCnt; excelFileIndex++) {
					workBookBox = new WorkBookBox(false);
					// "ctiCallStat-" + excelFileIndex + ".xls"
					zipOutStream.putNextEntry(new ZipEntry(String.format(fileNamePattern, excelFileIndex, FileType.Excel.getFileSuffix())));
					
					int i = 1;
					for(targetPage = (excelFileIndex*maxSheetCntInFile) ; targetPage < pageCount && i <= maxSheetCntInFile; targetPage ++) {
						pageInfo.setTargetPage(targetPage + 1);
						//
						pagedList = this.getQueryService().pageSearch(pageInfo, queryCondition);
						dataList = pagedList.getPageDataList();
						this.getExcelService().doMultiSheetExport(workBookBox, "sheet" + targetPage, dataList);
						//
						dataList.clear();
						dataList = null;
						pagedList = null;
						//
						i ++;
					}
					workBookBox.write2OutputStream(zipOutStream);
					zipOutStream.closeEntry();
					//
					workBookBox = null;
				}
				rtnFileType = FileType.Zip;
			}
			finally{
				zipOutStream.flush();
				zipOutStream.close();			
			}
		}
		return rtnFileType;		
	}
	
	public FileType doExport(String perPageSizeStr, String maxSheetCntInFileStr, String fileNamePatternStr, Object queryCondition, OutputStream outStream) throws Exception {
		int perSheetRowsCntPara = perSheetRowsCnt;
		if(StringUtils.hasText(perPageSizeStr)) {
			perSheetRowsCntPara = Integer.valueOf(perPageSizeStr);
		}
		int maxSheetCntInFilePara = maxSheetCntInFile;
		if(StringUtils.hasText(maxSheetCntInFileStr)) {
			maxSheetCntInFilePara = Integer.valueOf(maxSheetCntInFileStr);
		}		
		//
		String fileNamePatternPara = fileNamePattern;
		if(StringUtils.hasText(fileNamePatternStr)) {
			fileNamePatternPara = fileNamePatternStr;
		}
		
		return this.doExport(perSheetRowsCntPara, maxSheetCntInFilePara, fileNamePatternPara, queryCondition, outStream);
	}
	
	@SuppressWarnings("unchecked")
	public FileType doExport(Object queryCondition, OutputStream outStream) throws Exception {
		return this.doExport(perSheetRowsCnt, maxSheetCntInFile, fileNamePattern, queryCondition, outStream);
	}
	
	public void doCsvExport(String perPageSizeStr, String fileNamePatternStr, Object queryCondition, OutputStream outStream) throws Exception {
		int perSheetRowsCntPara = perSheetRowsCnt;
		if (StringUtils.hasText(perPageSizeStr)) {
			perSheetRowsCntPara = Integer.valueOf(perPageSizeStr);
		}
		//
		String fileNamePatternPara = fileNamePattern;
		if (StringUtils.hasText(fileNamePatternStr)) {
			fileNamePatternPara = fileNamePatternStr;
		}

		this.doCsvExport(perSheetRowsCntPara, fileNamePatternPara, queryCondition, outStream);
	}
	
	public void doCsvExport(Object queryCondition, OutputStream outStream) throws Exception {
		this.doCsvExport(perSheetRowsCnt, fileNamePattern, queryCondition, outStream);		
	}

	@SuppressWarnings("unchecked")
	private void doCsvExport(int perSheetRowsCnt, String fileNamePattern, Object queryCondition, OutputStream outStream) throws Exception {
		if (perSheetRowsCnt > 5000) {
			log.warn("Max supported rows count at SMP is 5000!!!");
		}
		//
		int targetPage = 1;
		PageInfo pageInfo = new PageInfo(targetPage, perSheetRowsCnt);
		//
		PagedList pagedList = this.getQueryService().pageSearch(pageInfo, queryCondition);	

		int totalCount = pagedList.getTotalCount();
		pageInfo.setTotalCount(totalCount);
		//	
		
        int pageCount = pageInfo.getPageCount();
        
        log.info(String.format("pageCount = %d", pageCount));
        //
        List dataList = null;
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outStream));
		for (targetPage = 0; targetPage < pageCount; targetPage++) {
			pageInfo.setTargetPage(targetPage + 1);
			//
			pagedList = this.getQueryService().pageSearch(pageInfo, queryCondition);
			dataList = pagedList.getPageDataList();
			this.getExcelService().doExport(dataList, bufferedWriter ,targetPage);
			//
			dataList.clear();
			dataList = null;
			pagedList = null;
			//
		}		
		bufferedWriter.close();
	}

	// ------------------------------- get/set methods ----------------------------------
	public ExcelService getExcelService() {
		return excelService;
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	public ExportQueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(ExportQueryService queryService) {
		this.queryService = queryService;
	}

	public int getMaxSheetCntInFile() {
		return maxSheetCntInFile;
	}

	public void setMaxSheetCntInFile(int maxSheetCntInFile) {
		this.maxSheetCntInFile = maxSheetCntInFile;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public int getPerSheetRowsCnt() {
		return perSheetRowsCnt;
	}

	public void setPerSheetRowsCnt(int perSheetRowsCnt) {
		this.perSheetRowsCnt = perSheetRowsCnt;
	}
	
}
