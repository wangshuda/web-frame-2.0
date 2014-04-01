package com.cintel.frame.poi.excel.export;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-11-11 wangshuda created
 */
public enum FileType {
	Excel("xls", "application/vnd.ms-excel")
	,Zip("zip", "application/zip")
	;
	
	private String fileSuffix;
	
	private String contentType;
	
	private FileType(String fileSuffix, String contentType) {
		this.fileSuffix = fileSuffix;
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}
	
}
