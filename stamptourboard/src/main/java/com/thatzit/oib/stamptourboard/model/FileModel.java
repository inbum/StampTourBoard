package com.thatzit.oib.stamptourboard.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by inbum on 2017-04-19.
 */

public class FileModel implements Serializable {
    String name;
    long size;
    String container;
    String _id;
    Date Uploaded;

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getContainer() {
        return container;
    }

    public String get_id() {
        return _id;
    }

    public Date getUploaded() {
        return Uploaded;
    }
}
