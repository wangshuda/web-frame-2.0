package com.cintel.frame.net.http.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.FileUtils;


/**
 * 
 * @version 
 * @history 
 *          1.0.0 2013-12-10 wangshuda created
 */
public class HttpClientServiceImpl implements HttpClientService {
    private static Log log = LogFactory.getLog(HttpClientServiceImpl.class);
    //
    private final static String _UTF8 = "UTF-8";
    //
    private HttpClientProvider httpClientProvider;
    
    private boolean checkHttps = false;
    
    public HttpResponseCtx doDownLoad(String reqUrl) {
        return this.doPost(reqUrl, null, null, "GET");
    }
    
    public HttpResponseCtx doUpload(String reqUrl, String fileName, String contentType, byte[] dataByteArr) {
        HttpResponseCtx httpMsgRtnCtx = new HttpResponseCtx();
        //
        HttpClient httpClient = this.buildHttpClient(reqUrl);
        
        PostMethod filePostMethod = new PostMethod(reqUrl);
        
        try {
            Part[] dataParts = {new ByteArrayPart(dataByteArr, fileName, contentType)};
            //
            filePostMethod.setRequestEntity(new MultipartRequestEntity(dataParts, filePostMethod.getParams()));
            //
            httpMsgRtnCtx = this.exeHttpMethod(httpClient, filePostMethod);
        }
        catch (IOException ex) {
            log.error(reqUrl, ex);
        }
        //
        return httpMsgRtnCtx;
    }
    
    public HttpResponseCtx doPost(String reqUrl) {
        return this.doPost(reqUrl, null, null, "GET");
    }
    
    public HttpResponseCtx doPost(String reqUrl, String reqBody, String contentType) {
        return this.doPost(reqUrl, reqBody, contentType, "POST");
    }

    public HttpResponseCtx doPost(String reqUrl, String reqBody, String contentType, String reqMethod) {
        if(log.isInfoEnabled()) {
            log.info(reqBody);
        }
        //
        HttpResponseCtx httpMsgRtnCtx = new HttpResponseCtx();
        //
        HttpClient httpClient = this.buildHttpClient(reqUrl);
        try {
            HttpMethod httpMethod = null;
            if("POST".equalsIgnoreCase(reqMethod)) {
                httpMethod = new PostMethod(reqUrl);
                
                ((PostMethod)httpMethod).setRequestEntity(new StringRequestEntity(reqBody, contentType, _UTF8));
            }
            else {
                httpMethod = new GetMethod(reqUrl);
            }
            //
            httpMsgRtnCtx = this.exeHttpMethod(httpClient, httpMethod);
        }
        catch (Exception ex) {
            log.error("reqBody:" + reqBody);
            //
            log.error(reqUrl, ex);
        }
        //
        return httpMsgRtnCtx;
    }
    
    private HttpClient buildHttpClient(String reqUrl) {
        if(log.isInfoEnabled()) {
            log.info(reqUrl);
        }
        //
        if(checkHttps && reqUrl != null && reqUrl.startsWith("https")) {
            httpClientProvider.initHttpsProtocol();
        }
        //
        HttpClient httpClient = httpClientProvider.loadHttpClient();
        return httpClient;
    }
    //
    private String buildResponseHeader(final HttpMethod httpMethod, String headerKey, String subKey, String defaultValue) {
        String rtnValue = defaultValue;
        //
        Header responseHeader = httpMethod.getResponseHeader(headerKey);
        //
        if(responseHeader != null) {
            HeaderElement[] headerElementArr = responseHeader.getElements();
            if(headerElementArr != null) {
                for(HeaderElement headerElement:headerElementArr) {
                    NameValuePair nameValuePair = headerElement.getParameterByName(subKey);
                    if(nameValuePair != null) {
                        rtnValue = nameValuePair.getValue();
                        break;
                    }
                }
            }
        }
        return rtnValue;
    }
    
    private String buildResponseHeader(final HttpMethod httpMethod, String headerKey, String defaultValue) {
        Header responseHeader = httpMethod.getResponseHeader(headerKey);
        //
        return responseHeader != null ? responseHeader.getValue() : defaultValue;
    }
    
    private HttpResponseCtx exeHttpMethod(HttpClient httpClient, HttpMethod httpMethod) throws HttpException, IOException {
        HttpResponseCtx httpMsgRtnCtx = new HttpResponseCtx();
        //
        int httpRtnCode = httpClient.executeMethod(httpMethod);
        //
        httpMsgRtnCtx.setHttpCode(httpRtnCode);
        //
        String contentType = this.buildResponseHeader(httpMethod, "Content-Type", "");
        httpMsgRtnCtx.setContentType(contentType);
        //
        String contentDisposition = this.buildResponseHeader(httpMethod, "Content-Disposition", "");
        httpMsgRtnCtx.setContentDisposition(contentDisposition);
        //
        if(!httpMsgRtnCtx.isDataResonse()) {
            InputStream responseIn = httpMethod.getResponseBodyAsStream();
            
            String responseTxt = FileUtils.copyToString(new InputStreamReader(responseIn, _UTF8));
            //
            if(log.isInfoEnabled()) {
                log.info(responseTxt);
            }
            //
            httpMsgRtnCtx.setResponseTxt(responseTxt);
        }
        else {
            byte[] responseByteArr = httpMethod.getResponseBody();
            httpMsgRtnCtx.setResponseByteArr(responseByteArr);
            //
            String contentLength = this.buildResponseHeader(httpMethod, "Content-Length", "0");
            httpMsgRtnCtx.setContentLength(contentLength);
            //
            String attachFileName = this.buildResponseHeader(httpMethod, "Content-Disposition", "filename", "");
            httpMsgRtnCtx.setAttachFileName(attachFileName);
        }
        //
        for(Header header:httpMethod.getResponseHeaders()) {
            httpMsgRtnCtx.addHead(header.getName(), header.getValue());
        }
        //
        return httpMsgRtnCtx;
    }
    //
    public HttpClientProvider getHttpClientProvider() {
        return httpClientProvider;
    }

    public void setHttpClientProvider(HttpClientProvider httpClientProvider) {
        this.httpClientProvider = httpClientProvider;
    }

    public boolean isCheckHttps() {
        return checkHttps;
    }

    public void setCheckHttps(boolean checkHttps) {
        this.checkHttps = checkHttps;
    }
}
