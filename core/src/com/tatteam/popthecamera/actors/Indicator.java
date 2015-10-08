package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Indicator extends Actor {

    private TextureRegion indicator;
    public boolean isMoving = false;
    public boolean clockwise = true;
    private float angle = 0;
    private float speed;

    public Indicator(TextureRegion indicator) {
        this.indicator = new TextureRegion(indicator);
        setBounds(getX(), getY(), this.indicator.getRegionWidth(), this.indicator.getRegionHeight());
        speed = 1;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void resetAngle() {
        angle = 0;
        setRotation(angle);
    }

    public void fadeOut() {
        addAction(sequence(alpha(0f, 0.15f), run(new Runnable() {
            @Override
            public void run() {
                fadeIn();
            }
        })));
    }

    public void fadeIn() {
        resetAngle();
        addAction(alpha(1, 0.1f));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (isMoving) {
            if (clockwise) {
                angle = (angle - speed) % 360;
            } else {
                angle = (angle + speed) % 360;
            }
            setRotation(angle);
        }
        batch.draw(indicator, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }
}
