package com.cintel.frame.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.RandomUtils;
import com.cintel.frame.util.StringUtils;

/**
 * 
 * @version $Id: RandomGeneratorTag.java 37671 2013-01-10 09:05:59Z wangshuda $
 * @history 
 *          1.0.0 2010-3-1 wangshuda created
 */
public class RandomGeneratorTag extends TagSupport {
	private static final long serialVersionUID = 6294032789411906953L;

	private Log log = LogFactory.getLog(RandomGeneratorTag.class);

    private static final String _UUID = "uuid";
    
    private static final String _DATA_ROW = "dataRow";
    
    protected String mode = _UUID;
    
	protected String var;

    protected int prefixLen = 4;
    
    protected int suffixLen = 4;

	//
	public int doStartTag() throws JspException {
		//
		try {
            String resultStr = "";
			if(_UUID.equals(mode)) {
                resultStr = RandomUtils.generateUuidStr();
            }
            else if(_DATA_ROW.equals(mode)) {
                resultStr = RandomUtils.genDataStreamNum("");
            }
            else {
                resultStr = RandomUtils.generateTimeStampRandomStr(prefixLen, suffixLen);
            }
            
			if(StringUtils.hasText(var)) {
				pageContext.setAttribute(var, resultStr, PageContext.REQUEST_SCOPE);
			}
			else {
                pageContext.getOut().print(resultStr);  
            }
		}

		catch (IOException ex) {
			log.error("Write to pageContext error!", ex);
		}
		//
		return EVAL_BODY_INCLUDE;
	}
    
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
    
    
    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
    
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getPrefixLen() {
        return prefixLen;
    }

    public void setPrefixLen(int prefixLen) {
        this.prefixLen = prefixLen;
    }

    public int getSuffixLen() {
        return suffixLen;
    }

    public void setSuffixLen(int suffixLen) {
        this.suffixLen = suffixLen;
    }
}
