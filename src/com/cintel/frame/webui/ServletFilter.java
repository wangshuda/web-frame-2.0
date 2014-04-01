package com.cintel.frame.webui;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServletFilter implements Filter {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(ServletFilter.class);
			
	protected boolean ignore = true;
	protected String encoding = null;
	protected FilterConfig filterConfig = null;
	
	public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
	}

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (ignore || (request.getCharacterEncoding() == null)) {
            if (this.encoding != null) {
                request.setCharacterEncoding(encoding);
            }
        }
        chain.doFilter(request, response);
	}
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        
        if (value == null) {
            this.ignore = true;
        }
        else if (value.equalsIgnoreCase("true")) {
            this.ignore = true;
        }
        else if (value.equalsIgnoreCase("yes")) {
            this.ignore = true;
        }
        else {
            this.ignore = false;
        }
    }
}
