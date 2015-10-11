package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Created by the_e_000 on 9/25/2015.
 */
public class TextView extends Actor {

    private BitmapFont font;
    private String text;
    private GlyphLayout layout;

    public TextView(FileHandle fontFile, FileHandle fontImage) {
        text = "";
        font = new BitmapFont(fontFile, fontImage, false);
        layout = new GlyphLayout();
        layout.setText(font, text);
    }

    @Override
    public float getWidth() {
        return layout.width;
    }

    @Override
    public float getHeight() {
        return layout.height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        layout.setText(font, text);
    }

    public BitmapFont getBitMapFont() {
        return font;
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        font.getData().setScale(scaleX, scaleY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, text, getX(), getY());
    }
}
