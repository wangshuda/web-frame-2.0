package com.cintel.frame.net.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.FileUtils;
import com.cintel.frame.util.StringUtils;


public class HttpUtils {
    public static final int HTTP_OK = 200;
    private static Log log = LogFactory.getLog(HttpUtils.class);
    //
    public static ResponseCtx doPost(String httpUrl, String reqBodyStr, Map<String, String> headMap) {
        String resultStr = "";
        HttpURLConnection httpConnection = null;
        
        int responseCode = -1;
        try {
            URL url = new URL(httpUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            //
            httpConnection.setConnectTimeout(5000);
            httpConnection.setReadTimeout(120000);
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            //
            for(Entry<String, String> entry:headMap.entrySet()) {
                httpConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            //
            if(!StringUtils.hasLength(reqBodyStr)) {
                httpConnection.setRequestMethod("GET");
                //
                httpConnection.connect();
            }
            else {
                httpConnection.setRequestMethod("POST");
                OutputStream out = httpConnection.getOutputStream();
                //
                byte[] byteArr = null;
                try {
                    byteArr = reqBodyStr.getBytes("UTF-8");
                }
                catch (UnsupportedEncodingException ex) {
                   log.error("", ex);
                   byteArr = reqBodyStr.getBytes();
                }
                //
                FileUtils.copy(new ByteArrayInputStream(byteArr), out);
            }
            //
            responseCode = httpConnection.getResponseCode();
            //
            InputStream inputStream = null;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
            }
            else {
                inputStream = httpConnection.getErrorStream();
            }
            //
            resultStr = new String(FileUtils.copyToByteArray(inputStream), "UTF-8");
        }
        catch(Exception ex) {
            log.error("", ex);
            //
            resultStr = ex.getMessage();
        }
        finally {
            if(httpConnection != null) {
                httpConnection.disconnect();
                httpConnection = null;
            }
        }
        //
        return new ResponseCtx(responseCode, resultStr);
    }
    
    @SuppressWarnings("unchecked")
    public static ResponseCtx doPost(String httpUrl, String reqBodyStr) {
        return doPost(httpUrl, reqBodyStr, Collections.EMPTY_MAP);
    }
}
