package com.cintel.frame.chart;

import java.awt.Color;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;

import com.cintel.frame.chart.dataset.MyCategoryDataset;
import com.cintel.frame.chart.dataset.RowKeyInfo;
import com.cintel.frame.util.StringUtils;


/**
 * 
 * @file    : ChartCreater.java
 * @author  : WangShuDa
 * @date    : 2008-10-15
 * @corp    : CINtel
 * @version : 1.0
 */
public class ChartCreater {
	
	/**
	 * 
	 * @method: chartToImage
	 * @return: void
	 * @author: WangShuDa
	 * @param chart
	 * @param out
	 * @param dataSet
	 * @param imgWidth
	 * @param imgHeight
	 * @throws IOException
	 */
	private static void chartToImage(JFreeChart chart) throws IOException {

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.WHITE);

		// get a reference to the plot for further customisation...
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.WHITE);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.WHITE);
		//
		//
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		//

	}

	/**
	 * 
	 * @method: createLineChart
	 * @return: void
	 * @author: WangShuDa
	 * @param out
	 * @param categoryDataset
	 * @param title
	 * @param horizontalTitle
	 * @param verticalTitle
	 * @param imgWidth
	 * @param imgHeight
	 * @throws IOException
	 */
	public static JFreeChart createLineChart(Dataset dataset, String title, String horizontalTitle, String verticalTitle
			 ) throws Exception {
        CategoryDataset categoryDataset = (CategoryDataset)dataset;
        
		// create the chart...
		JFreeChart chart = ChartFactory.createLineChart(title, // chart
																			// title
				horizontalTitle, // domain axis label
				verticalTitle, // range axis label
				categoryDataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		//customise the renderer...
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setBaseItemLabelsVisible(true);
		//
		configRowsColor(categoryDataset, renderer);
		//
		ChartCreater.chartToImage(chart);
		//
		return chart;
	}

    public static JFreeChart createPieChart(Dataset dataSet , String title) throws Exception {
        PieDataset pieDataset = (PieDataset)dataSet;
        // create the chart...
        JFreeChart chart = ChartFactory.createPieChart3D(title, pieDataset, true, false, false);
        
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        //customise the renderer...
        PiePlot piePlot = (PiePlot) chart.getPlot();
        piePlot.setBackgroundAlpha(0.5f);
        piePlot.setForegroundAlpha(0.5f);
        piePlot.setToolTipGenerator(new StandardPieToolTipGenerator());
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}:({2})"));
        piePlot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}:({2})"));        
        //
        return chart;
    }
    
	/**
	 * 
	 * @method: configRowsColor
	 * @return: void
	 * @author: WangShuDa
	 * @param categoryDataset
	 * @param renderer
	 * @throws Exception
	 */
	private static void configRowsColor(CategoryDataset categoryDataset, CategoryItemRenderer renderer) throws Exception {
		if(categoryDataset instanceof MyCategoryDataset) {
			int index = 0;
			MyCategoryDataset myCategoryDataset = (MyCategoryDataset)categoryDataset;
			
			for(RowKeyInfo rowKeyInfo:myCategoryDataset.getRowKeysInfoList()) {
				index = categoryDataset.getRowIndex(rowKeyInfo.getTitle());
				String color = rowKeyInfo.getColor();
				if(StringUtils.hasText(color)) {
					renderer.setSeriesPaint(index, ColorFactory.getColor(color));
				}
				else {
					renderer.setSeriesPaint(index, ColorFactory.getColor(index));
				}
			}
		}
		else {
			for(int i = 0; i < categoryDataset.getRowCount(); i ++) {
				renderer.setSeriesPaint(i, ColorFactory.getColor(i));
			}
		}

	}
	
	/**
	 * 
	 * @method: createBarChart
	 * @return: void
	 * @author: WangShuDa
	 * @param out
	 * @param categoryDataset
	 * @param title
	 * @param horizontalTitle
	 * @param verticalTitle
	 * @param imgWidth
	 * @param imgHeight
	 * @throws IOException
	 */
	public static JFreeChart createBarChart(Dataset dataset, String title, String horizontalTitle, String verticalTitle) throws Exception {

        CategoryDataset categoryDataset = (CategoryDataset)dataset;
		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart3D(title, // chart
																			// title
				horizontalTitle, // domain axis label
				verticalTitle, // range axis label
				categoryDataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		// set the range axis to display integers only...
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setBaseItemLabelsVisible(true);
		//
		configRowsColor(categoryDataset, renderer);
		//
		ChartCreater.chartToImage(chart);
		//
		return chart;
	}
}
