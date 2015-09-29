package com.tatteam.popthecamera.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tatteam.popthecamera.Constants;
import com.tatteam.popthecamera.Main;
import com.tatteam.popthecamera.PopTheCamera;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.DESKTOP_SCREEN_WIDTH;
        config.height = Constants.DESKTOP_SCREEN_HEIGHT;
        new LwjglApplication(new PopTheCamera(), config);
    }
}
