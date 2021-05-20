package me.ANONIMUS.proxy.enums;

public enum HolidayType {
    NEW_YEAR(1, 1, "Happy New Year"),
    XMAS(12, 24, "Merry Christmas");

    private final int month;
    private final int day;
    private final String wishes;

    HolidayType(int month, int day, String wishes) {
        this.month = month;
        this.day = day;
        this.wishes = wishes;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getWishes() {
        return wishes;
    }
}