package com.cintel.frame.auth;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-9-14 wangshuda created
 */
public enum GrantedCheckTypeEnum {
     ifAllGranted("ifAllGranted")
    ,ifAnyGranted("ifAnyGranted")
    ,ifNotGranted("ifNotGranted")
    ;
    
    private String valueStr;
    
    public boolean sameTo(String str) {
        return this.valueStr.equalsIgnoreCase(str);
    }
    
    private GrantedCheckTypeEnum(String valueStr) {
        this.valueStr = valueStr;
    }
    
    public String toString() {
        return this.valueStr;
    }
}
