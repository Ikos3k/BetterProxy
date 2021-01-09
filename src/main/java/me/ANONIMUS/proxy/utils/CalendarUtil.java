package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.enums.HolidayType;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
    public static HolidayType getHoliday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) == 24) {
            return HolidayType.XMAS;
        }
        if (calendar.get(Calendar.MONTH) + 1 == 1 && calendar.get(Calendar.DATE) == 1) {
            return HolidayType.NEW_YEAR;
        }
        return null;
    }

    public static boolean isHoliday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) == 24 || calendar.get(Calendar.MONTH) + 1 == 1 && calendar.get(Calendar.DATE) == 1;
    }
}