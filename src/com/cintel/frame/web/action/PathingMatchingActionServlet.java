
package com.cintel.frame.web.action;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cintel.frame.util.FileUtils;

/**
 * 
 * @file    : PathingMatchingActionServlet.java
 * @author  : WangShuDa
 * @date    : 2009-6-17
 * @corp    : CINtel
 * @version : 1.1
 */
public class PathingMatchingActionServlet extends ActionServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void initOther() throws ServletException {
		try {
			String strutsFilesLocationConfigText = null;
			// get original config files, maybe pattern.
			strutsFilesLocationConfigText = getServletConfig().getInitParameter("config");
			if (strutsFilesLocationConfigText != null) {
				ServletContextResourcePatternResolver resolve = new ServletContextResourcePatternResolver(getServletConfig().getServletContext());
				
				String[] strutsFilesLocationArr = StringUtils.tokenizeToStringArray(strutsFilesLocationConfigText, ",; \t\n");

				StringBuffer strutsConfig = new StringBuffer();
				for (String path : strutsFilesLocationArr) {
					Resource[] resources = resolve.getResources(path.trim());
					for (Resource strutsFile : resources) {
						strutsConfig.append(",");
						strutsConfig.append(String.valueOf(strutsFile.getURL()));
						//
						LogFactory.getLog(this.getClass()).debug(strutsFile.getURL());
					}
				}
				config = strutsConfig.substring(1).toString();
				//
				LogFactory.getLog(this.getClass()).debug(config);
			}
		}
		catch (Exception e) {
			LogFactory.getLog(this.getClass()).error("", e);
			throw new ServletException();
		}

		// copy from super.initOther
		String value = getServletConfig().getInitParameter("convertNull");
		if ("true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value)
				|| "1".equalsIgnoreCase(value)) {

			convertNull = true;
		}

		if (convertNull) {
			ConvertUtils.deregister();
			ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
			ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);
			ConvertUtils.register(new BooleanConverter(null), Boolean.class);
			ConvertUtils.register(new ByteConverter(null), Byte.class);
			ConvertUtils.register(new CharacterConverter(null), Character.class);
			ConvertUtils.register(new DoubleConverter(null), Double.class);
			ConvertUtils.register(new FloatConverter(null), Float.class);
			ConvertUtils.register(new IntegerConverter(null), Integer.class);
			ConvertUtils.register(new LongConverter(null), Long.class);
			ConvertUtils.register(new ShortConverter(null), Short.class);
		}
		// end copy from super.initOther
	}

	@Override
	protected void parseModuleConfigFile(Digester digester, String path) throws UnavailableException {
		InputStream input = null;
		try {
			Resource resource = new UrlResource(path);
			//
			LogFactory.getLog(this.getClass()).debug(resource.getURL());
			//
			InputSource is = new InputSource(resource.getURL().toExternalForm());
			input = resource.getInputStream();
			//
			is.setByteStream(input);
			digester.parse(is);
		} catch (MalformedURLException e) {
			handleConfigException(path, e);
		} catch (IOException e) {
			handleConfigException(path, e);
		} catch (SAXException e) {
			handleConfigException(path, e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new UnavailableException(e.getMessage());
				}
			}
		}
	}

	private void handleConfigException(String path, Exception e) throws UnavailableException {
		String msg = internal.getMessage("configParse", path);
		log.error(msg, e);
		throw new UnavailableException(msg);
	}

	public static void main(String[] args) throws IOException {
		String xmlFileInJar = "jar:file:D:/ProgramFiles/Java/jakarta-tomcat-5.0.28-deploy/common/lib/crs-frame.jar!/DefaultInesMsgTemplate.xml";
		if(xmlFileInJar.startsWith("jar:file")) {
			URL url = new URL(xmlFileInJar);
			InputStream input = url.openStream();

			System.out.println(new String(FileUtils.copyToByteArray(input)));
		}
		

	}
}
