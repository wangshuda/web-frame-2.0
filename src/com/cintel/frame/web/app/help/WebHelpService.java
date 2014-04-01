package com.cintel.frame.web.app.help;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.core.io.Resource;

import com.cintel.frame.util.FileUtils;
import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;
import com.cintel.frame.webui.DomainService;
import com.cintel.frame.webui.IDomainVo;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-9-5 wangshuda created
 */
public class WebHelpService extends DomainService implements IWebHelpService{	
	private WebHelpTextLoader webHelpTextLoader;

	@Override
	public IDomainVo get(IDomainVo vo) {
		IDomainVo domainVo = super.get(vo);
		if (domainVo == null) {
			WebHelpInfo webHelpInfo = (WebHelpInfo) vo;
			List<Resource> list = webHelpTextLoader.loadWebHelpText();

			String helpText = "";
			String helpKey = webHelpInfo.getHelpKey();
			try {
				for (Resource resource : list) {
					String fileName = resource.getFilename();
					if (fileName.contains(helpKey)) {						
						InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(resource.getFile()), "UTF-8");

						helpText = FileUtils.copyToString(inputStreamReader);
						break;
					}
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
			webHelpInfo.setHelpText(helpText);
			domainVo = webHelpInfo;			
		}
		return domainVo;
	}
	
	@Override
	public void insert(IDomainVo domainVo) {
		if(domainVo instanceof WebHelpInfo){
			WebHelpInfo webHelpInfo = (WebHelpInfo) domainVo;
			String helpKey = webHelpInfo.getHelpKey();
			if(this.isExist(helpKey)){
				throw new ErrorReportException(new ErrorInfo("error.webHelpInfo.helpKeyExist", helpKey));
			}
			super.insert(domainVo);
		}
		
	}
	
	
	//---------------get and set methods-------------------	

	public WebHelpTextLoader getWebHelpTextLoader() {
		return webHelpTextLoader;
	}

	public void setWebHelpTextLoader(WebHelpTextLoader webHelpTextLoader) {
		this.webHelpTextLoader = webHelpTextLoader;
	}


	
	
}
