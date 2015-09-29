package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by dongc_000 on 9/27/2015.
 */
public class SoundHelper {

    private static SoundHelper instance;
    public static boolean enableSound = true;
    private Sound failSound;
    private Sound successSound;
    private Sound finishLevelSound;

    private SoundHelper() {
    }

    public static SoundHelper getInstance() {
        if (instance == null) {
            instance = new SoundHelper();
        }
        return instance;
    }

    public void initSound() {
        successSound = Gdx.audio.newSound(Gdx.files.internal("sounds/success.wav"));
        failSound = Gdx.audio.newSound(Gdx.files.internal("sounds/fail.mp3"));
        finishLevelSound = Gdx.audio.newSound(Gdx.files.internal("sounds/finish_level.wav"));

    }

    public void playSuccessSound() {
        playSuccessSound(1f);
    }

    public void playFailSound() {
        playFailSound(1f);
    }

    public void playFinishLevelSound() {
        playFinishLevelSound(1f);
    }

    public void playSuccessSound(float volume) {
        if (enableSound) {
            successSound.play(volume);
        }
    }

    public void playFailSound(float volume) {
        if (enableSound) {
            failSound.play(volume);
        }
    }

    public void playFinishLevelSound(float volume) {
        if (enableSound) {
            finishLevelSound.play(volume);
        }
    }

    public void dispose() {
        if (instance != null) {
            successSound.dispose();
            failSound.dispose();
            finishLevelSound.dispose();
            instance = null;
        }
    }
}
