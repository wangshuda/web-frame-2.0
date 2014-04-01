package com.cintel.frame.web.app.area;

import com.cintel.frame.webui.IDomainVo;

/**
 * @file : $Id: AreaInfo.java 13450 2009-12-17 00:30:18Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
*/
public class AreaInfo implements IDomainVo {

    private String areaCode;
    private String areaName;

    public String getOid() {
    	/*if(areaCode == null){
    		return "";
    	}*/
		return areaCode;
    }

	public void setOid(String oid) {
		if(oid == null) {
			return;
		}
		String oidStr[] = oid.split(",");
        areaCode = oidStr[0];
	}
	
      public String getAreaCode() {
            return areaCode;
      }
      public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
      }

      public String getAreaName() {
            return areaName;
      }
      public void setAreaName(String areaName) {
            this.areaName = areaName;
      }
      
    

}