package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.tatteam.popthecamera.Constants;

/**
 * Created by dongc_000 on 9/25/2015.
 */
public class CameraButton extends Actor {

    private TextureRegion cameraButton;
    private OnPressFinishListener listener;
    private MoveToAction press;
    private MoveToAction release;

    public CameraButton(TextureRegion cameraButton) {
        this.cameraButton = new TextureRegion(cameraButton);
        setBounds(getX(), getY(), cameraButton.getRegionWidth(), cameraButton.getRegionHeight());
        press = new MoveToAction() {
            @Override
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    press.reset();
                }
                return complete;
            }
        };

        release = new MoveToAction() {
            @Override
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    if (listener != null) {
                        listener.onPressFinish();
                    }
                    release.reset();
                }
                return complete;
            }
        };
    }

    public void setOnPressFinishListener(OnPressFinishListener listener) {
        this.listener = listener;
    }

    public void press() {
        float defaultX = getX();
        float defaultY = getY();

        press.setPosition(defaultX, defaultY - 10f);
        press.setDuration(Constants.CAMERA_BUTTON_PRESS_DURATION);

        release.setPosition(defaultX, defaultY);
        release.setDuration(Constants.CAMERA_BUTTON_RELEASE_DURATION);

        addAction(new SequenceAction(press, release));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(cameraButton, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

    public interface OnPressFinishListener {
        void onPressFinish();
    }
}
