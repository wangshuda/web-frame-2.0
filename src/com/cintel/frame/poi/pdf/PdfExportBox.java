package com.cintel.frame.poi.pdf;

import java.io.OutputStream;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class PdfExportBox {
	
	private Document document;
	private int columnNum;
	private PdfPTable table;

	public PdfExportBox(){
		this.document = new Document(PageSize.A4.rotate());
		this.columnNum = 0;
		this.table = null;
	}
	
	public PdfExportBox(int columnNum){
		this.document = new Document(PageSize.A4.rotate());
		this.columnNum = columnNum;
		this.table = new PdfPTable(this.columnNum);
		this.table.setWidthPercentage(100);
	}
	
	public void setDocument(Document document){
		this.document = document;
	}
	public Document getDocument(){
		return this.document;
	}
	
	public void setTable(PdfPTable table){
		this.table = table;
	}
	public PdfPTable getTable(){
		return this.table;
	}
	
	public PdfPTable createRow(Row headerRowInfo)throws Exception{
		PdfPTable rowTable = new PdfPTable(this.columnNum);
		
		BaseFont bfont = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
		Font font = new Font(bfont,9);
		    
		PdfPCell cellInRow = new PdfPCell();
		cellInRow.setHorizontalAlignment(Element.ALIGN_CENTER);
		Cell[] headerCells = headerRowInfo.getHeaderCells();
		for(int i = 0; i < headerCells.length; i ++){
		    cellInRow.setColspan(headerCells[i].getColspan());
		    cellInRow.setPhrase(new Phrase(headerCells[i].getContent(),font));
		    rowTable.addCell(cellInRow);
		}
		
		
		return rowTable;
	}
	
	
	public void createHeader(Row[] headerRows)throws Exception{
		for(int i = 0; i < headerRows.length; i ++){
			PdfPCell headerCell = new PdfPCell(this.createRow(headerRows[i]));
			headerCell.setColspan(this.columnNum);
			this.table.addCell(headerCell);
		}
	}
	
	public void createBody(List<ColumnInfo> columnInfoList, List<Object> dataList)throws Exception{
		
		double[] totalResult = new double[columnInfoList.size()];
		PdfPCell contentCell = new PdfPCell();
		contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
			BaseFont bfont = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
		    Font font = new Font(bfont,9);
		    
		    PdfPCell columnNameCell = new PdfPCell();
		    columnNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    for(ColumnInfo colinfo: columnInfoList){
		    	columnNameCell.setPhrase(new Phrase(colinfo.getTitle(),font));
		    	this.table.addCell(columnNameCell);
		    }
		    
			for(Object obj : dataList){
				for(ColumnInfo column : columnInfoList){
					String cellValue = String.valueOf(PropertyUtils.getProperty(obj, column.getPropertyName()));
					contentCell.setPhrase(new Phrase(cellValue,font));
					this.table.addCell(contentCell);
					if("int".equals(column.getJavaType())||"double".equals(column.getJavaType())||"float".equals(column.getJavaType())){
						totalResult[column.getIndex()] += Double.parseDouble(cellValue);
					}
				}
			}		
	}
	
	public void createBody(List<ColumnInfo> columnInfoList, Row[] bodyRows)throws Exception{
		PdfPCell contentCell = new PdfPCell();
		contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
			BaseFont bfont = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
		    Font font = new Font(bfont,9);
		    
		    PdfPCell columnNameCell = new PdfPCell();
		    columnNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    for(ColumnInfo colinfo: columnInfoList){
		    	columnNameCell.setPhrase(new Phrase(colinfo.getTitle(),font));
		    	this.table.addCell(columnNameCell);
		    }
		    
			for(Row row : bodyRows){
				 PdfPCell bodyRowCell = new PdfPCell(this.createRow(row));
				 bodyRowCell.setColspan(this.columnNum);
				 this.table.addCell(bodyRowCell);
			}
			
	}
	
	public void createFooter(Row[] footerRows)throws Exception{
		for(int i = 0; i < footerRows.length; i ++){
			PdfPCell headerCell = new PdfPCell(this.createRow(footerRows[i]));
			headerCell.setColspan(this.columnNum);
			this.table.addCell(headerCell);
		}
	}
	
	
	public void exportPdf(OutputStream outputStream)throws Exception{
		PdfWriter.getInstance(this.document, outputStream);
			
		this.document.open();
		this.document.add(this.table);
		this.document.close();
	}
}
