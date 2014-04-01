package com.cintel.frame.webui;

import java.util.List;
import java.util.Map;



public interface IDomainDao {
	
	public List list(Object listConditions);
	public List list(String statementName, Object parameterObject);
	public List list(String statementName, Object parameterObject, int skipResults, int maxResults);
	
	public IDomainVo get(IDomainVo vo);
	public Object get(String statementName, Object parameterObject);
	
	public void insert(IDomainVo domainVo);
	public void insert(String statementName, IDomainVo domainVo);
	
	public int delete(IDomainVo vo);
	public int delete(String id);
	public int delete(String statementName, Object parameterObject);
	public int batchDelete(Map parameterMap);
	public int batchDelete(List selectedIdsList);
	
	public int update(IDomainVo domainVo);
	
	public int update(String statementName, Object parameterObject);
	
	public List search(Object searchVo);
	
	public int total(Object parameterObject);		
	
	public int total(String statementName, Object parameterObject);
	
	public List pageSearch(Object searchVo);	
}
