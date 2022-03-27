package me.Ikos3k.proxy.utils;

import me.Ikos3k.proxy.enums.HolidayType;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
    public static HolidayType getHoliday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (HolidayType holidayType : HolidayType.values()) {
            if (calendar.get(Calendar.MONTH) + 1 == holidayType.getMonth() && calendar.get(Calendar.DATE) == holidayType.getDay()) {
                return holidayType;
            }
        }
        return null;
    }

    public static boolean isHoliday() {
        return getHoliday() != null;
    }
}