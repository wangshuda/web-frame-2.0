package com.cintel.frame.chart.dataset;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @file    : RowKeyTag.java
 * @author  : WangShuDa
 * @date    : 2008-10-15
 * @corp    : CINtel
 * @version : 1.0
 */
public class RowKeyTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	//
	@SuppressWarnings("unused")
	protected final Log log = LogFactory.getLog(this.getClass());
	//
	private String color;
	
	private String property;

	private String title;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	/**
	 * 
	 * @method: doStartTag
	 * @author: WangShuDa
	 * @return
	 * @throws JspException
	 */
	public int doStartTag() throws JspException {
		CreaterTag createrTag = getCreaterTag();
		createrTag.addRowKeyInfo(new RowKeyInfo(property, title, color));
		
		return super.doStartTag();
	}
	
	/**
	 * 
	 * @method: getCreaterTag
	 * @return: CreaterTag
	 * @author: WangShuDa
	 * @return
	 */
	private CreaterTag getCreaterTag() {
		return (CreaterTag)findAncestorWithClass(this, CreaterTag.class);
	}
}
