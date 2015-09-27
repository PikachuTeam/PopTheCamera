package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.tatteam.popthecamera.Main;

import java.util.Random;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Dot extends Actor {

    private TextureRegion dot;
    private Random random;
    private OnFadeCompleteListener listener;

    public Dot(TextureRegion dot) {
        this.dot = new TextureRegion(dot);
        setBounds(getX(), getY(), this.dot.getRegionWidth(), this.dot.getRegionHeight());
        random = new Random();
    }

    public void setOnFadeCompleteListener(OnFadeCompleteListener listener) {
        this.listener = listener;
    }

    public void initPosition() {
        int type = random.nextInt(2);
        if (type == 0) {
            setRotation(random.nextInt(60) + 30);
        } else {
            setRotation(random.nextInt(60) + 270);
        }
    }

    public void randomPosition(boolean clockwise) {
        float rotation;
        int rnd = random.nextInt(50) + 30;
        if (clockwise) {
            rotation = getRotation() + rnd;
        } else {
            rotation = getRotation() - rnd;
        }
        if (rotation < 0) {
            rotation += 360;
        } else if (rotation > 360) {
            rotation -= 360;
        }
        setRotation(rotation);
    }

    public void fadeOut(final int type) {
        AlphaAction alphaAction = new AlphaAction() {
            @Override
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    if (listener != null) {
                        listener.onFadeOutComplete(type);
                    }
                }
                return complete;
            }
        };
        alphaAction.setAlpha(0f);
        alphaAction.setDuration(0.1f);
        addAction(alphaAction);
    }

    public void fadeIn(final int type) {
        final AlphaAction alphaAction = new AlphaAction() {
            @Override
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    if (listener != null) {
                        listener.onFadeInComplete(type);
                    }
                }
                return complete;
            }
        };
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        addAction(alphaAction);
    }

    @Override

    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(dot, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

    public interface OnFadeCompleteListener {
        void onFadeOutComplete(int type);

        void onFadeInComplete(int type);
    }
}
