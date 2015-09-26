package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
//        this.indicator = new Te(Gdx.files.internal("images/large/indicator.png"));
        this.indicator=new TextureRegion(new Texture(Gdx.files.internal("images/large/indicator.png")));
        setBounds(getX(), getY(), this.indicator.getRegionWidth(), this.indicator.getRegionHeight());
        accelerator = 1;
    }

    public void setAccelerator(float accelerator) {
        this.accelerator = accelerator;
    }

    public void setCenterOrigin(float radius) {
        setOrigin(getWidth() / 2, -radius);
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
