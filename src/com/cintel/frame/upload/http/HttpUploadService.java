package com.cintel.frame.upload.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.webdav.lib.WebdavResource;
import org.springframework.util.Assert;

import com.cintel.frame.net.http.HttpMethodExecutor;
import com.cintel.frame.net.http.auth.HttpAuthCtx;
import com.cintel.frame.upload.IUploadService;
import com.cintel.frame.upload.ResourceInfo;
import com.cintel.frame.util.DateUtils;
import com.cintel.frame.util.FileUtils;
import com.cintel.frame.util.MyAntPathMatcher;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;

/**
 * @version: $Id: HttpUploadService.java 39972 2013-06-13 08:57:44Z wangshuda $
 * @history: 
 * 1.0.0  2009/12/03 wangshuda created. depends the webdav, to see
 * 1.0.1  2009/12/08 wangshuda 
 * 					 1) modified the method 'listResourceInfo', if do not exist the dir/url, return empty array. 
 *                   2) modify the method 'save', using InputStreamRequestEntity to put file.
 *                   3) added the method 'range'.
 *                   4) modify the method 'get', return the inputstream can be read and closed after returning. 
 * 1.0.2  2009/12/17 wangshuda using the 'httpMethodExecutor' to do http request.
 * 1.0.3  2010/01/12 wangshuda construct InputStreamRequestEntity with the file size, or else can not put super max file.
 * 1.0.4  2011/12/19 wangshuda skip the ClientAbortException.
 * .
 */
public class HttpUploadService implements IUploadService {
	private static Log log = LogFactory.getLog(HttpUploadService.class);
	
	private final static String _URL_SEPERATOR = "/";
	
	/**
	 * 
	 * statusCode == HttpStatus.SC_OK
					|| statusCode == HttpStatus.SC_NO_CONTENT
					|| statusCode == HttpStatus.SC_NOT_FOUND
					|| statusCode == HttpStatus.SC_CREATED
					|| statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_PARTIAL_CONTENT
					|| statusCode == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE
	 */
	public HttpMethodExecutor httpMethodExecutor;
	
	public HttpUploadService(HttpMethodExecutor httpMethodExecutor) {
		httpMethodExecutor.addEnableStatusCode(new int[]{HttpStatus.SC_OK, HttpStatus.SC_NO_CONTENT, HttpStatus.SC_NOT_FOUND
					,HttpStatus.SC_CREATED, HttpStatus.SC_MOVED_PERMANENTLY, HttpStatus.SC_PARTIAL_CONTENT, HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE });
		this.setHttpMethodExecutor(httpMethodExecutor);
	}
	
	public void checkAndCreateURL(String filePath) {
		if(log.isDebugEnabled()) {
			log.debug(MessageFormat.format("checkAndCreateURL with filePath:{0}", filePath));
		}
		
		String targetPathURL = this.getTargetFileURL(filePath, null, true);
		List<String> subPathNameList = new LinkedList<String>();
		this.createPath(targetPathURL, subPathNameList);
	}

	public int save(InputStream in, String filePath, String fileName, long size) {
		if(log.isDebugEnabled()) {
			log.debug(MessageFormat.format("save with filePath:{0}, fileName:{1}", filePath, fileName));
		}
		//
		String targetFileUrl = this.getTargetFileURL(filePath, fileName, false);
		//
		this.checkAndCreateURL(filePath);
		//
		PutMethod httpPutMethod = new PutMethod(targetFileUrl);
		//
		httpPutMethod.setRequestEntity(new InputStreamRequestEntity(in));
		//
		log.debug("PutMethod");
		return httpMethodExecutor.executeMethod(httpPutMethod);
	}

	public void get(String filePath, String fileName, OutputStream out) {
		if(log.isDebugEnabled()) {
			log.debug(MessageFormat.format("get with filePath:{0}, fileName:{1}", filePath, fileName));
		}
		
		String targetFileURL = getTargetFileURL(filePath, fileName, false);
		
		if(log.isDebugEnabled()) {
			log.debug("Get file at : " + targetFileURL);
		}
		
		this.getByURL(targetFileURL, out);
	}
	

