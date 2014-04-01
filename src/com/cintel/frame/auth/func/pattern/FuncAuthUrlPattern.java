package com.cintel.frame.auth.func.pattern;

import com.cintel.frame.webui.IDomainVo;

public class FuncAuthUrlPattern implements IDomainVo {

	private int id;

	private int funcId;

	private String authUrlPattern;

	public String getOid() {
		return String.valueOf(id);
	}

	public void setOid(String oid) {
		if (oid == null) {
			return;
		}
		String oidStr[] = oid.split(",");
		id = Integer.parseInt(oidStr[0]);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFuncId() {
		return funcId;
	}

	public void setFuncId(int funcId) {
		this.funcId = funcId;
	}

	public String getAuthUrlPattern() {
		return authUrlPattern;
	}

	public void setAuthUrlPattern(String authUrlPattern) {
		this.authUrlPattern = authUrlPattern;
	}

}