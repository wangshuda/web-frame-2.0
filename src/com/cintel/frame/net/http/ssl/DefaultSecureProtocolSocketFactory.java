package com.cintel.frame.net.http.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

public class DefaultSecureProtocolSocketFactory implements SecureProtocolSocketFactory {
    private static class TrustAnyTrustManager implements X509TrustManager {
        
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
   
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
   
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
    //
    private SSLContext sslContext = null;

    public DefaultSecureProtocolSocketFactory() {
    }

    private SSLContext createEasySSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] { new TrustAnyTrustManager() }, null);
            return context;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private SSLContext getSSLContext() {
        if (this.sslContext == null) {
            this.sslContext = this.createEasySSLContext();
        }
        return this.sslContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String,
     *      int, java.net.InetAddress, int)
     */
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException,
            UnknownHostException {

        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String,
     *      int, java.net.InetAddress, int,
     *      org.apache.commons.httpclient.params.HttpConnectionParams)
     */
    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort,
            final HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        if (timeout == 0) {
            return createSocket(host, port, localAddress, localPort);
        }
        else {
            return ControllerThreadSocketFactory.createSocket(this, host, port, localAddress, localPort, timeout);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int)
     */
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    /*
     * (non-Javadoc)
     * 
     * @see SecureProtocolSocketFactory#createSocket(java.net.Socket,java.lang.String,int,boolean)
     */
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
            UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }
}
