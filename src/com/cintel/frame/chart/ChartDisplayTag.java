package com.cintel.frame.chart;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.DateUtils;
import com.cintel.frame.util.StringUtils;

/**
 * 
 * @file    : ChartDisplayTag.java
 * @author  : WangShuDa
 * @date    : 2008-10-14
 * @corp    : CINtel
 * @version : 1.0
 */
public class ChartDisplayTag extends TagSupport {
	private static final long serialVersionUID = 6294032789411906953L;

	private Log log = LogFactory.getLog(ChartDisplayTag.class);

	private String chartId;

	private int width = 400;
	
	private int height = 300;

	// if is true, the image request will config with the parameter, and delete the chart from the session
	private boolean removeFromSession = false;
	
	/**
	 * config it with the parameters and set it with the value of src 
	 */
	private String imageRenderPath="/chart2ImageServlet";
	
	/**
	 * css style
	 */
	private String style; 
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}


	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		if (chartId != null) {
			try {
				JspWriter writer = pageContext.getOut();
				writer.write("<img src='");
				writer.write(imageRenderPath);
				writer.write("?chartId=" + chartId);

				writer.write("&width=" + width);
				writer.write("&height=" + height);
				//
				if(removeFromSession) {
					writer.write("&removeFromSession=" + removeFromSession);
				}
                //
                writer.write("&randomStr=" + DateUtils.getCurrentTimeString("yyyyMMddHHmmssSSS"));
				
				writer.write("'");
				if(StringUtils.hasText(style)) {
					writer.write("class='" + style + "'");
				}
				
				writer.write("/>");

			}
			catch (Exception ex) {
				log.error("ChartDisplayTag Error !", ex);

				return SKIP_BODY;
			}
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public String getImageRenderPath() {
		return imageRenderPath;
	}

	public void setImageRenderPath(String imageRenderPath) {
		this.imageRenderPath = imageRenderPath;
	}

	public boolean isRemoveFromSession() {
		return removeFromSession;
	}

	public void setRemoveFromSession(boolean removeFromSession) {
		this.removeFromSession = removeFromSession;
	}
}