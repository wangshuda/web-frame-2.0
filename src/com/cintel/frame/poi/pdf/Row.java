package com.cintel.frame.poi.pdf;

public class Row {
	private Cell[] headerCells;
	
	public void setHeaderCells(Cell[] headerCells){
		this.headerCells = headerCells;
	}
	public Cell[] getHeaderCells(){
		return this.headerCells;
	}
}
