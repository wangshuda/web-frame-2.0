package com.cintel.frame.net.http.provider;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import com.cintel.frame.net.http.ssl.DefaultSecureProtocolSocketFactory;

public class HttpClientProviderImpl implements HttpClientProvider {
    
    private MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
    
    public void initHttpsProtocol(int port) {
        ProtocolSocketFactory protocolSocketFactory = new DefaultSecureProtocolSocketFactory();
        Protocol.registerProtocol("https", new Protocol("https", protocolSocketFactory, port));
    }
    
    public void initHttpsProtocol() {
        this.initHttpsProtocol(443);
    }
    
    public HttpClient loadHttpClient() {
        HttpClient httpclient = new HttpClient(manager);
        //
        return httpclient;
    }

    public MultiThreadedHttpConnectionManager getManager() {
        return manager;
    }

    public void setManager(MultiThreadedHttpConnectionManager manager) {
        this.manager = manager;
    }
}