    public long loadSourceSize(String targetFileURL) { 
        WebdavResource webDavResource = this.loadWebDavResource(targetFileURL);
        
        long rtnSize = webDavResource.getGetContentLength();
        
        return rtnSize;
    }
    
    

    public void getByURL(String targetFileURL, OutputStream out, boolean autoClose) {
        HttpMethod httpMethod = new GetMethod(targetFileURL);
        int statusCode = httpMethodExecutor.executeMethod(httpMethod, false);
        try {
            //
            if (statusCode == HttpStatus.SC_OK) {
                FileUtils.copy(httpMethod.getResponseBodyAsStream(), out, autoClose);
            }
        }
        catch(SocketException ex) {
            log.error(targetFileURL, ex);
            throw new ErrorReportException(new ErrorInfo("error.HttpUploadService", new String[]{httpMethod.getName(), targetFileURL}));
        }
        catch (IOException ex) {
            ByteArrayOutputStream exMsgOut = new ByteArrayOutputStream();
            ex.printStackTrace(new PrintStream(exMsgOut));
            String exMsgStr = new String(exMsgOut.toByteArray());
            //
            if(exMsgStr.indexOf("ClientAbortException") >= 0) {
                log.warn("ClientAbortException-->" + targetFileURL);
            }
            else {
                log.error(targetFileURL, ex);
                throw new ErrorReportException(new ErrorInfo("error.HttpUploadService", new String[]{httpMethod.getName(), targetFileURL}));
            }
        }
        finally {
            httpMethod.releaseConnection();
            //
            httpMethod = null;
        }
    }
    
	public void getByURL(String targetFileURL, OutputStream out){
		this.getByURL(targetFileURL, out, true);
	}
	
	public void get(String fileName, OutputStream out) {
		this.get(null, fileName, out);
	}
	
