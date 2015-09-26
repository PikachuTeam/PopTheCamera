package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Created by the_e_000 on 9/25/2015.
 */
public class TextView extends Actor {

    private BitmapFont font;
    private String text;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private GlyphLayout layout;

    public TextView(FileHandle fontFile) {
        text = "";
        generator = new FreeTypeFontGenerator(fontFile);
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.color = new Color(1, 1, 1, 1);
        font = generator.generateFont(parameter);
        layout = new GlyphLayout();
        layout.setText(font, text);
    }

    public void setFontSize(int fontSize) {
        parameter.size = fontSize;
        font = generator.generateFont(parameter);
        layout.setText(font, text);
    }

    public void setFont(FileHandle fontFile) {
        generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont(parameter);
    }

    @Override
    public float getWidth() {
        return layout.width;
    }

    @Override
    public float getHeight() {
        return layout.height;
    }

    public int getFontSize() {
        return parameter.size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        layout.setText(font, text);
    }

    public void setFontColor(float r, float g, float b, float a) {
        parameter.color = new Color(r, g, b, a);
        font = generator.generateFont(parameter);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, text, getX(), getY());
    }
}
