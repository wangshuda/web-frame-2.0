package com.cintel.frame.net.http.provider;

import org.apache.commons.httpclient.HttpClient;

public interface HttpClientProvider {
    
    public void initHttpsProtocol();
    
    public void initHttpsProtocol(int port);
    
    public HttpClient loadHttpClient();
}
