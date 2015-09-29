package com.tatteam.popthecamera.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.tatteam.popthecamera.GDXGameLauncher;

/**
 * Created by ThanhNH on 9/29/2015.
 */
public class GameFragment extends AndroidFragmentApplication {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        return initializeForView(new GDXGameLauncher(), config);
    }
}
