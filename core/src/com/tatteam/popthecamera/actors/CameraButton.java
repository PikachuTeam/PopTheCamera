package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * Created by dongc_000 on 9/25/2015.
 */
public class CameraButton extends Actor {

    private TextureRegion cameraButton;
    private OnPressFinishListener listener;

    public CameraButton(TextureRegion cameraButton) {
        this.cameraButton = new TextureRegion(cameraButton);
        setBounds(getX(), getY(), cameraButton.getRegionWidth(), cameraButton.getRegionHeight());
    }

    public void setOnPressFinishListener(OnPressFinishListener listener) {
        this.listener = listener;
    }

    public CameraButton(TextureRegion cameraButton, float width, float height, float x, float y) {
        this.cameraButton = new TextureRegion(cameraButton);
        setBounds(x, y, width, height);
    }

    public void press() {
        float defaultX = getX();
        float defaultY = getY();

        MoveToAction action1 = new MoveToAction();
        action1.setPosition(defaultX, defaultY - 10f);
        action1.setDuration(0.3f);

        MoveToAction action2 = new MoveToAction() {
            @Override
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    if (listener != null) {
                        listener.onPressFinish();
                    }
                }
                return complete;
            }
        };
        action2.setPosition(defaultX, defaultY);
        action2.setDuration(0.2f);

        addAction(new SequenceAction(action1, action2));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(cameraButton, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

    public interface OnPressFinishListener {
        void onPressFinish();
    }
}
