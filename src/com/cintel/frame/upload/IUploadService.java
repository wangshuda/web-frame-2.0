package com.cintel.frame.upload;

import java.io.InputStream;
import java.io.OutputStream;

public interface IUploadService {
	/**
	 * createDir
	 * 
	 * @param filePath
	 */
	public void createDir(String filePath);
	
	/**
	 * rename
	 * 
	 * @param filePath
	 * @param fileName
	 * @param newFileName
	 */
	public void rename(String filePath, String fileName, String newFileName);
	
	/**
	 * listResourceInfo
	 * 
	 * @param filePath
	 * @return: if do not exist return empty array.
	 */
	public ResourceInfo[] listResourceInfo(String filePath);
	
	/**
	 * 
	 * @param pathLocation
	 * @param includeNamesPattern
	 * @return
	 */
	public ResourceInfo[] listResourceInfo(String pathLocation, String[] includeNamesPattern);
	
	/**
	 * findResourceInfo
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public ResourceInfo findResourceInfo(String filePath, String fileName);
	
	/**
	 * findResourceInfo
	 * 
	 * @param fileName
	 * @return
	 */
	public ResourceInfo findResourceInfo(String fileName);
	
	/**
	 * 
	 * @param in
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public int save(InputStream in, String filePath, String fileName);
	
	public int save(InputStream in, String filePath, String fileName, long size);
	
	/**
	 * 
	 * @param in
	 * @param fileName
	 * @return
	 */
	public int save(InputStream in, String fileName);
	
	/**
	 * replace with 'save(InputStream in, String filePath, String fileName)'
	 * 
	 * @param fileData
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	@Deprecated
	public int save(byte[] fileData, String filePath, String fileName);

	/**
	 * replace with 'save(InputStream in, String fileName)'
	 * 
	 * @param fileData
	 * @param fileName
	 * @return
	 */
	@Deprecated
	public int save(byte[] fileData, String fileName);

	/**
	 * delete
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public int delete(String filePath, String fileName);

	/**
	 * delete
	 * 
	 * @param fileName
	 * @return
	 */
	public int delete(String fileName);

	/**
	 * replace with 'get(String filePath, String fileName, OutputStream out)'
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	@Deprecated 
	public InputStream get(String filePath, String fileName);

	/**
	 * replace with 'replace get(String fileName, OutputStream out)'
	 * 
	 * @param fileName
	 * @return
	 */
	@Deprecated 
	public InputStream get(String fileName);
	
	/**
	 * 
	 * @param fileName
	 * @param out
	 */
	public void get(String fileName, OutputStream out);
	
    public String getTargetFileURL(String filePath, String fileName, boolean endWithUrlSeperator);
    
	public void get(String filePath, String fileName, OutputStream out);
	
	public void getByURL(String targetFileURL, OutputStream out);
	
    public void getByURL(String targetFileURL, OutputStream out, boolean autoClose);
    
    public ResourceInfo loadResourceCtx(String targetFileURL);
    
 
	/**
	 * getSuffixedFileName
	 * 
	 * @param fileName
	 * @return
	 */
	public String getSuffixedFileName(String fileName);

	/**
	 * isExist
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isExist(String fileName);
	
	/**
	 * 
	 * @param filePath
	 * @param fileName
	 * @param startIndex: startIndex > endIndex is necessary.
	 * @param endIndex
	 * @return
	 */
	public String range(String filePath, String fileName, int startIndex, int endIndex);
}
