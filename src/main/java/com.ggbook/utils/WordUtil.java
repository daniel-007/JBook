package com.ggbook.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 创建word文档工具类（对POI的封装）
 * Created by panhs on 2017/4/17.
 */
public class WordUtil {

    /**
     * 设置文档页边距
     * @param docx
     * @param left  左边距
     * @param right 右边距
     * @param top   上边距
     * @param bottom 下边距
     * @return
     */
    public static XWPFDocument setMargins(XWPFDocument docx, long left, long right, long top, long bottom){
        CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(left));
        pageMar.setRight(BigInteger.valueOf(right));
        pageMar.setTop(BigInteger.valueOf(top));
        pageMar.setBottom(BigInteger.valueOf(bottom));
        return docx;
    }

    /**
     * 给文档新增并设置一段自定义样式文本
     * @param docx  操作的文档
     * @param align 段落对齐
     * @param wordFont  字体样式
     * @param wordSize  字体大小
     * @param wordBold  是否加粗
     * @param text  内容
     * @param underline 是否下划线
     * @return
     */
    public static XWPFDocument setCustomizeText(XWPFDocument docx,ParagraphAlignment align,String wordFont,int wordSize,boolean wordBold,
                                                String text,boolean underline,boolean page,int padding){
        XWPFParagraph para = docx.createParagraph();
        para.setPageBreak(page);
        para.setAlignment(align);
        XWPFRun run = para.createRun();
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii(wordFont);
        fonts.setEastAsia(wordFont);
        fonts.setHAnsi(wordFont);
//        run.setTextPosition(padding);
        run.setFontSize(wordSize);
        run.setBold(wordBold);
        run.setText(text);
        if (underline){
            run.setUnderline(UnderlinePatterns.WORDS);
        }
        return docx;
    }

    /**
     *
     * 给文档新增并设置一段自定义样式文本
     * @param docx  操作的文档
     * @param json 参数
     * @return
     */
    public static XWPFDocument setCustomizeText(XWPFDocument docx, String json){

        JSONObject param = JSON.parseObject(json);
        String align = param.getString("align");
        if (align == null) align = "left";
        String wordFont = param.getString("font");
        if (wordFont == null) wordFont = "宋体";
        int wordSize = param.getIntValue("fontSize");
        if (wordSize == 0) wordSize = 12;
        boolean wordBold = param.getBooleanValue("isBold");
        String text = param.getString("text");
        if (text == null) text = "";
        boolean underline = param.getBooleanValue("isUnderLine");
        boolean page = param.getBooleanValue("page");
        int padding = param.getIntValue("textPosition");
        int indentation = param.getIntValue("indentation");
        boolean beforeAutoSpacing = param.getBooleanValue("beforeAutoSpacing");
        boolean afterAutoSpacing = param.getBooleanValue("afterAutoSpacing");
        int indentationLeft = param.getIntValue("indentationLeft");


        XWPFParagraph para = docx.createParagraph();
        para.setPageBreak(page);
        para.setIndentationFirstLine(indentation);
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
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
//        run.setTextPosition(padding);
        run.setFontSize(wordSize);
        run.setBold(wordBold);
        run.setText(text);
        if (underline){
            run.setUnderline(UnderlinePatterns.WORDS);
        }
        return docx;
    }

    /**
     * 给【段落】新增并设置一小段自定义样式文本
     * @param para  操作的段落
     * @param align 段落对齐
     * @param wordFont  字体样式
     * @param wordSize  字体大小
     * @param wordBold  是否加粗
     * @param text  内容
     * @param underline 是否下划线
     * @return
     */
    public static void setCustomizeText(XWPFParagraph para,ParagraphAlignment align,String wordFont,int wordSize,boolean wordBold,
                                        String text,boolean underline,int padding){
        para.setAlignment(align);
        XWPFRun run = para.createRun();
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii(wordFont);
        fonts.setEastAsia(wordFont);
        fonts.setHAnsi(wordFont);
//        run.setTextPosition(padding);
        run.setFontSize(wordSize);
        run.setBold(wordBold);
        run.setText(text);
        if (underline){
            run.setUnderline(UnderlinePatterns.WORDS);
        }
    }

    /**
     * 给【段落】新增并设置一小段自定义样式文本
     * @param para  操作的段落
     * @param json
     */
    public static void setCustomizeText(XWPFParagraph para, String json){
        JSONObject param = JSON.parseObject(json);
        String align = param.getString("align");
        if (align == null) align = "left";
        String wordFont = param.getString("font");
        if (wordFont == null) wordFont = "宋体";
        int wordSize = param.getIntValue("fontSize");
        if (wordSize == 0) wordSize = 12;
        boolean wordBold = param.getBooleanValue("isBold");
        String text = param.getString("text");
        if (text == null) text = "";
        boolean underline = param.getBooleanValue("isUnderLine");
        int padding = param.getIntValue("textPosition");
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
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
//        run.setTextPosition(padding);
        run.setFontSize(wordSize);
        run.setBold(wordBold);
        run.setText(text);
        if (underline){
            run.setUnderline(UnderlinePatterns.WORDS);
        }
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
     * 设置一段正文文字
     * @param docx
     * @param text
     * @return
     */
    public static XWPFDocument setText(XWPFDocument docx, String text, int padding){
        XWPFParagraph para = docx.createParagraph();
        para.setIndentationFirstLine(480);
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        XWPFRun run = para.createRun();
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");

        if (text.contains("[bold]")) {
            text = text.replace("[bold]", "");
            run.setBold(true);
        }
//        run.setTextPosition(padding);
        run.setFontSize(12);
        run.setText(text);
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 设置一段正文文字
     * @param docx
     * @param text
     * @return
     */
    public static XWPFDocument setText(XWPFDocument docx, String text, int padding, int fontSize,ParagraphAlignment align){
        XWPFParagraph para = docx.createParagraph();
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        para.setAlignment(align);
        XWPFRun run = para.createRun();
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");
//        run.setTextPosition(padding);
        run.setFontSize(fontSize);
        run.setText(text);
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一级标题
     * @param docx
     * @param title
     * @return
     */
    public static XWPFDocument setTitleOne(XWPFDocument docx, String title,ParagraphAlignment align, boolean page){
        XWPFParagraph para = docx.createParagraph();
        para.setAlignment(align);
        para.setPageBreak(page);
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(276));//行距1.15
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        para.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(200));
        XWPFRun run = para.createRun();
        para.setStyle("标题 1");
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("黑体");
        fonts.setEastAsia("黑体");
        fonts.setHAnsi("黑体");
//        run.setTextPosition(50);
        run.setFontSize(16);
        run.setBold(true);
        run.setText(title);
        return docx;
    }

    /**
     * 给文档新增并设置二级标题
     * @param docx
     * @param title
     * @return
     */
    public static XWPFDocument setTitleTwo(XWPFDocument docx, String title, boolean page){
        XWPFParagraph para = docx.createParagraph();
        para.setPageBreak(page);
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(271));//行距1.13
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        para.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(83));
        XWPFRun run = para.createRun();
        para.setStyle("标题 2");
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("黑体");
        fonts.setEastAsia("黑体");
        fonts.setHAnsi("黑体");
