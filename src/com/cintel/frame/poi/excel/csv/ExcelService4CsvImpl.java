package com.cintel.frame.poi.excel.csv;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import com.cintel.frame.poi.excel.ColumnInfo;
import com.cintel.frame.poi.excel.ExcelService;
import com.cintel.frame.poi.excel.hssf.WorkBookBox;

/**
 * 
 * @version $Id$
 * @history 
 *          1.0.0 2010-9-30 wangshuda created
 */
public class ExcelService4CsvImpl implements ExcelService {
	
	private String delimitedChar = ",";
	
	private CsvFileConfig csvFileConfig;
	
	public void doExport(List<Object> dataList, OutputStream outputStream) throws Exception {
		BufferedWriter  bufferedWriter  = new BufferedWriter(new OutputStreamWriter(outputStream));
		int columnCnt = csvFileConfig.getColumnCnt();
		String[] dataArr = new String[columnCnt];
		
		Set<ColumnInfo> columnInfoList = csvFileConfig.getColumnInfoList();
		//
		if(csvFileConfig.isWithTitle()) {
			for(ColumnInfo columnInfo:columnInfoList) {
				dataArr[columnInfo.getIndex()] = columnInfo.getTitle();
			}	
			bufferedWriter.write(StringUtils.arrayToDelimitedString(dataArr, delimitedChar));	
			bufferedWriter.newLine();
		}	
		//
		for(Object dataObj:dataList) {
			for(ColumnInfo columnInfo:columnInfoList) {
				dataArr[columnInfo.getIndex()] = String.valueOf(PropertyUtils.getProperty(dataObj, columnInfo.getPropertyName()));
			}	
			bufferedWriter.write(StringUtils.arrayToDelimitedString(dataArr, delimitedChar));	
			bufferedWriter.newLine();
		}
		bufferedWriter.flush();
	}
	
	public List<Object> doImport(InputStream inputStream) throws Exception {
		BufferedReader  bufferedReader  = new BufferedReader(new InputStreamReader(inputStream));
		//		
		Class dataObjCls = Class.forName(csvFileConfig.getCommandClass());
		Set<ColumnInfo> columnInfoList = csvFileConfig.getColumnInfoList();
		//
		String valueStr;
		int dataArrLen = 0;
		String lineStr = null;
		Object valueObj = null;
		
		String [] lineDataArr = null;
		//
		String propertyName;
		Class propertyClass = null;
		PropertyDescriptor propertyDescriptor = null;
		//
		int loopRow = 0;
		List<Object> dataInfo = new ArrayList<Object>();
		while((lineStr = bufferedReader.readLine()) != null) {
			if(csvFileConfig.isWithTitle() && loopRow == 0) {
				loopRow ++;
				continue;
			}
			lineDataArr = StringUtils.delimitedListToStringArray(lineStr, delimitedChar);
			//
			dataArrLen = lineDataArr.length;
			//
			Object dataObj = dataObjCls.newInstance();
			//
			for(ColumnInfo columnInfo:columnInfoList) {
				if(columnInfo.getIndex() < dataArrLen) {
		            propertyName = columnInfo.getPropertyName();
		            propertyDescriptor = PropertyUtils.getPropertyDescriptor(dataObj, propertyName);
		            
		            propertyClass = propertyDescriptor.getPropertyType();
		            //
		            valueStr = lineDataArr[columnInfo.getIndex()];
		            if(propertyClass.equals(String.class)) {
		                valueObj = valueStr;
		            }
		            else if(propertyClass.equals(int.class) || propertyClass.equals(Integer.class)) {
		                valueObj = Integer.parseInt(valueStr);
		            }
		            else if(propertyClass.equals(double.class) || propertyClass.equals(Double.class)) {
		                valueObj = Double.parseDouble(valueStr);
		            }
		            else {
		                String message = "Unknown the property type with name:{0} in class:{1}. Can only support: String ,int and double!";
		                throw new IllegalArgumentException(MessageFormat.format(message, propertyName, propertyName));
		            }
		            //
		            PropertyUtils.setProperty(dataObj, propertyName, valueObj);
				}
			}
			//
			if((csvFileConfig.isWithTitle() && loopRow >= 1) || !csvFileConfig.isWithTitle()) {
				dataInfo.add(dataObj);
			}
			//
			loopRow ++;
		}
		return dataInfo;
	}
	

	public CsvFileConfig getCsvFileConfig() {
		return csvFileConfig;
	}

	public void setCsvFileConfig(CsvFileConfig csvFileConfig) {
		this.csvFileConfig = csvFileConfig;
	}

	public String getDelimitedChar() {
		return delimitedChar;
	}

	public void setDelimitedChar(String delimitedChar) {
		this.delimitedChar = delimitedChar;
	}

	/**
	 * 
	 */
	public void doMultiSheetExport(WorkBookBox workBookBox, String newSheetName, List<Object> dataList) throws Exception {
		throw new UnsupportedOperationException("Do not support method for doMultiSheetExport");
	}

	public void doExport(List<Object> dataList, BufferedWriter bufferedWriter, int targetPage) throws Exception {
		// TODO Auto-generated method stub		
	}
}
