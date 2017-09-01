package com.ggbook.utils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang.time.DateFormatUtils;

import java.lang.Character.UnicodeBlock;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class ToolUtil {
	
    /**
     * 将普通字符（中英文，包括符号）转换成unicode（utf-16）字符
     * @param ansi ansi字符（中英文，字符等都行）
     * @return unicode（utf-16）字符
     */
    public static String ans2Unicode(String ansi){
        StringBuilder result = new StringBuilder();
        for(int i=0; i<ansi.length(); i++){
            char ch = ansi.charAt(i);
            String stringChar = "" + ch;
            byte[] byteChar = stringChar.getBytes();
            //此处的编码方式应该是操作系统默认的GB编码，即汉字占2个字节且第一个字节的最高位是1，
            //如果理解为有符号数的话，就是负数；而英文占1个字节，符合ASC2码。
            if(byteChar[0] < 1){
                result.append("\\u");
            }else{
                result.append("\\u00");
            }
            result.append(Integer.toHexString((int)ch));
        }
        return result.toString();
    }

    /**
     * 将unicode字符转换成普通字符（中英文，包括符号）
     * @param unicode unicode（utf-16）字符（以\\u00开头）
     * @return ansi字符
     */
    public static String unicode2Ans(String unicode){
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(unicode);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            unicode = unicode.replace(matcher.group(1), ch + "");
        }
        return unicode;
    }

    /**
     * 格式化数字
     * @param num
     * @param patten
     * @return
     * @throws Exception
     */
    public static String formatNumber(int num , String patten) {
        DecimalFormat df = new DecimalFormat(patten);
        return df.format(num);
    }

    /**
     * 格式化数字
     * @param num
     * @param patten
     * @return
     * @throws Exception
     */
    public static String formatNumber(double num , String patten) {
        DecimalFormat df = new DecimalFormat(patten);
        return df.format(num);
    }

    /**
     * 去掉集合中的重复值
     * @param list 传入的带有重复值的集合
     * @return 没有重复值的集合
     */
    public static List<String> removeRepeat(List<String> list){
    	return list.isEmpty() ? new ArrayList<String>() : new ArrayList<String>(new LinkedHashSet<String>(list));
    }
    
    /**
	 * 生成订单系统流水号 yyMMddHHmmss+8位随机数
	 * @return
	 */
	public static String generationRandom(){
		String date = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        Random r = new Random();
        String tmp = Long.toString(r.nextLong());
        return date + tmp.substring(4, 10);
	}

    /**
     * 获取座位号
     * @param seats 原座位号
     * @param count 数量
     * @return
     */
    public static Map<String, Object> getSeats(String seats, int count){
        if(StrKit.isBlank(seats) || count<=0){
            return null;
        }

        List<String> _list = Arrays.asList(seats.split(","));
        if(count>_list.size()){
            return null;
        }
        List<Integer> list = new ArrayList<>();
        for(String no : _list){
            if (StrKit.isBlank(no)) continue;
            list.add(Integer.parseInt(no));
        }
        Collections.sort(list);
        List<Integer> _new = list.subList(count, list.size());
        String[] s = new String[_new.size()];
        int i = 0;
        for(Integer no : _new){
            s[i++] = ""+no;
        }
        List<String> newSeats = new ArrayList<>();
        for(Integer no : list.subList(0, count)){
            newSeats.add(""+no);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("news", StrKit.join(s,","));
        map.put("seats", newSeats);
        return map;
    }

    /**
     * 设置座位号
     * @param seats 原座位号
     * @param nos 要返设的座位号
     * @return
     */
    public static String setSeats(String seats, List<String> nos){
        if(nos==null || nos.isEmpty()){
            return null;
        }

        if(!StrKit.isBlank(seats)){
            nos.addAll(Arrays.asList(seats.split(",")));
        }
        List<Integer> list = new ArrayList<>();
        for(String no : nos){
            list.add(Integer.parseInt(no));
        }
        Collections.sort(list);
        int i = 0;
        String[] s = new String[list.size()];
        for(Integer no : list){
            s[i++] = ""+no;
        }
        return StrKit.join(s,",");
    }


    /**
     * 包含emoji的字符串转为16进制可读字符串
     * @param inStr
     * @return
     */
    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN
                    || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == UnicodeBlock.LATIN_1_SUPPLEMENT
                    || ub == UnicodeBlock.HIRAGANA
                    || ub == UnicodeBlock.KATAKANA
                    || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub == UnicodeBlock.LATIN_EXTENDED_B
                    || ub == UnicodeBlock.LATIN_EXTENDED_A) {
                sb.append((char) myBuffer[i]);
            }else {
                int s = (int) myBuffer[i];
                if (s < 0){
                    s = s + 2 ^ 32;
                }
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS.toUpperCase();
                sb.append(unicode);
            }
        }
        return sb.toString();
    }

    /**
     * 取本周星期六
     * @return
     */
    public static Date getSaturday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 取本周星期日
     * @return
     */
    public static Date getSunday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 取上一个月今天
     * @return
     */
    public static Date getMonthBegin(){
        //取一个月前
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 取今天最后
     * @return
     */
    public static Date getToday(){
        //取现在
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * json2map
     * @param json
     * @return
     */
    public static Map<String, String> json2map(JSONObject json){
        Map<String, String> map = new HashMap<>();
        if(json==null){
            return map;
        }

        for(String key : json.keySet()){
            map.put(key, json.getString(key));
        }
        return map;
    }

    /**
     * 生成哈尔滨seatno
     * @return
     */
    public static String generationHEBseatno(int i){
        String date = DateFormatUtils.format(new Date(), "yyMMddHHmmss");
        Random r = new Random();
        String tmp = Long.toString(r.nextLong());
        return "w" + date + tmp.substring(7, 10) + i;
    }

    /**
     * 生成10位随机数
     * @return
     */
    public static String createRandom10(){
        return Long.toString(new Random().nextLong()).substring(5, 10)+ Long.toString(new Random().nextLong()).substring(5, 10);
    }

    /**
     * 获取保险保额(哈尔滨)
     * @param premium 保险单价
     * @return
     */
    public static double getHEBAmount(int premium){
        switch (premium) {
            case 1:
                return 20000;
            case 2:
                return 40000.00d;
            case 4:
                return 50000.00d;
            default:
                return 0.00d;
        }
    }

    public static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }
}
