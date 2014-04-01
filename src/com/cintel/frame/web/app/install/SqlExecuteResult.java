package com.cintel.frame.web.app.install;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.cintel.frame.web.app.install.sql.EntityType;

/**
 * @file : $Id: SqlExecuteResult.java 13450 2009-12-17 00:30:18Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
*/
public class SqlExecuteResult {

	private String name;
	
	private EntityType type;
	
	private boolean checkOk = true;
	
	private String executedSqlText;
	
	private String reportMsg;

	public SqlExecuteResult(String name, String type) {
		this.checkOk = true;
		this.name = name;
		this.type = EntityType.valueOf(type);
	}
	//
	public void parseException(Exception ex) {
		this.checkOk = false;
		//
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintStream(out));
		this.reportMsg = new String(out.toByteArray());
	}
	
	public boolean isCheckOk() {
		return checkOk;
	}

	public void setCheckOk(boolean checkOk) {
		this.checkOk = checkOk;
	}

	public String getReportMsg() {
		return reportMsg;
	}

	public void setReportMsg(String reportMsg) {
		this.reportMsg = reportMsg;
	}

	public String getExecutedSqlText() {
		return executedSqlText;
	}

	public void setExecutedSqlText(String executedSqlText) {
		this.executedSqlText = executedSqlText;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

}
