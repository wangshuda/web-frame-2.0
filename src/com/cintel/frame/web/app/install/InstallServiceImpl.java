package com.cintel.frame.web.app.install;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.web.app.install.sql.EntityDDLContext;
import com.cintel.frame.web.app.install.sql.EntitySQLContext;
import com.cintel.frame.web.app.install.sql.EntityType;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @file : $Id: InstallServiceImpl.java 15770 2010-01-14 06:24:41Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
*/
@SuppressWarnings("unchecked")
public class InstallServiceImpl implements InstallService {
	private static Log log = LogFactory.getLog(InstallServiceImpl.class);
	//
	private InstallDao installDao;
	//
	private Map<String, EntityDDLContext> sqlTemplateMap = new HashMap<String, EntityDDLContext>();
	// <'SqlMapClientName', Map<'table/index/view name', EntitySQLContext>>, 
	// and Map<'table/index/view name', EntitySQLContext> load by com.cintel.frame.web.app.install.sql.DatabaseContextLoader
	private Map<String, Map<String, EntitySQLContext>> dbContextMap = Collections.EMPTY_MAP;
	
	// using com.cintel.frame.web.app.install.sql.SqlMapClientLoader to load the map in spring.
	private Map<String, SqlMapClient> sqlMapClientNameMap = Collections.EMPTY_MAP;
	//
	private void executeSql(String sqlMapClientName, String sql) {
		SqlMapClient sqlMapClient = sqlMapClientNameMap.get(sqlMapClientName);
		this.getInstallDao().executeSql(sqlMapClient, sql);
	}

	private String getEntityDropSql(String sqlMapClientName, String entityName) {
		Map<String, EntitySQLContext> sqlCtxMap = dbContextMap.get(sqlMapClientName);
		EntitySQLContext sqlCtx = sqlCtxMap.get(entityName);
		//
		String entityDropSql = null;
		if(sqlCtx.getType().equals(EntityType.index.toString())) {
			//
			String dependTable = sqlCtx.getDependTable();
			entityDropSql = String.format(sqlTemplateMap.get(EntityType.index.toString()).getDropTemplateSqlText(), dependTable, entityName);
		}
		else {
			//
			entityDropSql = String.format(sqlTemplateMap.get(sqlCtx.getType()).getDropTemplateSqlText(), entityName);
		}
		return entityDropSql;
	}
	
	public List<SqlExecuteResult> executeCreateSql(String sqlMapClientName, String[] entityNameArr) {
		Map<String, EntitySQLContext> sqlCtxMap = this.getDbContextMap().get(sqlMapClientName);
		//
		List<SqlExecuteResult> sqlExecuteResultList = new LinkedList<SqlExecuteResult>();
		EntitySQLContext entitySqlCtx = null;
		SqlExecuteResult sqlExecuteResult = null;
		
		if(entityNameArr != null) {
			for(String entityName:entityNameArr) {
				entitySqlCtx = sqlCtxMap.get(entityName);
				sqlExecuteResult = new SqlExecuteResult(entitySqlCtx.getName(), entitySqlCtx.getType());
				try {
					this.executeSql(sqlMapClientName, entitySqlCtx.getCreateSqlText());
				}
				catch(Exception ex) {
					log.error("", ex);
					sqlExecuteResult.parseException(ex);
				}
				//
				sqlExecuteResultList.add(sqlExecuteResult);
			}
		}
		//
		return sqlExecuteResultList;
	}
	

