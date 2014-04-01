package com.cintel.frame.web.page;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.exception.WrappedRuntimeException;
import org.displaytag.pagination.Pagination;
import org.displaytag.properties.TableProperties;
import org.displaytag.util.Href;
import org.displaytag.util.RequestHelper;
import org.displaytag.util.RequestHelperFactory;

/**
 * 
 * @version      1.0
 * @author       WangShuDa
 * @date         2008-07-24
 * @description  supported the page info to show in the displaytag 
 *               for the page list from web service.
 *               change page size.
 */
public class PageBannerTag extends TagSupport {
    private static final long serialVersionUID = -8885774855317790283L;

    private Log log = LogFactory.getLog(PageBannerTag.class);

    protected int pageSize;

    protected int targetPage = 1;

    protected String requestURI;

    protected int totalCount;
    
    protected boolean printSelInfo = true;
    
    protected boolean printPageInfo = true;
    
    protected boolean printResultInfo = true;
    
    protected boolean printPageHref = true;
    
    // set in the method initParameters.
    private int pageCount;

    public void write(String string) {
        if (string != null) {
            try {
                pageContext.getOut().write(string);
            }
            catch (IOException e) {
                throw new WrappedRuntimeException(getClass(), e);
            }
        }
    }
    
    public int doEndTag() throws JspException {
        try {
            Href navigationHref = (Href) this.baseHref.clone();

            String resultBanner = getSearchResultsSummary();
            String pageBanner = getPageNavigationBar(navigationHref);

            if(printResultInfo) {
                write(resultBanner);
            }
            
            if(printPageInfo) {
                write(pageBanner);
            }
            
            if(this.printSelInfo) {
                //
            	Href pageSizeParameterHref = (Href)(navigationHref.addParameter(this.properties.getPaginationPageNumberParam(), 1));
                write(this.getPageSizeSelCtrHtmlData(pageSizeParameterHref));
                //
                write(this.getPageGotoSelCtrHtmlData(navigationHref));
            }
            //
            write(this.properties.getPageBannerSuffixContext());
        } catch (Exception ex) {
            log.error("PageBannerTag", ex);

            return SKIP_BODY;
        }
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
        initParameters();

        return SKIP_BODY;
    }

    //===============================the next code please see the source of displaytag===============================
    //===============================the next code please see the source of displaytag===============================
    private Href baseHref;

    private TableProperties properties;

    private static RequestHelperFactory rhf;

    public void initParameters() {

        pageCount = totalCount / pageSize
                + ((totalCount % pageSize) > 0 ? 1 : 0);

        this.properties = TableProperties
                .getInstance((HttpServletRequest) pageContext.getRequest());

        if (rhf == null) {
            // first time initialization
            rhf = this.properties.getRequestHelperFactoryInstance();
        }
        RequestHelper requestHelper = rhf
                .getRequestHelperInstance(this.pageContext);

        initHref(requestHelper);

    }

    protected void initHref(RequestHelper requestHelper) {
        // get the href for this request
        this.baseHref = requestHelper.getHref();

        if (this.requestURI != null) {
            // if user has added a requestURI create a new href
            String fullURI = requestURI;
            // call encodeURL to preserve session id when cookies are disabled
            fullURI = ((HttpServletResponse) this.pageContext.getResponse())
                    .encodeURL(fullURI);
            baseHref.setFullUrl(fullURI);
            
            //TODO Added by WangShuDa 2008/8/28 for parameters (pageSize and totalCount)
            //==================Added by WangShuDa 2008/8/28 START==================
            baseHref.addParameter(this.properties.getPageSizeParameterName(), pageSize);
            baseHref.addParameter(this.properties.getPageTotalCountParaName(), totalCount);
            //==================Added by WangShuDa 2008/8/28 END==================
        }
    }

    /**
     * Returns the index into the master list of the first object that should
     * appear on the current page that the user is viewing.
     * 
     * @return int index of the first object that should appear on the current
     *         page
     */
    public int getFirstIndexForCurrentPage() {
        return getFirstIndexForPage(this.targetPage);
    }

