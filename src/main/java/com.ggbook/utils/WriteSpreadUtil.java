package com.ggbook.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 写spread数据结构工具类
 */
public class WriteSpreadUtil {

    public static void getSysMap(Map map) {

        String level = (String) map.get("level");
        map.put("s", "S" + level);
        map.put("a", "A" + level);
        map.put("g", "G" + level);
        map.put("assetLevel", "1"); // 资产重要级别
        map.put("assetValue", "1000"); // 价值
        map.put("assetEval", "1000"); // 赋值
        map.put("indexDb", "1"); // 指标库
        map.put("bizDescribe", ""); // 业务概述
        map.put("sysExplain", ""); // 系统说明
    }

    /**
     *
     * @param sheetName
     * @param columnCount
     * @param headData
     * @param dataTable
     * @return
     */
    public static Map getCommonSheets(String sheetName, int columnCount, String[] headData, String[][] dataTable) {
        Map cdOneMap = new HashMap();
        Map ctBodyMap = new HashMap();
        cdOneMap.put("tBody", ctBodyMap);
        ctBodyMap.put("version", "10.0.1");
        ctBodyMap.put("tabStripRatio", 0.6);
        ctBodyMap.put("tabEditable", false);
        ctBodyMap.put("newTabVisible", false);
        ctBodyMap.put("allowExtendPasteRange", true);
        Map cSheets = new HashMap();
        ctBodyMap.put("sheets", cSheets);
        Map cjfMap = new HashMap();
        cSheets.put(sheetName, cjfMap); // sheets
        cjfMap.put("name", sheetName);
        cjfMap.put("rowCount", dataTable.length);
        cjfMap.put("index", 0);
        cjfMap.put("columnCount", columnCount);
        cjfMap.put("columns", getColumns(columnCount));
        cjfMap.put("activeRow", 1);
        cjfMap.put("colHeaderData", getColHeaderData(headData));
        Map cDataMap = new HashMap();
        cjfMap.put("data", cDataMap);
        Map cDataTableMap = getDataTable(dataTable);
        cDataMap.put("dataTable", cDataTableMap);
        return cdOneMap;
    }

    public static Map getColHeaderData(String[] arr) {

        Map hMap = new HashMap();
        Map defaultDataNode = new HashMap();
        Map themeFont = new HashMap();
        themeFont.put("themeFont", "Body");
        defaultDataNode.put("style", themeFont);
        Map dataTable = new HashMap();
        Map dataTableItem = new HashMap();
        dataTable.put(0, dataTableItem);
        for(int i=0; i<arr.length; i++) {
            Map map = new HashMap();
            map.put("value", arr[i]);
            dataTableItem.put(i, map);
        }
        hMap.put("defaultDataNode", defaultDataNode);
        hMap.put("dataTable", dataTable);
        return hMap;
    }

    public static Map getColumns(int colNum) {

        Map map = new HashMap();
        for(int i=0; i<colNum; i++) {
            Map sMap = new HashMap();
            sMap.put("size", 200);
            map.put(i, sMap);
        }
        return map;
    }

    public static Map getDataTable(String[][] att) {

        Map map = new HashMap();
        for(int i=0; i<att.length; i++) {
            Map iMap = new HashMap();
            for(int j=0; j<att[i].length; j++) {
                Map jMap = new HashMap();
                jMap.put("value", att[i][j]);
                iMap.put(j, jMap);
            }
            map.put(i, iMap);
        }
        return map;
    }
}
