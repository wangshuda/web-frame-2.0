package com.cintel.frame.webui;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.FormatUtils;
import com.cintel.frame.web.page.PageInfo;
import com.cintel.frame.web.page.PagedList;

/**
 * 
 * @file    : DomainService.java
 * @author  : WangShuDa
 * @date    : 2008-9-4
 * @corp    : CINtel
 * @version : 1.1
 * @desc    : 1.1 --> change the return type from void to ErrorInfo for the method of insert.
 */
public class DomainService implements IDomainService {
	@SuppressWarnings("unused")
	protected Log log = LogFactory.getLog(this.getClass());

	protected IDomainDao dao;

	//
	public void setDao(IDomainDao dao) {
		this.dao = dao;
	}

    protected List doSearch(Object searchVo) {
        return dao.search(searchVo);
    }
    
	@SuppressWarnings("unchecked")
	public List search(Object searchVo) {
		if (searchVo instanceof Map) {
			FormatUtils.escapeForSql((Map) searchVo, true);
		}
		//
		List resultList = this.doSearch(searchVo);
		//
		if (searchVo instanceof Map) {
			FormatUtils.escapeForSql((Map) searchVo, false);
		}
		//
		return resultList;
	}

	public IDomainVo get(IDomainVo vo) {
		return dao.get(vo);
	}

	public Object get(String statementName, Object parameterObject) {
		return dao.get(statementName, parameterObject);
	}

	public List list(Object listConditions) {
		return dao.list(listConditions);
	}

	public List list(String statementName, Object parameterObject) {
		return dao.list(statementName, parameterObject);
	}

	public List list(String statementName, Object parameterObject, int skipResults, int maxResults) {
		return dao.list(statementName, parameterObject, skipResults, maxResults);
	}

	public void insert(IDomainVo domainVo) {
		dao.insert(domainVo);
	}

	public void insert(String statementName, IDomainVo domainVo) {
		dao.insert(statementName, domainVo);
	}

	public int delete(IDomainVo vo) {
		return dao.delete(vo);
	}

	public int delete(String id) {
		return dao.delete(id);
	}
	
	public int delete(String statementName, Object parameterObject) {
		return dao.delete(statementName, parameterObject);
	}
	
	public int batchDelete(Map parameterMap) {
		return dao.batchDelete(parameterMap);
	}
	
	public int batchDelete(List selectedIdsList) {
		return dao.batchDelete(selectedIdsList);
	}
	
	public int update(IDomainVo domainVo) {
		return dao.update(domainVo);
	}
	
	public int update(String statementName, Object parameterObject) {
		return dao.update(statementName, parameterObject);
	}

	public List getJsonOption(Object object) {
		return dao.list("listJsonOption", object);
	}

	/**
	 * @return : if exist return true, or else return false.
	 */
	public boolean isExist(Object object) {
		Object countResult =  dao.get("isUnique", object);
		return ((countResult != null && (Integer)countResult > 0) ? true : false);
	}
	

	public int total(Object parameterObject) {
		return dao.total(parameterObject);
	}

	public int total(String statementName, Object parameterObject) {
		return dao.total(statementName, parameterObject);
	}
	
	@SuppressWarnings("unchecked")
	public PagedList pageSearch(PageInfo pageInfo, Object queryConditinObj) {
		PagedList<Object> pagedList = new PagedList<Object>();
		//
		int totalCount = 0;
		if (pageInfo == null || pageInfo.isFirstQuery()) {
			totalCount = dao.total(queryConditinObj);
		} else {
			totalCount = pageInfo.getTotalCount();
		}			
		((Map) queryConditinObj).put("pageInfo", pageInfo);
		List resultList = dao.pageSearch(queryConditinObj);
		if (resultList != null && resultList.size() > 0) {
			pagedList.setDataArr(resultList.toArray());
			pagedList.setPageDataList(resultList);
		} else {
			pagedList.setDataArr(new Object[0]);
			pagedList.setPageDataList(Collections.EMPTY_LIST);			
		}
		pagedList.setTotalCount(totalCount);
		return pagedList;				
	}

	public IDomainDao getDao() {
		return dao;
	}	
}
