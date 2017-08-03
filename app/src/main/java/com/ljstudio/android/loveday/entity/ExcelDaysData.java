package com.ljstudio.android.loveday.entity;

import com.ljstudio.android.loveday.views.excel.annotations.ExcelContent;
import com.ljstudio.android.loveday.views.excel.annotations.ExcelContentCellFormat;
import com.ljstudio.android.loveday.views.excel.annotations.ExcelSheet;
import com.ljstudio.android.loveday.views.excel.annotations.ExcelTitleCellFormat;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

/**
 * LoveDay，作为备份时的导出Excel的中间格式化实体，所有字段都为 String
 */
@ExcelSheet(sheetName = "LoveDay")
public class ExcelDaysData {

	@ExcelContent(titleName = "标题")
	private String Title;

	@ExcelContent(titleName = "日期")
	private String Date;

	@ExcelContent(titleName = "天数")
	private String Days;

	@ExcelContent(titleName = "单位")
	private String Unit;

	@ExcelContent(titleName = "是否置顶")
	private String IsTop;

	@ExcelTitleCellFormat(titleName = "标题")
	private static WritableCellFormat getTitleFormat() {
		WritableCellFormat format = new WritableCellFormat();
		try {
			// 单元格格式
			// 背景颜色
			 format.setBackground(Colour.PINK);
			// 边框线
			format.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.RED);
			// 设置文字居中对齐方式;
			format.setAlignment(Alignment.CENTRE);
			// 设置垂直居中;
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 设置自动换行
			format.setWrap(false);

			// 字体格式
			WritableFont font = new WritableFont(WritableFont.ARIAL);
			// 字体颜色
			font.setColour(Colour.BLUE2);
			// 字体加粗
			font.setBoldStyle(WritableFont.BOLD);
			// 字体加下划线
			font.setUnderlineStyle(UnderlineStyle.SINGLE);
			// 字体大小
			font.setPointSize(20);
			format.setFont(font);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	@ExcelTitleCellFormat(titleName = "日期")
	private static WritableCellFormat getTitleFormatF2() {
		WritableCellFormat format = new WritableCellFormat();
		try {
			// 单元格格式
			// 背景颜色
			format.setBackground(Colour.PINK);
			// 边框线
			format.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.RED);
			// 设置文字居中对齐方式;
			format.setAlignment(Alignment.CENTRE);
			// 设置垂直居中;
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 设置自动换行
			format.setWrap(false);

			// 字体格式
			WritableFont font = new WritableFont(WritableFont.ARIAL);
			// 字体颜色
			font.setColour(Colour.BLUE2);
			// 字体加粗
			font.setBoldStyle(WritableFont.BOLD);
			// 字体加下划线
			font.setUnderlineStyle(UnderlineStyle.SINGLE);
			// 字体大小
			font.setPointSize(20);
			format.setFont(font);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	@ExcelTitleCellFormat(titleName = "天数")
	private static WritableCellFormat getTitleFormatF3() {
		WritableCellFormat format = new WritableCellFormat();
		try {
			// 单元格格式
			// 背景颜色
			format.setBackground(Colour.PINK);
			// 边框线
			format.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.RED);
			// 设置文字居中对齐方式;
			format.setAlignment(Alignment.CENTRE);
			// 设置垂直居中;
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 设置自动换行
			format.setWrap(false);

			// 字体格式
			WritableFont font = new WritableFont(WritableFont.ARIAL);
			// 字体颜色
			font.setColour(Colour.BLUE2);
			// 字体加粗
			font.setBoldStyle(WritableFont.BOLD);
			// 字体加下划线
			font.setUnderlineStyle(UnderlineStyle.SINGLE);
			// 字体大小
			font.setPointSize(20);
			format.setFont(font);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	@ExcelTitleCellFormat(titleName = "单位")
	private static WritableCellFormat getTitleFormatF4() {
		WritableCellFormat format = new WritableCellFormat();
		try {
			// 单元格格式
			// 背景颜色
			format.setBackground(Colour.PINK);
			// 边框线
			format.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.RED);
			// 设置文字居中对齐方式;
			format.setAlignment(Alignment.CENTRE);
			// 设置垂直居中;
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 设置自动换行
			format.setWrap(false);

			// 字体格式
			WritableFont font = new WritableFont(WritableFont.ARIAL);
			// 字体颜色
			font.setColour(Colour.BLUE2);
			// 字体加粗
			font.setBoldStyle(WritableFont.BOLD);
			// 字体加下划线
			font.setUnderlineStyle(UnderlineStyle.SINGLE);
			// 字体大小
			font.setPointSize(20);
			format.setFont(font);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	@ExcelTitleCellFormat(titleName = "是否置顶")
	private static WritableCellFormat getTitleFormatF5() {
		WritableCellFormat format = new WritableCellFormat();
		try {
			// 单元格格式
			// 背景颜色
			format.setBackground(Colour.PINK);
			// 边框线
			format.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.RED);
			// 设置文字居中对齐方式;
			format.setAlignment(Alignment.CENTRE);
			// 设置垂直居中;
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 设置自动换行
			format.setWrap(false);

			// 字体格式
			WritableFont font = new WritableFont(WritableFont.ARIAL);
			// 字体颜色
			font.setColour(Colour.BLUE2);
			// 字体加粗
			font.setBoldStyle(WritableFont.BOLD);
			// 字体加下划线
			font.setUnderlineStyle(UnderlineStyle.SINGLE);
			// 字体大小
			font.setPointSize(20);
			format.setFont(font);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	private static int f1flag = 0;
	private static int f2flag = 0;
	private static int f3flag = 0;
	private static int f4flag = 0;
	private static int f5flag = 0;

	@ExcelContentCellFormat(titleName = "标题")
	private WritableCellFormat f1() {
		WritableCellFormat format = null;
		try {
			format = new WritableCellFormat();
			if ((f1flag & 1) != 0) {
				format.setBackground(Colour.GRAY_25);
			}

			if (Title.contains("4")) {
				format.setBackground(Colour.RED);
			}

			f1flag++;
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	@ExcelContentCellFormat(titleName = "日期")
	private WritableCellFormat f2() {
		WritableCellFormat format = null;
		try {
			format = new WritableCellFormat();
			if ((f2flag & 1) != 0) {
				format.setBackground(Colour.GRAY_25);
			}
			f2flag++;
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	@ExcelContentCellFormat(titleName = "天数")
	private WritableCellFormat f3() {
		WritableCellFormat format = null;
		try {
			format = new WritableCellFormat();
			if ((f3flag & 1) != 0) {
				format.setBackground(Colour.GRAY_25);
			}
			f3flag++;
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	@ExcelContentCellFormat(titleName = "单位")
	private WritableCellFormat f4() {
		WritableCellFormat format = null;
		try {
			format = new WritableCellFormat();
			if ((f4flag & 1) != 0) {
				format.setBackground(Colour.GRAY_25);
			}
			f4flag++;
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	@ExcelContentCellFormat(titleName = "是否置顶")
	private WritableCellFormat f5() {
		WritableCellFormat format = null;
		try {
			format = new WritableCellFormat();
			if ((f5flag & 1) != 0) {
				format.setBackground(Colour.GRAY_25);
			}
			f5flag++;
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return format;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getDays() {
		return Days;
	}

	public void setDays(String days) {
		Days = days;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public String getIsTop() {
		return IsTop;
	}

	public void setIsTop(String isTop) {
		IsTop = isTop;
	}
}
