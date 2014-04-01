package com.cintel.frame.poi.pdf;

public class Cell {
	private String content;
	private int colspan;
	
	public void setContent(String content){
		this.content = content;
	}
	public String getContent(){
		return this.content;
	}
	
	public void setColspan(int colspan){
		this.colspan = colspan;
	}
	public int getColspan(){
		return this.colspan;
	}
}
