package com.ljstudio.android.loveday.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guoxinyu on 15-9-8.
 */
public class StringUtil {

    /**
     * 判断是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNull(String obj) {
        return null == obj || "".equals(obj);
    }

    /**
     * 是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数组是否为空
     *
     * @param values
     * @return
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    /**
     * 判断是否符合邮箱格式 符合返回ture
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        if (TextUtils.isEmpty(strEmail)) {
            return false;
        }
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail.trim());
        return m.matches();
    }

    /**
     * 判断是否为固定电话 正确为ture
     *
     * @param tel
     * @return
     */
    public static boolean isTel(String tel) {
        boolean iscall = false;
        if (tel.length() >= 9 && tel.length() <= 13) {
            iscall = true;
        }
        return iscall;
    }

    /**
     * 是否包含特殊字符
     *
     * @param tel
     * @return
     */
    public static boolean isN(String tel) {
        // Pattern pattern =
        // Pattern.compile("^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$");
        // String regEx =
        // "[`~#$&*()=|{}':;',\\[\\].<>/?~！#￥……&*（）—|{}【】‘；：”“’。，、？]";

        String regEx = "[#$&*()|]";

        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(tel.trim());
        return m.find();
    }

    /**
     * 判断是否为特殊字符
     *
     * @param pInput
     * @return
     */
    public static boolean isSpecialChar(String pInput) {
        if (pInput == null) {
            return false;
        }
        String regEx = ".*[&|'|>|<|\\\\|/].*$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(Pattern.compile("[\\r|\\n]").matcher(pInput).replaceAll(""));
        return matcher.matches();
    }

    public static String StringFilter(String str) {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~#$&*()=|{}':;',\\[\\].<>/?~！#￥……&*（）—|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判断是否为手机号码 正确返回true
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        // Pattern pattern =
        // Pattern.compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?13[0-9]\\d{8}|15[089]\\d{8}");
        Pattern pattern = Pattern.compile("([0-9]{3})([0-9]{4})([0-9]{4})");
        Matcher m = pattern.matcher(phone.trim());
        return m.matches();
    }

    /**
     * 判断输入是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        String strPattern = "[0-9]*";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str.trim());
        return m.matches();
    }

    /**
     * 检查是不是生日类型 支持闰年,2月特殊判断等等
     */
    public static boolean isBirthday(String birth) {
        // Pattern pt =
        // Pattern.compile("^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$");
        // Pattern pt =
        // Pattern.compile("((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(10|12|0?[13578])([-\\/\\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(11|0?[469])([-\\/\\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(0?2)([-\\/\\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([3579][26]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$))");
        Pattern pt = Pattern.compile("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29))$");
        return pt.matcher(birth).matches();
    }

    /**
     * 判断输入是否是数字或者字母
     *
     * @param str
     * @return
     */
    public static boolean isNumOrLetter(String str) {
        String strPattern = "^[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str.trim());
        return m.matches();
    }

    /**
     * 匹配身份证
     *
     * @param idCard
     * @return true 如果匹配，false 不匹配
     */
    public static boolean isIDCard(String idCard) {
        String pattern = "^\\d{10}|\\d{13}|\\d{15}|\\d{17}(\\d|x|X)$";
        return idCard.matches(pattern);
    }

    /**
     * 判断字符长度
     *
     * @param str
     * @param maxLen
     * @return
     */
    public static boolean isLength(String str, int maxLen) {
        char[] cs = str.toCharArray();
        int count = 0;
        int last = cs.length;
        for (int i = 0; i < last; i++) {
            if (cs[i] > 255)
                count += 2;
            else
                count++;
        }
        return count >= maxLen;
    }

    /**
     * 得到格式化时间
     *
     * @param timeInSeconds
     * @return
     */
    public static String getFormatTimeMsg(int timeInSeconds) {
        int hours, minutes, seconds;
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;

        String minStr = String.valueOf(minutes);
        String secStr = String.valueOf(seconds);

        if (minStr.length() == 1)
            minStr = "0" + minStr;
        if (secStr.length() == 1)
            secStr = "0" + secStr;

        return (minStr + "分" + secStr + "秒");
    }

