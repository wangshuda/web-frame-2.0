package com.cintel.frame.web.app.install;

import java.util.List;
import java.util.Map;

import com.cintel.frame.web.app.install.sql.EntitySQLContext;

/**
 * @file : $Id: InstallService.java 13450 2009-12-17 00:30:18Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
*/
public interface InstallService  {
	public Map<String, Map<String, EntitySQLContext>> getDbContextMap();
	
	public List<SqlExecuteResult> executeCreateSql(String sqlMapClientName, String[] entityNameArr);
	
	public List<SqlExecuteResult> executeReCreateSql(String sqlMapClientName, String[] entityNameArr);
	
	public List<SqlExecuteResult> executeDropSql(String sqlMapClientName, String[] entityNameArr);
	
	public List<SqlExecuteResult> executeCheckSql(String sqlMapClientName, String[] entityNameArr);
}
