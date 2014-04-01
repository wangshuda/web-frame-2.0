package com.cintel.frame.net.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.cintel.frame.util.FileUtils;

/**
 * 
 * @file    : HttpMsgSender.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-9-8 wangshuda created
 */
public class HttpMsgSender {
    private static Log log = LogFactory.getLog(HttpMsgSender.class);
    
    public static String doPost(String httpUrl, InputStream reqBodyIn, String contentType) {
        String resultStr = "";
        HttpURLConnection httpConnection = null;
        
        try {
            URL url = new URL(httpUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            //
            
            if(StringUtils.hasText(contentType)){
            	 httpConnection.setRequestProperty("Content-Type", contentType);
            }
            httpConnection.setConnectTimeout(5000);
            httpConnection.setReadTimeout(120000);
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            //
            if(reqBodyIn == null) {
                httpConnection.setRequestMethod("GET");
                //
                httpConnection.connect();
            }
            else {
                httpConnection.setRequestMethod("POST");
                OutputStream out = httpConnection.getOutputStream();
                FileUtils.copy(reqBodyIn, out);
            }
            //
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                //
                InputStream inputStream = httpConnection.getInputStream();
                
                String responseStr = FileUtils.copyToString(new InputStreamReader(inputStream,"UTF-8")); 
                //
                if(log.isDebugEnabled()) {
                    log.debug("requestUrl = " + httpUrl);
                    log.debug("responseStr = " + responseStr);
                }
                //
                if(responseStr != null && responseStr.length() > 0) {
                    resultStr = responseStr;
                }
            }
        }
        catch(Exception ex) {
            log.error(httpUrl, ex);
        }
        finally {
            if(httpConnection != null) {
                httpConnection.disconnect();
                httpConnection = null;
            }
        }
        //
        return resultStr;
    }
    
    public static String post(String httpUrl, String reqBody) {
        log.debug("reqBody=" + reqBody);
		return post(httpUrl, reqBody, null);
	}
    
    public static String post(String httpUrl, String reqBody, String contentType) {
        String resultStr = "";
        
        byte[] byteArr = null;
        try {
            byteArr = reqBody.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
           log.error("", ex);
           byteArr = reqBody.getBytes();
        }
        //
        resultStr =  post(httpUrl, new ByteArrayInputStream(byteArr), contentType);
        return resultStr;
    }
    
    public static String post(String httpUrl, InputStream in, String contentType) {
        String resultStr = "";
        
        resultStr = doPost(httpUrl, in, contentType);
        //
        return resultStr;
    }
    /**
     * 
     * @param httpUrlWitPara
     * @return
     */
    public static String post(String httpUrlWitPara)  {
        String resultStr = "";
        resultStr = doPost(httpUrlWitPara, null,null);
        //
        return resultStr;
    }
}