    /**
     * 剪切字符串
     *
     * @param str
     * @param len
     * @return
     */
    public static String cutString(String str, int len) {
        if (!StringUtil.isNull(str)) {
            if (str.length() >= len)
                str = str.substring(0, len) + "...";
        }
        return str;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value
     * @return
     */
    public static int getLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 将时间戳转成日期字符
     *
     * @param StringTime
     * @return
     */
    public static String getStringTime2Date(String StringTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.parseLong(StringTime)));
    }

    /**
     * 将时间戳转成日期字符
     *
     * @param longtime
     * @return
     */
    public static String getLongTime2Date(long longtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(longtime));
    }

    /**
     * 将日期字符转成时间戳
     *
     * @param time
     * @return
     */
    public static long getDate2LongTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sdf.parse(time);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 将时间戳转成日期字符
     *
     * @param longtime
     * @return
     */
    public static String getLongTime3Date(long longtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return getChineseDate(sdf.format(new Date(longtime)));
    }

    /**
     * 将日期字符转成时间戳
     *
     * @param time
     * @return
     */
    public static String getDate2ChineseTime(String time) {
        String[] timeSplit = time.split("-");
        String date = timeSplit[1] + "月" + timeSplit[2] + "日";
        return date;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }

    /**
     * 获取现在时间
     *
     * @return 返回长时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateLongLen() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }

    /**
     * 转换时间
     *
     * @return 返回长时间字符串格式yyyy-MM-dd HH:mm
     */
    public static String getDateToString(Date mDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(mDate);
    }

    /**
     * 转换时间
     *
     * @return 返回长时间字符串格式yyyy年MM月dd日
     */
    public static String getDateToChToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        return formatter.format(date);
    }

    /**
     * 根据time获取现在时间
     *
     * @return
     */
    public static String getStringDateforPhoto(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return formatter.format(currentTime);
    }

    /**
     * 获取现在时间
     *
     * @return
     */
    public static String getStringDateforPhoto() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return formatter.format(currentTime);
    }

    /**
     * 获取前天时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getBeforeYesterday() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 2);

        String beforeYesterday = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return beforeYesterday;
    }

    /**
     * 获取昨天时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getYesterday() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String yesterDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return yesterDay;
    }

    /**
     * 获取明天时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getTomorrow() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /**
     * 获取后天时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getDayAfterTomorrow() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 2);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateLong() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(currentTime);
    }

    /**
     * 根据年和月算出该月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthOfDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, 1);
        long time = c.getTimeInMillis();

        c.set(Calendar.MONTH, (month + 1));
        long nexttime = c.getTimeInMillis();

        long cha = nexttime - time;
        int s = (int) (cha / (24 * 60 * 60 * 1000));

        return s;
    }

    /**
     * 返回指定的时间格式
     *
     * @param date
     * @return
     */
    public static String getChineseDate(String date) {
        String[] split_str = date.split("-");
        String date_str = split_str[0] + "年" + split_str[1] + "月" + split_str[2] + "日";
        return date_str;
    }

    /**
     * 返回指定的日期
     */
    public static String getSpecificDate(String date, int flag) {
        int sperator;
        if (isEmpty(date)) {
            return "";
        } else {
            if (0 == flag) {// 年
                String year = date.substring(0, 4);
                // System.out.println("年是:" + year);
                return year;
            } else if (1 == flag) {// 月
                sperator = date.indexOf("-");
                String month = date.substring(sperator + 1, sperator + 3);
                // System.out.println("月是:" + month);
                return month;
            } else if (2 == flag) {// 日
                sperator = date.lastIndexOf("-");
                String day = date.substring(sperator + 1, sperator + 3);
                // System.out.println("日是:" + day);
                return day;
            } else if (3 == flag) {// 时
                sperator = date.indexOf("T");
                String hour = date.substring(sperator + 1, sperator + 3);
                // System.out.println("时是:" + hour);
                return hour;
            } else if (4 == flag) {// 分
                sperator = date.indexOf(":");
                String minute = date.substring(sperator + 1, sperator + 3);
                // System.out.println("分是:" + minute);
                return minute;
            } else if (5 == flag) {// 秒
                sperator = date.lastIndexOf(":");
                String second = date.substring(sperator + 1, sperator + 3);
                // System.out.println("秒是:" + second);
                return second;
            } else {
                return date.replace('T', ' ');
            }
        }
    }

    /**
     * 时间处理处理, 当时间小于10时,在前面机上0
     *
     * @param time 具体数字
     * @return 返回结果
     */
    public static String parseTime(String time) {
        String result;
        int parameter = Integer.valueOf(time);
        if (parameter >= 10) {
            result = String.valueOf(parameter);
        } else {
            result = "0" + parameter;
        }
        return result;
    }

    /**
     * Unicode 转 GBK
     *
     * @param s
     * @return
     */
    public static String UnicodeToGBK2(String s) {
        String[] k = s.split(";");
        String rs = "";
        for (int i = 0; i < k.length; i++) {
            int strIndex = k[i].indexOf("&#");
            String newstr = k[i];
            if (strIndex > -1) {
                String kstr = "";
                if (strIndex > 0) {
                    kstr = newstr.substring(0, strIndex);
                    rs += kstr;
                    newstr = newstr.substring(strIndex);
                }
                int m = Integer.parseInt(newstr.replace("&#", ""));
                char c = (char) m;
                rs += c;
            } else {
                rs += k[i];
            }
        }
        return rs;
    }

    public static String GetYearForDate(Date date) {
        String tmp = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int yearInt = calendar.get(Calendar.YEAR);
        tmp = String.valueOf(yearInt);
        return tmp;
    }

    public static String GetMonthForDate(Date date) {
        String tmp = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthInt = calendar.get(Calendar.MONTH) + 1;
        tmp = String.valueOf(monthInt);
        return tmp;
    }

    public static String GetDayForDay(Date date) {
        String tmp = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        tmp = String.valueOf(day);
        if (tmp.length() == 1) {
            tmp = "0" + tmp;
        }
        return tmp;
    }

    /**
     * 得到两个日期间隔的天数
     */
    public static int getDaysBetween(String beginDate, String endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date bDate = format.parse(beginDate);
        Date eDate = format.parse(endDate);
        Calendar d1 = new GregorianCalendar();
        d1.setTime(bDate);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(eDate);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);

        }
        return days;
    }

    /**
     * 比较两个时间
     *
     * @param year1
     * @param month1
     * @param day1
     * @param year2
     * @param month2
     * @param day2
     * @return
     */
    public static int complateDay(int year1, int month1, int day1, int year2, int month2, int day2) {
        Calendar xmas = new GregorianCalendar(year1, month1, day1);
        Calendar newYear = new GregorianCalendar(year2, month2, day2);
        // 两个日期相差的毫秒数
        long timeDiffMillis = newYear.getTimeInMillis() - xmas.getTimeInMillis();
        // 两个日期相差的天
        long diffDays = timeDiffMillis / (1000 * 60 * 60 * 24);

        if (diffDays > 0) {
            if (diffDays > 365) {
                return 1;
            }
        } else {
            return -1;
        }
        return (int) diffDays;
    }

    public static int getIntervalDays(Date fDate, Date oDate) {
        if (null == fDate || null == oDate) {
            return -1;
        }
        long intervalMilli = oDate.getTime() - fDate.getTime();
        return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }


    public static int countNum(String str) {
        int total = 0;
        char[] t1 = null;
        t1 = str.toCharArray();
        int t0 = t1.length;
        for (int i = 0; i < t0; i++) {
            if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                total += 2;
            } else {
                total++;
            }
        }
        return total;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
