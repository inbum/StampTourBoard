package com.thatzit.oib.stamptourboard.helper;

/**
 * Created by inbum on 2017-01-25.
 */

public enum ExtraName {
    POST_DATA("POST_DATA"),
    TOWN_NAME("TOWN_NAME");

    private String name;

    ExtraName() {
    }

    ExtraName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