	public List<SqlExecuteResult> executeReCreateSql(String sqlMapClientName, String[] entityNameArr) {
		Map<String, EntitySQLContext> sqlCtxMap = this.getDbContextMap().get(sqlMapClientName);
		//
		List<SqlExecuteResult> sqlExecuteResultList = new LinkedList<SqlExecuteResult>();
		EntitySQLContext entitySqlCtx = null;
		SqlExecuteResult sqlExecuteResult = null;
		if(entityNameArr != null) {
			for(String entityName:entityNameArr) {
				entitySqlCtx = sqlCtxMap.get(entityName);
				sqlExecuteResult = new SqlExecuteResult(entitySqlCtx.getName(), entitySqlCtx.getType());
				//
				try {
					this.executeSql(sqlMapClientName, this.getEntityDropSql(sqlMapClientName, entityName)); // drop
					//
					this.executeSql(sqlMapClientName, entitySqlCtx.getCreateSqlText());  //create
				}
				catch(Exception ex) {
					log.error("", ex);
					sqlExecuteResult.parseException(ex);
				}
				//
				sqlExecuteResultList.add(sqlExecuteResult);
			}
		}
		return sqlExecuteResultList;
	}
	
	public List<SqlExecuteResult> executeDropSql(String sqlMapClientName, String[] entityNameArr) {
		Map<String, EntitySQLContext> sqlCtxMap = this.getDbContextMap().get(sqlMapClientName);
		//
		List<SqlExecuteResult> sqlExecuteResultList = new LinkedList<SqlExecuteResult>();
		EntitySQLContext entitySqlCtx = null;
		SqlExecuteResult sqlExecuteResult = null;
		if(entityNameArr != null) {
			for(String entityName:entityNameArr) {
				entitySqlCtx = sqlCtxMap.get(entityName);
				sqlExecuteResult = new SqlExecuteResult(entitySqlCtx.getName(), entitySqlCtx.getType());
				//
				try {
					this.executeSql(sqlMapClientName, this.getEntityDropSql(sqlMapClientName, entityName));
				}
				catch(Exception ex) {
					log.error("", ex);
					sqlExecuteResult.parseException(ex);
				}
				//
				sqlExecuteResultList.add(sqlExecuteResult);
			}
		}
		return sqlExecuteResultList;
	}
	
	public List<SqlExecuteResult> executeCheckSql(String sqlMapClientName, String[] entityNameArr) {
		Map<String, EntitySQLContext> sqlCtxMap = this.getDbContextMap().get(sqlMapClientName);
		//
		List<SqlExecuteResult> sqlExecuteResultList = new LinkedList<SqlExecuteResult>();
		EntitySQLContext entitySqlCtx = null;
		SqlExecuteResult sqlExecuteResult = null;
		String executeSqlText = null;
		if(entityNameArr != null) {
			for(String entityName:entityNameArr) {
				entitySqlCtx = sqlCtxMap.get(entityName);
				sqlExecuteResult = new SqlExecuteResult(entitySqlCtx.getName(), entitySqlCtx.getType());
				executeSqlText = String.format(sqlTemplateMap.get(entitySqlCtx.getType()).getCheckTemplateSqlText(), entityName);
				//
				try {
					this.executeSql(sqlMapClientName, executeSqlText);
				}
				catch(Exception ex) {
					log.error("", ex);
					sqlExecuteResult.parseException(ex);
				}
				//
				sqlExecuteResultList.add(sqlExecuteResult);
			}
		}
		return sqlExecuteResultList;
	}
	// -------------------------- get/set methods --------------------------
	public InstallDao getInstallDao() {
		return installDao;
	}

	public void setInstallDao(InstallDao installDao) {
		this.installDao = installDao;
	}

	public Map<String, Map<String, EntitySQLContext>> getDbContextMap() {
		return dbContextMap;
	}

	public void setDbContextMap(Map<String, Map<String, EntitySQLContext>> dbContextMap) {
		this.dbContextMap = dbContextMap;
	}
	
	public Map<String, EntityDDLContext> getSqlTemplateMap() {
		return sqlTemplateMap;
	}

	public void setSqlTemplateMap(Map<String, EntityDDLContext> sqlTemplateMap) {
		this.sqlTemplateMap = sqlTemplateMap;
	}

	public Map<String, SqlMapClient> getSqlMapClientNameMap() {
		return sqlMapClientNameMap;
	}

	public void setSqlMapClientNameMap(Map<String, SqlMapClient> sqlMapClientNameMap) {
		this.sqlMapClientNameMap = sqlMapClientNameMap;
	}
	
}
