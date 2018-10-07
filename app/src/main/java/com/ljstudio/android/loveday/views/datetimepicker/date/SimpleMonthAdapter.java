package com.ljstudio.android.loveday.views.datetimepicker.date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.Calendar;
import java.util.HashMap;

public class SimpleMonthAdapter extends BaseAdapter implements com.ljstudio.android.loveday.views.datetimepicker.date.SimpleMonthView.OnDayClickListener {

    protected static int WEEK_7_OVERHANG_HEIGHT = 7;
    protected static final int MONTHS_IN_YEAR = 12;

	private final Context mContext;
	private final DatePickerController mController;

	private CalendarDay mSelectedDay;

	public SimpleMonthAdapter(Context context, DatePickerController datePickerController) {
		mContext = context;
		mController = datePickerController;
		init();
		setSelectedDay(mController.getSelectedDay());
	}

	private boolean isSelectedDayInMonth(int year, int month) {
		return (mSelectedDay.year == year) && (mSelectedDay.month == month);
	}

	public int getCount() {
        return ((mController.getMaxYear() - mController.getMinYear()) + 1) * MONTHS_IN_YEAR;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		com.ljstudio.android.loveday.views.datetimepicker.date.SimpleMonthView v;
        HashMap<String, Integer> drawingParams = null;
		if (convertView != null) {
			v = (com.ljstudio.android.loveday.views.datetimepicker.date.SimpleMonthView) convertView;
            drawingParams = (HashMap<String, Integer>) v.getTag();
        } else {
			v = new com.ljstudio.android.loveday.views.datetimepicker.date.SimpleMonthView(mContext);
			v.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			v.setClickable(true);
			v.setOnDayClickListener(this);
		}
        if (drawingParams == null) {
            drawingParams = new HashMap<String, Integer>();
        }
        drawingParams.clear();

        final int month = position % MONTHS_IN_YEAR;
        final int year = position / MONTHS_IN_YEAR + mController.getMinYear();

        int selectedDay = -1;
        if (isSelectedDayInMonth(year, month)) {
            selectedDay = mSelectedDay.day;
        }

		v.reuse();

        drawingParams.put(com.ljstudio.android.loveday.views.datetimepicker.date.SimpleMonthView.VIEW_PARAMS_SELECTED_DAY, selectedDay);
        drawingParams.put(com.ljstudio.android.loveday.views.datetimepicker.date.SimpleMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(com.ljstudio.android.loveday.views.datetimepicker.date.SimpleMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(com.ljstudio.android.loveday.views.datetimepicker.date.SimpleMonthView.VIEW_PARAMS_WEEK_START, mController.getFirstDayOfWeek());
		v.setMonthParams(drawingParams);
		v.invalidate();

		return v;
	}

	protected void init() {
		mSelectedDay = new CalendarDay(System.currentTimeMillis());
	}

	public void onDayClick(SimpleMonthView simpleMonthView, CalendarDay calendarDay) {
		if (calendarDay != null) {
			onDayTapped(calendarDay);
        }
	}

	protected void onDayTapped(CalendarDay calendarDay) {
		mController.tryVibrate();
		mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day);
		setSelectedDay(calendarDay);
	}

	public void setSelectedDay(CalendarDay calendarDay) {
		mSelectedDay = calendarDay;
		notifyDataSetChanged();
	}

	public static class CalendarDay {
		private Calendar calendar;

		int day;
		int month;
		int year;

		public CalendarDay() {
			setTime(System.currentTimeMillis());
		}

		public CalendarDay(int year, int month, int day) {
			setDay(year, month, day);
		}

		public CalendarDay(long timeInMillis) {
			setTime(timeInMillis);
		}

		public CalendarDay(Calendar calendar) {
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
		}

		private void setTime(long timeInMillis) {
			if (calendar == null) {
				calendar = Calendar.getInstance();
            }
			calendar.setTimeInMillis(timeInMillis);
			month = this.calendar.get(Calendar.MONTH);
			year = this.calendar.get(Calendar.YEAR);
			day = this.calendar.get(Calendar.DAY_OF_MONTH);
		}

		public void set(CalendarDay calendarDay) {
		    year = calendarDay.year;
			month = calendarDay.month;
			day = calendarDay.day;
		}

		public void setDay(int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;
		}
	}
}