package com.thatzit.oib.stamptourboard.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by inbum on 2017-06-05.
 */

public class CommentsModel implements Serializable {
    String _id;
    Date Updated;
    Date Created;
    String contents;
    PostedByModel postedBy;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getUpdated() {
        return Updated;
    }

    public void setUpdated(Date updated) {
        Updated = updated;
    }

    public Date getCreated() {
        return Created;
    }

    public void setCreated(Date created) {
        Created = created;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public PostedByModel getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(PostedByModel postedBy) {
        this.postedBy = postedBy;
    }

    public String getCreatedTimeStr(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.M.d HH:mm:ss");
        return simpleDateFormat.format(new Date(getCreated().getTime() ));
    }
}
