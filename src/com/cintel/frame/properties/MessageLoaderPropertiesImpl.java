package com.cintel.frame.properties;

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


public class MessageLoaderPropertiesImpl implements MessageLoader, ServletContextAware{
    private static Log log = LogFactory.getLog(MessageLoaderPropertiesImpl.class);

    private String propertiesFileLocation;
        
    public String getMessage(String key) {
        return PropertiesUtils.getMessage(key);
    }
    
    public void loadProperties() {
        String[] proFilesLocationArr = StringUtils.tokenizeToStringArray(propertiesFileLocation, ",; \t\n");
        try {
            List<Resource> resourceList = new LinkedList<Resource>();
            
            for (String path : proFilesLocationArr) {
                Resource[] resources = resolve.getResources(path.trim());
                for (Resource resource : resources) {
                    resourceList.add(resource);
                }
            }
            
            //
            int i = 0;
            String filePath;
            String[] parsedPropertiesFilePath = new String[resourceList.size()];
            for(Resource resource:resourceList) {
                filePath = resource.getFile().getAbsolutePath();
                parsedPropertiesFilePath[i++] = filePath;
                //
                if(log.isDebugEnabled()) {
                    log.debug(filePath);
                }
            }
            //
            PropertiesUtils.initial(parsedPropertiesFilePath);
        }
        catch(IOException ex) {
            log.error("", ex);
        }
    }

    
    private ServletContextResourcePatternResolver resolve;
    
    public void setServletContext(ServletContext servletContext)  {
        resolve = new ServletContextResourcePatternResolver(servletContext);
        this.loadProperties();
    }


    public String getPropertiesFileLocation() {
        return propertiesFileLocation;
    }


    public void setPropertiesFileLocation(String propertiesFileLocation) {
        this.propertiesFileLocation = propertiesFileLocation;
    }
}
