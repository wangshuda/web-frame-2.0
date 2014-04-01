package com.cintel.frame.web.app.install;

import com.ibatis.sqlmap.client.SqlMapClient;

public interface InstallDao {
	public void executeSql(SqlMapClient sqlMapClient, String sql);
}
