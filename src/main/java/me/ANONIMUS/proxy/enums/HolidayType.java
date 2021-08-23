package me.ANONIMUS.proxy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HolidayType {
    NEW_YEAR(1, 1, "Happy New Year"),
    XMAS(24, 12, "Merry Christmas");

    private final int day;
    private final int month;
    private final String wishes;
}