package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;

/**
 * Created by ThanhNH on 9/27/2015.
 */
public class AssetsLoader {
    private static AssetsLoader instance;
    private ScreenResolution[] screenResolutions = new ScreenResolution[]{ScreenResolution.MEDIUM, ScreenResolution.HIGH};
    private ScreenResolution activeScreenResolution;

    private AssetsLoader() {
    }

    public static AssetsLoader getInstance() {
        if (instance == null) {
            instance = new AssetsLoader();
        }
        return instance;
    }

    public void init() {
        activeScreenResolution = screenResolutions[0];
        int screenWidth = Gdx.graphics.getWidth();
        for (int i = screenResolutions.length - 1; i >= 0; i--) {
            ScreenResolution screenResolution = screenResolutions[i];
            if (screenWidth >= screenResolution.smallWidth) {
                activeScreenResolution = screenResolution;
                break;
            }
        }
    }

    public ScreenResolution getActiveScreenResolution() {
        return activeScreenResolution;
    }

    public String getImagePath() {
        return activeScreenResolution.getImagePath();
    }

    public String getFontPath() {
        return activeScreenResolution.getFontPath();
    }

    public static enum ScreenResolution {
        HIGH(1152, "images/high/", "fonts/high/"),
        MEDIUM(768, "images/medium/", "fonts/medium/"),
        LOW(512, "images/low/", "fonts/low/");

        private int smallWidth;
        private String imagePath, fontPath;
        
        private ScreenResolution(int smallWidth, String imagePath, String fontPath) {
            this.smallWidth = smallWidth;
            this.imagePath = imagePath;
            this.fontPath = fontPath;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getFontPath() {
            return fontPath;
        }

        public int getSmallWidth() {
            return smallWidth;
        }
    }

}
