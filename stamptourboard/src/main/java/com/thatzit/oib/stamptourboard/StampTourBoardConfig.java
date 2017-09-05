package com.thatzit.oib.stamptourboard;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inbum on 2017-08-31.
 */

public class StampTourBoardConfig implements Parcelable {

    private String applicationRegion;
    private String applicationID;

    private String userNick;
    private String userAccesstoken;

    //
    private List<String> selectTownNames;
    private String hintSelectTwonName;

    private String sharePacakgeName;

    public StampTourBoardConfig() {
    }

    public String getApplicationRegion() {
        return applicationRegion;
    }

    public void setApplicationRegion(String applicationRegion) {
        this.applicationRegion = applicationRegion;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserAccesstoken() {
        return userAccesstoken;
    }

    public void setUserAccesstoken(String userAccesstoken) {
        this.userAccesstoken = userAccesstoken;
    }

    public List<String> getSelectTownNames() {
        return selectTownNames;
    }

    public void setSelectTownNames(List<String> selectTownNames) {
        this.selectTownNames = selectTownNames;
    }

    public String getHintSelectTwonName() {
        return hintSelectTwonName;
    }

    public void setHintSelectTwonName(String hintSelectTwonName) {
        this.hintSelectTwonName = hintSelectTwonName;
    }

    public String getSharePacakgeName() {
        return sharePacakgeName;
    }

    public void setSharePacakgeName(String sharePacakgeName) {
        this.sharePacakgeName = sharePacakgeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.applicationRegion);
        dest.writeString(this.applicationID);
        dest.writeString(this.userNick);
        dest.writeString(this.userAccesstoken);
        dest.writeStringList(this.selectTownNames);
        dest.writeString(this.hintSelectTwonName);
        dest.writeString(this.sharePacakgeName);
    }

    protected StampTourBoardConfig(Parcel in) {

        this.applicationRegion = in.readString();
        this.applicationID = in.readString();
        this.userNick = in.readString();
        this.userAccesstoken = in.readString();
        this.selectTownNames = new ArrayList<String>();
        in.readStringList(selectTownNames);
//        this.selectTownNames = new ArrayList<String>();
//        in.readList(selectTownNames, List.class.getClassLoader());
        this.hintSelectTwonName = in.readString();
        this.sharePacakgeName = in.readString();
    }

    public static final Creator<StampTourBoardConfig> CREATOR = new Creator<StampTourBoardConfig>() {
        @Override
        public StampTourBoardConfig createFromParcel(Parcel source) {
            return new StampTourBoardConfig(source);
        }

        @Override
        public StampTourBoardConfig[] newArray(int size) {
            return new StampTourBoardConfig[size];
        }
    };
}
