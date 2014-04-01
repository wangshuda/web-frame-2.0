package com.cintel.frame.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-11-15 wangshuda created
 */
public class ResourceListLoader implements FactoryBean, ServletContextAware {
	private static Log log = LogFactory.getLog(ResourceListLoader.class);
	//
	private String resourceLocation;
	
	private List<Resource> resourceList = new ArrayList<Resource>(0);
	
	// -------------------- methods for FactoryBean
	public Object getObject() throws Exception {
		return resourceList;
	}

	public Class getObjectType() {
		return List.class;
	}

	public boolean isSingleton() {
		return true;
	}

	// --------- methods for ServletContextAware
	public void setServletContext(ServletContext servletCtx) {
		String[] resourceLocationArr = StringUtils.tokenizeToStringArray(resourceLocation, ",; \t\n");
        try {
        	ServletContextResourcePatternResolver resolve = new ServletContextResourcePatternResolver(servletCtx);

            for (String path : resourceLocationArr) {
                Resource[] resources = resolve.getResources(path.trim());
                for (Resource resource : resources) {
                    resourceList.add(resource);
                }
            }
        }
        catch(IOException ex) {
            log.error(new StringBuffer("Loading resource failed with :").append(resourceLocation), ex);
        }
	}

	// --------------------------- get/set methods ---------------------------
	public String getResourceLocation() {
		return resourceLocation;
	}

	public void setResourceLocation(String resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

}
