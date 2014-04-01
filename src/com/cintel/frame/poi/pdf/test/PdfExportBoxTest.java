package com.cintel.frame.poi.pdf.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cintel.frame.poi.pdf.Cell;
import com.cintel.frame.poi.pdf.Row;
import com.cintel.frame.poi.pdf.PdfExportBox;
import com.cintel.frame.poi.pdf.ColumnInfo;

public class PdfExportBoxTest {
	
	// inner class
	class CallRecord {
		private String agentId;
		private String recordDatetime;
		private int callTimes;
		private int communicateTimes;
		private int noAnswer;
		private int noConnect;
		private int agentGiveup;
		private int ringDuration;
		private int communicateDuration;
		private int minCommunicateDuration;
		private int maxCommunicateDuration;
		
		
		public CallRecord(){
			
		}
		
		public CallRecord(String agentId, String recordDatetime, int callTimes, int communicateTimes, 
				          int noAnswer, int noConnect, int agentGiveup, int ringDuration, int communicateDuration,
				          int minCommunicateDuration,int maxCommunicateDuration){
			this.agentId = agentId;
			this.recordDatetime = recordDatetime;
			this.callTimes = callTimes;
			this.communicateTimes = communicateTimes;
			this.noAnswer = noAnswer;
			this.noConnect = noConnect;
			this.agentGiveup = agentGiveup;
			this.ringDuration = ringDuration;
			this.communicateDuration = communicateDuration;
			this.minCommunicateDuration = minCommunicateDuration;
			this.maxCommunicateDuration = maxCommunicateDuration;
			
		}
		public void setAgentId(String agentId){
			this.agentId = agentId;
		}
		public String getAgentId(){
			return this.agentId;
		}
		
		public void setRecordDatetime(String recordDatetime){
			this.recordDatetime = recordDatetime;
		}
		public String getRecordDatetime(){
			return this.recordDatetime;
		}
		
		public void setCallTimes(int callTimes){
			this.callTimes = callTimes;
		}
		public int getCallTimes(){
			return this.callTimes;
		}
		
		public void setCommunicateTimes(int communicateTimes){
			this.communicateTimes = communicateTimes;
		}
		public int getCommunicateTimes(){
			return this.communicateTimes;
		}
		
		public void setNoAnswer(int noAnswer){
			this.noAnswer = noAnswer;
		}
		public int getNoAnswer(){
			return this.noAnswer;
		}
		
		public void setNoConnect(int noConnect){
			this.noConnect = noConnect;
		}
		public int getNoConnect(){
			return this.noConnect;
		}
		
		public void setAgentGiveup(int agentGiveup){
			this.agentGiveup = agentGiveup;
		}
		public int getAgentGiveup(){
			return this.agentGiveup;
		}
		
		public void setRingDuration(int ringDuration){
			this.ringDuration = ringDuration;
		}
		public int getRingDuration(){
			return this.ringDuration;
		}
		
		public void setCommunicateDuration(int communicateDuration){
			this.communicateDuration = communicateDuration;
		}
		public int getCommunicateDuration(){
			return this.communicateDuration;
		}
		
		public void setMinCommunicateDuration(int minCommunicateDuration){
			this.minCommunicateDuration = minCommunicateDuration;
		}
		public int getMinCommunicateDuration(){
			return this.minCommunicateDuration;
		}
		
