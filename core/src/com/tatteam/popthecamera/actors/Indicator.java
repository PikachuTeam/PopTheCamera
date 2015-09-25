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

    public Indicator() {
        indicator = new TextureRegion();
        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    public Indicator(TextureRegion indicator, float width, float height, float x, float y, float originX, float originY) {
        this.indicator = new TextureRegion(indicator);
        setBounds(x, y, width, height);
        setOrigin(originX, originY);
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
                angle--;
                if (angle == -360) {
                    angle = 0;
                }
            } else {
                angle++;
                if (angle == 360) {
                    angle = 0;
                }
            }
            setRotation(angle);
        }
        batch.draw(indicator, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

}
