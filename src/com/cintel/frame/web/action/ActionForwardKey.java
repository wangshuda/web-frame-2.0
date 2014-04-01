package com.cintel.frame.web.action;

public class  ActionForwardKey {
	private String error;
	private String success;
	
	private String list;
	private String search;
	private String read;
	private String edit;
    private String listForSel;
    
	private String afterInsert;
	private String afterUpdate;
	private String afterDelete;
	
	public ActionForwardKey() {
		error = "error";
		success = "success";
		
		edit = "edit";
		search = "search";
		list = "list";
		read = "read";
        
        listForSel = "listForSel";

		afterInsert = list;
		afterUpdate = list;
		afterDelete = list;
	}

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public String getEdit() {
		return edit;
	}
	public void setEdit(String edit) {
		this.edit = edit;
	}
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	
	public String getRead() {
		return read;
	}
	public void setRead(String read) {
		this.read = read;
	}
	
	public String getAfterDelete() {
		return afterDelete;
	}
	public void setAfterDelete(String afterDelete) {
		this.afterDelete = afterDelete;
	}
	
	public String getAfterInsert() {
		return afterInsert;
	}
	public void setAfterInsert(String afterInsert) {
		this.afterInsert = afterInsert;
	}
	
	public String getAfterUpdate() {
		return afterUpdate;
	}
	public void setAfterUpdate(String afterUpdate) {
		this.afterUpdate = afterUpdate;
	}

    public String getListForSel() {
        return listForSel;
    }

    public void setListForSel(String listForSel) {
        this.listForSel = listForSel;
    }
}
