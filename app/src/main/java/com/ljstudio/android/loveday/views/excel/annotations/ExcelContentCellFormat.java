package com.ljstudio.android.loveday.views.excel.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应内容的单元格的格式
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface ExcelContentCellFormat {
    public String titleName();
}