package com.ljstudio.android.loveday.utils;


public class SystemOutUtil {

//	private final static boolean isRelease = true;
	private final static boolean isRelease = false;
	private final static String APPLICATION_TAG = "HESVIT_WORK---->";
	
	
	public static void sysOut(Object object) {
		if (isRelease) {
			// 软件发布
		} else {
			System.out.println(APPLICATION_TAG + object.toString());
		}
	}
	
}
