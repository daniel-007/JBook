package com.ggbook.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by goodminya on 2017/7/12.
 */
public class WordKit {

    /**
     * 给文档新增并设置一级标题
     * @param docx
     * @param title
     * @return
     */
    public static XWPFDocument setCustomerTitle(XWPFDocument docx, String title, JSONObject jStyle){
        String font = jStyle.getString("font");
        if (StrKit.isBlank(font)) {
            font = "黑体";
        }
        String style = jStyle.getString("style");
        if (StrKit.isBlank(style)) {
            style = "标题 1";
        }
        String align = jStyle.getString("align");
        if (StrKit.isBlank(style)) {
            align = "left";
        }
        int fontSize = jStyle.getIntValue("fontSize");
        if (fontSize == 0) {
            fontSize = 16;
        }
        int indentation = jStyle.getIntValue("indentation");
        boolean pageBreak = jStyle.getBooleanValue("pageBreak");
        boolean beforeAutoSpacing = jStyle.getBooleanValue("beforeAutoSpacing");
        boolean afterAutoSpacing = jStyle.getBooleanValue("afterAutoSpacing");
        int beforeLineSpacing = jStyle.getIntValue("beforeLineSpacing");
        int indentationLeft = jStyle.getIntValue("indentationLeft");
        int lineSpacing = jStyle.getIntValue("lineSpacing");
        if (lineSpacing == 0) {
            lineSpacing = 360;
        }
        ParagraphAlignment alignment = ParagraphAlignment.LEFT;
        switch (align) {
            case "left":
                alignment = ParagraphAlignment.LEFT;
                break;
            case "center":
                alignment = ParagraphAlignment.CENTER;
                break;
            case "right":
                alignment = ParagraphAlignment.RIGHT;
                break;
        }

        XWPFParagraph para = docx.createParagraph();
        para.setAlignment(alignment);
        para.setPageBreak(pageBreak);
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(lineSpacing));//行距1.15
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        para.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(beforeLineSpacing));
        para.setIndentationLeft(indentationLeft);
        //段前自动间距
        if (beforeAutoSpacing) {
            para.getCTP().addNewPPr().addNewSpacing().setBeforeAutospacing(STOnOff.ON);
        }
        //段后自动间距
        if (afterAutoSpacing) {
            para.getCTP().addNewPPr().addNewSpacing().setAfterAutospacing(STOnOff.ON);
        }

        XWPFRun run = para.createRun();
        para.setStyle(style);
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii(font);
        fonts.setEastAsia(font);
        fonts.setHAnsi(font);
        run.setFontSize(fontSize);
        run.setBold(true);
        run.setText(title);
        return docx;
    }

    public static XWPFDocument setTitle(XWPFDocument docx, int level,String align, String title){
        XWPFParagraph para = docx.createParagraph();

        int stLine = 276;//行距1.15
        int textPosition = 50;
        int fontSize = 16;
        int beforeLine = 42;
        boolean pageBreak = false;
        ParagraphAlignment alignment = ParagraphAlignment.LEFT;
        switch (align) {
            case "left":
                alignment = ParagraphAlignment.LEFT;
                break;
            case "center":
                alignment = ParagraphAlignment.CENTER;
                break;
            case "right":
                alignment = ParagraphAlignment.RIGHT;
                break;
        }
        switch (level) {
            case 1:
                para.setStyle("标题 1");
                stLine = 276;//行距1.15
                textPosition = 50;
                fontSize = 16;
                pageBreak = true;
                beforeLine = 42;
                break;
            case 2:
                para.setStyle("标题 2");
                stLine = 271;//行距1.13
                textPosition = 40;
                fontSize = 15;//小三
                pageBreak = true;
                beforeLine = 42;
                break;
            case 3:
                para.setStyle("标题 3");
                stLine = 271;//行距1.13
                textPosition = 35;
                fontSize = 14;//四号
                pageBreak = false;
                beforeLine = 42;
                break;
            case 4:
                para.setStyle("标题 4");
                stLine = 360;//行距1.5
                textPosition = 28;
                fontSize = 12;//小四
                pageBreak = false;
                beforeLine = 120;
                break;
        }
        para.setAlignment(alignment);
        para.setPageBreak(pageBreak);
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(stLine));
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        para.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(beforeLine));
        XWPFRun run = para.createRun();
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("黑体");
        fonts.setEastAsia("黑体");
        fonts.setHAnsi("黑体");
        run.setTextPosition(textPosition);
        run.setFontSize(fontSize);
        run.setBold(true);
        run.setText(title);
        return docx;
    }

    /**
     * 分页符
     * @param docx
     */
    public static void pageBreaks(XWPFDocument docx){
        XWPFParagraph p = docx.createParagraph();
        p.setPageBreak(true);
    }

    /**
     * 设置空段落（起到换行效果）
     * @param docx
     * @param enterLine 多少行
     */
    public static void setEnter(XWPFDocument docx,int enterLine){
        for (int i=0; i<enterLine; i++){
            docx.createParagraph();
        }
    }

    /**
     * 给【段落】新增并设置一小段自定义样式文本
     * @param para
     * @param text
     * @param jStyle
     */
    public static void setText(XWPFParagraph para, String text, JSONObject jStyle){
        String align = jStyle.getString("align");
        if (align == null) align = "left";
        String wordFont = jStyle.getString("font");
        if (wordFont == null) wordFont = "宋体";
        int wordSize = jStyle.getIntValue("fontSize");
        if (wordSize == 0) wordSize = 12;
        boolean wordBold = jStyle.getBooleanValue("isBold");
        boolean underline = jStyle.getBooleanValue("isUnderLine");
        int padding = jStyle.getIntValue("textPosition");
        int lineSpacing = jStyle.getIntValue("lineSpacing");
        if (lineSpacing == 0) {
            lineSpacing = 360;
        }
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(lineSpacing));
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);

        switch (align) {
            case "center":
                para.setAlignment(ParagraphAlignment.CENTER);
                break;
            case "right":
                para.setAlignment(ParagraphAlignment.RIGHT);
                break;
            case "left":
                para.setAlignment(ParagraphAlignment.LEFT);
                break;
        }
        XWPFRun run = para.createRun();
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii(wordFont);
        fonts.setEastAsia(wordFont);
        fonts.setHAnsi(wordFont);
        run.setTextPosition(padding);
        run.setFontSize(wordSize);
        run.setBold(wordBold);
        run.setText(text);
        if (underline){
            run.setUnderline(UnderlinePatterns.WORDS);
        }
    }

    /**
     * 给文档新增并设置一段自定义样式文本
     * @param docx
     * @param text
     * @param jStyle
     * @return
     */
    public static XWPFDocument setText(XWPFDocument docx, String text, JSONObject jStyle){
        String align = jStyle.getString("align");
        if (align == null) align = "left";
        String wordFont = jStyle.getString("font");
        if (wordFont == null) wordFont = "宋体";
        int wordSize = jStyle.getIntValue("fontSize");
        if (wordSize == 0) wordSize = 12;
        boolean wordBold = jStyle.getBooleanValue("isBold");
        boolean underline = jStyle.getBooleanValue("isUnderLine");
        boolean page = jStyle.getBooleanValue("page");
        int padding = jStyle.getIntValue("textPosition");
        int indentation = jStyle.getIntValue("indentation");
        boolean beforeAutoSpacing = jStyle.getBooleanValue("beforeAutoSpacing");
        boolean afterAutoSpacing = jStyle.getBooleanValue("afterAutoSpacing");
        int indentationLeft = jStyle.getIntValue("indentationLeft");
        int lineSpacing = jStyle.getIntValue("lineSpacing");
        if (lineSpacing == 0) {
            lineSpacing = 360;
        }


        XWPFParagraph para = docx.createParagraph();
        para.setPageBreak(page);
        para.setIndentationFirstLine(indentation);
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(lineSpacing));
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        para.setIndentationLeft(indentationLeft);
        //段前自动间距
        if (beforeAutoSpacing) {
            para.getCTP().addNewPPr().addNewSpacing().setBeforeAutospacing(STOnOff.ON);
        }
        //段后自动间距
        if (afterAutoSpacing) {
            para.getCTP().addNewPPr().addNewSpacing().setAfterAutospacing(STOnOff.ON);
        }

        switch (align) {
            case "center":
                para.setAlignment(ParagraphAlignment.CENTER);
                break;
            case "right":
                para.setAlignment(ParagraphAlignment.RIGHT);
                break;
            case "left":
                para.setAlignment(ParagraphAlignment.LEFT);
                break;
        }
        XWPFRun run = para.createRun();
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii(wordFont);
        fonts.setEastAsia(wordFont);
        fonts.setHAnsi(wordFont);
        run.setTextPosition(padding);
        run.setFontSize(wordSize);
        run.setBold(wordBold);
        run.setText(text);
        if (underline){
            run.setUnderline(UnderlinePatterns.WORDS);
        }
        return docx;
    }

    /**
     * 给文档新增并设置一个常规表格内容，给【信息系统等级测评基本信息表】（测评报告文档）定制的特定表格
     * @param docx
     * @return
     */
    public static XWPFDocument setTableBaseInfo(XWPFDocument docx,List<List<String>> dataList){
        int rowNum = 18;
        int cellNum = 5;
        XWPFTable table = docx.createTable(rowNum,cellNum);
        final int width = 2000;

        //合并单元格
        mergeCellsHorizontal(table,0,0,cellNum-1);
        mergeCellsHorizontal(table,1,1,2);
        mergeCellsHorizontal(table,2,1,2);
        mergeCellsHorizontal(table,3,0,cellNum-1);
        mergeCellsHorizontal(table,4,1,cellNum-1);
        mergeCellsHorizontal(table,5,1,2);
        mergeCellsHorizontal(table,9,0,cellNum-1);
        mergeCellsHorizontal(table,10,1,2);
        mergeCellsHorizontal(table,11,1,2);

        mergeCellsVertically(table,0,6,8);
        mergeCellsVertically(table,0,12,14);
        mergeCellsVertically(table,0,15,17);

        List<Integer> headList = new ArrayList<>();
        headList.add(0);headList.add(3);headList.add(9);

        for (int i=0; i<rowNum; i++){
            if (headList.indexOf(i)>-1){
                table.getRow(i).setHeight(600);
            }else{
                table.getRow(i).setHeight(530);
            }
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                if (headList.indexOf(i)>-1){
//                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "EEECE1",true,width);
                }else{
//                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,width);
                }

            }
        }
        pageBreaks(docx);
        return docx;
    }

    /**
     * 设置表格单个格子的文字
     * @param cell  格子
     * @param text  文字信息
     * @param jStyle 样式
     */
    private static void setCellText(XWPFTableCell cell, String text, JSONObject jStyle) {
        if(cell==null){
            return;
        }
        if (StrKit.isBlank(text)) {
            text = "";
        }
        String bgcolor = jStyle.getString("bgcolor");
        boolean wordBold = jStyle.getBoolean("isBold");
        int width = jStyle.getIntValue("width");
        String align = jStyle.getString("align");

        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        cell.setColor(bgcolor);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);

        switch (align) {
            case "left":
                cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.LEFT);
                break;
            case "right":
                cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.RIGHT);
                break;
            case "center":
                cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                break;
        }
        String []s = text.split("@#@#");//按回车符分割字符

        XWPFParagraph pIO =cell.getParagraphs().get(0);
        pIO.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
        XWPFRun rIO = pIO.createRun();
        if (wordBold) {
            rIO.setBold(true);
        }
        rIO.setText(s[0]);

        if (s.length>1) {
            for (int i = 1; i < s.length; i++) {
                XWPFParagraph p = cell.addParagraph();//添加新段落
                p.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
                XWPFRun prun = p.createRun();
                if (wordBold) {
                    prun.setBold(true);
                    prun.setText(s[i]);
                } else {
                    prun.setText(s[i]);
                }
            }
        }
    }

    /**
     * word跨列合并单元格,stackoverflow的源码
     * @param table 表格
     * @param row   合并的行
     * @param fromCell  从第几列开始
     * @param toCell    合并到第几列
     */
    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if ( cellIndex == fromCell ) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * word跨行并单元格,stackoverflow的源码
     * @param table 表格
     * @param col   合并的列
     * @param fromRow   从第几行开始合并
     * @param toRow     合并到第几行
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if ( rowIndex == fromRow ) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

}