    /**
     * Returns the index into the master list of the last object that should
     * appear on the current page that the user is viewing.
     * 
     * @return int
     */
    protected int getLastIndexForCurrentPage() {

        return getLastIndexForPage(this.targetPage);
    }

    /**
     * Returns the index into the master list of the first object that should
     * appear on the given page.
     * 
     * @param pageNumber
     *            page number
     * @return int index of the first object that should appear on the given
     *         page
     */
    protected int getFirstIndexForPage(int pageNumber) {
        return (pageNumber - 1) * this.pageSize;
    }

    /**
     * Returns the index into the master list of the last object that should
     * appear on the given page.
     * 
     * @param pageNumber
     *            page number
     * @return int index of the last object that should appear on the given page
     */
    protected int getLastIndexForPage(int pageNumber) {
        int firstIndex = getFirstIndexForPage(pageNumber);
        int pageIndex = this.pageSize - 1;
        int lastIndex = this.totalCount - 1;

        return Math.min(firstIndex + pageIndex, lastIndex);
    }

    /**
     * getSearchResultsSummary
     * 
     * @return
     */
    @SuppressWarnings("deprecation")
	public String getSearchResultsSummary() {

        Object[] objs;
        String message;

        if (this.totalCount == 0) {
            objs = new Object[] { this.properties.getPagingItemsName() };
            message = this.properties.getPagingFoundNoItems();

        } else if (this.totalCount == 1) {
            objs = new Object[] { this.properties.getPagingItemName() };
            message = this.properties.getPagingFoundOneItem();
        } else if (pageCount == 1) {
            objs = new Object[] { new Integer(this.totalCount),
                    this.properties.getPagingItemsName(),
                    this.properties.getPagingItemsName() };
            message = this.properties.getPagingFoundAllItems();
        } else {
            objs = new Object[] { new Integer(this.totalCount),      // 0
                    this.properties.getPagingItemsName(),            // 1
                    new Integer(getFirstIndexForCurrentPage() + 1),  // 2
                    new Integer(getLastIndexForCurrentPage() + 1),   // 3
                    new Integer(this.targetPage),                    // 4
                    new Integer(pageCount),                          // 5
            		new Integer(this.pageSize)};                     // 6
            // Added by wangshuda 2010/06/11 START
            if(this.printPageHref) {
            	message = this.properties.getPagingFoundSomeItems();
            }
            else {
            	message = this.properties.getSomeItemsFountFull();
            }
            // Added by wangshuda 2010/06/11 END
        }

        return MessageFormat.format(message, objs);
    }


    private String getPageSizeOption(Href baseHref, String pageSizeParameter, int pageSize) {
		String optionText = "<option value=\"{0}\" {1}>{2}</option>";
        Href navigationHref = (Href)(baseHref.clone());
        
        Href hrefStr = navigationHref.addParameter(pageSizeParameter, pageSize);
        
        hrefStr = hrefStr.addParameter(this.properties.getPaginationPageNumberParam(), 1);
        
        String selectedStr = (this.pageSize == pageSize ? "selected" : "");
        return MessageFormat.format(optionText, new Object[]{hrefStr, selectedStr, pageSize});
    }
    
    public String getPageSizeSelCtrHtmlData(Href baseHref) {
        if (this.totalCount > 0) {
            StringBuffer buffer = new StringBuffer();
            int[] seqIndexArr = this.properties.getPageSizeSequenceIndexArr(this.pageSize, this.totalCount);
            for(int seqIndex:seqIndexArr) {
                buffer.append(this.getPageSizeOption(baseHref, properties.getPageSizeParameterName(), seqIndex));
            }
            //
            String pageSizeSelCtrText = this.properties.getPageSizeSelCtrText();
           
            return MessageFormat.format(pageSizeSelCtrText, new Object[]{buffer.toString()});
        }
        else {
            return "";
        }
    }

    private String getPageGotoOption(Href baseHref, String pageParameter, int pageIndex) {
        String optionText = "<option value=\"{0}\" {1}>{2}</option>";
        Href navigationHref = (Href)(baseHref.clone());
        
        Href hrefStr = navigationHref.addParameter(pageParameter, pageIndex);
        
        String selectedStr = (this.targetPage == pageIndex ? "selected" : "");
        return MessageFormat.format(optionText, new Object[]{hrefStr, selectedStr, pageIndex});
    }
    
