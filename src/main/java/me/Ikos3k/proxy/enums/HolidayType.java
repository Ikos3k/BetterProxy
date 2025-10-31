package me.Ikos3k.proxy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HolidayType {
    NEW_YEAR(1, 1, "Happy New Year"),
    XMAS(24, 12, "Merry Christmas"),
    HALLOWEEN(31, 10, "Happy Halloween"),
    VALENTINES(14, 2, "Happy Valentine's Day");

    private final int day;
    private final int month;
    private final String wishes;
}