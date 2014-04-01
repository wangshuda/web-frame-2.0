package com.cintel.frame.net.http.provider;

import java.util.HashMap;
import java.util.Map;

import com.cintel.frame.util.StringUtils;

public class HttpResponseCtx {
    
    private int httpCode;

    private String responseTxt;
    
    private byte[] responseByteArr;
    
    private int contentLength;
    
    private String contentType;
    
    private String contentDisposition;
    
    private String attachFileName;

    private Map<String, String> headMap = new HashMap<String, String>();
    
    public boolean isDataResonse() {
        return StringUtils.hasLength(contentDisposition);
    }
    //
    public boolean isOK() {
        return (httpCode == 200);
    }
    
    public void addHead(String key, String value) {
        headMap.put(key, value);
    }

    public Map<String, String> getHeadMap() {
        return headMap;
    }

    public void setHeadMap(Map<String, String> headMap) {
        this.headMap = headMap;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getResponseTxt() {
        return responseTxt;
    }

    public void setResponseTxt(String responseTxt) {
        this.responseTxt = responseTxt;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = Integer.valueOf(contentLength);
    }
    
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getResponseByteArr() {
        return responseByteArr;
    }

    public void setResponseByteArr(byte[] responseByteArr) {
        this.responseByteArr = responseByteArr;
    }
    public String getContentDisposition() {
        return contentDisposition;
    }
    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }
}
