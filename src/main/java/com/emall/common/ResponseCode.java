package com.emall.common;

/**
 * Created by Administrator on 2017-11-29.
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),

    ERROR(1, "ERROR"),

    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),

    NEED_LOGIN(20, "NEED_LOGIN");

    //信息码及描述
    private final int code;
    private final String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
