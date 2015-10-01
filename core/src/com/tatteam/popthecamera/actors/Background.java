package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by ThanhNH on 9/30/2015.
 */
public class Background extends Actor {
    private ShapeRenderer rectangle;
    private float width;
    private float height;

    public Background (float width, float height){
        rectangle = new ShapeRenderer();
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        rectangle.begin(ShapeRenderer.ShapeType.Filled);

        rectangle.rect(0, 0, width, height);
        rectangle.end();
        batch.begin();
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        rectangle.setColor(color);
    }
}
