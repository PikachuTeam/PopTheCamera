package com.tatteam.popthecamera.android;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tatteam.popthecamera.Constants;
import com.tatteam.popthecamera.GDXGameLauncher;

/**
 * Created by ThanhNH on 9/29/2015.
 */
public class GameFragment extends AndroidFragmentApplication implements GDXGameLauncher.OnGameListener {

    public static final String KEY_GAME_MODE = "game_mode";
    public static final int GAME_MODE_CLASSIC_SLOW = 1;
    public static final int GAME_MODE_CLASSIC_MEDIUM = 2;
    public static final int GAME_MODE_CLASSIC_FAST = 3;
    public static final int GAME_MODE_CLASSIC_CRAZY = 4;
    public static final int GAME_MODE_UNLIMITED = 10;

    private Handler handler;

    private GDXGameLauncher gameLauncher;

    private InterstitialAd interstitialAd;
    private int lossGameCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lossGameCounter = 0;
        handler = new Handler();
        setupAds();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        gameLauncher = new GDXGameLauncher();
        gameLauncher.setOnGameListener(this);
        Bundle bundle = this.getArguments();
        int gameMode = bundle.getInt(KEY_GAME_MODE, GAME_MODE_CLASSIC_MEDIUM);
        switch (gameMode) {
            case GAME_MODE_CLASSIC_SLOW:
                gameLauncher.setGameMode(Constants.GameMode.CLASSIC_SLOW);
                break;
            case GAME_MODE_CLASSIC_MEDIUM:
                gameLauncher.setGameMode(Constants.GameMode.CLASSIC_MEDIUM);
                break;
            case GAME_MODE_CLASSIC_FAST:
                gameLauncher.setGameMode(Constants.GameMode.CLASSIC_FAST);
                break;
            case GAME_MODE_CLASSIC_CRAZY:
                gameLauncher.setGameMode(Constants.GameMode.CLASSIC_CRAZY);
                break;
            case GAME_MODE_UNLIMITED:
                gameLauncher.setGameMode(Constants.GameMode.UNLIMITED);
                break;
        }
        return initializeForView(gameLauncher, config);
    }

    @Override
    public void onLossGame(GDXGameLauncher gameLauncher, Constants.GameMode gameMode, int currentLevel, int score) {
        displayAdsIfNeeded(gameMode, currentLevel, score);
    }

    @Override
    public void onGameBackPressed() {
        getFragmentManager().popBackStack();
    }

    private void displayAdsIfNeeded(Constants.GameMode gameMode, int currentLevel, int score) {
        if (!MainActivity.ADS_ENABLE)
            return;
        lossGameCounter++;
        if (gameMode == Constants.GameMode.UNLIMITED) {
            if (lossGameCounter % 3 == 0) {
                displayAds();
            }
        } else {
            if (gameMode == Constants.GameMode.CLASSIC_SLOW) {
                if (currentLevel >= 4 && lossGameCounter % 3 == 0) {
                    displayAds();
                }
            } else if (gameMode == Constants.GameMode.CLASSIC_MEDIUM) {
                if (currentLevel >= 4 && lossGameCounter % 3 == 0) {
                    displayAds();
                }
            } else if (gameMode == Constants.GameMode.CLASSIC_FAST) {
                if (currentLevel >= 4 && lossGameCounter % 4 == 0) {
                    displayAds();
                }
            } else if (gameMode == Constants.GameMode.CLASSIC_CRAZY) {
                if (currentLevel >= 4 && lossGameCounter % 4 == 0) {
                    displayAds();
                }
            }
        }
    }

    private void displayAds() {
        if (!MainActivity.ADS_ENABLE)
            return;
        updateOnUIThread(new Runnable() {
            @Override
            public void run() {
                interstitialAd.show();
            }
        });
    }

    private void updateOnUIThread(Runnable runnable) {
        handler.post(runnable);
    }

    private void setupAds() {
        if (!MainActivity.ADS_ENABLE)
            return;
        interstitialAd = new InterstitialAd(this.getActivity());
        interstitialAd.setAdUnitId(getString(R.string.big_banner_ad_unit_id));
        requestNewInterstitial();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }
}