//        run.setTextPosition(40);
        run.setFontSize(15);    //小三
        run.setBold(true);
        run.setText(title);
        return docx;
    }

    /**
     * 给文档新增并设置三级标题
     * @param docx
     * @param title
     * @return
     */
    public static XWPFDocument setTitleThree(XWPFDocument docx, String title){
        XWPFParagraph para = docx.createParagraph();
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(271));//行距1.13
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        para.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(83));
        XWPFRun run = para.createRun();
        para.setStyle("标题 3");
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("黑体");
        fonts.setEastAsia("黑体");
        fonts.setHAnsi("黑体");
//        run.setTextPosition(35);
        run.setFontSize(14);    //四号
        run.setBold(true);
        run.setText(title);
        return docx;
    }

    /**
     * 给文档新增并设置四级标题
     * @param docx
     * @param title
     * @return
     */
    public static XWPFDocument setTitleFour(XWPFDocument docx, String title){
        XWPFParagraph para = docx.createParagraph();
        para.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));//行距1.5
        para.getCTP().addNewPPr().addNewSpacing().setLineRule(STLineSpacingRule.EXACT);
        para.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(50));
        XWPFRun run = para.createRun();
        para.setStyle("标题 4");
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("黑体");
        fonts.setEastAsia("黑体");
        fonts.setHAnsi("黑体");
