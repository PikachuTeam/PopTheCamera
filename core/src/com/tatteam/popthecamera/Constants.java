package com.tatteam.popthecamera;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Constants {

    public final static String APP_TITLE = "Pop The Camera";

    public final static int DESKTOP_SCREEN_WIDTH = 400; //1152; // 576; 768; 1152
    public final static int DESKTOP_SCREEN_HEIGHT = 700; //1920; // 960; 1280; 1920

    public final static float BACKGROUND_ROTATION = 2f;

    public final static float ROTATION_DURATION = 0.1f;

    public final static float CAMERA_BUTTON_PRESS_DURATION = 0.4f;
    public final static float CAMERA_BUTTON_RELEASE_DURATION = 0.25f;

    public final static float TOUCH_OFFSET = 10f;

    public static enum GameMode {
        CLASSIC_SLOW(1.0f),
        CLASSIC_MEDIUM(1.6f),
        CLASSIC_FAST(2.0f),
        CLASSIC_CRAZY(2.5f),
        UNLIMITED(1.0f);//start from 1.0 and increase after each point

        public static final int UNLIMITED_INCREASING_POINT = 1;
        public static final float UNLIMITED_MAX_SPEED = CLASSIC_CRAZY.getSpeed();
        public static final float UNLIMITED_INCREASING_SPEED = 0.015f;

        private float speed;

        private GameMode(float speed) {
            this.speed = speed;
        }

        public float getSpeed() {
            return speed;
        }

        public float getUnlimitedNewSpeed() {
            if (this == UNLIMITED) {
                if (speed < UNLIMITED_MAX_SPEED) {
                    speed += UNLIMITED_INCREASING_SPEED;
                }
                return speed;
            }
            return 0;
        }

        public void resetUnlimitedSpeed() {
            if (this == UNLIMITED) {
                this.speed = 1.0f;
            }
        }
    }
}
