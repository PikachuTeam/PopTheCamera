package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.tatteam.popthecamera.ColorHelper;
import com.tatteam.popthecamera.GDXGameLauncher;

/**
 * Created by dongc_000 on 9/26/2015.
 */
public class Flash extends Actor {

    private ShapeRenderer rectangle;
    private OnDisappearListener listener;
    private AlphaAction alphaAction;
    private float width;
    private float height;
    public boolean isAppear = false;

    public Flash(float width, float height) {
        rectangle = new ShapeRenderer();
        alphaAction = new AlphaAction() {
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    if (listener != null) {
                        alphaAction.reset();
                        listener.onDisappear(Flash.this);
                    }
                }
                return complete;
            }
        };
        this.width = width;
        this.height = height;
    }

    public void setOnDisappearListener(OnDisappearListener listener) {
        this.listener = listener;
    }

    public void appear(Stage stage) {
        isAppear = true;
        stage.addActor(this);
        alphaAction.setAlpha(1f);
        alphaAction.setDuration(0.2f);
        addAction(alphaAction);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rectangle.begin(ShapeRenderer.ShapeType.Filled);
        rectangle.setColor(ColorHelper.FLASH_COLOR);
        rectangle.rect(0, 0, width, height);
        rectangle.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    public interface OnDisappearListener {
        void onDisappear(Flash flash);
    }
}
