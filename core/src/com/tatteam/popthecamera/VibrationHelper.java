package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;

/**
 * Created by the_e_000 on 9/27/2015.
 */
public class VibrationHelper {

    public static boolean enableVibration = true;

    // duration in second
    public static void vibrate(float duration) {
        if (enableVibration) {
            int tmp = (int) (duration * 1000);
            Gdx.input.vibrate(tmp);
        }
    }
}
