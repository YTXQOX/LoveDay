package com.ljstudio.android.loveday.views.excel.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类成员里对应Excel里的标题
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface ExcelContent {
	public String titleName();

}