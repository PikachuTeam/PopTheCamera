package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by dongc_000 on 9/25/2015.
 */
public class BaseLog {

    public static boolean enableLog = true;

    public static void checkBeta(double beta) {
        if (enableLog) {
            Gdx.app.log(Constants.APP_TITLE, "Beta: " + beta);
        }
    }

    public static void checkRotation(float rotation) {
        if (enableLog) {
            Gdx.app.log(Constants.APP_TITLE, "Rotation: " + rotation);
        }
    }

    public static void checkRotation(String type, double rotation) {
        if (enableLog) {
            Gdx.app.log(Constants.APP_TITLE, type + ": " + rotation);
        }
    }

    public static void tingTing() {
        if (enableLog) {
            Gdx.app.log(Constants.APP_TITLE, "Ting ting.");
        }
    }

    public static void printString(String msg) {
        if (enableLog) {
            Gdx.app.log(Constants.APP_TITLE, msg);
        }
    }

    public static void checkX(String object, float x) {
        if (enableLog) {
            Gdx.app.log(Constants.APP_TITLE, object + ": " + x);
        }
    }

    public static void checkSwap(boolean isSwap) {
        if (enableLog) {
            Gdx.app.log(Constants.APP_TITLE, "Check swap: " + isSwap);
        }
    }

    public static void checkColor(Color color) {
        if (enableLog) {
            Gdx.app.log(Constants.APP_TITLE, "Check color: " + color.r + " " + color.g + " " + color.b);
        }
    }
}
