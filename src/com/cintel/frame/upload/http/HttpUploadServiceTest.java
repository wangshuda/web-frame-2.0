package com.cintel.frame.upload.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.cintel.frame.net.http.HttpMethodExecutor;
import com.cintel.frame.net.http.auth.HttpAuthCtx;
import com.cintel.frame.upload.ResourceInfo;
import com.cintel.frame.util.FileUtils;

/**
 * 
 * @version $Id: HttpUploadServiceTest.java 24437 2011-01-24 08:21:48Z wangshuda $
 * @history 
 *          1.0.0 2009-12-17 wangshuda created
 */
public class HttpUploadServiceTest {
	private Log log = LogFactory.getLog(this.getClass());
	
	private HttpUploadService httpUploadService;
	
	private String parentPath = "vxml-wavs2";
	
	private String subPath = parentPath + "/wsd/";
	
	private File testFile = new File("E:\\outCallTest.txt");
	
	@Before
	public void initial() {
		HttpMethodExecutor httpMethodExecutor = new HttpMethodExecutor();
		
		HttpAuthCtx httpAuthCtx = new HttpAuthCtx();
		httpAuthCtx.setUserName("admin");
		httpAuthCtx.setUserPwd("cinmedia9999");
		// "admin", "cinmedia9999"
		//
		httpMethodExecutor.setAuthCtx(httpAuthCtx);
		//httpMethodExecutor.setTargetURL("http://192.168.2.180:9999/media/");
		httpMethodExecutor.setTargetURL("http://cinmedia:9999/media/");
		httpUploadService = new HttpUploadService(httpMethodExecutor);
	}
	
	//@Test
	public void testDelete() {
		log.debug("testDelete");
		httpUploadService.delete(parentPath, null);
	}
	
	//@Test
	public void testSave() throws IOException {
		log.debug("testSave");
		httpUploadService.save(new FileInputStream(testFile), subPath, testFile.getName());
	}
	
	//@Test
	public void testRange() {
		log.debug("testRange");
		
		String rangeText = httpUploadService.range(subPath, testFile.getName(), 0, 10);
		log.debug("rangeText = " + rangeText);
	}
	
	//@Test
	public void testDownLoad() throws IOException {
		log.debug("testDownLoad");
		
		InputStream in = httpUploadService.get(subPath, testFile.getName());
		
		FileUtils.copyToByteArray(in);
	}
	
	//@Test
	public void testFind() {
		log.debug("testFind");
		//
		ResourceInfo resourceInfo = httpUploadService.findResourceInfo(subPath, testFile.getName());
		log.debug(resourceInfo.getSize());
		log.debug(resourceInfo.getContentTypePrefix());
		log.debug(resourceInfo.getLastModified());
	}
	
	@Test
	public void testList() {
		log.debug("testList");
		
		ResourceInfo[] resourceInfoArr = httpUploadService.listResourceInfo(null, new String[]{"**.txt", "**.gz"});
		for(ResourceInfo resourceInfo2:resourceInfoArr) {
			log.debug(resourceInfo2.getName());
			log.debug(resourceInfo2.getSize());
			log.debug(resourceInfo2.getContentTypePrefix());
			log.debug(resourceInfo2.getLastModified());
		}
	}
}
