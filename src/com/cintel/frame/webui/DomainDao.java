package com.cintel.frame.webui;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


public class DomainDao extends SqlMapClientDaoSupport implements IDomainDao {
	protected String nameSpace;

	protected String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String namespace) {
		this.nameSpace = namespace;
	}

	public void insert(IDomainVo domainVo) {
		getSqlMapClientTemplate().insert(getNameSpace() + ".insert", domainVo);
	}

	public void insert(String statementName, IDomainVo domainVo) {
		getSqlMapClientTemplate().insert(getNameSpace() + "." + statementName, domainVo);
	}

	public int delete(IDomainVo domainVo) {
		return getSqlMapClientTemplate().delete(getNameSpace() + ".delete", domainVo);
	}

	public int delete(String id) {
		return getSqlMapClientTemplate().delete(getNameSpace() + ".delete", id);
	}

	public int batchDelete(Map parameterMap) {
		return getSqlMapClientTemplate().delete(getNameSpace() + ".batchDelete", parameterMap);
	}

	public int batchDelete(List selectedIdsList) {
		return getSqlMapClientTemplate().delete(getNameSpace() + ".batchDelete", selectedIdsList);
	}
	
	public int delete(String statementName, Object parameterObject) {
		return getSqlMapClientTemplate().delete(getNameSpace()+"."+statementName, parameterObject);
	
	}
	public int update(IDomainVo domainVo) {
		return getSqlMapClientTemplate().update(getNameSpace() + ".update", domainVo);
	}
	
	public int update(String statementName, Object parameterObject) {
		return getSqlMapClientTemplate().update(getNameSpace() + "." + statementName, parameterObject);
	}

	public IDomainVo get(IDomainVo domainVo) {
		return (IDomainVo) getSqlMapClientTemplate().queryForObject(getNameSpace() + ".get", domainVo);
	}

	public Object get(String statementName, Object parameterObject) {
		Object result = getSqlMapClientTemplate().queryForObject(getNameSpace() + "." + statementName, parameterObject);
		if(result instanceof String) {
			return result == null ? result : String.valueOf(result).trim();
		}
		else {
			return result;
		}
	}

	public List list(Object listConditions) {
		return getSqlMapClientTemplate().queryForList(getNameSpace() + ".list", listConditions);
	}

	public List list(String statementName, Object parameterObject) {
		return getSqlMapClientTemplate().queryForList(getNameSpace() + "." + statementName, parameterObject);
	}

	public List list(String statementName, Object parameterObject, int skipResults, int maxResults) {
		return getSqlMapClientTemplate().queryForList(getNameSpace() + "." + statementName, parameterObject, skipResults, maxResults);
	}
	
	public List search(Object searchVo) {
		return getSqlMapClientTemplate().queryForList(getNameSpace() + ".search", searchVo);
	}
	
	public int total(Object parameterObject){
		return ((Integer)getSqlMapClientTemplate().queryForObject(getNameSpace() + ".total", parameterObject)).intValue();
	}
	
	public int total(String statementName, Object parameterObject){
		return ((Integer)getSqlMapClientTemplate().queryForObject(getNameSpace() + "." + statementName, parameterObject)).intValue();
	}
	
	public List pageSearch(Object searchVo){	
		return getSqlMapClientTemplate().queryForList(getNameSpace() + ".pageSearch", searchVo);
	}
	
}
