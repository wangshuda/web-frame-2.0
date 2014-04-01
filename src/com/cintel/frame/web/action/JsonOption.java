package com.cintel.frame.web.action;


public class JsonOption {
	private String value;
	private String label;
	
    private Object rowData;
    
	public JsonOption() {
	}
    
	public JsonOption(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
    public JsonOption(String value, String label, Object rowData) {
        this.value = value;
        this.label = label;
        //
        this.rowData = rowData;
    }
    
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

    public Object getRowData() {
        return rowData;
    }

    public void setRowData(Object rowData) {
        this.rowData = rowData;
    }
	
}
