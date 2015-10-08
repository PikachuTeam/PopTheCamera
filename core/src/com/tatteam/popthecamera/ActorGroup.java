package com.tatteam.popthecamera;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by dongc_000 on 9/25/2015.
 */
public class ActorGroup {

    private Group actors;
    private OnShakeCompleteListener listener;

    public ActorGroup() {
        actors = new Group();
    }

    public float getY() {
        return actors.getY();
    }

    public float getX() {
        return actors.getX();
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
        actors.addAction(sequence(rotateTo(Constants.BACKGROUND_ROTATION, Constants.ROTATION_DURATION)
                , rotateTo(-Constants.BACKGROUND_ROTATION, Constants.ROTATION_DURATION * 2)
                , rotateTo(0, Constants.ROTATION_DURATION), run(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onShakeComplete();
                }
            }
        })));
    }

    public interface OnShakeCompleteListener {
        void onShakeComplete();
    }
}
