package com.cintel.frame.chart;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import com.cintel.frame.chart.dataset.MyCategoryDataset;
import com.cintel.frame.chart.dataset.MyPieDataSet;

/**
 * 
 * @file    : ChartCreateTag.java
 * @author  : WangShuDa
 * @date    : 2008-10-14
 * @corp    : CINtel
 * @version : 1.0
 */
public class ChartCreateTag extends TagSupport {
	private static final long serialVersionUID = 6294032789411906953L;


    
	private Log log = LogFactory.getLog(this.getClass());

	private String dataSet = null;

	private String chartId;
	
	private String title;

	private String horizontalTitle;

	private String verticalTitle;

	private String chartType = ChartType._LINE_CHART; // "line", "bar", pie

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	
	public String getHorizontalTitle() {
		return horizontalTitle;
	}

	public void setHorizontalTitle(String horizontalTitle) {
		this.horizontalTitle = horizontalTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVerticalTitle() {
		return verticalTitle;
	}

	public void setVerticalTitle(String verticalTitle) {
		this.verticalTitle = verticalTitle;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		if (dataSet != null) {
			try {
				Object savedDataSet = pageContext.getRequest().getAttribute(dataSet);
				
				if(savedDataSet == null) {
					log.error("saved categoryDataset is null !");

					return SKIP_BODY;
				}
				//
                Dataset dataSet;
				if(savedDataSet instanceof DefaultCategoryDataset) {
                    dataSet = (DefaultCategoryDataset)savedDataSet;
				}
				else if(savedDataSet instanceof MyCategoryDataset) {
                    dataSet = (MyCategoryDataset)savedDataSet;
				}
                else if(savedDataSet instanceof MyPieDataSet) {
                    dataSet = (MyPieDataSet)savedDataSet;
                }
				else {
					log.error("saved categoryDataset must be DefaultCategoryDataset or MyCategoryDataset null !");

					return SKIP_BODY;
				}

				//
				JFreeChart chart = null;
				if(ChartType._BAR_CHART.equals(chartType)) {
					chart = ChartCreater.createBarChart(dataSet, title, horizontalTitle, verticalTitle);
				}
				else if(ChartType._LINE_CHART.equals(chartType)) {
					// chart type is "line"
					chart = ChartCreater.createLineChart(dataSet, title, horizontalTitle, verticalTitle);
				}
                else if(ChartType._PIE_CHART.equals(chartType)) {
                    //  chart type is "pie"
                    chart = ChartCreater.createPieChart(dataSet, title);
                }
                else {
                    throw new IllegalArgumentException("Illegal chartType:" + chartType + ". Can only be: bar, line, pie. And pie chart can only be pie data set.");
                }
                //
				pageContext.setAttribute(chartId, chart, PageContext.SESSION_SCOPE);
			}
			catch (Exception ex) {
				log.error("ChartCreateTag Error !", ex);

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
}