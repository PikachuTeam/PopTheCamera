package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Dot extends Actor {

    private TextureRegion dot;

    public Dot() {
        dot = new TextureRegion();
        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    public Dot(TextureRegion dot, float width, float height, float x, float y, float originX, float originY) {
        this.dot = new TextureRegion(dot);
        setBounds(x, y, width, height);
        setOrigin(originX, originY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(dot, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }
}