//        run.setTextPosition(28);
        run.setFontSize(12);    //小四
        run.setBold(true);
        run.setText(title);
        return docx;
    }

    /**
     * 给文档新增并设置一个表格
     * @param docx
     * @param dataList
     * @return
     */
    public static XWPFDocument setTableSecond(XWPFDocument docx, List<List<String>> dataList, List<Integer> widthList, String bgcolorHead){
        int rowNum = 0;
        int cellNum = 0;
        if (dataList!=null && dataList.size()!=0){
            if (dataList.get(0).size()!=0){
                rowNum = dataList.size();
                cellNum = dataList.get(0).size();
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);
        //设置表头
        int i = 0;
        table.getRow(i).setHeight(500);
        List<String> topList = dataList.get(i);
        for (int j=0; j<topList.size(); j++){
            setCellText(docx, table.getRow(i).getCell(j), topList.get(j), bgcolorHead,true,widthList.get(j));
        }
        i++;
        //设置其他数据
        for (; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                XWPFTableCell cell = table.getRow(i).getCell(j);
                if(cell==null){
                    continue;
                }
                setCellText(docx, table.getRow(i).getCell(j), rowList.get(j), "FFFFFF",false,widthList.get(j));
            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个单列的无边框表格内容
     * @param docx
     * @param list
     * @return
     */
    public static XWPFDocument setNoBorderTable(XWPFDocument docx, List<String> list, int fontSize){
        int rowNum = 0;
        if (list!=null && list.size()!=0) rowNum = list.size();
        if (rowNum==0) return docx;
        XWPFTable table = docx.createTable(rowNum,1);

        CTTblBorders borders=table.getCTTbl().getTblPr().addNewTblBorders();
        CTBorder hBorder=borders.addNewInsideH();
        hBorder.setVal(STBorder.Enum.forString("none"));
        hBorder.setSz(new BigInteger("1"));

        CTBorder vBorder=borders.addNewInsideV();
        vBorder.setVal(STBorder.Enum.forString("none"));
        vBorder.setSz(new BigInteger("1"));

        CTBorder lBorder=borders.addNewLeft();
        lBorder.setVal(STBorder.Enum.forString("none"));
        lBorder.setSz(new BigInteger("1"));

        CTBorder rBorder=borders.addNewRight();
        rBorder.setVal(STBorder.Enum.forString("none"));
        rBorder.setSz(new BigInteger("1"));

        CTBorder tBorder=borders.addNewTop();
        tBorder.setVal(STBorder.Enum.forString("none"));
        tBorder.setSz(new BigInteger("1"));

        CTBorder bBorder=borders.addNewBottom();
        bBorder.setVal(STBorder.Enum.forString("none"));
        bBorder.setSz(new BigInteger("1"));

        final int width = 10000;

        for (int i=0; i<rowNum; i++){
            String text = list.get(i);
            table.getRow(i).setHeight(500);
            setNoBorderCellText(docx, table.getRow(i).getCell(0), text,false,width, fontSize);
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个常规表格内容
     * @param docx
     * @param list
     * @return
     */
    public static XWPFDocument setTable(XWPFDocument docx, List<Map<String, Object>> list, String bgcolorHead){
        int rowNum = 0;
        int cellNum = 0;
        if (list!=null && list.size()!=0){
            if (list.get(0).size()!=0){
                rowNum = list.size();
                cellNum = list.get(0).size();
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);
        final int width = getWidth(cellNum);
        //设置表头
        int i = 0;
        table.getRow(i).setHeight(500);
        Map<String, Object> topMap = list.get(i);
        for (int j=0; j<topMap.size(); j++){
            setCellText(docx, table.getRow(i).getCell(j), topMap.get(String.valueOf(j)).toString(), bgcolorHead,true,width);
        }
        i++;
        //设置其他数据
        for (; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            Map<String, Object> rowMap = list.get(i);
            for (int j=0; j<rowMap.size(); j++){
                setCellText(docx, table.getRow(i).getCell(j), rowMap.get(String.valueOf(j)).toString(), "FFFFFF",false,width);
            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个表格内容,给【现场测评】定制的特定表格
     * @param docx
     * @param list
     * @return
     */
    public static XWPFDocument setTableEvaluationOne(XWPFDocument docx, List<Map<String, Object>> list){
        int rowNum = 0;
        int cellNum = 0;
        if (list!=null && list.size()>=3){  //起码有三条数据（前两条是表格头），才认为是有数据
            Map<String, Object> dataRowMap = list.get(2);   //第三（2）条数据
            if (dataRowMap.size()!=0){
                rowNum = list.size();   //行数等于list集合大小
                cellNum = dataRowMap.size();    //列数等于第三（2）条数据大小
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);
        mergeCellsVertically(table,0,0,1);
        mergeCellsVertically(table,1,0,1);
        mergeCellsVertically(table,2,0,1);
        mergeCellsHorizontal(table,0,3,cellNum-1);

        mergeCellsMap(table, list, 0);
        mergeCellsMap(table, list, 1);
//        for (int i=2; i<rowNum; i+=4){
//            mergeCellsVertically(table,0,i,i+3);
//            mergeCellsVertically(table,1,i,i+3);
//        }

        final int width = getWidth(cellNum);
        //设置表头【两行】
        int i = 0;
        table.getRow(i).setHeight(500);
        Map<String, Object> topMap0 = list.get(i);
        for (int j=0; j<topMap0.size(); j++){
            if (j==0){
                setCellText(docx, table.getRow(i).getCell(j), topMap0.get(String.valueOf(j)).toString(), "CCCCCC",true, width/(cellNum-2));
            }else{
                setCellText(docx, table.getRow(i).getCell(j), topMap0.get(String.valueOf(j)).toString(), "CCCCCC",true, width);
            }
        }
        i++;
        table.getRow(i).setHeight(500);
        Map<String, Object> topMap1 = list.get(i);
        for (int j=0; j<topMap1.size(); j++){
            if (j==0){
                setCellText(docx, table.getRow(i).getCell(j), topMap1.get(String.valueOf(j)).toString(), "CCCCCC",true,width/(cellNum-2));
            }else{
                setCellText(docx, table.getRow(i).getCell(j), topMap1.get(String.valueOf(j)).toString(), "CCCCCC",true,width);
            }
        }
        i++;
        //设置其他数据
        for (; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            Map<String, Object> rowMap = list.get(i);
            for (int j=0; j<rowMap.size(); j++){
                Object value = rowMap.get(String.valueOf(j));
                if (j==0){
                    setCellText(docx, table.getRow(i).getCell(j), value==null?"":value.toString(), "FFFFFF",false,width/(cellNum-2));
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), value==null?"":value.toString(), "FFFFFF",false,width);
                }
            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个表格内容，给【系统安全保障评估】定制的特定表格
     * @param docx
     * @param list
     * @return
     */
    public static XWPFDocument setTableEvaluationTwo(XWPFDocument docx, List<Map<String, Object>> list){
        int rowNum = 0;
        int cellNum = 0;
        if (list!=null && list.size()!=0){
            if (list.get(0).size()!=0){
                rowNum = list.size();
                cellNum = list.get(0).size();
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);
        final int width = getWidth(cellNum);

        //合并单元格
        mergeCellsMap(table, list, 1);
        mergeCellsMap(table, list, cellNum-1);

        //设置表头
        int i = 0;
        table.getRow(i).setHeight(500);
        Map<String, Object> topMap = list.get(i);
        for (int j=0; j<topMap.size(); j++){
            if (j==0){
                setCellText(docx, table.getRow(i).getCell(j), topMap.get(String.valueOf(j)).toString(), "CCCCCC",true,800);
            }else{
                setCellText(docx, table.getRow(i).getCell(j), topMap.get(String.valueOf(j)).toString(), "CCCCCC",true,width);
            }
        }
        i++;
        //设置其他数据
        for (; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            Map<String, Object> rowMap = list.get(i);
            for (int j=0; j<rowMap.size(); j++){
                if (j==0){
                    setCellText(docx, table.getRow(i).getCell(j), rowMap.get(String.valueOf(j)).toString(), "FFFFFF",false,800);
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowMap.get(String.valueOf(j)).toString(), "FFFFFF",false,width);
                }
            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个表格内容，给【附录A等级测评结果记录】定制的特定表格
     * @param docx
     * @param dataList
     * @return
     */
    public static XWPFDocument setTableNote(XWPFDocument docx, List<List<String>> dataList, List<Integer> widthList){
        int rowNum = 0;
        int cellNum = 0;
        if (dataList!=null && dataList.size()>=2){
            if (dataList.get(1).size()!=0){
                rowNum = dataList.size();
                cellNum = dataList.get(1).size();
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);
        final int width = getWidth(cellNum);

        //合并单元格
        mergeCells(table, dataList, 0);
        mergeCells(table, dataList, 1);

        mergeCellsHorizontal(table,0,1,2);
        mergeCellsVertically(table,3,0,1);
        mergeCellsVertically(table,4,0,1);

        //设置表头
        int i = 0;
        table.getRow(i).setHeight(500);
        List<String> topList1 = dataList.get(i);
        for (int j=0; j<topList1.size(); j++){
            setCellText(docx, table.getRow(i).getCell(j), topList1.get(j).toString(), "CCCCCC",true,widthList.get(j), 9);
        }
        i++;
        table.getRow(i).setHeight(500);
        List<String> topList2 = dataList.get(i);
        for (int j=0; j<topList2.size(); j++){
            setCellText(docx, table.getRow(i).getCell(j), topList2.get(j).toString(), "CCCCCC",true,widthList.get(j), 9);
        }
        i++;
        //设置其他数据
        for (; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                XWPFTableCell cell = table.getRow(i).getCell(j);
                if(cell==null){
                    continue;
                }
                setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,widthList.get(j), 9);
            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个表格内容，给【控制点符合情况汇总】定制的特定表格
     * @param docx
     * @param list
     * @return
     */
    public static XWPFDocument setTablePoint(XWPFDocument docx, List<Map<String, Object>> list){
        int rowNum = 0;
        int cellNum = 0;
        if (list!=null && list.size()>=1){
            if (list.get(1).size()!=0){
                rowNum = list.size();
                cellNum = list.get(1).size();
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);
        final int width = getWidth(cellNum);

        //合并单元格
        mergeCellsHorizontal(table,0,4,7);
        mergeCellsVertically(table,0,0,1);
        mergeCellsVertically(table,1,0,1);
        mergeCellsVertically(table,2,0,1);
        mergeCellsVertically(table,3,0,1);
        mergeCellsHorizontal(table,list.size()-1,0,2);

        mergeCellsMap(table, list, 1);

        //设置表头
        int i = 0;
        table.getRow(i).setHeight(500);
        Map<String, Object> topMap1 = list.get(i);
        for (int j=0; j<topMap1.size(); j++){
            if (j==0){
                setCellText(docx, table.getRow(i).getCell(j), topMap1.get(String.valueOf(j)).toString(), "CCCCCC",true,800);
            }else{
                setCellText(docx, table.getRow(i).getCell(j), topMap1.get(String.valueOf(j)).toString(), "CCCCCC",true,width);
            }
        }
        i++;
        table.getRow(i).setHeight(500);
        Map<String, Object> topMap2 = list.get(i);
        for (int j=0; j<topMap2.size(); j++){
            if (j==0){
                setCellText(docx, table.getRow(i).getCell(j), topMap2.get(String.valueOf(j)).toString(), "CCCCCC",true,800);
            }else{
                setCellText(docx, table.getRow(i).getCell(j), topMap2.get(String.valueOf(j)).toString(), "CCCCCC",true,width);
            }
        }
        i++;
        //设置其他数据
        for (; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            Map<String, Object> rowMap = list.get(i);
            String currbgColor = "FFFFFF";
            String pointFractionString = rowMap.get("3").toString();
            double pointFraction = -1;
            if (!"不适用".equals(pointFractionString)){
                pointFraction = Double.parseDouble(pointFractionString);
            }
            if (pointFraction<5&&pointFraction>0){    //部分符合
                currbgColor = "FFFF00";
            }else if (pointFraction==0){    //不符合
                currbgColor = "FF0000";
            }
            for (int j=0; j<rowMap.size(); j++){
                if (j==0){
                    setCellText(docx, table.getRow(i).getCell(j), rowMap.get(String.valueOf(j)).toString(), "FFFFFF",false,800);
                }else if (j==2){
                    setCellText(docx, table.getRow(i).getCell(j), rowMap.get(String.valueOf(j)).toString(), currbgColor,false,width);
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowMap.get(String.valueOf(j)).toString(), "FFFFFF",false,width);
                }
            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个常规表格内容，给【信息系统等级测评基本信息表】（方案与计划文档）定制的特定表格
     * @param docx
     * @return
     */
    public static XWPFDocument setTableBi(XWPFDocument docx,List<List<String>> dataList){
        int rowNum = 13;
        int cellNum = 5;
        XWPFTable table = docx.createTable(rowNum,cellNum);
        final int width = getWidth(cellNum);

        //合并单元格
        mergeCellsHorizontal(table,0,0,cellNum-1);
        mergeCellsHorizontal(table,1,1,cellNum-1);
        mergeCellsHorizontal(table,2,1,2);
        mergeCellsHorizontal(table,6,0,cellNum-1);

        mergeCellsHorizontal(table,7,1,2);
        mergeCellsHorizontal(table,8,1,2);
        mergeCellsHorizontal(table,11,2,cellNum-1);
        mergeCellsHorizontal(table,12,2,cellNum-1);

        mergeCellsVertically(table,0,3,5);
        mergeCellsVertically(table,0,9,10);
        mergeCellsVertically(table,0,11,12);

        List<Integer> headList = new ArrayList<>();
        headList.add(0);headList.add(6);

        for (int i=0; i<rowNum; i++){
            if (headList.indexOf(i)>-1){
                table.getRow(i).setHeight(600);
            }else{
                table.getRow(i).setHeight(530);
            }
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                if (headList.indexOf(i)>-1){
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "EEECE1",true,width);
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,width);
                }

            }
        }
//        setEnter(docx,1);
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
        final int width = getWidth(cellNum);

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
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "EEECE1",true,width);
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,width);
                }

            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个常规表格内容，给【信息系统等级测评基本信息表】定制的特定表格
     * @param docx
     * @return
     */
    public static XWPFDocument setTableLevelInfo(XWPFDocument docx,List<List<String>> dataList,List<Integer> widthList){
        int rowNum = 5;
        int cellNum = 4;
        XWPFTable table = docx.createTable(rowNum,cellNum);

        //合并单元格
        mergeCellsHorizontal(table,0,0,cellNum-1);
        mergeCellsHorizontal(table,2,1,3);
        mergeCellsHorizontal(table,3,1,3);
        List<Integer> headList = new ArrayList<>();
        headList.add(0);

        for (int i=0; i<rowNum; i++){
            if (headList.indexOf(i)>-1){
                table.getRow(i).setHeight(600);
            }else{
                table.getRow(i).setHeight(530);
            }
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                if (headList.indexOf(i)>-1){
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "EEECE1",true,widthList.get(j));
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,widthList.get(j));
                }

            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个常规表格内容，给【主要安全问题】定制的特定表格
     * @param docx
     * @return
     */
    public static XWPFDocument setTableSaveInfo(XWPFDocument docx, List<List<String>> dataList, List<Integer> widthList){
        int rowNum = 0;
        int cellNum = 0;
        if (dataList!=null && dataList.size()>=2){
            if (dataList.get(1).size()!=0){
                rowNum = dataList.size();
                cellNum = dataList.get(1).size();
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);

        //合并单元格
        mergeCellsHorizontal(table,0,1,cellNum-1);
        mergeCellsHorizontal(table,1,1,2);
        mergeCellsVertically(table,0,0,1);
        mergeCells(table, dataList, 1);

        List<Integer> headList = new ArrayList<>();
        headList.add(0);headList.add(1);

        for (int i=0; i<dataList.size(); i++){
            if (headList.indexOf(i)>-1){
                table.getRow(i).setHeight(500);
            }else{
                table.getRow(i).setHeight(500);
            }
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                if (headList.indexOf(i)>-1){
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "CCCCCC",true,widthList.get(j));
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,widthList.get(j));
                }

            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个常规表格内容，给【主机漏洞、应用系统漏洞】定制的特定表格
     * @param docx
     * @return
     */
    public static XWPFDocument setTableLoophole(XWPFDocument docx){
        List<List<String>> dataList = new ArrayList<>();
        List<String> rowDataList1 = new ArrayList<>();
        List<String> rowDataList2 = new ArrayList<>();
        List<String> rowDataList3 = new ArrayList<>();
        rowDataList1.add("扫描对象");rowDataList1.add("漏洞数量");
        rowDataList2.add("");rowDataList2.add("高风险漏洞");rowDataList2.add("中风险漏洞");rowDataList2.add("低风险漏洞");
        rowDataList3.add(" - - ");rowDataList3.add(" - - ");rowDataList3.add(" - -  ");rowDataList3.add(" - - ");
        dataList.add(rowDataList1);dataList.add(rowDataList2);dataList.add(rowDataList3);

        int rowNum = 3;
        int cellNum = 4;
        XWPFTable table = docx.createTable(rowNum,cellNum);

        //合并单元格
        mergeCellsHorizontal(table,0,1,cellNum-1);
        mergeCellsVertically(table,0,0,1);
        List<Integer> headList = new ArrayList<>();
        headList.add(0);headList.add(1);

        List<Integer> widthList = new ArrayList<>();
        widthList.add(3000);widthList.add(2000);widthList.add(2000);widthList.add(2000);

        for (int i=0; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                if (headList.indexOf(i)>-1){
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "CCCCCC",true,widthList.get(j));
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,widthList.get(j));
                }

            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个表格，给【系统定级情况】定制的特定表格
     * @param docx
     * @return
     */
    public static XWPFDocument setConsDataTable(XWPFDocument docx,List<List<String>> dataList,List<Integer> widthList){
        int rowNum = 7;
        int cellNum = 6;
        XWPFTable table = docx.createTable(rowNum,cellNum);

        //合并单元格
        mergeCellsHorizontal(table,0,0,cellNum-1);
        mergeCellsHorizontal(table,1,1,2);mergeCellsHorizontal(table,1,4,5);
        mergeCellsHorizontal(table,2,1,2);mergeCellsHorizontal(table,2,4,5);
        mergeCellsHorizontal(table,3,1,2);mergeCellsHorizontal(table,3,4,5);
        mergeCellsHorizontal(table,4,1,cellNum-1);
        mergeCellsHorizontal(table,5,2,cellNum-1);
        mergeCellsHorizontal(table,6,2,cellNum-1);
        mergeCellsVertically(table,0,5,6);

        List<Integer> headList = new ArrayList<>();
        headList.add(0);

        for (int i=0; i<rowNum; i++){
            if (i==4){
                table.getRow(i).setHeight(4000);
            }else if (i==5){
                table.getRow(i).setHeight(1000);
            }else if (i==6){
                table.getRow(i).setHeight(2000);
            }else {
                table.getRow(i).setHeight(500);
            }
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                if (headList.indexOf(i)>-1){
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "EEECE1",true,widthList.get(j));
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,widthList.get(j));
                }

            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个表格，给【系统定级情况】定制的特定表格
     * @param docx
     * @return
     */
    public static XWPFDocument setTestDataTable(XWPFDocument docx,List<List<String>> dataList,List<Integer> widthList){
        int rowNum = 0;
        int cellNum = 0;
        if (dataList!=null && dataList.size()>=3){
            if (dataList.get(2).size()!=0){
                rowNum = dataList.size();
                cellNum = dataList.get(2).size();
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);

        //合并单元格
        mergeCellsHorizontal(table,0,0,cellNum-1);
        mergeCellsHorizontal(table,1,2,cellNum-1);
        mergeCellsHorizontal(table,rowNum-1,0,cellNum-2);
        mergeCellsVertically(table,1,1,2);
        mergeCells(table,dataList,0);

        List<Integer> headList = new ArrayList<>();
        headList.add(0);headList.add(1);headList.add(2);

        for (int i=0; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                if (headList.indexOf(i)>-1){
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "EEECE1",true,widthList.get(j));
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,widthList.get(j));
                }

            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 给文档新增并设置一个表格，给【进度安排】定制的特定表格
     * @param docx
     * @return
     */
    public static XWPFDocument setSchedDataTable(XWPFDocument docx,List<List<String>> dataList,List<Integer> widthList){
        int rowNum = 0;
        int cellNum = 0;
        if (dataList!=null && dataList.size()>=1){
            if (dataList.get(0).size()!=0){
                rowNum = dataList.size();
                cellNum = dataList.get(0).size();
            }
        }
        if (rowNum==0 || cellNum==0){
            return docx;
        }
        XWPFTable table = docx.createTable(rowNum,cellNum);

        //合并单元格
        mergeCellsHorizontal(table,1,0,1);
        mergeCellsHorizontal(table,2,0,1);
        mergeCellsHorizontal(table,3,0,1);
        mergeCellsHorizontal(table,4,0,cellNum-1);
        mergeCellsHorizontal(table,5,0,1);
        mergeCellsHorizontal(table,6,0,1);
        mergeCellsHorizontal(table,7,0,cellNum-1);
        mergeCellsHorizontal(table,8,0,1);

        mergeCellsSched(table,dataList,10,rowNum-8,0,2);

        //倒数8行合并
        mergeCellsHorizontal(table,rowNum-8,0,1);
        mergeCellsHorizontal(table,rowNum-7,0,1);
        mergeCellsHorizontal(table,rowNum-6,0,cellNum-1);
        mergeCellsHorizontal(table,rowNum-5,0,1);
        mergeCellsHorizontal(table,rowNum-4,0,1);
        mergeCellsHorizontal(table,rowNum-3,0,1);
        mergeCellsHorizontal(table,rowNum-2,0,cellNum-1);
        mergeCellsHorizontal(table,rowNum-1,0,1);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(1);colorList.add(5);colorList.add(8);colorList.add(rowNum-5);

        table.getRow(0).setHeight(500);
        List<String> topList = dataList.get(0);
        for (int j=0; j<topList.size(); j++){
            setCellText(docx, table.getRow(0).getCell(j), topList.get(j).toString(), "CCCCCC",true,widthList.get(j));
        }
        for (int i=1; i<rowNum; i++){
            table.getRow(i).setHeight(500);
            List<String> rowList = dataList.get(i);
            for (int j=0; j<rowList.size(); j++){
                if (colorList.indexOf(i)>-1 && j==3){
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "DDDDDD",false,widthList.get(j));
                }else{
                    setCellText(docx, table.getRow(i).getCell(j), rowList.get(j).toString(), "FFFFFF",false,widthList.get(j));
                }
            }
        }
//        setEnter(docx,1);
        return docx;
    }

    /**
     * 自动判断合并多行单元格
     * @param table
     * @param list  需要写入表格的数据
     * @param col   需要跨行合并的那一列
     */
    public static void mergeCells(XWPFTable table, List<List<String>> list, int col){
        //合并单元格
        int firstRow = 0;   //开始合并的行
        String flagStr = list.get(0).get(col).toString();
        for (int i = 1; i < list.size(); i++) {
            List<String> arr = list.get(i);
            if (!flagStr.equals(arr.get(col).toString())){  //如果标识字段与该行的对应字段不相同，需要合并firstRow行到i-1行的单元格
                //设定合并列   //table   col 第几列 firstRow  首行  toRow最后一行
                mergeCellsVertically(table,col,firstRow,i-1);
                firstRow = i;   //重置开始合并的行
                flagStr = arr.get(col).toString();
            } else {
                arr.set(col, "");
                list.set(i, arr);
            }
        }
        mergeCellsVertically(table,col,firstRow,list.size()-1);//默认最后一次合并操作
    }

    /**
     * 自动判断合并多行单元格,给【进度安排】特定表格定制的特定合并方法
     * @param table
     * @param list  需要写入表格的数据
     * @param startRow  开始合并的行
     * @param endRow  结束合并的行
     * @param col   需要跨行合并的那一列
     * @param flwCol  跟随合并的列
     */
    public static void mergeCellsSched(XWPFTable table, List<List<String>> list, int startRow, int endRow,  int col, int flwCol){
        //合并单元格
        int firstRow = startRow;   //开始合并的行
        String flagStr = list.get(startRow).get(col).toString();
        for (; startRow < endRow; startRow++) {
            if (!flagStr.equals(list.get(startRow).get(col).toString())){  //如果标识字段与该行的对应字段不相同，需要合并firstRow行到i-1行的单元格
                //设定合并列   //table   col 第几列 firstRow  首行  toRow最后一行
                mergeCellsVertically(table,col,firstRow,startRow-1);
                mergeCellsVertically(table,flwCol,firstRow,startRow-1); //跟随合并的列
                firstRow = startRow;   //重置开始合并的行
                flagStr = list.get(startRow).get(col).toString();
            }
            if (startRow==endRow-1){
                mergeCellsVertically(table,col,firstRow,endRow-1);//最后一次合并操作
                mergeCellsVertically(table,flwCol,firstRow,endRow-1);//最后一次合并操作
            }
        }

    }

    /**
     * 自动判断合并多行单元格
     * @param table
     * @param list  需要写入表格的数据
     * @param col   需要跨行合并的那一列
     */
    public static void mergeCellsMap(XWPFTable table,List<Map<String, Object>> list, int col){
        //合并单元格
        int firstRow = 0;   //开始合并的行
        String flagStr = list.get(0).get(String.valueOf(col)).toString();
        for (int i = 1; i < list.size(); i++) {
            //如果标识字段与该行的对应字段不相同，需要合并firstRow行到i-1行的单元格
            Map<String, Object> valueMap = list.get(i);
            String colKey = String.valueOf(col);
            if (!flagStr.equals(valueMap.get(colKey).toString())){
                //设定合并列   //table   col 第几列 firstRow  首行  toRow最后一行
                mergeCellsVertically(table,col,firstRow,i-1);
                firstRow = i;   //重置开始合并的行
                flagStr = valueMap.get(colKey).toString();
            } else {
                valueMap.put(colKey, "");
                list.set(i, valueMap);
            }
        }
        mergeCellsVertically(table,col,firstRow,list.size()-1);//默认最后一次合并操作
    }

    /**
     * word跨列合并单元格,stackoverflow的源码
     * @param table 表格
     * @param row   合并的行
     * @param fromCol  从第几列开始
     * @param toCol    合并到第几列
     */
    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCol, int toCol) {
        for(int colIndex = fromCol; colIndex <= toCol; colIndex++){
            CTHMerge hmerge = CTHMerge.Factory.newInstance();
            if(colIndex == fromCol){
                // The first merged cell is set with RESTART merge value
                hmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                hmerge.setVal(STMerge.CONTINUE);
            }
            XWPFTableCell cell = table.getRow(row).getCell(colIndex);
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setHMerge(hmerge);
            } else {
                // only set an new TcPr if there is not one already
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setHMerge(hmerge);
                cell.getCTTc().setTcPr(tcPr);
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
        for(int rowIndex = fromRow; rowIndex <= toRow; rowIndex++){
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if(rowIndex == fromRow){
                // The first merged cell is set with RESTART merge value
                vmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                vmerge.setVal(STMerge.CONTINUE);
            }
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setVMerge(vmerge);
            } else {
                // only set an new TcPr if there is not one already
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setVMerge(vmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }

    /**
     * 增加自定义标题样式。这里用的是stackoverflow的源码
     * @param docx 目标文档
     * @param strStyleId 样式名称
     * @param headingLevel 样式级别
     */
    public static void addCustomHeadingStyle(XWPFDocument docx, String strStyleId, int headingLevel) {
        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);
        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);
        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);
        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);
        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = docx.createStyles();
        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);
    }

    /**
     * 设置表格单个格子的文字
     * @param docx
     * @param cell  格子
     * @param text  文字信息
     * @param width 宽
     */
    public static void setNoBorderCellText(XWPFDocument docx, XWPFTableCell cell, String text, boolean wordBold, int width, int fontSize) {
        if (text == null) text = "";
        if(cell==null){
            return;
        }
        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);

        if (text.indexOf("[bold]") > -1) {
            wordBold = true;
        }

        XWPFParagraph pIO =cell.getParagraphs().get(0);
        pIO.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
        pIO.setIndentationFirstLine(400);
        XWPFRun rIO = pIO.createRun();
        CTRPr rpr = rIO.getCTR().isSetRPr() ? rIO.getCTR().getRPr() : rIO.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");
        rIO.setFontSize(fontSize);
        if (wordBold) {
            rIO.setBold(true);
            rIO.setText(text.replace("[bold]", ""));
        } else {
            rIO.setText(text.replace("[bold]", ""));
        }
    }

    /**
     * 设置表格单个格子的文字
     * @param docx
     * @param cell  格子
     * @param text  文字信息
     * @param bgcolor   背景颜色
     * @param width 宽
     */
    private static void setCellText(XWPFDocument docx, XWPFTableCell cell, String text, String bgcolor, boolean wordBold, int width) {
        if (text == null) text = "";
        if(cell==null){
            return;
        }
        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        cell.setColor(bgcolor);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);

        if (text.indexOf("[bold]") > -1) {
            wordBold = true;
        }
        if (text.indexOf("[left]") > -1) {
            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.LEFT);
        } else if (text.indexOf("[right]") > -1) {
            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.RIGHT);
        } else {
            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
        }

        text = text.replace("[bold]", "").replace("[left]", "").replace("[right]", "");
        String []s = text.split("@#@#");//按回车符分割字符

        XWPFParagraph pIO =cell.getParagraphs().get(0);
        pIO.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
        pIO.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(40));
        XWPFRun rIO = pIO.createRun();
        CTRPr rpr = rIO.getCTR().isSetRPr() ? rIO.getCTR().getRPr() : rIO.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");
        rIO.setFontSize(11);
        if (wordBold) {
            rIO.setBold(true);
        }
        if(s.length>0) {
            rIO.setText(s[0]);
        }

        if (s.length>1) {
            for (int i = 1; i < s.length; i++) {
                XWPFParagraph p = cell.addParagraph();//添加新段落
                p.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
                pIO.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(40));
                XWPFRun prun = p.createRun();
                CTRPr prpr = prun.getCTR().isSetRPr() ? prun.getCTR().getRPr() : prun.getCTR().addNewRPr();
                CTFonts pfonts = prpr.isSetRFonts() ? prpr.getRFonts() : prpr.addNewRFonts();
                pfonts.setAscii("宋体");
                pfonts.setEastAsia("宋体");
                pfonts.setHAnsi("宋体");
                prun.setFontSize(11);
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
     * 设置表格单个格子的文字
     * @param docx
     * @param cell  格子
     * @param text  文字信息
     * @param bgcolor   背景颜色
     * @param width 宽
     */
    private static void setCellText(XWPFDocument docx, XWPFTableCell cell, String text, String bgcolor, boolean wordBold, int width, int fontSize) {
        if (text == null) text = "";
        if(cell==null){
            return;
        }
        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        cell.setColor(bgcolor);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);

        if (text.indexOf("[bold]") > -1) {
            wordBold = true;
        }
        if (text.indexOf("[left]") > -1) {
            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.LEFT);
        } else if (text.indexOf("[right]") > -1) {
            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.RIGHT);
        } else {
            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
        }

        text = text.replace("[bold]", "").replace("[left]", "").replace("[right]", "");
        String []s = text.split("@#@#");//按回车符分割字符

        XWPFParagraph pIO =cell.getParagraphs().get(0);
        pIO.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
        pIO.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(40));
        XWPFRun rIO = pIO.createRun();
        CTRPr rpr = rIO.getCTR().isSetRPr() ? rIO.getCTR().getRPr() : rIO.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");
        rIO.setFontSize(fontSize);
        if (wordBold) {
            rIO.setBold(true);
        }
        rIO.setText(s[0]);

        if (s.length>1) {
            for (int i = 1; i < s.length; i++) {
                XWPFParagraph p = cell.addParagraph();//添加新段落
                p.getCTP().addNewPPr().addNewSpacing().setLine(BigInteger.valueOf(360));
                pIO.getCTP().addNewPPr().addNewSpacing().setBeforeLines(BigInteger.valueOf(40));
                XWPFRun prun = p.createRun();
                CTRPr prpr = prun.getCTR().isSetRPr() ? prun.getCTR().getRPr() : prun.getCTR().addNewRPr();
                CTFonts pfonts = prpr.isSetRFonts() ? prpr.getRFonts() : prpr.addNewRFonts();
                pfonts.setAscii("宋体");
                pfonts.setEastAsia("宋体");
                pfonts.setHAnsi("宋体");
                prun.setFontSize(fontSize);
                if (wordBold) {
                    prun.setBold(true);
                    prun.setText(s[i]);
                } else {
                    prun.setText(s[i]);
                }
            }
        }
    }


    //计算列平均宽
    private static int getWidth(int num){
        int size = 10000;
        return size/num;
    }

    public static XWPFDocument setHeader(XWPFDocument docx,String text) throws Exception {
        CTP ctp = CTP.Factory.newInstance();
        CTR ctr = ctp.addNewR();
        CTText textt = ctr.addNewT();
        textt.setStringValue(text);

        XWPFParagraph codePara = new XWPFParagraph(ctp, docx);
        codePara.setAlignment(ParagraphAlignment.CENTER);
        codePara.setVerticalAlignment(TextAlignment.CENTER);
        codePara.setBorderBottom(Borders.THICK);
        XWPFParagraph[] newparagraphs = new XWPFParagraph[1];
        newparagraphs[0] = codePara;
        CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docx, sectPr);
        headerFooterPolicy.createHeader(STHdrFtr.DEFAULT, newparagraphs);
        return docx;
    }
    //页脚:显示页码信息
    public static XWPFDocument setFooter(XWPFDocument docx, String leftText) throws Exception{
        CTP ctp = CTP.Factory.newInstance();
        XWPFParagraph codePara = new XWPFParagraph(ctp, docx);
        XWPFRun r1 = codePara.createRun();
        r1.setText(leftText+"第");
        r1.setFontSize(11);
        CTRPr rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");

        r1 = codePara.createRun();
        CTFldChar fldChar = r1.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));

        r1 = codePara.createRun();
        CTText ctText = r1.getCTR().addNewInstrText();
        ctText.setStringValue("PAGE  \\* MERGEFORMAT");
        ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
        r1.setFontSize(11);
        rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR().addNewRPr();
        fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");

        fldChar = r1.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("end"));

        r1 = codePara.createRun();
        r1.setText("页");
        r1.setFontSize(11);
        rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR().addNewRPr();
        fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");
        codePara.setAlignment(ParagraphAlignment.CENTER);
        codePara.setVerticalAlignment(TextAlignment.CENTER);
        codePara.setBorderTop(Borders.THICK);
        XWPFParagraph[] newparagraphs = new XWPFParagraph[1];
        newparagraphs[0] = codePara;
        CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docx, sectPr);
        headerFooterPolicy.createFooter(STHdrFtr.DEFAULT, newparagraphs);
        return docx;
    }

    //页脚:显示页码信息【有总页数】
    public static XWPFDocument setPageNumberFooter(XWPFDocument docx, String leftText) throws Exception {
        CTP ctp = CTP.Factory.newInstance();
        XWPFParagraph codePara = new XWPFParagraph(ctp, docx);
        XWPFRun r1 = codePara.createRun();
        r1.setText(leftText+"                                                            第");
        r1.setFontSize(11);
        CTRPr rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");

        r1 = codePara.createRun();
        CTFldChar fldChar = r1.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));

        r1 = codePara.createRun();
        CTText ctText = r1.getCTR().addNewInstrText();
        ctText.setStringValue("PAGE  \\* MERGEFORMAT");
        ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
        r1.setFontSize(11);
        rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR().addNewRPr();
        fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");

        fldChar = r1.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("end"));
        r1 = codePara.createRun();
        r1.setText("页/共");
        r1.setFontSize(11);
        rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR().addNewRPr();
        fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");

        r1 = codePara.createRun();
        fldChar = r1.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));

        r1 = codePara.createRun();
        ctText = r1.getCTR().addNewInstrText();
        ctText.setStringValue("NUMPAGES  \\* MERGEFORMAT ");
        ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
        r1.setFontSize(11);
        rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR()
                .addNewRPr();
        fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");

        fldChar = r1.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("end"));

        r1 = codePara.createRun();
        r1.setText("页");
        r1.setFontSize(11);
        rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR()
                .addNewRPr();
        fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("宋体");
        fonts.setEastAsia("宋体");
        fonts.setHAnsi("宋体");

        codePara.setAlignment(ParagraphAlignment.CENTER);
        codePara.setVerticalAlignment(TextAlignment.CENTER);
        codePara.setBorderTop(Borders.THICK);
        XWPFParagraph[] newparagraphs = new XWPFParagraph[1];
        newparagraphs[0] = codePara;
        CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docx, sectPr);
        headerFooterPolicy.createFooter(STHdrFtr.DEFAULT, newparagraphs);
        return docx;
    }

    /**
     * 磅转换计算值
     * @param pt
     * @return
     */
    public static int pt2int(int pt) {
        return 20 * pt;
    }

    /**
     * 毫米转换计算值
     * @param mm
     * @return
     */
    public static int mm2int(double mm) {
        return (int) (mm * 56.7);
    }

    public static void sectionAutoSpacing(XWPFParagraph paragraph, ParagraphAlignment alignment) {
        paragraph.setAlignment(alignment);
        paragraph.getCTP().addNewPPr().addNewSpacing().setBeforeAutospacing(STOnOff.ON); // 段前自动间距
        paragraph.getCTP().addNewPPr().addNewSpacing().setAfterAutospacing(STOnOff.ON); // 段后自动间距
    }

}
