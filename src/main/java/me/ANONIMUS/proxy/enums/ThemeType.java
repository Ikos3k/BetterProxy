package me.ANONIMUS.proxy.enums;

public enum ThemeType {
    TEST('4', 'c', '7'),
    TEST2('2', 'a', '8'),
    DEFAULT('6', 'e', '4');

    private final char[] colors;

    ThemeType(char... colors) {
        this.colors = colors;
    }

    public String getColor(int i) {
        return "&" + colors[i - 1];
    }
}