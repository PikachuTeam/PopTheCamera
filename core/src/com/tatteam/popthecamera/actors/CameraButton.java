package com.tatteam.popthecamera.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.tatteam.popthecamera.Constants;
import com.tatteam.popthecamera.GDXGameLauncher;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

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

    public void press() {
        GDXGameLauncher.touchable = false;
        float defaultX = getX();
        float defaultY = getY();

        addAction(new SequenceAction(moveTo(defaultX, defaultY - 10f, Constants.CAMERA_BUTTON_PRESS_DURATION), moveTo(defaultX, defaultY, Constants.CAMERA_BUTTON_RELEASE_DURATION), run(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onPressFinish();
                }
            }
        })));
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
