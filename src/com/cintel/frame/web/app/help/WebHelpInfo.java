package com.cintel.frame.web.app.help;

import com.cintel.frame.webui.IDomainVo;
/**
 * @file : $Id$
 * @corp : CINtel
 * @version : 1.0
*/
public class WebHelpInfo implements IDomainVo{
	
    private String helpKey;
    private String menuKey;
    private String helpText;
    private int htmlSupport;

      public String getOid() {
		  return helpKey;
      }

      public String getHelpKey() {
          return helpKey;
      }
    
      public void setHelpKey(String helpKey) {
          this.helpKey = helpKey;
      }
    
	  public void setOid(String oid) {
		  if(oid == null) {
			return;
		  }
		  String oidStr[] = oid.split(",");
		  this.helpKey = oidStr[0];
	 }
	
      public String getMenuKey() {
            return menuKey;
      }
      public void setMenuKey(String menuKey) {
            this.menuKey = menuKey;
      }

      public String getHelpText() {
            return helpText;
      }
      
      public void setHelpText(String helpText) {
            this.helpText = helpText;
      }

      public int getHtmlSupport() {
            return htmlSupport;
      }
      
      public void setHtmlSupport(int htmlSupport) {
            this.htmlSupport = htmlSupport;
      }
           
}