		public void setMaxCommunicateDuration(int maxCommunicateDuration){
			this.maxCommunicateDuration = maxCommunicateDuration;
		}
		public int getMaxCommunicateDuration(){
			return this.maxCommunicateDuration;
		}
	}
	public static void main(String[] args)throws Exception{
		int columnNum = 11;
		
		// construct the header cells and rows
		Cell titleHeaderCell = new Cell();
		titleHeaderCell.setColspan(columnNum);
		titleHeaderCell.setContent("坐席外呼");
		
		Cell[] titleHeaderCells = {titleHeaderCell};
		Row titleRow = new Row();
		titleRow.setHeaderCells(titleHeaderCells);
		
		Cell secondaryHeaderCell1 = new Cell();
		secondaryHeaderCell1.setColspan(1);
		secondaryHeaderCell1.setContent("统计间隔");
		
		Cell secondaryHeaderCell2 = new Cell();
		secondaryHeaderCell2.setColspan(1);
		secondaryHeaderCell2.setContent("1天");
		
		Cell secondaryHeaderCell3 = new Cell();
		secondaryHeaderCell3.setColspan(1);
		secondaryHeaderCell3.setContent("统计间隔");
		
		Cell secondaryHeaderCell4 = new Cell();
		secondaryHeaderCell4.setColspan(6);
		secondaryHeaderCell4.setContent("2010-06-01 00:00 至 2010-06-18 17:16");
		
		Cell secondaryHeaderCell5 = new Cell();
		secondaryHeaderCell5.setColspan(1);
		secondaryHeaderCell5.setContent("创建时间");
		
		Cell secondaryHeaderCell6 = new Cell();
		secondaryHeaderCell6.setColspan(1);
		secondaryHeaderCell6.setContent("2010-06-18 17:27");
		
		Cell[] secondaryHeaderCells = {secondaryHeaderCell1,secondaryHeaderCell2,secondaryHeaderCell3,secondaryHeaderCell4,secondaryHeaderCell5,secondaryHeaderCell6};
		Row secondaryRow = new Row();
		secondaryRow.setHeaderCells(secondaryHeaderCells);
		
		Row[] headerRows = {titleRow,secondaryRow};
		
		
		// construct the columnInfo
		List<ColumnInfo> columnInfoList = new ArrayList<ColumnInfo>();
		
		ColumnInfo columnInfo1 = new ColumnInfo();
		columnInfo1.setTitle("工号");
		columnInfo1.setJavaType("String");
		columnInfo1.setIndex(0);
		columnInfo1.setPropertyName("agentId");
		columnInfoList.add(columnInfo1);
		
		ColumnInfo columnInfo2 = new ColumnInfo();
		columnInfo2.setTitle("统计时间");
		columnInfo2.setJavaType("String");
		columnInfo2.setIndex(1);
		columnInfo2.setPropertyName("recordDatetime");
		columnInfoList.add(columnInfo2);
		
		ColumnInfo columnInfo3 = new ColumnInfo();
		columnInfo3.setTitle("呼叫次数");
		columnInfo3.setJavaType("int");
		columnInfo3.setIndex(2);
		columnInfo3.setPropertyName("callTimes");
		columnInfoList.add(columnInfo3);
		
		ColumnInfo columnInfo4 = new ColumnInfo();
		columnInfo4.setTitle("通话次数");
		columnInfo4.setJavaType("int");
		columnInfo4.setIndex(3);
		columnInfo4.setPropertyName("communicateTimes");
		columnInfoList.add(columnInfo4);
		
		ColumnInfo columnInfo5 = new ColumnInfo();
		columnInfo5.setTitle("用户久无应答");
		columnInfo5.setJavaType("int");
		columnInfo5.setIndex(4);
		columnInfo5.setPropertyName("noAnswer");
		columnInfoList.add(columnInfo5);
		
		ColumnInfo columnInfo6 = new ColumnInfo();
		columnInfo6.setTitle("用户未接通");
		columnInfo6.setJavaType("int");
		columnInfo6.setIndex(5);
		columnInfo6.setPropertyName("noConnect");
		columnInfoList.add(columnInfo6);
		
		ColumnInfo columnInfo7 = new ColumnInfo();
		columnInfo7.setTitle("座席放弃");
		columnInfo7.setJavaType("int");
		columnInfo7.setIndex(6);
		columnInfo7.setPropertyName("agentGiveup");
		columnInfoList.add(columnInfo7);
		
		ColumnInfo columnInfo8 = new ColumnInfo();
		columnInfo8.setTitle("振铃时长");
		columnInfo8.setJavaType("int");
		columnInfo8.setIndex(7);
		columnInfo8.setPropertyName("ringDuration");
		columnInfoList.add(columnInfo8);
		
		ColumnInfo columnInfo9 = new ColumnInfo();
		columnInfo9.setTitle("通话时长");
		columnInfo9.setJavaType("int");
		columnInfo9.setIndex(8);
		columnInfo9.setPropertyName("communicateDuration");
		columnInfoList.add(columnInfo9);
		
		ColumnInfo columnInfo10 = new ColumnInfo();
		columnInfo10.setTitle("最短通话时长");
		columnInfo10.setJavaType("int");
		columnInfo10.setIndex(9);
		columnInfo10.setPropertyName("minCommunicateDuration");
		columnInfoList.add(columnInfo10);
		
		ColumnInfo columnInfo11 = new ColumnInfo();
		columnInfo11.setTitle("最长通话时长");
		columnInfo11.setJavaType("int");
		columnInfo11.setIndex(10);
		columnInfo11.setPropertyName("maxCommunicateDuration");
		columnInfoList.add(columnInfo11);
		
		
		PdfExportBoxTest pdfExportBoxTest = new PdfExportBoxTest();
		// data need to be exported
		CallRecord record1 = pdfExportBoxTest.new CallRecord("2000","2010-06-02 00:00",4,4,0,0,0,0,63,0,23);
		CallRecord record2 = pdfExportBoxTest.new CallRecord("2000","2010-06-03 00:00",4,1,0,0,3,4,6,0,1);
		CallRecord record3 = pdfExportBoxTest.new CallRecord("3001","2010-06-11 00:00",5,4,0,0,1,79,600,0,575);
		CallRecord record4 = pdfExportBoxTest.new CallRecord("5000","2010-06-03 00:00",4,4,0,0,0,26,659,0,571);
		
		List<Object> list = new ArrayList<Object>();
		list.add(record1);
		list.add(record2);
		list.add(record3);
		list.add(record4);
		
		
		String filePath = "D:/pdf/callRecord.pdf";
		
		PdfExportBox pdfExportBox = new PdfExportBox(columnNum);
		
		// create the header of the statement
		pdfExportBox.createHeader(headerRows);
		
		// create the body of the statement
		pdfExportBox.createBody(columnInfoList, list);
		
		// construct the footer cells and rows
		Cell footerCell1 = new Cell();
		footerCell1.setColspan(2);
		footerCell1.setContent("合计");
		Cell[] footerCells = new Cell[columnNum-1];
		footerCells[0] = footerCell1;
		Cell footerCell2 ;
		int i = 1;
		for(ColumnInfo columnInfo : columnInfoList){
			if("agentId".equals(columnInfo.getPropertyName())||"recordDatetime".equals(columnInfo.getPropertyName())){
				continue;
			}else{
				footerCell2 = new Cell();
				footerCell2.setColspan(1);
				
				footerCells[i] = footerCell2;
				i++;
			}
			
		}
		Row footRow = new Row();
		footRow.setHeaderCells(footerCells);
		
		Row[] footerRows = {footRow};
		
		// create the footer of the statement
		pdfExportBox.createFooter(footerRows);
		
		
		try{
			// export the statment as an pdf document.
			pdfExportBox.exportPdf(new FileOutputStream(filePath));
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		System.out.println("the call record doucment has been consturcted successfully");
	}
}
