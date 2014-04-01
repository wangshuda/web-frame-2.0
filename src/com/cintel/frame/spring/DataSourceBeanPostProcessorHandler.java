package com.cintel.frame.spring;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;

import com.cintel.frame.util.StringUtils;
import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;

/**
 * 
 * @version $Id: DataSourceBeanPostProcessorHandler.java 16631 2010-02-03 05:41:19Z wangshuda $
 * @history 
 *          1.0.0 2010-2-2 wangshuda created
 */
@SuppressWarnings("unchecked")
public class DataSourceBeanPostProcessorHandler extends BeanPostProcessorHandlerAbstractImpl implements BeanPostProcessorHandler  {
	private Log log = LogFactory.getLog(this.getClass());
	//
	private String backUpJdbcURL;
	
	private boolean checkDataSource(DataSource dataSource) {
		Connection connection = null;
		boolean dbIsOk = false;
		try {
			connection = dataSource.getConnection();
			//
			dbIsOk = !connection.isReadOnly();
		}
		catch (SQLException ex) {
			log.fatal("Failed when load the jdbc connection. Please check the the database server or the jdbc config!", ex);
			dbIsOk = false;
		}
		finally {
			if(connection != null) {
				try {
					connection.close();
					connection = null;
				} 
				catch (SQLException ex) {
					dbIsOk = false;
					log.fatal("Failed when close the jdbc connection. Please check the the database server or the jdbc config!", ex);
				}
			}
			
		}
		//
		return dbIsOk;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object beanObj, String beanName) throws BeansException {
		if(beanObj instanceof javax.sql.DataSource) {
			if(log.isDebugEnabled()) {
				log.debug(String.format("Check the connection for the spring bean:%s", beanName));
			}
			//
			DataSource dataSource = (DataSource)beanObj;
			//
			boolean dbIsOk = this.checkDataSource(dataSource);
			if(!dbIsOk) {
				if(dataSource instanceof org.apache.commons.dbcp.BasicDataSource) {
					log.warn(String.format("JDBC with URL:%s is read only or can nod access!", ((BasicDataSource)dataSource).getUrl()));
					//
					if(StringUtils.hasText(backUpJdbcURL)) {
						((BasicDataSource)(dataSource)).setUrl(backUpJdbcURL);
						
						boolean backUpDbIsOk = this.checkDataSource(dataSource);
						if(!backUpDbIsOk) {
							log.fatal(String.format("Backup JDBC URL :%s is also read only or can not Access!", ((BasicDataSource)dataSource).getUrl()));
							//
							throw new ErrorReportException(new ErrorInfo("Sys.error.DbAccessBad", new String[]{beanName}));
						}
						else {
							log.warn(String.format("backUpJdbcURL:%s is Ok! ", backUpJdbcURL));
							//
							((BasicDataSource)beanObj).setUrl(backUpJdbcURL);
						}
					}
					else {
						throw new ErrorReportException(new ErrorInfo("Sys.error.DbAccessBad", new String[]{beanName}));
					}
				}
				else {
					log.warn("Can only support org.apache.commons.dbcp.BasicDataSource for back up jdbc url.");
					//
					log.fatal(String.format("Data Source: %s is read only!", beanName));
					//
					throw new ErrorReportException(new ErrorInfo("Sys.error.DbAccessBad", new String[]{beanName}));
				}
			}
		}
		return beanObj;
	}

	public String getBackUpJdbcURL() {
		return backUpJdbcURL;
	}

	public void setBackUpJdbcURL(String backUpJdbcURL) {
		this.backUpJdbcURL = backUpJdbcURL;
	}
}
