package com.tatteam.popthecamera;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by the_e_000 on 9/26/2015.
 */
public class ColorHelper {

    private static ColorHelper instance;
    private Color[] brightestColor;
    private Color[] normalColor;
    private Color[] darkestColor;
    private int index;
    private int length;

    private ColorHelper() {
    }

    public void initColor() {
        brightestColor = new Color[]{
                new Color(0, 131f / 255f, 143f / 255f, 1),
                new Color(0x00E0F4FF),
                new Color(0x907AFFFF)
        };
        normalColor = new Color[]{
                new Color(0, 96f / 255f, 100f / 255f, 1),
                new Color(0x00C2C9FF),
                new Color(0x7361CCFF)
        };
        darkestColor = new Color[]{
                new Color(0, 77f / 255f, 80f / 255f, 1),
                new Color(0x00AFB5FF),
                new Color(0x564999FF)
        };
        length = brightestColor.length;
        index = 0;
    }

    public static ColorHelper getInstance() {
        if (instance == null) {
            instance = new ColorHelper();
        }
        return instance;
    }

    public Color getBrightestColor(int index) {
        if (index >= length) {
            index -= length;
        }
        return brightestColor[index];
    }

    public Color getDarkestColor(int index) {
        if (index >= length) {
            index -= length;
        }
        return darkestColor[index];
    }

    public Color getNormalColor(int index) {
        if (index >= length) {
            index -= length;
        }
        return normalColor[index];
    }

    public int getIndex() {
        return index;
    }

    public void setColor(Actor actor1, Actor actor2, Actor actor3) {
        if (index >= length) {
            index -= length;
        }
        actor1.setColor(brightestColor[index]);
        actor2.setColor(normalColor[index]);
        actor3.setColor(darkestColor[index]);
        index++;
    }

    public void dispose() {
        if (instance != null) {
            instance = null;
            brightestColor = null;
            normalColor = null;
            darkestColor = null;
        }
    }
}