    public String getPageGotoSelCtrHtmlData(Href baseHref) {
    	int maxSelPageCnt = this.properties.getPageGotoMaxSelPageCnt();
    	String pageParameter = properties.getPaginationPageNumberParam();
        if (this.totalCount > 0 && this.pageCount > 1) {
        	if(this.pageCount < maxSelPageCnt) {
	            StringBuffer buffer = new StringBuffer();
	            for(int i = 0; i < this.pageCount; i ++) {
	                buffer.append(this.getPageGotoOption(baseHref, this.properties.getPaginationPageNumberParam(), i + 1));
	            }
	            //
	            String pageGogoSelCtrText = this.properties.getPageGotoSelCtrText();
	           
	            return MessageFormat.format(pageGogoSelCtrText, new Object[]{buffer.toString()});
        	}
        	else {
        		String pageGogoInputCtrText = this.properties.getPageGotoInputCtrText();
    			//
    	        Href navigationHref = (Href)(baseHref.clone());
    	        
    	        navigationHref.removeParameter(pageParameter);
    			// baseHref, pageParameterName, maxPageCnt, currentPageIndex
    			return MessageFormat.format(pageGogoInputCtrText, new Object[]{navigationHref, pageParameter, String.valueOf(this.pageCount), String.valueOf(this.targetPage)});
    		
        	}
        }
        else {
            return "";
        }
    }
    
    /**
     * getPageNavigationBar
     * 
     * @param baseHref
     * @return
     */
    public String getPageNavigationBar(Href baseHref) {

        this.properties = TableProperties
                .getInstance((HttpServletRequest) pageContext.getRequest());

        int startPage;
        int endPage;

        String pageParameter = properties.getPaginationPageNumberParam();

        Pagination pagination = new Pagination(baseHref, pageParameter);
        pagination.setCurrent(targetPage);

        // center the selected page, but only if there are {groupSize} pages
        // available after it, and check that the
        // result is not < 1
        startPage = 1;
        endPage = pageCount;

        if (targetPage != 1) {
            pagination.setFirst(new Integer(1));
            pagination.setPrevious(new Integer(targetPage - 1));
        }

        for (int j = startPage; j <= endPage; j++) {
            pagination.addPage(j, (j == targetPage));
        }

        if (targetPage != pageCount) {
            pagination.setNext(new Integer(targetPage + 1));
            pagination.setLast(new Integer(pageCount));
        }

        // format for previous/next banner
        
        String bannerFormat = "";
        if(printPageHref) {
            if (pagination.isOnePage()) {
                bannerFormat = this.properties.getPagingBannerOnePage();
            } else if (pagination.isFirst()) {
                bannerFormat = this.properties.getPagingBannerFirst();
            } else if (pagination.isLast()) {
                bannerFormat = this.properties.getPagingBannerLast();
            } else {
                bannerFormat = this.properties.getPagingBannerFull();
            }
        }

        //
        return pagination.getFormattedBanner(this.properties
                .getPagingPageLink(), this.properties.getPagingPageSelected(),
                this.properties.getPagingPageSeparator(), bannerFormat);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(int targetPage) {
        this.targetPage = targetPage;
    }

    public boolean isPrintPageInfo() {
        return printPageInfo;
    }

    public void setPrintPageInfo(boolean printPageInfo) {
        this.printPageInfo = printPageInfo;
    }

    public boolean isPrintResultInfo() {
        return printResultInfo;
    }

    public void setPrintResultInfo(boolean printResultInfo) {
        this.printResultInfo = printResultInfo;
    }

    public boolean isPrintSelInfo() {
        return printSelInfo;
    }

    public void setPrintSelInfo(boolean printSelInfo) {
        this.printSelInfo = printSelInfo;
    }

	public boolean isPrintPageHref() {
		return printPageHref;
	}

	public void setPrintPageHref(boolean printPageHref) {
		this.printPageHref = printPageHref;
	}
}