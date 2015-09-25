package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by the_e_000 on 9/25/2015.
 */
public class SoundHelper {

    private static SoundHelper instance = null;
    private Sound sound;
    public boolean enableSound = true;
    private long playId;

    private SoundHelper() {
    }

    public static SoundHelper getInstance() {
        if (instance == null) {
            instance = new SoundHelper();
        }
        return instance;
    }

    public void makeSound(FileHandle fileHandle) {
        sound = Gdx.audio.newSound(fileHandle);
    }

    public void playSound() {
        if (enableSound) {
            sound.play();
        }
    }

    public void stopSound() {
        sound.stop();
    }

    public void dispose() {
        sound.dispose();
    }
}
