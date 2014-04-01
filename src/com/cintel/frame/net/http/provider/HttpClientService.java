package com.cintel.frame.net.http.provider;

public interface HttpClientService {
    public HttpResponseCtx doPost(String reqUrl);
    
    public HttpResponseCtx doPost(String reqUrl, String reqBody, String contentType);
    
    public HttpResponseCtx doPost(String reqUrl, String reqBody, String contentType, String reqMethod);
    
    public HttpResponseCtx doDownLoad(String reqUrl);
    
    public HttpResponseCtx doUpload(String reqUrl, String fileName, String contentType, byte[] dataByteArr);
}
