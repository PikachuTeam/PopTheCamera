package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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


    public TextView(FileHandle fontFile) {
        text = "";
        generator = new FreeTypeFontGenerator(fontFile);
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = generator.generateFont(parameter);
    }

    public void setFontSize(int fontSize) {
        parameter.size = fontSize;
        font = generator.generateFont(parameter);
    }

    public void setFont(FileHandle fontFile) {
        generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont(parameter);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFontColor(float r, float g, float b, float a) {
        font.setColor(r, g, b, a);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, text, getX(), getY());
    }
}
