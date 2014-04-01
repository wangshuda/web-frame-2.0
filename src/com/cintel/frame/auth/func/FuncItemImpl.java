package com.cintel.frame.auth.func;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

import com.cintel.frame.auth.user.FuncItem;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 
 * @file : FuncItemImpl.java
 * @author : WangShuDa
 * @date : 2009-3-26
 * @corp : CINtel
 * @version : 1.0
 * @desc : 
 *      2009/03/26 Added the method 'isExistSubMenu'
 *      2009/07/23 Override the method equals
 *      2009/09/03 Added the showInWelcomePage property.
 */
@SuppressWarnings("unchecked")
public class FuncItemImpl implements FuncItem, Serializable {
    private static final long serialVersionUID = -5620213671391227558L;

    private static final String _OPEN_MENU = "A";
    
    @SuppressWarnings("unused")
    private static final String _CLOSE_MENU = "B";
    
    protected int menuId;

    protected int parentId;

    protected int menuOrderId;

    protected String menuKey;

    protected String requestUrl;

    protected String menuTitle;

    protected String engTitle;
    
    protected String status;

    protected String remark;
    
    protected String iconImgKey;
    
    protected boolean isFavorite;

    protected boolean showInWelcomePage = false;
    
    @XStreamOmitField
    protected List<FuncItem> subItemsList = Collections.EMPTY_LIST;
    
    @XStreamOmitField
    private String menuMode = null;
    
    @XStreamOmitField
    private String authUrlPattern;
    
    @XStreamOmitField
    private boolean checkSameKey = true;
    
    @XStreamOmitField
    private List<String> authUrlPatternList = Collections.EMPTY_LIST;

    // menuTitle, MenuNode, MenuKey
    
    private static final String _MENUM_KEY = "MenuKey";
    private static final String _MENUM_TITLE = "MenuTitle";
    private static final String _MENUM_NODE = "MenuNode";
    
    private boolean isMenuTitleMode() {
        return _OPEN_MENU.equalsIgnoreCase(this.getStatus()) && StringUtils.hasLength(this.getRequestUrl());
    }
    
    public String buildMenuMode() {
        String funcMode = _MENUM_KEY;
        if(this.isMenuTitleMode()) {
            funcMode = _MENUM_TITLE;
        }
        else {
            List<FuncItem> subItemsList = this.getSubItemsList();
            //
            if(!subItemsList.isEmpty()) {
                for(FuncItem subFuncItem:subItemsList) {
                    if(subFuncItem.isEnableShowAtPage()) {
                        funcMode = _MENUM_NODE;
                        break;
                    }
                }
            }

        }
        return funcMode;
    }
    
    public boolean isIsKeyMode() {
        return _MENUM_KEY.equalsIgnoreCase(this.getMenuMode());
    }

    public boolean isIsNodeMode() {
        return _MENUM_NODE.equalsIgnoreCase(this.getMenuMode());
    }

    public boolean isIsTitleMode() {
        return _MENUM_TITLE.equalsIgnoreCase(this.getMenuMode());
    }
    
    public String getMenuMode() {
        if(menuMode == null) {
            this.menuMode = this.buildMenuMode();
        }
        //
        return menuMode;
    }
    //
    public boolean isEnableShowAtPage() {
        boolean rtnValue = false;
        //
        if(this.isMenuTitleMode()) {
            rtnValue = true;
        }
        else {
            for(FuncItem subFuncItem:this.getSubItemsList()) {
                if(subFuncItem.isEnableShowAtPage()) {
                    rtnValue = true;
                    break;
                }
            }
        }
        return rtnValue;
    }
    
    @Override
    public boolean equals(Object other) {
        if(other == null || !(other instanceof FuncItem)) {
            return false;
        }
        //
        FuncItem otherFuncItem = (FuncItem)other;
        if(otherFuncItem.getId() == this.menuId && otherFuncItem.getParentId() == this.parentId) {
            return true;
        }
        //
        String otherKey = otherFuncItem.getKey();
        if(this.checkSameKey && ((otherKey != null) && otherKey.equals(this.menuKey))) {
            return true;
        }
        return false;
    }
    
    public boolean isExistSubMenu() {
        return (this.getSubItemsList() != null && !this.getSubItemsList().isEmpty());
    }

    public List<String> getAuthUrlPatternList() {
        return authUrlPatternList;
    }

    public void setAuthUrlPatternList(List<String> authUrlPatternList) {
        this.authUrlPatternList = authUrlPatternList;
    }

    public String getOid() {
        return String.valueOf(this.menuId);
    }

    public void setOid(String oid) {
        if (oid == null) {
            return;
        }
        String oidStr[] = oid.split(",");
        menuId = Integer.parseInt(oidStr[0]);
    }

    public int getId() {
        return menuId;
    }

    public void setId(int menuId) {
        this.menuId = menuId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getKey() {
        return this.menuKey;
    }

    public void setKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getTitle() {
        return this.menuTitle;
    }

    public void setTitle(String title) {
        this.menuTitle = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return this.remark;
    }

    public void setDescription(String remark) {
        this.remark = remark;
    }

    public List<FuncItem> getSubItemsList() {
        return subItemsList;
    }

    public void setSubItemsList(FuncItem[] subItemsArr) {
        this.subItemsList = new ArrayList<FuncItem>();
        Collections.addAll(this.subItemsList, subItemsArr);
    }
    
    public void setSubItemsList(List<FuncItem> subItemsList) {
        this.subItemsList = subItemsList;
    }

    @Deprecated
    /** replace the method with getAuthUrlPatternList * */
    public String getAuthUrlPattern() {
        return authUrlPattern;
    }

    @Deprecated
    /** replace the method with setAuthUrlPatternList * */
    public void setAuthUrlPattern(String authUrlPattern) {
        this.authUrlPattern = authUrlPattern;
    }

    public int getMenuOrderId() {
        return menuOrderId;
    }

    public void setMenuOrderId(int menuOrderId) {
        this.menuOrderId = menuOrderId;
    }

    /**
     * Match with the db sort.
     * 
     * @method: compareTo
     * @author: WangShuDa
     * @param funcItem
     * @return
     */
    public int compareTo(FuncItem funcItem) {
        if (this.menuOrderId < funcItem.getMenuOrderId()) {
            return 1;
        }

        if (this.menuOrderId > funcItem.getMenuOrderId()) {
            return -1;
        }

        if (this.menuId < funcItem.getId()) {
            return 1;
        }

        if (this.menuId > funcItem.getId()) {
            return -1;
        }
        //

        return 0;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isShowInWelcomePage() {
        return showInWelcomePage;
    }

    public void setShowInWelcomePage(boolean showInWelcomePage) {
        this.showInWelcomePage = showInWelcomePage;
    }

    public String getEnglishTitle() {
        return StringUtils.hasLength(this.engTitle) ? this.engTitle : this.menuKey;
    }

    public void setEnglishTitle(String engTitle) {
        this.engTitle = engTitle;
    }

	public String getImgPath() {
		return this.iconImgKey;
	}

    public String getIconImgKey() {
        return iconImgKey;
    }

    public void setIconImgKey(String iconImgKey) {
        this.iconImgKey = iconImgKey;
    }

    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;
    }

    public String getEngTitle() {
        return engTitle;
    }

    public void setEngTitle(String engTitle) {
        this.engTitle = engTitle;
    }

    // new get/set method.
    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isCheckSameKey() {
        return checkSameKey;
    }

    public void setCheckSameKey(boolean checkSameKey) {
        this.checkSameKey = checkSameKey;
    }
}