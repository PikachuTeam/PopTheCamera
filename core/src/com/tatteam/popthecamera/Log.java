package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;

/**
 * Created by dongc_000 on 9/27/2015.
 */
public class Log {

    public static boolean enableLog = true;

    public static void writeLog(String log) {
        if(enableLog) {
            Gdx.app.log(Constants.APP_TITLE, log);
        }
    }

    public static void writeLog(String title, String log) {
        if(enableLog) {
            Gdx.app.log(Constants.APP_TITLE, title + ": " + log);
        }
    }
}
