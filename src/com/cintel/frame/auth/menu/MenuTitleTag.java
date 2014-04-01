package com.cintel.frame.auth.menu;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.UrlUtils;
import com.cintel.frame.util.WebUtils;

/**
 * 
 * @file : MenuTitleTag.java
 * @author : WangShuDa
 * @date : 2008-9-18
 * @corp : CINtel
 * @version : 1.0
 */
public class MenuTitleTag extends TagSupport {
    private static final long serialVersionUID = 3296455521570139421L;

    protected static final Log log = LogFactory.getLog(MenuTitleTag.class);

    private String menuPrefix = "\u5F53\u524D\u4F4D\u7F6E\uFF1A";

    private String menuPrefixImg = "";
    
    private String menuLink = "&nbsp;>>>&nbsp;";

    private int menuId = -1;

    private String menuKey = null;

    private String suffixContent = "";
    
    private boolean needHelp = false;
    
    private String helpImg = "";
    
    private String helpImgCss = "menuHelpImg";
    
    private String helpKey = "";
    
    private String helpLink = "";
    
    private String relativeHelpLink = "";

    private String divCss = "titleDiv";

    private String fontCss = "titleMenu";

    private String menuServiceSpringDefineId = "MenuServiceFactory";
    
    private String contextName = "";
    
    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
        //
        String menuPrefix = pageContext.getServletContext().getInitParameter("MenuTitleTag.Prefix");
        if (StringUtils.hasText(menuPrefix)) {
            this.menuPrefix = menuPrefix;
        }
        //
        String menuLink = pageContext.getServletContext().getInitParameter("MenuTitleTag.Link");
        if (StringUtils.hasText(menuLink)) {
            this.menuLink = menuLink;
        }
        //
        String springDefineId = pageContext.getServletContext().getInitParameter(
                "MenuTitleTag.MenuServiceSpringDefineId");
        if (StringUtils.hasText(springDefineId)) {
            this.menuServiceSpringDefineId = springDefineId;
        }
        HttpServletRequest httpRequest = (HttpServletRequest)pageContext.getRequest();
        String contextPath = httpRequest.getContextPath();
        
