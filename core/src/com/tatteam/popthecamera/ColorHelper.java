package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by the_e_000 on 9/26/2015.
 */
public class ColorHelper {

    private static ColorHelper instance;
    private Color[] brightestColor;
    private Color[] normalColor;
    private Color[] darkestColor;
    public final static Color FAIL_COLOR = new Color(0xff2d2dff);
    public final static Color FLASH_COLOR = new Color(0.93f, 0.93f, 0.93f, 0.37f);

    private ColorHelper() {

    }

    public void initColor() {

        Json json = new Json();
        ColorFactory colorFactory = json.fromJson(ColorFactory.class,
                Gdx.files.internal("colors/color_items.json"));

        brightestColor = new Color[colorFactory.colors.size()];
        normalColor = new Color[colorFactory.colors.size()];
        darkestColor = new Color[colorFactory.colors.size()];

        for (int i = 0; i < colorFactory.colors.size(); i++) {
            brightestColor[i] = new Color(Color.valueOf(colorFactory.colors.get(i).len2));
            normalColor[i] = new Color(Color.valueOf(colorFactory.colors.get(i).len3));
            darkestColor[i] = new Color(Color.valueOf(colorFactory.colors.get(i).len4));
        }

    }

    public static ColorHelper getInstance() {
        if (instance == null) {
            instance = new ColorHelper();
        }
        return instance;
    }


    public Color getBackgroundColor(int level) {
        int index = (level - 1) % brightestColor.length;
        return normalColor[index];
    }


    public void setColor(int level, Actor actor1, Actor actor2, Actor actor3) {
        int index = (level - 1) % brightestColor.length;
        actor1.setColor(brightestColor[index]);
        actor2.setColor(normalColor[index]);
        actor3.setColor(darkestColor[index]);
    }

    public void setColorUnlimitedMode(int score, Actor background ,Actor len2, Actor len3, Actor len4) {
        int index = score % brightestColor.length;
        len2.setColor(brightestColor[index]);
        len3.setColor(normalColor[index]);
        len4.setColor(darkestColor[index]);
        background.setColor(normalColor[index]);
    }

    public Color getBackGroundColorUnlimitedMode(int score){
        int index = score % brightestColor.length;
        return normalColor[index];
    }

    public void dispose() {
        if (instance != null) {
            instance = null;
            brightestColor = null;
            normalColor = null;
            darkestColor = null;
        }
    }

    private static class ColorFactory {
        ArrayList<ColorItem> colors;
    }

    private static class ColorItem {
        String len2, len3, len4;
    }

}
