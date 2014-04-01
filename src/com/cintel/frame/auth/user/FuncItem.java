package com.cintel.frame.auth.user;

import java.util.List;

import com.cintel.frame.webui.IDomainVo;

public interface FuncItem extends IDomainVo, Comparable<FuncItem> {

	public int getId();
	
	public int getParentId();
	
	public int getMenuOrderId();
	
	public String getTitle();
    
    public String getEnglishTitle();

	public String getRequestUrl();
	
	public String getKey();
	
    public boolean isIsTitleMode();
    
    public boolean isIsNodeMode();
    
    public boolean isIsKeyMode();
    
    public String getMenuMode();
    
    public boolean isEnableShowAtPage();
    
	public boolean isExistSubMenu();
	@Deprecated
	/** replace the method with getAuthUrlPatternList **/
	public String getAuthUrlPattern();
	
	public List<FuncItem> getSubItemsList();
	
	public void setSubItemsList(List<FuncItem> subItemList);
	
	public List<String> getAuthUrlPatternList();
	
	public void setAuthUrlPatternList(List<String> authUrlPatternList);
	
	public boolean isFavorite();
	
    public String getIconImgKey();
    
	public String getImgPath();
}
