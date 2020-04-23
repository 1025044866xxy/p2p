package com.xxy.p2p.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    // private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    private static final ThreadLocal<SimpleDateFormat> dateFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 时间转换成默认格式 yyyy-MM-dd
     */
    public static String dateDefaultFormat(String time) {
        SimpleDateFormat sf = dateFormatter.get();
        if (StringUtils.isBlank(time)) {
            return null;
        }

        try {
            return sf.format(sf.parse(time));
        } catch (ParseException e) {
            return null;
        }
    }

    public static String format(String time, String formatter) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(formatter);
        SimpleDateFormat defaultSF = dateFormatter.get();
        return sf.format(defaultSF.parse(time));
    }

    public static String format(Date time, String formatter) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(formatter);
        return sf.format(time);
    }

    /**
     * Date 转 字符串
     */
    public static String dateDefaultFormat(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }

    public static String addOneDay(String date) throws Exception {
        SimpleDateFormat sf = dateFormatter.get();
        Date origDate = sf.parse(date);

        return sf.format(origDate.getTime() + 3600000 * 24);
    }

    public static String addDeltaDays(String date, Integer delta)
        throws ParseException {
        SimpleDateFormat sf = dateFormatter.get();
        Date origDate = sf.parse(date);
        long deltaTime = 3600000l * 24 * delta;
        return sf.format(origDate.getTime() + deltaTime);
    }

    public static String reduceOneDay(String date) throws Exception {
        SimpleDateFormat sf = dateFormatter.get();
        Date origDate = sf.parse(date);

        return sf.format(origDate.getTime() - 3600000 * 24);
    }

    public static String getNowDay() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(new Date());
    }

    /**
     * 判断输入的时间是否在今日之前
     */
    public static Boolean isBeforeToday(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        Date origDate = sf.parse(date);
        Date today = sf.parse(sf.format(new Date()));
        return origDate.before(today);
    }

    /**
     * 判断startTime 是否早于 endTime
     */
    public static Boolean timeBefore(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return false;
        }

        Date startDate = sf.parse(startTime);
        Date endDate = sf.parse(endTime);
        return startDate.before(endDate);
    }

    /**
     * 判断startTime 是否晚于 endTime
     */
    public static Boolean timeAfter(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return false;
        }

        Date startDate = sf.parse(startTime);
        Date endDate = sf.parse(endTime);
        return startDate.after(endDate);
    }

    public static String subtractOneDay(String date) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        if (StringUtils.isEmpty(date)) {
            return null;
        }

        Date origDate = sf.parse(date);

        // 将日期+1天
        Calendar c = Calendar.getInstance();
        c.setTime(origDate);
        c.add(Calendar.DAY_OF_MONTH, -1);

        return sf.format(c.getTime());
    }

    public static String lastYearDate() throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        // 将日期+1天
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);

        return sf.format(c.getTime());
    }

    public static String lastWeekDate() throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        // 将日期+1天
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);

        return sf.format(c.getTime());
    }

    /**
     * 返回给定时间多少天之前的日期字符串
     */
    public static String subtractDays(Date date, Integer i) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -i);

        return sf.format(c.getTime());
    }

    /**
     * 返回昨日时间字符串
     */
    public static String getYesterday() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        return sf.format(c.getTime());
    }

    public static String getOffsetDay(int offset) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return getOffsetDay(DateUtil.getNowDay(), offset);
    }


    public static String getOffsetDay(String date, int offset) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        // 将日期+1天
        Calendar c = Calendar.getInstance();

        try {

            if (!StringUtils.isEmpty(date)) {
                Date origDate = sf.parse(date);
                c.setTime(origDate);
            }
        } catch (Exception e) {
            return null;
        }

        c.add(Calendar.DAY_OF_MONTH, offset);

        return sf.format(c.getTime());

    }

    public static String getOffsetYear(String date, int offset) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        if (StringUtils.isEmpty(date)) {
            return null;
        }

        try {

            Date origDate = sf.parse(date);

            // 将日期+1天
            Calendar c = Calendar.getInstance();
            c.setTime(origDate);
            c.add(Calendar.YEAR, offset);

            return sf.format(c.getTime());

        } catch (Exception e) {
            return null;
        }

    }

    public static Date dateParse(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        Date origDate = sf.parse(date);
        return origDate;
    }

    public static long daysBetweenCount(String sT, String eT) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date sD = sf.parse(sT);
        Date eD = sf.parse(eT);
        long count = daysBetweenCount(sD, eD);
        return count;
    }

    public static long daysBetweenCount(Date startDate, Date endDate) throws ParseException {
        long count = (endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24);
        return count;
    }

    public static boolean daysBetweenCountOverYear(String sT, String eT) throws ParseException {
        if (daysBetweenCount(sT, eT) > 366) {
            return true;
        }

        return false;
    }

    /**
     * 时间间隔两年判断
     * @param sT
     * @param eT
     * @return
     * @throws ParseException
     */
    public static boolean daysBetweenCountOverTwoYear(String sT, String eT) throws ParseException {
        if (daysBetweenCount(sT, eT) > 366 * 2) {
            return true;
        }

        return false;
    }

    /**
     * 起始时间相差周数
     */
    public static Integer weekBetweenCount(String sT, String eT) throws Exception {

        Map<String, Integer> sW = getWeekAndYear(sT);
        Map<String, Integer> eW = getWeekAndYear(eT);

        Integer sY = Integer.valueOf(sW.get("year"));
        Integer eY = Integer.valueOf(eW.get("year"));

        // 相差几年
        Integer delta = eY - sY;
        Integer weekCount = eW.get("week") - sW.get("week") + 52 * delta;

        return weekCount;
    }

    public static Integer monthBetweenCount(String sT, String eT) throws Exception {
        Integer sM = Integer.valueOf(sT.substring(5, 7));
        Integer eM = Integer.valueOf(eT.substring(5, 7));
        Integer deltaMonth = eM - sM;

        Integer sY = Integer.valueOf(sT.substring(0, 4));
        Integer eY = Integer.valueOf(eT.substring(0, 4));
        Integer deltaYear = eY - sY;

        return deltaMonth + deltaYear * 12;
    }

    public static Map<String, Integer> getWeekAndYear(String date) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        Calendar cal = Calendar.getInstance();

        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = dateFormatter.get();
        SimpleDateFormat formatMon = new SimpleDateFormat("MM");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTime(d);
        int month = Integer.valueOf(formatMon.format(d));
        int year = Integer.valueOf(formatYear.format(d));

        int week = cal.get(Calendar.WEEK_OF_YEAR);
        result.put("week", week);
        if (week == 1 && month == 12) {
            result.put("year", year + 1);
        } else {

            result.put("year", year);
        }

        return result;
    }

    public static String getYearWeek(String date) {
        Map<String, Integer> map = getWeekAndYear(date);
        Integer week = map.get("week");
        Integer year = map.get("year");
        return week < 10 ? year + "-0" + week : year + "-" + week;
    }

    public static String getYearMonth(String date) {
        return date.substring(0, 7);
    }

    /**
     * 加n周
     * @param yearWeek "2018-10 2018年的第10周"
     */
    public static String addDeltaWeek(String yearWeek, int n) {
        Integer year = Integer.valueOf(yearWeek.split("-")[0]);
        Integer week = Integer.valueOf(yearWeek.split("-")[1]);

        int newWeek = week + n;
        int x = newWeek % 52 == 0 ? 52 : newWeek % 52;
        int y = year;
        if (newWeek > 52) {
            y = year + newWeek / 52;
            if (newWeek % 52 == 0) {
                y = y - 1;
            }
        }

        return x < 10 ? y + "-0" + x : y + "-" + x;
    }

    public static String addOneMonth(String yearMonth) {
        Integer year = Integer.valueOf(yearMonth.substring(0, 4));
        Integer month = Integer.valueOf(yearMonth.substring(5, 7));

        if (month >= 12) {
            month = 1;
            year++;
        } else {
            month++;
        }

        return month < 10 ? year + "-0" + month : year + "-" + month;
    }

    /**
     * 周的第一天
     */
    public static String weekToFirstDay(String year, String week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, Integer.valueOf(year));
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, (Integer.valueOf(week) - 1) * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    public static String getFirstDayOfWeek(Date date) {
        SimpleDateFormat sdf = dateFormatter.get();
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return sdf.format(c.getTime());
    }

    /**
     * 周的最后一天
     */
    public static String weekToLastDay(String year, String week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, Integer.valueOf(year));
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, (Integer.valueOf(week) - 1) * 7);

        return weekToLastDay(cal.getTime());
    }

    public static String weekToLastDay(Date date) {
        SimpleDateFormat sdf = dateFormatter.get();
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return sdf.format(c.getTime());
    }

    /**
     * 月的最后一天
     */
    public static String monthToLastDay(String year, String month) {
        SimpleDateFormat sdf = dateFormatter.get();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return sdf.format(cal.getTime());
    }

    public static Integer getDayOfWeek(String datetime) throws Exception {
        SimpleDateFormat f = dateFormatter.get();
        Calendar cal = Calendar.getInstance();
        Date date = f.parse(datetime);
        cal.setTime(date);
        //老外按照周日算第一天
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static Integer getDayOfWeek() throws Exception {

        return getDayOfWeek(getNowDay());
    }

    public static String dateToWeek(String datetime) throws Exception {
        SimpleDateFormat f = dateFormatter.get();
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date = f.parse(datetime);
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static String dateToMonth(String month) {
        switch (month) {
            case "01":
                return "一月";
            case "02":
                return "二月";
            case "03":
                return "三月";
            case "04":
                return "四月";
            case "05":
                return "五月";
            case "06":
                return "六月";
            case "07":
                return "七月";
            case "08":
                return "八月";
            case "09":
                return "九月";
            case "10":
                return "十月";
            case "11":
                return "十一月";
            case "12":
                return "十二月";
            default:
                return "";
        }
    }

    public static String getFirstDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 2;

        return getOffsetDay(-w);
    }

    public static String getLastDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK);

        return getOffsetDay(9 - w);
    }

    /**
     * 获取当月第一天
     * @return
     */
    public static String getCurrWeekFirst() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        Date theDate = calendar.getTime();

        // 第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return dateDefaultFormat(gcLast.getTime());
    }

    /**
     * 获取当月第一天
     * @return
     */
    public static String getCurrMonthFirst() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        Date theDate = calendar.getTime();

        // 第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        return dateDefaultFormat(gcLast.getTime());
    }

    /**
     * 获取当前季度
     * @return
     */
    public static int getQuarter() {
        Calendar c = Calendar.getInstance();
        int month = c.get(c.MONTH) + 1;
        int quarter = 0;
        if (month >= 1 && month <= 3) {
            quarter = 1;
        } else if (month >= 4 && month <= 6) {
            quarter = 2;
        } else if (month >= 7 && month <= 9) {
            quarter = 3;
        } else {
            quarter = 4;
        }
        return quarter;
    }

    /**
     * 获取季度首天
     * @param num
     * @return
     */
    public static String getCurrQuarterFirst(int num) {
        String[] s = new String[2];
        String str = "";
        // 设置本年的季
        Calendar quarterCalendar = null;
        switch (num) {
            case 1: // 本年到现在经过了一个季度，在加上前4个季度
                quarterCalendar = Calendar.getInstance();
                quarterCalendar.set(Calendar.MONTH, 3);
                quarterCalendar.set(Calendar.DATE, 1);
                quarterCalendar.add(Calendar.DATE, -1);
                str = dateDefaultFormat(quarterCalendar.getTime());
                s[0] = str.substring(0, str.length() - 5) + "01-01";
                s[1] = str;
                break;
            case 2: // 本年到现在经过了二个季度，在加上前三个季度
                quarterCalendar = Calendar.getInstance();
                quarterCalendar.set(Calendar.MONTH, 6);
                quarterCalendar.set(Calendar.DATE, 1);
                quarterCalendar.add(Calendar.DATE, -1);
                str = dateDefaultFormat(quarterCalendar.getTime());
                s[0] = str.substring(0, str.length() - 5) + "04-01";
                s[1] = str;
                break;
            case 3:// 本年到现在经过了三个季度，在加上前二个季度
                quarterCalendar = Calendar.getInstance();
                quarterCalendar.set(Calendar.MONTH, 9);
                quarterCalendar.set(Calendar.DATE, 1);
                quarterCalendar.add(Calendar.DATE, -1);
                str = dateDefaultFormat(quarterCalendar.getTime());
                s[0] = str.substring(0, str.length() - 5) + "07-01";
                s[1] = str;
                break;
            case 4:// 本年到现在经过了四个季度，在加上前一个季度
                quarterCalendar = Calendar.getInstance();
                str = dateDefaultFormat(quarterCalendar.getTime());
                s[0] = str.substring(0, str.length() - 5) + "10-01";
                s[1] = str.substring(0, str.length() - 5) + "12-31";
                break;
        }
        return s[0];
    }

    /**
     * 获取当年第一天
     * @return
     */
    public static String getCurrYearFirst(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        return dateDefaultFormat(calendar.getTime());
    }

    public static long getDayNum(String startDate, String endDate) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startDate));
        long time1 = cal.getTimeInMillis();

        cal.setTime(sdf.parse(endDate));
        long time2 = cal.getTimeInMillis();

        return (time2-time1)/(1000*3600*24);
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(getCurrWeekFirst());
//        System.out.println(getCurrMonthFirst());
//        System.out.println(getCurrQuarterFirst(getQuarter()));
//        System.out.println(getCurrYearFirst());

//        System.out.println(getDayNum("2019-10-11", "2019-11-11"));

        String startDate="2019-10-11";
        String endDate="2019-11-11";


        System.out.println(getDayNum(startDate, endDate));
    }

}
