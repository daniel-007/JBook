package com.ggbook.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 去掉HTML标记 工具类
 */
public class HTMLSpiritUtil {
//    private static final String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
//    private static final String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
//    private static final String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
    private static final Pattern p_script = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern p_style = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>",Pattern.CASE_INSENSITIVE);
    private static final Pattern p_html=Pattern.compile("<[^>]+>",Pattern.CASE_INSENSITIVE);
    private static final String regEx_special = "\\&[a-zA-Z]{1,10};";

    /**
     * 将字符串中的<script></script>标签、<style></style>标签、html标签都去掉
     * @param tabStr
     * @return
     */
    public static String delAll(String tabStr){
        tabStr = delHtml(tabStr);
        tabStr = delScript(tabStr);
        tabStr = delStyle(tabStr);
        tabStr = delSpecial(tabStr);
        tabStr = tabStr.replaceAll("\\s*", "");
        return tabStr;
    }

    /**
     * 将字符串中的<script></script>标签去掉
     * @param tabStr
     * @return
     */
    public static String delScript(String tabStr){
        Matcher m_script = p_script.matcher(tabStr);
        tabStr = m_script.replaceAll(""); //过滤script标签
        return tabStr;
    }

    /**
     * 将字符串中的<style></style>标签去掉
     * @param tabStr
     * @return
     */
    public static String delStyle(String tabStr){
        Matcher m_style = p_style.matcher(tabStr);
        tabStr = m_style.replaceAll(""); //过滤style标签
        return tabStr;
    }

    /**
     * 将字符串中的html标签去掉
     * @param tabStr
     * @return
     */
    public static String delHtml(String tabStr){
        Matcher m_html=p_html.matcher(tabStr);
        tabStr=m_html.replaceAll(""); //过滤html标签
        return tabStr;
    }

    /**
     * 将字符串中的特殊字符去掉 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * @param tabStr
     * @return
     */
    public static String delSpecial(String tabStr){
        Pattern p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
        Matcher m_special = p_special.matcher(tabStr);
        tabStr = m_special.replaceAll(""); // 过滤特殊标签
        return tabStr;
    }

}
