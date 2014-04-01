package com.cintel.frame.net.http;

import org.dom4j.Document;

public class ResponseCtx {
    private String bodyStr;

    private int status;

    private Document xmlDoc;
    
    public ResponseCtx(int status, String bodyStr) {
        this.bodyStr = bodyStr;
        this.status = status;
    }

    public String getBodyStr() {
        return bodyStr;
    }

    public int getStatus() {
        return status;
    }
    
    public boolean isHttpOk() {
        return (status == 200);
    }

    public Document getXmlDoc() {
        return xmlDoc;
    }

    public void setXmlDoc(Document xmlDoc) {
        this.xmlDoc = xmlDoc;
    }
}
