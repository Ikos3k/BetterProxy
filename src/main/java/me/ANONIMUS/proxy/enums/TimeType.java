package me.ANONIMUS.proxy.enums;

public enum TimeType {
    DAY(0, 6000),
    NIGHT(0, 18000),
    DEFAULT(0, 0);

    private final int age;
    private final int time;

    TimeType(int age, int time) {
        this.age = age;
        this.time = time;
    }

    public int getAge() {
        return age;
    }

    public int getTime() {
        return time;
    }
}