package com.cintel.frame.chart.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.data.general.Dataset;

import com.cintel.frame.chart.ChartType;


public class CreaterTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	//
	@SuppressWarnings("unused")
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private String name;
	
	private String dataSet;
	
	private String columnProperty;
	
    private String chartType = ChartType._BAR_CHART;
    
	private List<RowKeyInfo> rowKeysInfoList = new ArrayList<RowKeyInfo>();
	
	public String getColumnProperty() {
		return columnProperty;
	}

	public void setColumnProperty(String columnProperty) {
		this.columnProperty = columnProperty;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public void addRowKeyInfo(RowKeyInfo rowKeyInfo) {
		rowKeysInfoList.add(rowKeyInfo);
	}
	

	/**
	 * 
	 * @method: getList
	 * @return: Iterable
	 * @author: WangShuDa
	 * @param pageContext
	 * @return
	 * @throws Exception
	 */
	private Iterable getList(PageContext pageContext) throws Exception {
		Iterable list = Collections.EMPTY_LIST;
		Object listObj = pageContext.getRequest().getAttribute(name);
		
		if(listObj instanceof Iterable) {
			list = (Iterable)listObj;
		}
		else if(listObj instanceof Object[]) {
			log.debug("the list is an array of Object");
			list = Arrays.asList(listObj);
		}
		else {
			throw new Exception("The object getted from attribute with name:" + name + " is not Iterable or object array.");
		}
		//
		return list;
	}
	
    @Override
    public int doStartTag() throws JspException {
        int returnValue = EVAL_PAGE;
        //
        rowKeysInfoList.clear();
        //
        return returnValue;
    }
    
    protected MyCategoryDataset genCategoryDataset(Iterable list) throws Exception {
        MyCategoryDataset categoryDataset = new MyCategoryDataset();
        Double number;
        String columnValue;
        for(Object row:list) {
            columnValue = BeanUtils.getProperty(row, columnProperty);
            //
            for(RowKeyInfo rowKeyInfo:rowKeysInfoList) {
                number = Double.parseDouble(BeanUtils.getProperty(row, rowKeyInfo.getProperty()));
                
                categoryDataset.addValue(number, rowKeyInfo.getTitle(), columnValue);
            }
        }
        categoryDataset.setRowKeysInfoList(rowKeysInfoList);
        //
        return categoryDataset;
    }
    
    
    protected MyPieDataSet genPieDataset(Object obj) throws Exception {
        MyPieDataSet pieDataSet = new MyPieDataSet();
        Double number;        
        for(RowKeyInfo rowKeyInfo:rowKeysInfoList) {
            number = Double.parseDouble(BeanUtils.getProperty(obj, rowKeyInfo.getProperty()));
            
            pieDataSet.setValue(rowKeyInfo.getTitle(), number);
        }
        
        pieDataSet.setRowKeysInfoList(rowKeysInfoList);
        //
        return pieDataSet;
    }
	/**
	 * 
	 * @method: doEndTag
	 * @author: WangShuDa
	 * @return
	 * @throws JspException
	 */
	public int doEndTag() throws JspException {
		int returnValue = EVAL_PAGE;

		try {
            Dataset chartDataSet = null;
            //
            if(ChartType._PIE_CHART.equals(this.chartType)) {
                Object obj = pageContext.getRequest().getAttribute(name);;
                
                chartDataSet = this.genPieDataset(obj);
            }
            else {
                Iterable list = getList(pageContext);
                
                chartDataSet= this.genCategoryDataset(list);
            }
			//
			pageContext.setAttribute(dataSet, chartDataSet, PageContext.REQUEST_SCOPE);
		}
		catch(Exception ex) {
			throw new JspException(ex);
		}
		//
		return returnValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
}
