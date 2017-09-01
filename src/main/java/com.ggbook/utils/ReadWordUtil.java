package com.ggbook.utils;

import com.jfinal.kit.StrKit;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 读取word文档工具类
 */
public class ReadWordUtil {

    /**
     * 根据文件路径读取读取word文件 将表格数据转List数据结构
     * @param url
     * @return
     */
    public static List<List<String[]>> getWordTableUrl(String url) {
        FileInputStream in = null;
        try {
            in=new FileInputStream(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return getWordTableInputStream(in);
    }

    /**
     * 根据输入流读取word文件 将表格数据转List数据结构
     * @param inputStream
     * @return
     */
    public static List<List<String[]>> getWordTableInputStream(FileInputStream inputStream) {
        List<List<String[]>> tabList = new ArrayList(); // 表格集合
        try {
            POIFSFileSystem pfs=new POIFSFileSystem(inputStream);
            HWPFDocument hwpf=new HWPFDocument(pfs);
            Range range =hwpf.getRange();
            TableIterator it=new TableIterator(range);
            int tableCount = 1;
            while(it.hasNext()){
                List<String[]> arrList = new ArrayList();
                Table tb=(Table)it.next();
                for(int i=0;i<tb.numRows();i++){
                    TableRow tr=tb.getRow(i);

                    if(tableCount != 1) { // 排除第一个表格
                        // 如果第二列为空 则不加入
                        int cellNum = tr.numCells();
                        if (cellNum < 2) continue;
                        TableCell tdItem=tr.getCell(2);
                        Paragraph para=tdItem.getParagraph(0);
                        if(StrKit.isBlank(para.text().trim())) {
                            continue ;
                        }
                    }

                    String[] arr = new String[tr.numCells()];
                    for(int j=0;j<tr.numCells();j++){
                        TableCell td=tr.getCell(j);
                        for(int k=0;k<td.numParagraphs();k++){
                            Paragraph para=td.getParagraph(k);
                            arr[j] = arr[j] == null ? "" + para.text().trim() : arr[j] + para.text().trim();
                        }
                    }
                    arrList.add(arr);
                }
                tabList.add(arrList);
                tableCount ++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tabList;
    }

    /**
     * 通过文件路径读取文件
     * @param url
     * @return
     */
    public static XWPFDocument getWordStringUrl(String url, Map params) {
        FileInputStream in = null;
        try {
            in=new FileInputStream(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getWordStringInputStream(in, params);
    }

    public static XWPFDocument getWordStringInputStream(FileInputStream inputStream, Map params) {
        XWPFDocument doc = null;
        try {
            doc = new XWPFDocument(inputStream);
            WordReplaceUtil.me.replaceInPara(doc, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return doc;
    }

}
