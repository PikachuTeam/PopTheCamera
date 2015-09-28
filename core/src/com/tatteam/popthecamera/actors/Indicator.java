package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Indicator extends Actor {

    private TextureRegion indicator;
    public boolean isMoving = false;
    public boolean clockwise = true;
    private float angle = 0;
    private float accelerator;
    private AlphaAction fadeIn;
    private AlphaAction fadeOut;

    public Indicator(TextureRegion indicator) {
        this.indicator = new TextureRegion(indicator);
        setBounds(getX(), getY(), this.indicator.getRegionWidth(), this.indicator.getRegionHeight());
        fadeIn = new AlphaAction() {
            @Override
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    fadeIn.reset();
                }
                return complete;
            }
        };
        fadeOut = new AlphaAction() {
            @Override
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    fadeIn();
                    fadeOut.reset();
                }
                return complete;
            }
        };
        accelerator = 1;
    }

    public void setAccelerator(float accelerator) {
        this.accelerator = accelerator;
    }

    public void resetAngle() {
        angle = 0;
        setRotation(angle);
    }

    public void fadeOut() {
        fadeOut.setAlpha(0f);
        fadeOut.setDuration(0.15f);
        addAction(fadeOut);
    }

    public void fadeIn() {
        resetAngle();
        fadeIn.setAlpha(1);
        fadeIn.setDuration(0.1f);
        addAction(fadeIn);
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
