package com.cintel.frame.webui;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class CharTypeHandler implements TypeHandlerCallback {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(CharTypeHandler.class);
	
	public Object getResult(ResultGetter getter) throws SQLException {
		String str = getter.getString();
		return (str == null ? str: str.trim());
	}

	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		String str = ((String) parameter);
		setter.setString(str);
	}

	public Object valueOf(String str) {
		return (str == null ? str : str.trim());
	}
}
