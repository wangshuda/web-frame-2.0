package com.cintel.frame.chart;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.cintel.frame.util.StringUtils;

/**
 * 
 * @file    : Chart2ImageServlet.java
 * @author  : WangShuDa
 * @date    : 2008-10-14
 * @corp    : CINtel
 * @version : 1.0
 */
public class Chart2ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * 
	 * @method: doGet
	 * @author: WangShuDa
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("Call doPost!");
		
		String chartId = request.getParameter("chartId");
		String widthStr = request.getParameter("width");
		String heightStr = request.getParameter("height");
		String removeFromSession = request.getParameter("removeFromSession");
		
		try {
			int width = 400;
			int height = 300;
			
			OutputStream out = response.getOutputStream();
			
			JFreeChart chart = (JFreeChart)request.getSession().getAttribute(chartId);
			//if removeFromSession is true, then the remove action should be done after getting immediately.
			if(StringUtils.hasText(removeFromSession) && Boolean.parseBoolean(removeFromSession)) {
				log.debug("Remove chart:" + chartId + " from session!");
				request.getSession().removeAttribute(chartId);
			}
			//
			if(chart != null) {
				//
				if(StringUtils.hasText(widthStr)) {
					width = Integer.parseInt(widthStr);
				}
				if(StringUtils.hasText(heightStr)) {
					height = Integer.parseInt(heightStr);
				}
				//
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				//
				response.setContentType("image/png");
				
				ChartUtilities.writeChartAsPNG(out, chart, width, height);
			}
			else {
				log.warn("the chart saved in the session with chartId [" + chartId + "] is null!");
			}
		}
		catch(Exception ex) {
			log.debug("Chart2ImageServlet doPost error !", ex);
		}
		//
	}
}
