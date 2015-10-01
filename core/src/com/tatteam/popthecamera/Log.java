package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;

/**
 * Created by dongc_000 on 9/27/2015.
 */
public class Log {

    private final static boolean LOG_ENABLE = false;

    public static void writeLog(String log) {
        if(LOG_ENABLE) {
            Gdx.app.log(Constants.APP_TITLE, log);
        }
    }

    public static void writeLog(String title, String log) {
        if(LOG_ENABLE) {
            Gdx.app.log(Constants.APP_TITLE, title + ": " + log);
        }
    }
}
