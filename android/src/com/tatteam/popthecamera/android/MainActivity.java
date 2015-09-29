package com.tatteam.popthecamera.android;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks {
    public static final boolean ADS_ENABLE = true;

    private View layout_flash, layout_flash_background;
    private boolean isBackKeyAvailable = false;
    private long backPressed;
    private AppRate appRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalSharedPreferManager.getInstance().init(getApplicationContext());
        setupRateAppDialog();
        addHomeFragment();
        displaySplashScreen();

    }

    private void addHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.container, fragment);
        trans.commit();

    }

    @Override
    public void onBackPressed() {
        if (isBackKeyAvailable) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null && fragmentManager.getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                if (!showRateAppDialog()) {
                    handleDoubleBackToExit();
                }

            }

        }
    }


    @Override
    protected void onDestroy() {
        LocalSharedPreferManager.getInstance().destroy();
        super.onDestroy();
    }

    private void displaySplashScreen() {
        layout_flash = findViewById(R.id.layout_flash);
        layout_flash_background = findViewById(R.id.layout_flash_background);
        layout_flash.setVisibility(View.VISIBLE);
        layout_flash_background.setVisibility(View.VISIBLE);
        layout_flash.setAlpha(0);
        layout_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ObjectAnimator set1 = ObjectAnimator.ofFloat(layout_flash, "alpha", 0.0f, 1f).setDuration(750);
        ObjectAnimator set2 = ObjectAnimator.ofFloat(layout_flash, "alpha", 1f, 1f).setDuration(700);
        set2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                layout_flash_background.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator set3 = ObjectAnimator.ofFloat(layout_flash, "alpha", 1f, 0f).setDuration(750);
        set3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                layout_flash.setVisibility(View.GONE);
                isBackKeyAvailable = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(set1, set2, set3);
        set.start();
    }


    private void setupRateAppDialog() {
        LocalSharedPreferManager.getInstance().setRateAppLaunchTime();
        appRate = AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(0) // default 10
                .setRemindInterval(0) // default 1
                .setShowLaterButton(false)
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        if (which == -1) {//rate
                            LocalSharedPreferManager.getInstance().setIsRateApp(true);
                        } else {//close
                            appRate.clearAgreeShowDialog();
                            LocalSharedPreferManager.getInstance().setRateAppRemindInterval();
//                            LocalSharedPreferManager.getInstance().resetRateAppLaunchTime();
                            finish();
                        }
                    }
                });
    }


    private boolean showRateAppDialog() {
        if (!LocalSharedPreferManager.getInstance().isRateApp() && LocalSharedPreferManager.getInstance().isRateAppOverLaunchTime(3) && LocalSharedPreferManager.getInstance().isRateAppOverDate(1)) {
            appRate.showRateDialogIfMeetsConditions(this);
            return true;
        }
        return false;
    }

    private void handleDoubleBackToExit() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            backPressed = System.currentTimeMillis();
            Toast.makeText(this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void exit() {

    }
}
