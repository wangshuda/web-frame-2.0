package com.cintel.frame.poi.pdf;

public class ColumnInfo {
	private String title;
	private String propertyName;
	private String javaType;
	private int index;
	
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	
	public void setPropertyName(String propertyName){
		this.propertyName = propertyName;
	}
	public String getPropertyName(){
		return this.propertyName;
	}
	
	public void setJavaType(String javaType){
		this.javaType = javaType;
	}
	public String getJavaType(){
		return this.javaType;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	public int getIndex(){
		return this.index;
	}
}
