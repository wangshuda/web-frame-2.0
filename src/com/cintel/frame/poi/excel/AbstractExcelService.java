package com.cintel.frame.poi.excel;

import java.text.MessageFormat;
import java.util.Set;

import com.cintel.frame.poi.excel.hssf.CellData;
import com.cintel.frame.util.StringUtils;

public abstract class AbstractExcelService implements ExcelService {
    
    protected WorkSheetInfo workSheetInfo;

    protected void doTitleCheck(CellData[] titleCellDataArr) {
        Set<ColumnInfo> columnInfoSet = workSheetInfo.getColumnInfoList();
        //
        String titleInCellData = null;
        String titleInColumnInfo = null;
        //
        for (ColumnInfo columnInfo : columnInfoSet) {
            titleInColumnInfo = columnInfo.getTitle();
            titleInCellData = titleCellDataArr[columnInfo.getIndex()].getValueStr();
            if (!(StringUtils.hasText(titleInColumnInfo) && StringUtils.hasText(titleInCellData) && titleInCellData
                    .equals(titleInColumnInfo))) {
                String errorMessage = "Title is different between column info index:{0} with title:{1} and cell data title : {2}.";
                throw new IllegalArgumentException(MessageFormat.format(errorMessage, columnInfo.getIndex(), titleInColumnInfo, titleInCellData ));
            }
        }
    }

    public WorkSheetInfo getWorkSheetInfo() {
        return workSheetInfo;
    }

    public void setWorkSheetInfo(WorkSheetInfo workSheetInfo) {
        this.workSheetInfo = workSheetInfo;
    }
}
