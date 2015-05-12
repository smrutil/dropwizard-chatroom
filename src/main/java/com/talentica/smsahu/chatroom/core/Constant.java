package com.talentica.smsahu.chatroom.core;

public enum Constant {

    DEFAULT_CHAT_ROOM_NAME("default");

    private final String value;

    Constant(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Constant fromValue(String v) {
        for (Constant c: Constant.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }

}
