package com.thatzit.oib.stamptourboard.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by inbum on 2017-04-21.
 */

public class PostsRes implements Serializable {
    int count;
    ArrayList<PostData> posts = new ArrayList<>();

    public class PostData implements Serializable{
        String _id;
        String title;
        String townName;
        String contents;
        String ApplicationId;
        int __v;
        Date Updated;
        Date Created;
        ArrayList<FileModel> files;
        PostedByModel postedBy;
        int views;

        public String get_id() {
            return _id;
        }

        public String getTitle() {
            return title;
        }

        public String getTownName() {
            return townName;
        }

        public String getContents() {
            return contents;
        }

        public String getApplicationId() {
            return ApplicationId;
        }

        public int get__v() {
            return __v;
        }

        public Date getUpdated() {
            return Updated;
        }

        public Date getCreated() {
            return Created;
        }

        public String getCreatedTimeStr(){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.M.d HH:mm");
            return simpleDateFormat.format(new Date(getCreated().getTime()));
        }


        public ArrayList<FileModel> getFiles() {
            return files;
        }

        public PostedByModel getPostedBy() {
            return postedBy;
        }

        public int getViews() {
            return views;
        }
    }

    public int getCount() {
        return count;
    }

    public ArrayList<PostData> getPosts() {
        return posts;
    }
}
