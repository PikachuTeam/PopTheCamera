package com.tatteam.popthecamera;

import com.tatteam.popthecamera.actors.Dot;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Constants {

    public final static String APP_TITLE = "Pop The Camera";

    public final static int SCREEN_WIDTH = 768 / 2;
    public final static int SCREEN_HEIGHT = 1280 / 2;

    public final static float VIEWPORT_WIDTH = 1152f;
    public final static float VIEWPORT_HEIGHT = 1920f;

    public final static float BACKGROUND_ROTATION = 2f;
    public final static float BACKGROUND_WIDTH = 900f;
    public final static float BACKGROUND_HEIGHT = 700f;

    public final static float LENS1_WIDTH = 625f;
    public final static float LENS1_HEIGHT = 625f;
    public final static float LENS1_X = BACKGROUND_WIDTH / 2 - LENS1_WIDTH / 2;
    public final static float LENS1_Y = 10f;

    public final static float LENS2_WIDTH = 465f;
    public final static float LENS2_HEIGHT = 465f;
    public final static float LEN2_X = 0f;
    public final static float LEN2_Y = 0f;

    public final static float GROUP_LENS_X = BACKGROUND_WIDTH / 2 - LENS2_WIDTH / 2;
    public final static float GROUP_LENS_Y = 92f;

    public final static float LENS3_WIDTH = 315f;
    public final static float LENS3_HEIGHT = 315f;
    public final static float LENS3_X = LENS2_WIDTH / 2 - LENS3_WIDTH / 2;
    public final static float LENS3_Y = LENS2_HEIGHT / 2 - LENS3_HEIGHT / 2;

    public final static float LENS4_WIDTH = 175f;
    public final static float LENS4_HEIGHT = 175f;
    public final static float LENS4_X = LENS2_WIDTH / 2 - LENS4_WIDTH / 2;
    public final static float LENS4_Y = LENS2_HEIGHT / 2 - LENS4_HEIGHT / 2;

    public final static float ROTATION_DURATION = 0.3f;

    public final static float INDICATOR_WIDTH = 20f;
    public final static float INDICATOR_HEIGHT = LENS2_HEIGHT / 2 - LENS3_HEIGHT / 2 - 3;
    public final static float INDICATOR_X = LENS2_WIDTH / 2 - INDICATOR_WIDTH / 2;
    public final static float INDICATOR_Y = LENS2_HEIGHT - INDICATOR_HEIGHT - 1.5f;
    public final static float INDICATOR_ORIGIN_X = INDICATOR_WIDTH / 2;
    public final static float INDICATOR_ORIGIN_Y = -LENS3_HEIGHT / 2;

    public final static float DOT_WIDTH = (LENS2_HEIGHT / 2 - LENS3_HEIGHT / 2) / 2;
    public final static float DOT_HEIGHT = DOT_WIDTH;
    public final static float DOT_X = LENS2_WIDTH / 2 - DOT_WIDTH / 2;
    public final static float DOT_Y = LENS2_HEIGHT - DOT_HEIGHT - DOT_HEIGHT / 2f;
    public final static float DOT_ORIGIN_X = INDICATOR_ORIGIN_X;
    public final static float DOT_ORIGIN_Y = INDICATOR_ORIGIN_Y;

    public final static float CAMERA_BUTTON_WIDTH = 100f;
    public final static float CAMERA_BUTTON_HEIGHT = 40f;
    public final static float CAMERA_BUTTON_X = 100f;
    public final static float CAMERA_BUTTON_Y = BACKGROUND_HEIGHT - 90f;
}