	public InputStream get(String filePath, String fileName) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.get(filePath, fileName, out);
		return new ByteArrayInputStream(out.toByteArray());
	}

    
	public int delete(String filePath, String fileName) {
		if(log.isDebugEnabled()) {
			log.debug(MessageFormat.format("Delete with filePath:{0}, fileName:{1}", filePath, fileName));
		}
		//
		String targetFileURL = this.getTargetFileURL(filePath, fileName, false);
		
		if(log.isDebugEnabled()) {
			log.debug("Delete file at : " + targetFileURL);
		}
		//
		HttpMethod httpMethod = new DeleteMethod(targetFileURL);
		int statusCode = httpMethodExecutor.executeMethod(httpMethod);
		if(statusCode == HttpStatus.SC_NO_CONTENT || statusCode == HttpStatus.SC_NOT_FOUND) {
			return 0;
		}
		else {
			log.warn("Response error with: " + statusCode);
			throw new ErrorReportException(new ErrorInfo("error.HttpUploadService" , new String[]{httpMethod.getName(), targetFileURL}));
		}
	}

	public boolean isExist(String filePath, String fileName) {
		if(log.isDebugEnabled()) {
			log.debug(MessageFormat.format("isExist with filePath:{0}, fileName:{1}", filePath, fileName));
		}
		
		String targetFileURL = this.getTargetFileURL(filePath, fileName, false);
		//
		HttpMethod httpMethod = new GetMethod(targetFileURL);
		try {
			int statusCode = httpMethodExecutor.executeMethod(httpMethod);
			return (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_MOVED_PERMANENTLY);
		}
		catch(Exception ex) {
			log.warn("", ex);
			return false;
		}
	}

	private boolean isExistURL(String targetPathURL) {
		StringBuffer buffer = new StringBuffer(targetPathURL);
		// if do not link with the _URL_SEPERATOR("/"), the request received the 301 status code,
		// the reality url saved in the resopns options named with "Location", which is the request url added the _URL_SEPERATOR
		// so check it firstly, if not then linked the _URL_SEPERATOR after of the request url.
		if(!targetPathURL.endsWith(_URL_SEPERATOR)) {
			buffer.append(_URL_SEPERATOR); 
		}
		//
		HttpMethod httpMethod = new GetMethod(buffer.toString());
		try {
			int statusCode = httpMethodExecutor.executeMethod(httpMethod);
			
			return (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_MOVED_PERMANENTLY);
		}
		catch(Exception ex) {
			log.warn("", ex);
			return false;
		}
	}
	
	private void createPath(String targetFileURL, List<String> subPathNameList) {
		if(targetFileURL.endsWith(_URL_SEPERATOR)) {
			targetFileURL.substring(0, targetFileURL.length() - 1);
		}
		
		int lastUrlSerperatorIndex = targetFileURL.lastIndexOf(_URL_SEPERATOR);

		String parentPathURL = targetFileURL.substring(0, lastUrlSerperatorIndex);
		
		String subPathName = targetFileURL.substring(lastUrlSerperatorIndex + 1);
		//
		if(StringUtils.hasText(subPathName)) {
			subPathNameList.add(0, subPathName);
		}
		//
		boolean existTargetParent = this.isExistURL(parentPathURL);
		
		if(existTargetParent) {
			String targetPathURL = parentPathURL;
			StringBuffer buffer = new StringBuffer(targetPathURL);
			for(String nextSubPathNameInList:subPathNameList) {
				buffer.append(_URL_SEPERATOR);
				buffer.append(nextSubPathNameInList);
				//
				MkcolMethod httpMkcolMethod = new MkcolMethod(buffer.toString());
				httpMethodExecutor.executeMethod(httpMkcolMethod);
			}
		}
		else {
			this.createPath(parentPathURL, subPathNameList);
		}
	}
	public void createDir(String filePath) {
		String targetFileUrl = this.getTargetFileURL(filePath, null, true);
		//
		this.checkAndCreateURL(targetFileUrl);
	}

	/**
	 * Depends the webdav in slide.
	 */
	public ResourceInfo findResourceInfo(String filePath, String fileName) {
		ResourceInfo fileInfo = new ResourceInfo();
		fileInfo.setName(fileName);
		fileInfo.setPathLocation(filePath);
		//
		String targetFileURL = this.getTargetFileURL(filePath, fileName, false);
		
		WebdavResource webDavResource = this.loadWebDavResource(targetFileURL);
		return this.webDavResource2Info(webDavResource);
	}

	public ResourceInfo findResourceInfo(String fileName) {
		return this.findResourceInfo(null, fileName);
	}

    public ResourceInfo loadResourceCtx(String targetFileURL) {
        WebdavResource webDavResource = this.loadWebDavResource(targetFileURL);
        return this.webDavResource2Info(webDavResource);
    }
    
	/**
	 * Depends the webdav in slide.
	 */
	@SuppressWarnings("deprecation")
    private WebdavResource loadWebDavResource(String targetURL) {
		try {
			HttpAuthCtx authCtx = httpMethodExecutor.getAuthCtx();
			if(authCtx != null) {
				Credentials cred = new UsernamePasswordCredentials(authCtx.getUserName(), authCtx.getUserPwd());
				//
				return new WebdavResource(targetURL, cred);
			}
			else {
				return new WebdavResource(targetURL);
			}
		}
        catch(HttpException httpEx) {
            log.error("loadWebdavResource error!", httpEx);
            throw new ErrorReportException("error.HttpUploadService.loadWebdavResource", MessageFormat.format("{0}-->{1}", httpEx.getReasonCode(), targetURL));
        }
		catch (Exception ex) {
			log.error("loadWebdavResource error!", ex);
			throw new ErrorReportException(new ErrorInfo("error.HttpUploadService.loadWebdavResource", new String[]{targetURL}));
		}
	}
	
	/**
	 * Depends the webdav in slide.
	 */
	private ResourceInfo webDavResource2Info(WebdavResource webDavResource) {
        ResourceInfo resourceInfo = null;
        if(webDavResource != null) {
            resourceInfo = new ResourceInfo();
            
            resourceInfo.setContentType(webDavResource.getGetContentType());
            resourceInfo.setName(webDavResource.getDisplayName());
            resourceInfo.setSize(webDavResource.getGetContentLength());
            //
            resourceInfo.setLastModified(DateUtils.getDate14FromDate((new Date(webDavResource.getGetLastModified()))));
            resourceInfo.setCreatedDateTime(DateUtils.getDate14FromDate((new Date(webDavResource.getCreationDate()))));
            //
            resourceInfo.setTargetURL(webDavResource.getHttpURL().toString());
            
            resourceInfo.setDirectory(webDavResource.isCollection()); 
        }

		//
		return resourceInfo;
	}
	
	public ResourceInfo[] listResourceInfo(String pathLocation, String[] includeNamesPattern) {
		
		String targetPathURL = this.getTargetFileURL(pathLocation, null, true);
		
		if(log.isDebugEnabled()) {
			log.debug("listResourceInfo:" + targetPathURL);
		}
		
		boolean exist = this.isExistURL(targetPathURL);
		if(!exist) {
			log.warn("can not find:" + targetPathURL);
			//
			return new ResourceInfo[0];
		}
		//
		WebdavResource parentWebDavResource = this.loadWebDavResource(targetPathURL);
		if(!parentWebDavResource.isCollection()) {
			throw new ErrorReportException("error.HttpUploadService.listResourceInfo");
		}
		else {
			WebdavResource[] subWebDavResourceArr = null;
			try {
				if(parentWebDavResource.exists()) {
					subWebDavResourceArr = parentWebDavResource.listWebdavResources();
				}
				else {
					log.warn("can not find:" + targetPathURL);
				}
			}
			catch (Exception ex) {
				log.error("loadWebdavResource error!", ex);
				throw new ErrorReportException(new ErrorInfo("error.HttpUploadService.listResourceInfo", new String[]{targetPathURL}));
			}
			//
			List<ResourceInfo> resultList = new ArrayList<ResourceInfo>();
			//
			boolean canBeList = false;
			for(WebdavResource subWebDavResource:subWebDavResourceArr) {
				canBeList = this.canBeList(subWebDavResource.getName(), includeNamesPattern);
				//
				if(canBeList) {
					ResourceInfo loogResourceInfo = this.webDavResource2Info(subWebDavResource);
					loogResourceInfo.setPathLocation(pathLocation);
					resultList.add(loogResourceInfo);
				}
			}
			//
			ResourceInfo[] subResourceInfoArr = resultList.toArray(new ResourceInfo[resultList.size()]);
			this.sort(subResourceInfoArr);
			//
			return subResourceInfoArr;
		}
	}
		
	public ResourceInfo[] sort(ResourceInfo[] resourceInfos) {
		ResourceInfo temp;
		if (resourceInfos != null && resourceInfos.length > 0) {
			for (int i = 0; i < resourceInfos.length; i++) {
				for (int j = i + 1; j < resourceInfos.length; j++) {
					String createdTimeI = resourceInfos[i].getCreatedDateTime();
					String createdTimeJ = resourceInfos[j].getCreatedDateTime();
					if (StringUtils.hasText(createdTimeI) && StringUtils.hasText(createdTimeJ)) {						
						if (Long.parseLong(createdTimeI) < Long.parseLong(createdTimeJ)) {
							temp = resourceInfos[i];
							resourceInfos[i] = resourceInfos[j];
							resourceInfos[j] = temp;
						}
					}
				}
			}
		}
		//
		return resourceInfos;
	}
	
	private boolean canBeList(String fileName, String[] includeNamesPattern) {
		boolean result = false;
		MyAntPathMatcher antPath = new MyAntPathMatcher();
		for(String namePattern:includeNamesPattern) {
			if(antPath.matchIgnoreCase(namePattern, fileName)) {
				return true;
			}
		}
		return result;
	}
	/**
	 * Depends the webdav in slide.
	 */
	public ResourceInfo[] listResourceInfo(String pathLocation) {
		return this.listResourceInfo(pathLocation, new String[]{"**"});
	}
	

	public void rename(String filePath, String fileName, String newFileName) {
		if(log.isDebugEnabled()) {
			log.debug(MessageFormat.format("rename {0} {1} to {2}.", filePath, fileName, newFileName));
		}
		//
		String oldFileURL = this.getTargetFileURL(filePath, fileName, false);
		String newFileURL = this.getTargetFileURL(filePath, newFileName, false);
		
		WebdavResource webDavResource = this.loadWebDavResource(oldFileURL);
		try {
			String oldFilePath = webDavResource.getPath();
			String destinationPath = oldFilePath.replace(fileName, newFileName);
			//
			webDavResource.moveMethod(destinationPath);
		}
		catch (Exception ex) {
			log.error("rename error!", ex);
			//
			throw new ErrorReportException(new ErrorInfo("error.HttpUploadService.rename", new String[]{oldFileURL, newFileURL}));
		}
	}
	
	public String range(String filePath, String fileName, int startIndex, int endIndex) {
		Assert.isTrue(startIndex < endIndex, "Be sure startIndex < endIndex!");
		//
		String targetFileURL = this.getTargetFileURL(filePath, fileName, false);
		GetMethod httpGetMethod = new GetMethod(targetFileURL);
		httpGetMethod.addRequestHeader("Range", "bytes=" + startIndex + "-" + endIndex);
		int statusCode = httpMethodExecutor.executeMethod(httpGetMethod, false);
		String resultStr = null;
		if(statusCode == HttpStatus.SC_OK ||
				statusCode == HttpStatus.SC_PARTIAL_CONTENT) {
			try {
				log.debug("Response Content-Range:" + httpGetMethod.getResponseHeader("Content-Range"));
				//
				resultStr = httpGetMethod.getResponseBodyAsString();
			}
			catch (IOException ex) {
				log.error("getResponseBodyAsString error!", ex);
			}
			finally {
				httpGetMethod.releaseConnection();
			}
		}
		return resultStr;
	}

	private String organizeFileSeperator(String filePath) {
		return filePath.replace("\\", _URL_SEPERATOR);
	}

	
	public String getTargetFileURL(String filePath, String fileName, boolean endWithUrlSeperator) {
		StringBuffer buffer = new StringBuffer();
		
		boolean filePathIsEmpty = !StringUtils.hasText(filePath);
		
		String targetURL = httpMethodExecutor.getTargetURL();
		// if the filePath started with the targetURL, means the file path is an file URL
		if(filePathIsEmpty || !filePath.startsWith(targetURL)) {
			buffer.append(targetURL);
		}
		//
		if(!filePathIsEmpty) {
			buffer.append(this.organizeFileSeperator(filePath));

			if(!_URL_SEPERATOR.equals("" + buffer.charAt(buffer.length() - 1))) {
				buffer.append(_URL_SEPERATOR);
			}
		}
		//
		if(StringUtils.hasText(fileName)) {
			if(!_URL_SEPERATOR.equals("" + buffer.charAt(buffer.length() - 1))) {
				buffer.append(_URL_SEPERATOR);
			}
			//
			buffer.append(fileName);
		}
		//
		if(!endWithUrlSeperator && _URL_SEPERATOR.equals("" + buffer.charAt(buffer.length() - 1))) {
			buffer.substring(0, buffer.length() - 1);
		}
		//
		return buffer.toString();
	}
	
	public boolean isExist(String fileName) {
		return this.isExist(null, fileName);
	}
	
	public int delete(String fileName) {
		return this.delete(null, fileName);
	}

	public InputStream get(String fileName) {
		return this.get(null, fileName);
	}
	
	public int save(InputStream in, String fileName) {
		return this.save(in, null, fileName);
	}
	
	public int save(InputStream in, String filePath, String fileName) {
		return this.save(in, filePath, fileName, -2L);
	}
	
	@Deprecated
	public int save(byte[] fileData, String fileName) {
		return this.save(fileData, null, fileName);
	}

	
	@Deprecated
	public int save(byte[] fileData, String filePath, String fileName) {
		return this.save(new ByteArrayInputStream(fileData), filePath, fileName);
	}
	
	public String getSuffixedFileName(String fileName) {
		return fileName;
	}
	// ----------------- get/set methods -----------------
	
	public HttpMethodExecutor getHttpMethodExecutor() {
		return httpMethodExecutor;
	}

	public void setHttpMethodExecutor(HttpMethodExecutor httpMethodExecutor) {
		this.httpMethodExecutor = httpMethodExecutor;
	}

}
