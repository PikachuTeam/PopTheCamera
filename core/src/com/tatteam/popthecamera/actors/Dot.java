package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tatteam.popthecamera.GDXGameLauncher;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Dot extends Actor {

    private TextureRegion dot;
    private Random random;
    private OnFadeCompleteListener listener;
    private int type;

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
        int rnd = random.nextInt(50) + 40;
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

    // type: 1 when win or play again; 2 when dot changes rotation
    public void fadeOut(int type) {
        GDXGameLauncher.touchable = false;
        this.type = type;
        addAction(sequence(alpha(0f, 0.15f), run(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onFadeOutComplete(Dot.this.type);
                }
            }
        })));
    }

    public void fadeIn(int type) {
        this.type = type;
        addAction(sequence(alpha(1, 0.1f), run(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onFadeInComplete(Dot.this.type);
                }
            }
        })));
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
