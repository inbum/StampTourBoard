package com.thatzit.oib.stamptourboard.helper;

import com.thatzit.oib.stamptourboard.StampTourBoardConfig;

/**
 * Created by inbum on 2017-08-31.
 */

public class ConfigUtils {

    public static StampTourBoardConfig checkConfig(StampTourBoardConfig config) {
        if (config == null) {
            throw new IllegalStateException("StampTourBoardConfig cannot be null");
        }
        if (config.getUserNick() == null || config.getUserAccesstoken() == null ) {
            throw new IllegalStateException("You must set nick and accesstoken.");
        }
        return config;
    }
}