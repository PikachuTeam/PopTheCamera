package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Lens extends Actor {

    private TextureRegion lens;

    public Lens(TextureRegion lens) {
        this.lens = new TextureRegion(lens);
        setBounds(getX(), getY(), lens.getRegionWidth(), lens.getRegionHeight());
    }

    public Lens(TextureRegion lens, float width, float height, float x, float y) {
        this.lens = new TextureRegion(lens);
        setBounds(x, y, width, height);
    }

    public void setCenterPosition(float parentWidth, float parentHeight) {
        setPosition(parentWidth / 2 - getWidth() / 2, parentHeight / 2 - getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(lens, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }
}
