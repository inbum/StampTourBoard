package com.thatzit.oib.stamptourboard.model;

import java.io.Serializable;

/**
 * Created by inbum on 2017-04-19.
 */

public class PostedByModel implements Serializable {
    String Nick;
    String applicationId;

    public String getNick() {
        return Nick;
    }

    public String getApplicationId() {
        return applicationId;
    }
}
