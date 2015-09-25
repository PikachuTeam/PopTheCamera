package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Indicator extends Actor {

    private TextureRegion indicator;
    public boolean isMoving = false;
    public boolean clockwise = true;
    private float angle = 0;
    private float accelerator;

    public Indicator(TextureRegion indicator) {
        this.indicator = new TextureRegion(indicator);
        setBounds(getX(), getY(), indicator.getRegionWidth(), indicator.getRegionHeight());
        accelerator = 1;
    }

    public Indicator(TextureRegion indicator, float width, float height, float x, float y, float originX, float originY) {
        this.indicator = new TextureRegion(indicator);
        setBounds(x, y, width, height);
        setOrigin(originX, originY);
    }

    public void setAccelerator(float accelerator) {
        this.accelerator = accelerator;
    }

    public void setCenterOrigin(float radius) {
        setOrigin(getWidth() / 2, -radius);
    }

    public void setImage(TextureRegion indicator) {
        indicator.setRegion(indicator);
    }

    public void resetAngle() {
        angle = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (isMoving) {
            if (clockwise) {
                angle = (angle - accelerator) % 360;
            } else {
                angle = (angle + accelerator) % 360;
            }
            setRotation(angle);
        }
        batch.draw(indicator, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

}
