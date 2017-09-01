package com.ggbook.utils;

import java.math.BigDecimal;
import java.util.Random;

/**
 *  数字运算工具类
 * 1、两个数字的加减乘除运算
 * 2、舍入模式为：BigDecimal.ROUND_HALF_UP
 * 3、结果标度为2（保留两位小数点）
 * @author lanyongmao
 * @data  2015-1-21
 * @time 上午10:12:37
 *
 */
public class NumberUtil{

	/**
	 * 舍入模式BigDecimal.ROUND_HALF_UP：
	 * 1、向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。
	 * 2、如果舍弃部分 >= 0.5，则舍入行为与 ROUND_UP 相同；否则舍入行为与 ROUND_DOWN 相同。
	 * 3、注意，这是我们大多数人在小学时就学过的舍入模式，即四舍五入。 
	 */
	private static int BIGDECIMAL_ROUND = BigDecimal.ROUND_HALF_UP;
	/**
	 * 标度（两位小数点）
	 */
	private static int SCALE = 2;
	
	/**
	 * 两数相加
	 * @param a 被加数
	 * @param b 加数
	 * @return a+b,标度为2的结果
	 */
	public static BigDecimal add(Object a, Object b){
		return isNum(a) && isNum(b) ? new BigDecimal(a.toString()).add(new BigDecimal(b.toString())).setScale(SCALE, BIGDECIMAL_ROUND) : new BigDecimal("0.00");
	}
	
	/**
	 * 两数相减
	 * @param a 被减数
	 * @param b 减数
	 * @return a-b,标度为2的结果
	 */
	public static BigDecimal subtract(Object a, Object b){
		return isNum(a) && isNum(b) ? new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString())).setScale(SCALE, BIGDECIMAL_ROUND) : new BigDecimal("0.00");
	}
	
	/**
	 * 两数相乘
	 * @param a 被乘数
	 * @param b 乘数
	 * @return a*b,标度为2的结果
	 */
	public static BigDecimal multiply(Object a, Object b){
		return multiply(a, b, SCALE);
	}

	/**
	 * 两数相乘
	 * @param a 被乘数
	 * @param b 乘数
	 * @param scale 保留多少位小数
	 * @return a*b,结果
	 */
	public static BigDecimal multiply(Object a, Object b, int scale){
		return isNum(a) && isNum(b) ? new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString())).setScale(scale, BIGDECIMAL_ROUND) : new BigDecimal("0.00");
	}
	
	/**
	 * 两数相除
	 * @param a 被除数
	 * @param b 除数
	 * @return a/b,标度为2的结果
	 */
	public static BigDecimal divide(Object a, Object b){
		return divide(a, b, SCALE);
	}

	/**
	 * 两数相除
	 * @param a 被除数
	 * @param b 除数
	 * @param scale 保留多少位小数
	 * @return a/b,结果
	 */
	public static BigDecimal divide(Object a, Object b, int scale){
		return isNum(a) && isNum(b) && isNotZero(b)? new BigDecimal(a.toString()).divide(new BigDecimal(b.toString()), 10, BigDecimal.ROUND_HALF_DOWN) : new BigDecimal("0.00");
	}
	
	/**
	 * 判断是不是一个数
	 * @param object 被判断量
	 * @return true-是一个数  false-不是一个数
	 */
	public static boolean isNum(Object object){
		return object.toString().matches("^[-+]?([0-9]+)([.]([0-9]+))?|([.]([0-9]+))$");
	}
	
	/**
	 * 判断是不是一个非零值
	 * @param object 被判断量
	 * @return true-非零  false-零
	 */
	public static boolean isNotZero(Object object){
		return !object.toString().matches("^[-+]?([0]+)([.]([0]+))?|([.]([0]+))$");
	}
	
	/**
	 * 截取double数的有效数字。
	 * @param input 原double数
	 * @param scale 小数点后有效位数
	 * @return 截取后的double数
	 */
    public static double scaleDouble(double input, int scale) {
    	if(Double.isNaN(input)){
    		return 0.0;
    	}
    	if(0 >= scale){
    		return input;
    	}
        return new BigDecimal(Double.toString(input)).setScale(scale, BIGDECIMAL_ROUND).doubleValue();
    }
    
	/** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */  
    public static String subZeroAndDot(String s){  
    	if(s == null || "".equals(s)){
    		return "0";
    	}
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }  
    
    /**
	 * 判断两个数字数值上是否相等
	 * @param a
	 * @param b
	 * @return 数值上是否相等
	 */
    public static boolean equals(Object a, Object b) {
    	if(isNum(a)&&isNum(b)){
    		return subtract(a,b).doubleValue()==0d;
    	}else{
    		return false;
    	}
    }

	public static String getRandomStr(int length) {
		if (length <= 0) {
			return "";
		}
		Random random = new Random();

		String result = "";

		for (int i = 0; i < length; i++) {

			result += random.nextInt(10);

		}
		return result;
	}

}
