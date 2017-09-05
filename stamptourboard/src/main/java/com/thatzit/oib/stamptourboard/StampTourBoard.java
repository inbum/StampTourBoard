package com.thatzit.oib.stamptourboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.thatzit.oib.stamptourboard.helper.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inbum on 2017-08-31.
 */

public abstract class StampTourBoard {

    private StampTourBoardConfig config;

    public abstract void start(int requestCode);

    public static class StampTourBoardWithActivity extends StampTourBoard {

        private Activity activity;

        public StampTourBoardWithActivity(Activity activity) {
            this.activity = activity;
            init(activity);
        }

        @Override
        public void start(int requestCode) {
            Intent intent = getIntent(activity);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /* --------------------------------------------------- */
    /* > Stater */
    /* --------------------------------------------------- */

    public void init(Context context) {
        config = StampTourBoardConfigFactory.createDefault(context);
    }

    public static StampTourBoardWithActivity create(Activity activity) {
        return new StampTourBoardWithActivity(activity);
    }


    /* --------------------------------------------------- */
    /* > Builder */
    /* --------------------------------------------------- */

    public StampTourBoard applicationRegion(String region) {
        config.setApplicationRegion(region);
        return this;
    }

    public StampTourBoard applicationID(String appId) {
        config.setApplicationID(appId);
        return this;
    }

    public StampTourBoard userNick(String userNick) {
        config.setUserNick(userNick);
        return this;
    }

    public StampTourBoard userAccesstoken(String userAccesstoken) {
        config.setUserAccesstoken(userAccesstoken);
        return this;
    }

    public StampTourBoard selectTownNames(List<String> townNames) {
        config.setSelectTownNames(townNames);
        return this;
    }

    public StampTourBoard hintSelectTownName(String hintSelectTownName) {
        config.setHintSelectTwonName(hintSelectTownName);
        return this;
    }

    public StampTourBoard sharePacakgeName(String sharePacakgeName) {
        config.setSharePacakgeName(sharePacakgeName);
        return this;
    }

    protected StampTourBoardConfig getConfig() {
        return config;
    }

    public Intent getIntent(Context context) {
        StampTourBoardConfig config = ConfigUtils.checkConfig(getConfig());
        Intent intent = new Intent(context, StampTourBoardActivity.class);
        intent.putExtra(StampTourBoardConfig.class.getSimpleName(), config);
        return intent;
    }

    /* --------------------------------------------------- */
    /* > Helper */
    /* --------------------------------------------------- */

}
