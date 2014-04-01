package com.cintel.frame.web.app.install;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;

public class InstallDaoImpl implements InstallDao {
	private String nameSpace;

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String namespace) {
		this.nameSpace = namespace;
	}
	
	public void executeSql(SqlMapClient sqlMapClient, String sql) {
		SqlMapClientTemplate sqlMapClientTemplate = new SqlMapClientTemplate();
		sqlMapClientTemplate.setSqlMapClient(sqlMapClient);
		
		sqlMapClientTemplate.update(nameSpace + ".executeSql" , sql);
	}
}
