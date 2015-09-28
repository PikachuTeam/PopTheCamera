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
    public final static Color FAIL_COLOR = new Color(0xff2d2dff);
    public final static Color FLASH_COLOR = new Color(0.93f, 0.93f, 0.93f, 0.37f);

    private ColorHelper() {
    }

    public void initColor() {
        brightestColor = new Color[]{
                new Color(0x00838fff),
                new Color(0xdf96d2ff),
                new Color(0x00ae63ff),
                new Color(0xddd8abff),
                new Color(0x8686d2ff),
                new Color(0x905b74ff),
                new Color(0x4a9cd1ff),
                new Color(0x4ad190ff),
                new Color(0xda9273ff),
                new Color(0xdf989aff)
        };
        normalColor = new Color[]{
                new Color(0x006064ff),
                new Color(0xc74fb2ff),
                new Color(0x008d50ff),
                new Color(0xb4ad71ff),
                new Color(0x4a4aa3ff),
                new Color(0x83405fff),
                new Color(0x3381b3ff),
                new Color(0x32b777ff),
                new Color(0xbf7555ff),
                new Color(0xcd6366ff)
        };
        darkestColor = new Color[]{
                new Color(0x004d50ff),
                new Color(0xaa2192ff),
                new Color(0x015732ff),
                new Color(0x857f50ff),
                new Color(0x3a3a70ff),
                new Color(0x7e254eff),
                new Color(0x16547cff),
                new Color(0x0f834bff),
                new Color(0xa35838ff),
                new Color(0xc15356ff)
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
        if (index < 0) {
            index = length - 1;
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