        if(StringUtils.hasText(contextPath)){
        	this.contextName = contextPath;
        }
        if (log.isDebugEnabled()) {
            log.debug("Config the menu title with the menu service:" + this.menuServiceSpringDefineId);
        }
    }

    public int doEndTag() throws JspException {
        try {
            //
            IMenuService menuService = (IMenuService) WebUtils.getSpringBean(pageContext.getSession(),
                    menuServiceSpringDefineId);

            LinkedList<FuncItem> funcItemsLinedList = null;
            if (StringUtils.hasText(menuKey)) {
                funcItemsLinedList = menuService.getFuncItemsLinedList(menuKey);
            }
            else {
                funcItemsLinedList = menuService.getFuncItemsLinedList(menuId);
            }
            //
            StringBuffer menuContext = new StringBuffer();
            //
            if (funcItemsLinedList != null && !funcItemsLinedList.isEmpty()) {
                //
                menuContext.append("<div class='" + divCss + "'>");
                //
                String i18nLocal = pageContext.getRequest().getLocale().toString();
                ResourceBundle rb = ResourceBundle.getBundle("messages.zh_CN.menu-message", pageContext
                        .getRequest().getLocale());
                if ("en_US".equals(i18nLocal)) {
                	rb = ResourceBundle.getBundle("messages.en_US.menu-message", pageContext.getRequest().getLocale());                  
                }
               
                this.menuPrefixImg = rb.getString("sys.springMenu.menuPrefixImg");
                this.menuPrefix = rb.getString("sys.springMenu.menuPrefix");
                
                menuContext.append("<font class='" + fontCss + "'>"); 
                menuContext.append("<img src='" + contextName + "/" + menuPrefixImg + "'/>&nbsp;&nbsp;");
                menuContext.append(menuPrefix);
                
                //
                FuncItem funcItem = null;
                Iterator<FuncItem> iterator = funcItemsLinedList.listIterator();
                while (iterator.hasNext()) {
                    funcItem = iterator.next();

                    if ("zh_CN".equals(i18nLocal)) {
                        if (!StringUtils.hasText(funcItem.getTitle())) {
                            rb = ResourceBundle.getBundle("messages.zh_CN.menu-message", pageContext
                                    .getRequest().getLocale());
                            
                            menuContext.append(rb.getString("sys.springMenu.title."+funcItem.getKey().trim()));
                        }
                        else {
                            menuContext.append(funcItem.getTitle());
                        }
                    }
                    else {
                        if (!StringUtils.hasText(funcItem.getEnglishTitle())) {
                            rb = ResourceBundle.getBundle("messages.en_US.menu-message", pageContext
                                    .getRequest().getLocale());
                            menuContext.append(rb.getString("sys.springMenu.title."+funcItem.getKey().trim()));
                        }
                        else {
                            menuContext.append(funcItem.getEnglishTitle());
                        }
                    }
                    // is not the last
                    if (iterator.hasNext()) {
                        menuContext.append(menuLink);
                    }
                }
                //
                menuContext.append(suffixContent);

                menuContext.append("</font>");
                if(StringUtils.hasLength(helpKey) || StringUtils.hasLength(helpLink) || needHelp == true){
                    this.helpImg = rb.getString("sys.springMenu.helpImg");
                    this.relativeHelpLink = rb.getString("sys.springMenu.relativeHelpLink");
                    
                    if(!StringUtils.hasLength(helpLink)){
                    	String contextPath = UrlUtils.getContextPath(pageContext);                    	
                    	this.helpLink = "cinPromptWin.helpWin('" + contextPath + relativeHelpLink + helpKey + "')";
                    }                    
                    menuContext.append("<img src='" + contextName + "/" +  helpImg + "' class='menuHelpImg' onclick=\"" + helpLink + "\"/>");
                   
                }                
                //
                menuContext.append("</div>");
            }
            //
            pageContext.getOut().print(menuContext.toString());

        }
        catch (Exception ex) {
            log.error("doEndTag Error !", ex);
            return SKIP_BODY;
        }
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    public static void main(String args[]) throws UnsupportedEncodingException {
        String prefix = "\u5F53\u524D\u4F4D\u7F6E\uFF1A";

        System.out.println(new String(prefix.getBytes()));

        System.out.println(prefix);
    }

    // ------------- get/set methods -------------
    public String getDivCss() {
        return divCss;
    }

    public void setDivCss(String divCss) {
        this.divCss = divCss;
    }

    public String getFontCss() {
        return fontCss;
    }

    public void setFontCss(String fontCss) {
        this.fontCss = fontCss;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuServiceSpringDefineId() {
        return menuServiceSpringDefineId;
    }

    public void setMenuServiceSpringDefineId(String menuServiceSpringDefineId) {
        this.menuServiceSpringDefineId = menuServiceSpringDefineId;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getSuffixContent() {
        return suffixContent;
    }

    public void setSuffixContent(String suffixContent) {
        this.suffixContent = suffixContent;
    }

	public String getHelpImg() {
		return helpImg;
	}

	public void setHelpImg(String helpImg) {
		this.helpImg = helpImg;
	}

	public boolean isNeedHelp() {
		return needHelp;
	}

	public void setNeedHelp(boolean needHelp) {
		this.needHelp = needHelp;
	}

	public String getHelpImgCss() {
		return helpImgCss;
	}

	public void setHelpImgCss(String helpImgCss) {
		this.helpImgCss = helpImgCss;
	}

	public String getHelpLink() {
		return helpLink;
	}

	public void setHelpLink(String helpLink) {
		this.helpLink = helpLink;
	}

	public String getMenuLink() {
		return menuLink;
	}

	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}

	public String getMenuPrefix() {
		return menuPrefix;
	}

	public void setMenuPrefix(String menuPrefix) {
		this.menuPrefix = menuPrefix;
	}

	public String getMenuPrefixImg() {
		return menuPrefixImg;
	}

	public void setMenuPrefixImg(String menuPrefixImg) {
		this.menuPrefixImg = menuPrefixImg;
	}

	public String getHelpKey() {
		return helpKey;
	}

	public void setHelpKey(String helpKey) {
		this.helpKey = helpKey;
	}
	
	public void setContextName(String contextName){
		this.contextName = contextName;
	}
	public String getContextName(){
		return this.contextName;
	}
}