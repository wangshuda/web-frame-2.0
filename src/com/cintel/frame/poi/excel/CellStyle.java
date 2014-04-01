package com.cintel.frame.poi.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class CellStyle {
	public static final short ALIGN_GENERAL = 0;
    public static final short ALIGN_LEFT = 1;
    public static final short ALIGN_CENTER = 2;
    public static final short ALIGN_RIGHT = 3;
    public static final short ALIGN_FILL = 4;
    public static final short ALIGN_JUSTIFY = 5;
    public static final short ALIGN_CENTER_SELECTION = 6;
    public static final short VERTICAL_TOP = 0;
    public static final short VERTICAL_CENTER = 1;
    public static final short VERTICAL_BOTTOM = 2;
    public static final short VERTICAL_JUSTIFY = 3;
    public static final short BORDER_NONE = 0;
    public static final short BORDER_THIN = 1;
    public static final short BORDER_MEDIUM = 2;
    public static final short BORDER_DASHED = 3;
    public static final short BORDER_HAIR = 4;
    public static final short BORDER_THICK = 5;
    public static final short BORDER_DOUBLE = 6;
    public static final short BORDER_DOTTED = 7;
    public static final short BORDER_MEDIUM_DASHED = 8;
    public static final short BORDER_DASH_DOT = 9;
    public static final short BORDER_MEDIUM_DASH_DOT = 10;
    public static final short BORDER_DASH_DOT_DOT = 11;
    public static final short BORDER_MEDIUM_DASH_DOT_DOT = 12;
    public static final short BORDER_SLANTED_DASH_DOT = 13;
    public static final short NO_FILL = 0;
    public static final short SOLID_FOREGROUND = 1;
    public static final short FINE_DOTS = 2;
    public static final short ALT_BARS = 3;
    public static final short SPARSE_DOTS = 4;
    public static final short THICK_HORZ_BANDS = 5;
    public static final short THICK_VERT_BANDS = 6;
    public static final short THICK_BACKWARD_DIAG = 7;
    public static final short THICK_FORWARD_DIAG = 8;
    public static final short BIG_SPOTS = 9;
    public static final short BRICKS = 10;
    public static final short THIN_HORZ_BANDS = 11;
    public static final short THIN_VERT_BANDS = 12;
    public static final short THIN_BACKWARD_DIAG = 13;
    public static final short THIN_FORWARD_DIAG = 14;
    public static final short SQUARES = 15;
    public static final short DIAMONDS = 16;
    public static final short LESS_DOTS = 17;
    public static final short LEAST_DOTS = 18;
    
	public int fontSize;
	public int fontHeight = 12;
	public int backgroundColor = new HSSFColor.WHITE().getIndex();
	public int alignment = ALIGN_CENTER;
	public int border = BORDER_THIN;
	public int vertical;
	public int height = -1;
	
	public static CellStyle loadDefaultHeadStyle() {
		CellStyle cellStyle = new CellStyle();
		cellStyle.setFontHeight(15);
		return cellStyle;
	}
	
	public static CellStyle loadDefaultCellStyle() {
		CellStyle cellStyle = new CellStyle();
		return cellStyle;
	}
	
	public HSSFCellStyle load2HssfCellStyle(HSSFWorkbook workBook) {
        HSSFFont cellFont = workBook.createFont();
        // set the size of the font.
        cellFont.setFontHeightInPoints((short)fontHeight);
        
        HSSFCellStyle cellStyle = workBook.createCellStyle();   
        // set the aligment of the cell in the head row
        cellStyle.setAlignment((short)alignment);
        cellStyle.setFont(cellFont);
        
        cellStyle.setBorderTop((short)border);
        cellStyle.setBorderBottom((short)border);
        cellStyle.setBorderLeft((short)border);
        cellStyle.setBorderRight((short)border);
        
        // set the backgroundcolor and forgroundcolor of the head row
        cellStyle.setFillForegroundColor((short)backgroundColor);
        cellStyle.setFillBackgroundColor((short)backgroundColor);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
        return cellStyle;
	}

	public int getFontHeight() {
		return fontHeight;
	}
	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getBorder() {
		return border;
	}
	public void setBorder(int border) {
		this.border = border;
	}
	public int getVertical() {
		return vertical;
	}
	public void setVertical(int vertical) {
		this.vertical = vertical;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
