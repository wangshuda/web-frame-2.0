package com.cintel.frame.web.app.help;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

public class WebHelpTextLoader implements ServletContextAware{
	private static Log log = LogFactory.getLog(WebHelpTextLoader.class);
	
	private String webHelpTextLocation;
	 
    private ServletContextResourcePatternResolver resolve;
    
    public void setServletContext(ServletContext servletContext)  {
        resolve = new ServletContextResourcePatternResolver(servletContext);
        this.loadWebHelpText();
    }
    
    public List<Resource> loadWebHelpText() {
        String[] webHelpTextLocationArr = StringUtils.tokenizeToStringArray(webHelpTextLocation, ",; \t\n");
        try {
            List<Resource> resourceList = new LinkedList<Resource>();
            
            for (String path : webHelpTextLocationArr) {
                Resource[] resources = resolve.getResources(path.trim());
                for (Resource resource : resources) {
                    resourceList.add(resource);
                }
            }
            return resourceList;
        }
        catch(IOException ex) {
            log.error("", ex);
        }
        return null;
    } 
   
    //-----------------get and set methods--------------------------
	public String getWebHelpTextLocation() {
		return webHelpTextLocation;
	}

	public void setWebHelpTextLocation(String webHelpTextLocation) {
		this.webHelpTextLocation = webHelpTextLocation;
	}
	
}
