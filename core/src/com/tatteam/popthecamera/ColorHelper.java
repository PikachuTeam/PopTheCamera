package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by the_e_000 on 9/26/2015.
 */
public class ColorHelper {

    private static final float COLOR_TRANSFORM_DURATION = 0.5f;

    private static ColorHelper instance;
    private Color[] brightestColor;
    private Color[] normalColor;
    private Color[] darkestColor;
    public final static Color FAIL_COLOR = new Color(0xff2d2dff);
    public final static Color FLASH_COLOR = new Color(0.93f, 0.93f, 0.93f, 0.37f);
    private MyColorAction colorLen2Action, colorLen3Action, colorLen4Action, colorBackgroundAction;

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

        colorLen2Action = new MyColorAction();
        colorLen3Action = new MyColorAction();
        colorLen4Action = new MyColorAction();
        colorBackgroundAction= new MyColorAction();
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
        colorLen2Action.setup(len2.getColor(), brightestColor[index], COLOR_TRANSFORM_DURATION);
        len2.addAction(colorLen2Action);
        colorLen3Action.setup(len3.getColor(), normalColor[index], COLOR_TRANSFORM_DURATION);
        len3.addAction(colorLen3Action);
        colorLen4Action.setup(len4.getColor(), darkestColor[index], COLOR_TRANSFORM_DURATION);
        len4.addAction(colorLen4Action);

        colorBackgroundAction.setup(background.getColor(), normalColor[index], COLOR_TRANSFORM_DURATION);
        background.addAction(colorBackgroundAction);

    }

    public Color getBackGroundColorUnlimitedMode(int score){
        return normalColor[score];
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

    public static class MyColorAction extends ColorAction {
        @Override
        public boolean act(float delta) {
            boolean complete = super.act(delta);
            if (complete) {
                this.reset();
            }
            return complete;
        }

        public void setup(Color from, Color to, float duration) {
            super.setColor(from);
            super.setEndColor(to);
            super.setDuration(duration);
        }
    }
}
