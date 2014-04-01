package com.cintel.frame.poi.excel;

public class PoiParseFieldCtx {
    private String valueStr;

    private String parseMsg;

    //
    public PoiParseFieldCtx(String valueStr, String parseMsg) {
        this.valueStr = valueStr;
        this.parseMsg = parseMsg;
    }

    public String getParseMsg() {
        return parseMsg;
    }

    public void setParseMsg(String parseMsg) {
        this.parseMsg = parseMsg;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }
}
