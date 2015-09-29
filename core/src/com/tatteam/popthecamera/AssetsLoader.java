package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;

/**
 * Created by ThanhNH on 9/27/2015.
 */
public class AssetsLoader {
    private static AssetsLoader instance;
    private ScreenResolution[] screenResolutions = new ScreenResolution[]{ScreenResolution.LOW, ScreenResolution.MEDIUM, ScreenResolution.HIGH};
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
            if (screenWidth >= screenResolution.getViewPortSize().getWidth()) {
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

    public ViewPortSize getViewPortSize() {
        return activeScreenResolution.getViewPortSize();
    }

    public static enum ScreenResolution {
        LOW(ViewPortSize.LOW, "images/low/", "fonts/low/"),
        MEDIUM(ViewPortSize.MEDIUM, "images/medium/", "fonts/medium/"),
        HIGH(ViewPortSize.HIGH, "images/high/", "fonts/high/");

        private ViewPortSize viewPortSize;
        private String imagePath, fontPath;

        private ScreenResolution(ViewPortSize viewPortSize, String imagePath, String fontPath) {
            this.viewPortSize = viewPortSize;
            this.imagePath = imagePath;
            this.fontPath = fontPath;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getFontPath() {
            return fontPath;
        }

        public ViewPortSize getViewPortSize() {
            return viewPortSize;
        }
    }

    public static enum ViewPortSize {
        LOW(576, 960),
        MEDIUM(768, 1280),
        HIGH(1152, 1920);

        private int width, height;

        private ViewPortSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }


}
