package com.cintel.frame.properties.db;

import com.cintel.frame.webui.IDomainVo;

public class DbProperty implements IDomainVo {

    private int id;

    private String v_key;

    private String value;

    private String description;

    private int status;

    private int cacheFlush;

    public String getOid() {
        return v_key;
    }

    public void setOid(String oid) {
        if (oid == null) {
            return;
        }
        String oidStr[] = oid.split(",");
        v_key = oidStr[0];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getV_key() {
        return v_key;
    }

    public void setV_key(String v_key) {
        this.v_key = v_key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCacheFlush() {
        return cacheFlush;
    }

    public void setCacheFlush(int cacheFlush) {
        this.cacheFlush = cacheFlush;
    }

}