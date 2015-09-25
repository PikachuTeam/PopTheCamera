package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.tatteam.popthecamera.Constants;

/**
 * Created by dongc_000 on 9/24/2015.
 */
public class Background extends Actor {

    private TextureRegion background;

    public Background() {
        this.background = new TextureRegion();
        setBounds(getX(), getY(), Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT);
    }

    public Background(TextureRegion background, float width, float height, float x, float y) {
        this.background = new TextureRegion(background);
        setBounds(x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(background, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }
}
