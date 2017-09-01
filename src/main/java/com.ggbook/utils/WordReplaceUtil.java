package com.ggbook.utils;

import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 替换Word文本内容
 * Created by panhs on 2017/4/12.
 */
public class WordReplaceUtil {
    public static final WordReplaceUtil me = new WordReplaceUtil();

    public XWPFDocument replaceParams(Map<String, Object> params, String filePath) throws Exception {
        InputStream inputStream = new FileInputStream(new File(filePath));
        XWPFDocument docx = new XWPFDocument(inputStream);   //注意：XWPFDocument只支持.docx后缀的文档读取
        replaceInPara(docx, params);
        replaceInTable(docx, params);
        inputStream.close();
        return docx;
    }

    /**
     * 替换段落里面的变量
     * @param doc    要替换的文档
     * @param params 参数
     */
    public void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            if (this.matcher(para.getParagraphText()).find()) {
                this.replaceInPara(para, params);
            }
        }
    }

    /**
     * 替换段落里面的变量
     * @param para   要替换的段落
     * @param params 参数
     */
    public void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        if (this.matcher(para.getParagraphText()).find()) {
            List<XWPFRun> runList = para.getRuns(); //获取
            int start = -1;
            int end = -1;
            String str = "";
            for (int i = 0; i < runList.size(); i++) {

                String runText = runList.get(i).toString();
                if (runText.length()==1){   //如果只读取了单个的字符，则联合下个text来判断
                    if (i<runList.size()-1){
                        if ('$' == runText.charAt(0)&&'{' == runList.get(i+1).toString().charAt(0)) {
                            start = i;
                        }
                    }
                }else{  //否则可以直接判断前两个字符是否符合
                    if ('$' == runText.charAt(0)&&'{' == runText.charAt(1)) {
                        start = i;
                    }
                }

                if ((start != -1)) {
                    str += runText;
                }
                if ('}' == runText.charAt(runText.length() - 1)) {
                    if (start != -1) {
                        end = i;
                        break;
                    }
                }
            }

            for (int i = start; i <= end; i++) {
                para.removeRun(i);
                i--;
                end--;
            }
            for (String key : params.keySet()) {
                if (str.equals(key)) {
                    if("${sysName}".equals(key)) {
                        XWPFRun newRun = para.insertNewRun(1);
                        newRun.setText((String) params.get(key));
                        newRun.setColor(runList.get(0).getColor());
                        newRun.setFontFamily(runList.get(0).getFontFamily());
                        newRun.setFontSize(18);
                        newRun.setBold(true);
                    } else {
                        XWPFRun newRun = para.insertNewRun(0);
                        newRun.setText((String) params.get(key));
                        newRun.setColor(runList.get(0).getColor());
                        newRun.setFontFamily(runList.get(0).getFontFamily());
                        newRun.setFontSize(18);
                        newRun.setBold(true);
                    }
                    break;
                }
            }

        }
    }

    /**
     * 替换表格里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    public void replaceInTable(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext()) {
            table = iterator.next();
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    paras = cell.getParagraphs();
                    for (XWPFParagraph para : paras) {
                        this.replaceInPara(para, params);
                    }
                }
            }
        }
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    public void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    public void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}