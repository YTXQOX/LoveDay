package com.ljstudio.android.loveday.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtil {

	public static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

	public static final SimpleDateFormat sdfDateTime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	public static final SimpleDateFormat sdfDateTime20 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
	public static final SimpleDateFormat sdfDateTime21 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
	public static final SimpleDateFormat sdfDateTime3 = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault());
	
	public static final SimpleDateFormat sdfDate1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	public static final SimpleDateFormat sdfDate10 = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
	public static final SimpleDateFormat sdfDate2 = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
	public static final SimpleDateFormat sdfDate3 = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
	public static final SimpleDateFormat sdfDate4 = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
	public static final SimpleDateFormat sdfDate5 = new SimpleDateFormat("MM-dd", Locale.getDefault());

	public static final SimpleDateFormat sdfTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	public static final SimpleDateFormat sdfTime2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
	public static final SimpleDateFormat sdfTime3 = new SimpleDateFormat("HH", Locale.getDefault());

	public static final SimpleDateFormat sdfWeek = new SimpleDateFormat("EEEE", Locale.getDefault());


	public static String getDate(Date date, SimpleDateFormat sdf) {
		String strCurrentDate = sdf.format(date);
		return strCurrentDate;
	}

	public static String getDate(String strDate, SimpleDateFormat sdf1, SimpleDateFormat sdf2) {
		Date date = convertStr2Date(strDate, sdf1);
		String strFormatDate = sdf2.format(date);
		return strFormatDate;
	}

	public static String getCurrentDateTime(SimpleDateFormat sdf) {
		String strCurrentDate;
		strCurrentDate = sdf.format(new Date());
		return strCurrentDate;
	}

	public static String getCurrentDate(SimpleDateFormat sdf) {
		String strCurrentDate;
		strCurrentDate = sdf.format(new Date());
		return strCurrentDate;
	}

	public static String getCurrentWeek(SimpleDateFormat sdf) {
		String strCurrentDate;
		strCurrentDate = sdf.format(new Date());
		return strCurrentDate;
	}

	public static String getSubDate(SimpleDateFormat sdf, int sub) {
		String strCurrentDate;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, sub);
		Date date = calendar.getTime();
		strCurrentDate = sdf.format(date);

		return strCurrentDate;
	}

	public static String getCurrentTime(SimpleDateFormat sdf) {
		String strCurrentTime;
		strCurrentTime = sdf.format(new Date());
		return strCurrentTime;
	}

	public static Date convertStr2Date(String strDate, SimpleDateFormat sdf) {
		Date date = null;
		try {
			if(TextUtils.isEmpty(strDate)) {
				date = new Date();
			} else {
				date = sdf.parse(strDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String convertTimestamp2Str(long timestamp, SimpleDateFormat sdf) {
		return sdf.format(new Date(timestamp));
	}

}
