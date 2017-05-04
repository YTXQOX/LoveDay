package com.ljstudio.android.loveday.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class DateUtil {

    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YEAR = "yyyy";
    public static final String DATE_MONTH = "MM";
    public static final String DATE_DAY = "dd";
    public static final String DATE_HOUR = "HH";
    public static final String DATE_MINUTE = "mm";
    public static final String DATE_SECOND = "ss";

    public static Calendar calendar = Calendar.getInstance();

    public static String changeTime(String time) throws Exception {
        if (StringUtil.isEmpty(time)) {
            throw new Exception("The time must not be null or ''");
        }
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = oldFormat.parse(time);
        return newFormat.format(date);
    }


    public static String currentyeartime() {
        SimpleDateFormat newFormatYear = new SimpleDateFormat("yy");
        return newFormatYear.format(new Date());
    }


    public static String currentyear() {
        SimpleDateFormat newFormatYear = new SimpleDateFormat("yyyy");
        return newFormatYear.format(new Date());
    }


    // 短日期格式
    public static String DATE_FORMAT = "yyyy-MM-dd";

    // 长日期格式
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convert2long(String date, String format) {
        try {
            if (!StringUtil.isEmpty(date)) {
                if (StringUtil.isEmpty(format))
                    format = DateUtil.TIME_FORMAT;

                SimpleDateFormat sf = new SimpleDateFormat(format);
                return sf.parse(date).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 处理日期和月 不够10加个0
     */
    public static String change(String string) {
        int i = Integer.valueOf(string);
        return i < 10 ? 0 + string : string;
    }

    /**
     * 得到两个日期间隔天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int betweenDays(Date date1, Date date2) {
        long day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1
                .getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)
                : (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
        return Integer.parseInt(String.valueOf(day));

    }

    /**
     * 得到两个日期间隔天数
     *
     * @param aMask 时间格式
     * @param Date1
     * @param Date2
     * @return
     * @throws ParseException
     */
    public static int betweenDays(String aMask, String Date1, String Date2) throws ParseException {
        return betweenDays(convertStringToDate(aMask, Date1), convertStringToDate(aMask, Date2));
    }

    /**
     * 日期增加或者减少秒，分钟，天，月，年
     *
     * @param date
     * @param type   类型
     * @param offset （整数）
     * @return 增加或者减少之后的日期
     */
    public static Date addDate(Date date, String type, int offset) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        if (type.equals(DATE_SECOND)) {
            gc.add(GregorianCalendar.SECOND, offset);
        } else if (type.equals(DATE_MINUTE)) {
            gc.add(GregorianCalendar.MINUTE, offset);
        } else if (type.equals(DATE_HOUR)) {
            gc.add(GregorianCalendar.HOUR, offset);
        } else if (type.equals(DATE_DAY)) {
            gc.add(GregorianCalendar.DATE, offset);
        } else if (type.equals(DATE_MONTH)) {
            gc.add(GregorianCalendar.MONTH, offset);
        } else if (type.equals(DATE_YEAR)) {
            gc.add(GregorianCalendar.YEAR, offset);
        }
        return gc.getTime();
    }

    public static String addDate(String aMask, String srcDate, String type,
                                 int offset) throws ParseException {
        return convertDateToString(aMask,
                addDate(convertStringToDate(aMask, srcDate), type, offset));
    }

    /**
     * This method generates a string representation of a date/time in the
     * format you specify on input
     *
     * @param aMask   the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws ParseException when String doesn't match the expected format
     * @see SimpleDateFormat
     */
    public static Date convertStringToDate(String aMask, String strDate)
            throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(aMask);
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
        return (date);
    }

    /**
     * 将日期转换为字符串
     *
     * @param aMask 格式字符串
     * @param date  日期
     * @return
     * @throws ParseException
     */
    public static String convertDateToString(String aMask, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(aMask);
        return sdf.format(date);
    }

    /**
     * 获取两个日期之间所有的天（yyyy-MM-dd）
     *
     * @param aMask
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static List<String> getBetweenDates(String aMask, String date1, String date2)
            throws ParseException {
        List<String> list = new ArrayList<String>();
        if (!date1.equals(date2)) {
            String tmp;
            if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
                tmp = date1;
                date1 = date2;
                date2 = tmp;
            }
            tmp = addDate(aMask, date1, DATE_DAY, 0);

            while (tmp.compareTo(date2) <= 0) {
                list.add(tmp);
                tmp = addDate(aMask, tmp, DATE_DAY, 1);
            }
        }
        return list;
    }

    /**
     * 比较两个日期的大小
     */
    public static int compareDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return 1;
        } else if (date1.getTime() < date2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 比较两个日期是否大于几个小时
     */
    public static int compareDate(Date date1, Date date2, int index) {
        if ((date1.getTime() - date2.getTime()) > (index * 60 * 60 * 1000)) {
            return 1;
        } else if ((date1.getTime() - date2.getTime()) == (index * 60 * 60 * 1000)) {
            return 0;
        } else {
            return -1;
        }
    }

    public static String compareDateDay(String time, String pattern) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != null) {
            try {
                Date tDate = new SimpleDateFormat(pattern).parse(time);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
                Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日");
                    long dTime = today.getTime() - tDate.getTime();
                    if (tDate.before(thisYear)) {
                        display = new SimpleDateFormat("yyyy年MM月dd日").format(tDate);
                    } else {

//                        if (dTime < tMin) {
//                            display = "刚刚";
//                        } else if (dTime < tHour) {
//                            display = (int) Math.ceil(dTime / tMin) + "分钟前";
//                        } else if (dTime < tDay && tDate.after(yesterday)) {
//                            display = (int) Math.ceil(dTime / tHour) + "小时前";
//                        } else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
//                            display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
//                        } else {
//                            display = halfDf.format(tDate);
//                        }
                        if (dTime < tDay && tDate.after(yesterday)) {
                            display = new SimpleDateFormat("HH:mm").format(tDate);
                        } else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
                            display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
                        } else {
                            display = halfDf.format(tDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return display;
    }

    public static boolean compareDateTF(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return true;
        } else if (date1.getTime() < date2.getTime()) {
            return false;
        } else {
            return false;
        }
    }

    public static Date getDate(Date date, int dayOffset) {
        long millis = date.getTime();
        millis += (long) dayOffset * 24 * 60 * 60 * 1000;
        return new Date(millis);
    }

    /**
     *
     */
    public static int getMonthDays(int year, int month) {
        int day = 0;

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day = 30;
                break;
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    day = 29;
                } else {
                    day = 28;
                }
                break;
        }

        return day;
    }

    public static int iSTodayDate(String strTodayDate, String strWhichDate) {
        SystemOutUtil.sysOut("strWhichDate-->" + strWhichDate);

        int i = 100;
        Date todayDate = DateFormatUtil.convertStr2Date(strTodayDate, DateFormatUtil.sdfDate1);
        Date whichDate = DateFormatUtil.convertStr2Date(strWhichDate, DateFormatUtil.sdfDate1);

        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(todayDate);

        Date dateBefore = getCalendarByIndex(calendarToday, -1);
        Date dateAfter = getCalendarByIndex(calendarToday, 1);

        String strDateBefore = DateFormatUtil.getDate(dateBefore, DateFormatUtil.sdfDate1);
        String strDateAfter = DateFormatUtil.getDate(dateAfter, DateFormatUtil.sdfDate1);
        SystemOutUtil.sysOut("strDateBefore-->" + strDateBefore);
        SystemOutUtil.sysOut("strDateAfter-->" + strDateAfter);
        if (strWhichDate.equals(strTodayDate)) {
            i = 0;
        } else if (strWhichDate.equals(strDateBefore)) {
            i = -1;
        } else if (strWhichDate.equals(strDateAfter)) {
            i = 1;
        } else {
            return 100;
        }

        return i;
    }

    // getCaledarByIndex 得到相隔天数的 Date
    public static Date getCalendarByIndex(Calendar calendar, int index) {
        calendar.add(Calendar.DAY_OF_MONTH, index);

        Date tempDate = calendar.getTime();

        return tempDate;
    }

}
