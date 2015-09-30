package com.tatteam.popthecamera.android;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * Created by ThanhNH on 9/29/2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String[] LEVEL_NAMES = {"Slow", "Medium", "Fast", "Crazy"};

    private AdView mAdView;
    private View btnClassicalMode;
    private View btnUnlimitedMode;
    private View btnNext, btnPre;
    private TextView tvLevel;
    private MediaPlayer changeLevel, enterMode;
    private int levelIndex = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLevel = MediaPlayer.create(this.getActivity(), R.raw.change_level);
        enterMode = MediaPlayer.create(this.getActivity(), R.raw.start_game);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(this.getActivity(), R.layout.fragment_home, null);
        findViews(view);
        setupAds();
        return view;
    }


    @Override
    public void onDestroy() {
        changeLevel.release();
        enterMode.release();
        super.onDestroy();
    }

    public void findViews(View parent) {
        mAdView = (AdView) parent.findViewById(R.id.adView);
        btnNext = parent.findViewById(R.id.btnNextMode);
        btnPre = parent.findViewById(R.id.btnPreMode);
        btnClassicalMode = parent.findViewById(R.id.btn_classical_mode);
        btnUnlimitedMode = parent.findViewById(R.id.btn_unlimited_mode);
        tvLevel = (TextView) parent.findViewById(R.id.tvLevel);

        btnNext.setSoundEffectsEnabled(false);
        btnPre.setSoundEffectsEnabled(false);
        btnClassicalMode.setSoundEffectsEnabled(false);
        btnUnlimitedMode.setSoundEffectsEnabled(false);

        btnClassicalMode.setOnClickListener(this);
        btnUnlimitedMode.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPre.setOnClickListener(this);

        updateLevelText();
    }


    private void setupAds() {
        if (MainActivity.ADS_ENABLE) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnClassicalMode) {
            enterMode.start();
            addGameFragment(getClassicGameMode());
        } else if (view == btnUnlimitedMode) {
            enterMode.start();
            addGameFragment(GameFragment.GAME_MODE_UNLIMITED);
        } else if (view == btnNext) {
            changeLevel.start();
            if (levelIndex >= LEVEL_NAMES.length - 1) {
                levelIndex = 0;
            } else {
                levelIndex++;
            }
            updateLevelText();
        } else if (view == btnPre) {
            changeLevel.start();
            if (levelIndex <= 0) {
                levelIndex = LEVEL_NAMES.length - 1;
            } else {
                levelIndex--;
            }
            updateLevelText();
        }
    }

    private void updateLevelText() {
        tvLevel.setText(LEVEL_NAMES[levelIndex]);
    }

    private int getClassicGameMode() {
        switch (levelIndex) {
            case 0:
                return GameFragment.GAME_MODE_CLASSIC_SLOW;
            case 1:
                return GameFragment.GAME_MODE_CLASSIC_MEDIUM;
            case 2:
                return GameFragment.GAME_MODE_CLASSIC_FAST;
            case 3:
                return GameFragment.GAME_MODE_CLASSIC_CRAZY;
        }
        return GameFragment.GAME_MODE_CLASSIC_MEDIUM;
    }


    private void addGameFragment(final int gameMode) {
        View view = getView();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f).setDuration(300);
        alphaAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                GameFragment fragment = new GameFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(GameFragment.KEY_GAME_MODE, gameMode);
                fragment.setArguments(bundle);
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit,
                        R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
                trans.replace(R.id.container, fragment);
                trans.addToBackStack(GameFragment.class.getSimpleName());
                trans.commit();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        alphaAnimator.start();

    }

}
