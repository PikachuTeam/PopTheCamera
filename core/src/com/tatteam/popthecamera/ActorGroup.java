package com.tatteam.popthecamera;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * Created by dongc_000 on 9/25/2015.
 */
public class ActorGroup {

    private Group actors;
    private OnShakeCompleteListener listener;

    public ActorGroup() {
        actors = new Group();
    }

    public Group getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        actors.addActor(actor);
    }

    public void setOnShakeCompleteListener(OnShakeCompleteListener listener) {
        this.listener = listener;
    }

    public void setPosition(float x, float y) {
        actors.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        actors.setSize(width, height);
    }

    public float getWidth() {
        return actors.getWidth();
    }

    public float getHeight() {
        return actors.getHeight();
    }

    public void setOrigin(float x, float y) {
        actors.setOrigin(x, y);
    }

    public void shake() {
        RotateToAction action1 = new RotateToAction();
        action1.setRotation(Constants.BACKGROUND_ROTATION);
        action1.setDuration(Constants.ROTATION_DURATION);

        RotateToAction action2 = new RotateToAction();
        action2.setRotation(-Constants.BACKGROUND_ROTATION);
        action2.setDuration(Constants.ROTATION_DURATION * 2);

        RotateToAction action3 = new RotateToAction() {
            @Override
            public boolean act(float delta) {
                boolean complete = super.act(delta);
                if (complete) {
                    if (listener != null) {
                        listener.onShakeComplete();
                    }
                }
                return complete;
            }
        };
        action3.setRotation(0);
        action3.setDuration(Constants.ROTATION_DURATION);

        actors.addAction(new SequenceAction(action1, action2, action3));
    }

    public interface OnShakeCompleteListener {
        void onShakeComplete();
    }
}
