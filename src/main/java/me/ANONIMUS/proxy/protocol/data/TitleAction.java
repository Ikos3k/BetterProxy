package me.ANONIMUS.proxy.protocol.data;

import lombok.Getter;

@Getter
public enum TitleAction {
    TITLE, SUBTITLE, TIMES, HIDE, ACTIONBAR, RESET;

    public int getIdByProtocol(int protocol) {
        switch (this) {
            case TITLE:
                return 0;
            case SUBTITLE:
                return 1;
            case TIMES:
                if (protocol >= 315) {
                    return 3;
                }
                return 2;
            case HIDE:
                if (protocol >= 315) {
                    return 4;
                }
                return 3;
            case RESET:
                if (protocol >= 315) {
                    return 5;
                }
                return 4;
            case ACTIONBAR:
                return 2;
            default:
                return -1;
        }
    }

    public static TitleAction getById(int id, int protocol) {
        if (protocol >= 315) {
            switch (id) {
                case 0:
                    return TITLE;
                case 1:
                    return SUBTITLE;
                case 2:
                    return ACTIONBAR;
                case 3:
                    return TIMES;
                case 4:
                    return HIDE;
                case 5:
                    return RESET;
            }
        } else {
            switch (id) {
                case 0:
                    return TITLE;
                case 1:
                    return SUBTITLE;
                case 2:
                    return TIMES;
                case 3:
                    return HIDE;
                case 4:
                    return RESET;
            }
        }
        return RESET;
    }
}
