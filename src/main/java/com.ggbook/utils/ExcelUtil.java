package com.ggbook.utils;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;
import java.util.Map;

/**
 * 创建excel文档工具类（对POI的封装）
 * Created by panhs on 2017/4/24.
 */
public class ExcelUtil {
    public static final ExcelUtil me = new ExcelUtil();

    /**
     * 创建表格
     * @param dataMap   数据
     * @return
     */
    public XSSFWorkbook createExcel(Map<String, List<List<String>>> dataMap, boolean range){
        XSSFWorkbook excel = new XSSFWorkbook();
        for (String key: dataMap.keySet()) {
            List<List<String>> sheetList = dataMap.get(key);
            XSSFSheet sheet = excel.createSheet(key);   //多个sheet/切换

            if (range) createCellRange(sheet,sheetList);   //如果true，设置合并对应单元格

            XSSFRow row0 = sheet.createRow(0);
            List<String> headList = sheetList.get(0);
            for (int j = 0; j < headList.size(); j++) { //表头设置
                sheet.setColumnWidth(j,20*256);
                String cellString = headList.get(j);
                XSSFCell cell = row0.createCell(j);
                XSSFCellStyle cellStyle = getStyle(excel,(short)12);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(cellString);
            }
            for (int i = 1; i < sheetList.size(); i++) {
                List<String> rowList = sheetList.get(i);
                XSSFRow row = sheet.createRow(i);
                XSSFCellStyle cellStyle = getStyle(excel,(short)10);
                for (int j = 0; j < rowList.size(); j++) {  //表主体数据设置
                    String cellString = rowList.get(j);
                    XSSFCell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(cellString);
                }
            }
        }
        return excel;
    }


    /**
     * 设置excel表的样式
     * @param excel 表格
     * @param size  字体大小
     * @return
     */
    private XSSFCellStyle getStyle(XSSFWorkbook excel,short size){
        XSSFCellStyle cellStyle = excel.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = excel.createFont(); // 字体
        font.setFontHeightInPoints(size);
        font.setFontName("微软雅黑");
        cellStyle.setFont(font);
        // 设置单元格字体
        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框
        return cellStyle;
     }

    private void createCellRange(XSSFSheet sheet,List<List<String>> sheetList){
        int firstRow = 0;   //开始合并的行
        String flagStr = sheetList.get(0).get(0);
        for (int i = 1; i < sheetList.size(); i++) {
            if (!flagStr.equals(sheetList.get(i).get(0))){  //如果标识字段与该行的首字段不相同，需要合并firstRow行到i-1行的单元格
                //设定合并单元格区域范围   //firstRow  首行  lastRow最后一行 firstCol首列  lastCol最后一列
                sheet.addMergedRegion(new CellRangeAddress(firstRow, i-1, 0, 0));
                firstRow = i;   //重置开始合并的行
                flagStr = sheetList.get(i).get(0);
            }
        }
    }
}
