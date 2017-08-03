package com.ljstudio.android.loveday.views.excel.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类上面对应Excel里的表名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
public @interface ExcelSheet {
	public String sheetName();
}