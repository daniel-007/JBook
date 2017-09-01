package com.ggbook.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    public static final String FORMAT_LONGDATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_SHORTDATETIME = "yyyy-MM-dd";
    public static final String FORMAT_LONGDATETIME_COMPACT = "yyyyMMddHHmmss";
    public static final String FORMAT_SHORTDATETIME_COMPACT = "yyyyMMdd";
    public static final String FORMAT_YYYYMM_COMPACT = "yyyyMM";
    public static final String FORMAT_MIDDLEDATETIME = "yyyy-MM-dd HH:mm";

    private DateUtil() {
    }


    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的日期时间字符串
     * 出异常则返回空字符串
     */
    public static String getLongDate(Timestamp d) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(FORMAT_LONGDATETIME).format(d);
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的日期时间字符串
     * 出异常则返回空字符串
     */
    public static String getLongDate(Date d) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(FORMAT_LONGDATETIME).format(d);
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的日期时间字符串
     * 出异常则返回空字符串
     */
    public static String getLongDate(long time) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(FORMAT_LONGDATETIME).format(time);
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 根据yyyy-MM-dd HH:mm:ss格式字符串，返回Timestamp日期对象
     * 出异常则返回null
     */
    public static Timestamp getMiddleDate(String dateTime) {
        Timestamp d = null;
        try {
            d = getDateTimeFormatByString(dateTime, FORMAT_MIDDLEDATETIME);
        } catch (Exception e) {
        }
        return d;
    }

    /**
     * 根据yyyy-MM-dd HH:mm格式字符串，返回Timestamp日期对象
     * 出异常则返回null
     */
    public static Timestamp getLongDate(String dateTime) {
        Timestamp d = null;
        try {
            d = getDateTimeFormatByString(dateTime, FORMAT_LONGDATETIME);
        } catch (Exception e) {
        }
        return d;
    }

    /**
     * 返回yyyy-MM-dd格式的日期字符串
     * 出异常则返回空字符串
     */
    public static String getShortDate(Timestamp d) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(FORMAT_SHORTDATETIME).format(d);
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 返回yyyy-MM-dd格式的日期字符串
     * 出异常则返回空字符串
     */
    public static String getShortDate(long time) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(FORMAT_SHORTDATETIME).format(time);
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 返回yyyy-MM-dd格式的日期字符串
     * 出异常则返回空字符串
     */
    public static String getShortDate(Date d) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(FORMAT_SHORTDATETIME).format(d);
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 返回指定形式的是日期字符串
     *
     * @param formatPattern 如:yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd 等
     */
    public static String getStringDate(Timestamp d, String formatPattern) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(formatPattern).format(d);
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 返回指定形式的是日期字符串
     *
     * @param formatPattern 如:yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd 等
     */
    public static String getStringDate(Date d, String formatPattern) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(formatPattern).format(d);
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 返回指定形式的是日期字符串
     *
     * @param formatPattern 如:yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd 等
     */
    public static String getStringDate(long time, String formatPattern) {
        String s = "";
        try {
            s = FastDateFormat.getInstance(formatPattern).format(time);
        } catch (Exception e) {
        }
        return s;
    }

    private static Timestamp formatDateTime(String argDate, String fFlag, Locale locale) {
        Timestamp tt = null;
        try {
            if (locale == null) {
                locale = Locale.getDefault();
            }
            SimpleDateFormat objSDF = new SimpleDateFormat(fFlag, locale);
            Date date = new Date(argDate);
            String s = objSDF.format(date);
            int lng = s.trim().length();
            objSDF.applyPattern(fFlag.trim().substring(0, lng));
            tt = new Timestamp(objSDF.parse(s).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tt;

    }

    public static Timestamp getDateTimeFormatByString(String argDate, String fFlag) {
        Timestamp tt = null;
        try {
            int lng = argDate.trim().length();
            SimpleDateFormat objSDF = new SimpleDateFormat(fFlag);
            objSDF.applyPattern(fFlag.trim().substring(0, lng));
            tt = new Timestamp(objSDF.parse(argDate).getTime());
        } catch (Exception e) {
            try {
                tt = formatDateTime(argDate, fFlag, null);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return tt;
    }

    public static Timestamp getDateTimeFormatByString(String argDate, String fFlag, Locale locale) {
        Timestamp tt = null;
        try {
            int lng = argDate.trim().length();
            SimpleDateFormat objSDF = new SimpleDateFormat(fFlag, locale);
            objSDF.applyPattern(fFlag.trim().substring(0, lng));
            tt = new Timestamp(objSDF.parse(argDate).getTime());
        } catch (Exception e) {
            try {
                tt = formatDateTime(argDate, fFlag, locale);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return tt;
    }


    public static Date getDateFormatByString(String argDate, String fFlag) {
        Date tt = null;
        try {
            int lng = argDate.trim().length();
            SimpleDateFormat objSDF = new SimpleDateFormat(fFlag);
            objSDF.applyPattern(fFlag.trim().substring(0, lng));
            tt = new Date(objSDF.parse(argDate).getTime());
        } catch (Exception e) {
            try {
                tt = formatDateTime(argDate, fFlag, null);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return tt;
    }

    public static Date getDateFormatByString(String argDate, String fFlag, Locale locale) {
        Date tt = null;
        try {
            int lng = argDate.trim().length();
            SimpleDateFormat objSDF = new SimpleDateFormat(fFlag, locale);
            objSDF.applyPattern(fFlag.trim().substring(0, lng));
            tt = new Date(objSDF.parse(argDate).getTime());
        } catch (Exception e) {
            try {
                tt = formatDateTime(argDate, fFlag, locale);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return tt;
    }

    public static boolean judgeDate(String argDate) {
        boolean tag = false;
        try {
            Timestamp tt = getDateTimeFormatByString(argDate, FORMAT_LONGDATETIME);
            tag = true;
        } catch (Exception e) {
        }
        return tag;
    }


    /**
     * 获取天或月或者周的时间
     *
     * @param selType 通过类型(curDay,curMonth,curWeek)
     * @return List<Timestamp>
     */
    public static List<Timestamp> getDayOrMonthOrWeek(String selType) {
        List<Timestamp> timestampList = new ArrayList<Timestamp>();
        //日历对象
        Calendar calendar = Calendar.getInstance();
        //暂时月或日或周的存贮
        String dymdStr = "";
        //返回天或月的开始及结束时间
        String dmwFirstAndEndStr[] = new String[2];
        //天的开始时
        String dayBeginTime = " 00:00:00";
        //天的结束时
        String dayEndTime = " 23:59:59";
        //月的开始时
        String monthBeginTime = "-01 00:00:00";
        //按日查
        if (selType != null) {
            if (selType.trim().equalsIgnoreCase("curDay")) {
                StringBuffer sb = new StringBuffer(8);
                sb.append(calendar.get(Calendar.YEAR));
                sb.append("-");
                sb.append(one2TwoDigit((calendar.get(Calendar.MONTH) + 1)));
                sb.append("-");
                sb.append(one2TwoDigit(calendar.get(Calendar.DAY_OF_MONTH)));
                dymdStr = sb.toString();

                dmwFirstAndEndStr[0] = dymdStr + dayBeginTime;
                dmwFirstAndEndStr[1] = dymdStr + dayEndTime;
            }
            //按月查
            if (selType.trim().equalsIgnoreCase("curMonth")) {
                StringBuffer sb = new StringBuffer(10);
                sb.append(calendar.get(Calendar.YEAR));
                sb.append("-");
                sb.append(one2TwoDigit((calendar.get(Calendar.MONTH) + 1)));
                dymdStr = sb.toString();

                calendar.set(Calendar.DATE, 1);//把日期设置为当月第一天
                calendar.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
                int MaxDate = calendar.get(Calendar.DATE);

                dmwFirstAndEndStr[0] = dymdStr + monthBeginTime;
                dmwFirstAndEndStr[1] = dymdStr + "-" + MaxDate + dayEndTime;
            }
            //按周查
            if (selType.trim().equalsIgnoreCase("curWeek")) {
                Calendar startDate = (Calendar) calendar.clone();
                Calendar endDate = (Calendar) calendar.clone();
                int nowWeekNum = calendar.get(Calendar.DAY_OF_WEEK);
                startDate.add(Calendar.DATE, 1 - nowWeekNum);
                endDate.add(Calendar.DATE, 7 - nowWeekNum);
                //开始时间
                StringBuffer startSB = new StringBuffer(8);
                startSB.append(startDate.get(Calendar.YEAR));
                startSB.append("-");
                startSB.append(one2TwoDigit((startDate.get(Calendar.MONTH) + 1)));
                startSB.append("-");
                startSB.append(one2TwoDigit(startDate.get(Calendar.DAY_OF_MONTH)));
                startSB.append(dayBeginTime);

                //结束时间
                StringBuffer endSB = new StringBuffer(8);
                endSB.append(endDate.get(Calendar.YEAR));
                endSB.append("-");
                endSB.append(one2TwoDigit((endDate.get(Calendar.MONTH) + 1)));
                endSB.append("-");
                endSB.append(one2TwoDigit(endDate.get(Calendar.DAY_OF_MONTH)));
                endSB.append(dayEndTime);

                dmwFirstAndEndStr[0] = startSB.toString();
                dmwFirstAndEndStr[1] = endSB.toString();
            }
        }
        timestampList.add(Timestamp.valueOf(dmwFirstAndEndStr[0]));
        timestampList.add(Timestamp.valueOf(dmwFirstAndEndStr[1]));

        return timestampList;
    }

    /**
     * 一位转两位，如 1 则转为 01 ,即在一位前补零
     *
     * @param one
     * @return
     */
    private static String one2TwoDigit(int one) {
        if (one / 10 >= 1)
            return String.valueOf(one);
        return "0" + String.valueOf(one);
    }

    /**
     * 返回日期的任意值
     *
     * @param date
     * @param valueType，传入的参数分别为Y(年)，M(月),，D(日)，h(时)，m(分)，s(秒)
     * @return
     * @throws Exception
     */
    public static int getIntTimeValueByTimestamp(Timestamp date, char valueType) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        switch (valueType) {
            case 'Y':
                return calendar.get(Calendar.YEAR);//返回年份
            case 'M':
                return calendar.get(Calendar.MONTH) + 1;//返回月份,月份的计算是从0开始的，所以要加1
            case 'D':
                return calendar.get(Calendar.DAY_OF_MONTH);//返回当月的日
            case 'h':
                return calendar.get(Calendar.HOUR_OF_DAY);//返回小时
            case 'm':
                return calendar.get(Calendar.MINUTE);//返回分钟
            case 's':
                return calendar.get(Calendar.SECOND);//返回秒
        }
        return -1;
    }

    /**
     * 得到现在系统时间
     */
    public static long getNowTime() {
        return System.currentTimeMillis();
    }

    /**
     * 返回日期格式字符串
     */

    public static String getLongDate(String longDate, String curLang, String formatPattern) {
        String result = "";
        long longTime = Long.parseLong(longDate);
        try {
            if (curLang.equals("en")) {
                result = FastDateFormat.getInstance(formatPattern, Locale.ENGLISH).format(new Date(longTime));
            } else {
                result = FastDateFormat.getInstance(formatPattern).format(new Date(longTime));
            }
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 返回当前月有多少日
     */
    public static int daysInMonth(int year, int month) {
        if (month <= 0 || month > 12) {
            System.out.println("非法参数：month ");
            return -1;
        }
        int days = 31;
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            days = 30;
        }
        if (month == 2) {
            if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
                days = 29;
            } else {
                days = 28;
            }
        }
        return days;
    }

    public static List<String> getDates(String beginDate, String endDate) throws Exception {
        return getDates(beginDate, endDate, false);
    }

    public static List<String> getDates(String beginDate, String endDate, boolean includeEndDate) throws Exception {
        try {
            long bTime = DateUtil.getLongDate(beginDate + " 00:00:00").getTime();
            long eTime = DateUtil.getLongDate(endDate + " 00:00:00").getTime();
            return getDates(bTime, eTime, includeEndDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回日期区域中的所有日期
     * 比如beginDate=2011-10-10, endDate=2011-10-15,则返回20111010,20111011,20111012,20111013,20111014
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<String> getDates(long beginDate, long endDate) throws Exception {
        return getDates(beginDate, endDate, false);
    }

    /**
     * 返回日期区域中的所有日期
     * 比如beginDate=2011-10-10, endDate=2011-10-15
     * 如果includeEndDate==false则返回20111010,20111011,20111012,20111013,20111014
     * 如果includeEndDate==true则返回20111010,20111011,20111012,20111013,20111014,20111015
     *
     * @param beginDate
     * @param endDate
     * @param includeEndDate : 是否包含最后的日期
     * @return
     * @throws Exception
     */
    public static List<String> getDates(long beginDate, long endDate, boolean includeEndDate) throws Exception {
        List<String> result = getDates(beginDate, endDate, FORMAT_SHORTDATETIME_COMPACT, includeEndDate);
        return result;
    }

    /**
     * 返回日期区域中的所有日期
     * 比如beginDate=2011-10-10, endDate=2011-10-15
     * 如果includeEndDate==false则返回20111010,20111011,20111012,20111013,20111014
     * 如果includeEndDate==true则返回20111010,20111011,20111012,20111013,20111014,20111015
     *
     * @param beginDate
     * @param endDate
     * @param includeEndDate : 是否包含最后的日期
     * @return
     * @throws Exception
     */
    public static List<String> getDates(long beginDate, long endDate, String formatPattern, boolean includeEndDate) throws Exception {
        List<String> result = new Vector();

        long oneDayTime = 24 * 60 * 60 * 1000;
        if (includeEndDate) {
            endDate += oneDayTime;
        }
        long date = beginDate;
        int i = 0;
        while (date < endDate) {
            String yyyymmdd = DateUtil.getStringDate(date, formatPattern);
            result.add(yyyymmdd);

            if (i > 100) {
                break;//防止出现死循环
            }
            date += oneDayTime;
            i++;
        }
        return result;
    }

    /**
     * //判断当前日期属于星期几
     * 返回值为 1、2、3、4、5、6、7
     *
     * @param time
     * @return
     */
    public static int getWeekForTime(long time) {
        Date newDate = new Date(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) week = 7;
        return week;
    }

    /**
     * 判断当前日期属于星期几
     * 返回值为 一、二、三、四、五、六、日
     *
     * @param longDate
     * @return
     */
    public static String getWeekStrForTime(String longDate) {
        long time = Long.parseLong(longDate);
        int week = getWeekForTime(time);
        String weekValue = "";
        if (week == 1) {
            weekValue = "一";
        } else if (week == 2) {
            weekValue = "二";
        } else if (week == 3) {
            weekValue = "三";
        } else if (week == 4) {
            weekValue = "四";
        } else if (week == 5) {
            weekValue = "五";
        } else if (week == 6) {
            weekValue = "六";
        } else if (week == 7) {
            weekValue = "日";
        }
        return weekValue;
    }

    /**
     * 根据出生日期计算年龄
     *
     * @param birthday isNominal是否为虚岁 true为虚岁 false为周岁
     * @return
     * @throws Exception
     */
    public static int calcAge(long birthday, boolean isNominal) throws Exception {
        int age = 0;
        long curTime = System.currentTimeMillis();
        if (birthday <= 0 || curTime < birthday) {
            //当出生日期为0或大于当前时间的时候不作处理
            return age;
        }


        if (isNominal) {
            String birthDayYear = FastDateFormat.getInstance("yyyy").format(new Date(birthday));
            String curYear = FastDateFormat.getInstance("yyyy").format(new Date(curTime));

            int birthYearTime = Integer.valueOf(birthDayYear);
            int curYearTime = Integer.valueOf(curYear);
            age = curYearTime - birthYearTime;
        } else {
            long yearLongTime = curTime - birthday;
            age = (int) (yearLongTime / 1000 / 24 / 60 / 60 / 365);
        }
        return age;
    }

    /**
     * 根据出生日期计算虚岁年龄
     *
     * @param birthday
     * @return
     * @throws Exception
     */
    public static int calcAge(long birthday) throws Exception {
        return calcAge(birthday, true);
    }

    /**
     * 根据出生日期计算年龄
     *
     * @param birthday 格式：1989-01-01
     * @return
     * @throws Exception
     */
    public static int calcAge(String birthday, boolean isNominal) throws Exception {
        int age = 0;
        if (StringUtils.isNotBlank(birthday)) {
            long birthdayLongTime = getDateTimeFormatByString(birthday, FORMAT_SHORTDATETIME).getTime();
            age = calcAge(birthdayLongTime, isNominal);
        }
        return age;
    }

    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convert2long(String date, String format) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }


    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = DateUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }


    // 计算当月最后一天,返回字符串
    public String getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);//加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);//减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 上月第一天
    public String getPreviousMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);//减一个月，变为下月的1号
        //lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获取当月第一天
    public String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获得本周星期日的日期
    public static String getCurrentWeekday() {
        int weeks = 0;
        int mondayPlus = DateUtil.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    //获取当天时间
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);//可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    // 获得当前日期与本周日相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;         //因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    //获得本周一的日期
    public static String getMondayOFWeek() {
        int weeks = 0;
        weeks = 0;
        int mondayPlus = DateUtil.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    //获得相应周的周六的日期
    public String getSaturday() {
        int weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得上周星期日的日期
    public static String getPreviousWeekSunday() {
        int weeks = 0;
        weeks--;
        int mondayPlus = DateUtil.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    public static Date getPreviousWeekSundayTime() {
        int weeks = 0;
        weeks--;
        int mondayPlus = DateUtil.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        return currentDate.getTime();
    }

    // 获得上周星期一的日期
    public static String getPreviousWeekday() {
        int weeks = 0;
        weeks--;
        int mondayPlus = DateUtil.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    public static Date getPreviousWeekdayTime() {
        int weeks = 0;
        weeks--;
        int mondayPlus = DateUtil.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        return currentDate.getTime();
    }

    // 获得下周星期一的日期
    public static String getNextMonday() {
        int weeks = 0;
        weeks++;
        int mondayPlus = DateUtil.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得下周星期日的日期
    public String getNextSunday() {

        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    private static int getMonthPlus() {
        int MaxDate;
        Calendar cd = Calendar.getInstance();
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);//把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        MaxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -MaxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }

    //获得上月最后一天的日期
    public static String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得下个月第一天的日期
    public static String getNextMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得下个月最后一天的日期
    public static String getNextMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//加一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得明年最后一天的日期
    public static String getNextYearEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得明年第一天的日期
    public static String getNextYearFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }

    //获得本年有多少天
    public static int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);//把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);//把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    public static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);//获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);//把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);//把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    //获得本年第一天的日期
    public static String getCurrentYearFirst() {
        int yearPlus = DateUtil.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }


    //获得本年最后一天的日期 *
    public static String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }


    //获得上年第一天的日期 *
    public static String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

    //获得上年最后一天的日期
    public static String getPreviousYearEnd() {
        int MaxYear = 0;
        int weeks = 0;
        weeks--;
        int yearPlus = DateUtil.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        DateUtil.getThisSeasonTime(11);
        return preYearDay;
    }

    //获得本季度
    public static String getThisSeasonTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;//years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = DateUtil.getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;

    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 最后一天
     */
    public static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (DateUtil.isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }


    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }
    /**
     * 增加分
     * @param date
     * @param amount
     * @return
     */
    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }
}
