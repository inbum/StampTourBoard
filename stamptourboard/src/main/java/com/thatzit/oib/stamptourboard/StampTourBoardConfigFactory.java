package com.thatzit.oib.stamptourboard;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by inbum on 2017-08-31.
 */

public class StampTourBoardConfigFactory {

    public static StampTourBoardConfig createDefault(Context context) {
        StampTourBoardConfig config = new StampTourBoardConfig();
        config.setApplicationRegion("thatzit");
        config.setApplicationID("stamptour");
        config.setSelectTownNames(new ArrayList<String>());
        config.setHintSelectTwonName("place");
        return config;
    }

}
