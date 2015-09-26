package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;

/**
 * Created by dongc_000 on 9/26/2015.
 */
public class AlphaRectangle extends Actor {

    private ShapeRenderer rectangle;

    public AlphaRectangle() {
        rectangle = new ShapeRenderer();
        setBounds(getX(), getY(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void appear(Stage stage) {
        stage.addActor(this);
        AlphaAction alphaAction1 = new AlphaAction() {
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    getParent().removeActor(AlphaRectangle.this);
                }
                return complete;
            }
        };
        alphaAction1.setAlpha(1f);
        alphaAction1.setDuration(0.3f);

        addAction(alphaAction1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rectangle.begin(ShapeRenderer.ShapeType.Filled);
//        rectangle.setColor(1f, 1f, 1f, 0.5f);
        rectangle.setColor(0f, 67f/255f, 55/255f, 0.9f);
//        rectangle.setColor(0.7f, 0.7f, 0.7f, 0.7f);
        rectangle.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        rectangle.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }
}
