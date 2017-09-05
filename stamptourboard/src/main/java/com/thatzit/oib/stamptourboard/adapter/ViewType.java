package com.thatzit.oib.stamptourboard.adapter;

/**
 * Created by inbum on 2017-01-25.
 */

public enum ViewType {
    POSTBOARD_COMMENT_NONE(2000),
    POSTBOARD_COMMENT_NORMAL(2001)
    ;

    private int code;

    ViewType() {
    }

    ViewType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
