package com.tatteam.popthecamera;

import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.uikit.*;
import org.robovm.objc.annotation.CustomClass;

import java.io.File;

/**
 * Created by admin on 10/7/15.
 */

@CustomClass("MainScreenViewController")
public class MainScreenViewController extends UIViewController {


    private GDXGameLauncher gameLauncher;
    private IOSLauncher app;
    public static final String KEY_GAME_MODE = "game_mode";
    private static final String[] LEVEL_NAMES = {"Slow", "Medium", "Fast", "Crazy"};

    private int levelIndex = 1;




    private final UIImageView logo;
    private final UIButton next;
    private final UIButton prev;
    private final UIButton start;
    private final UIButton crazy;
    private final UILabel mode;


    public MainScreenViewController () {
        super("ViewController", null);

        UIView view = getView();
        logo = (UIImageView) view.getViewWithTag(1);
        next = (UIButton) view.getViewWithTag(2);
        prev = (UIButton) view.getViewWithTag(3);
        start = (UIButton) view.getViewWithTag(4);
        crazy = (UIButton) view.getViewWithTag(5);
        mode = (UILabel) view.getViewWithTag(6);


//        [NSColor colorWithRed:0 green:0.51 blue:0.56 alpha:1]
        view.setBackgroundColor(UIColor.fromRGBA(0,0.51,0.56,1));

//        logo.setImage(img);




        next.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
            @Override
            public void onTouchUpInside (UIControl control, UIEvent event) {
                playsound("change_level");
                if (levelIndex >= LEVEL_NAMES.length - 1) {
                    levelIndex = 0;
                } else {
                    levelIndex++;
                }
                updateLevelText();

            }
        });


        prev.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
            @Override
            public void onTouchUpInside (UIControl control, UIEvent event) {
                playsound("change_level");
                if (levelIndex <= 0) {
                    levelIndex = LEVEL_NAMES.length - 1;
                } else {
                    levelIndex--;
                }
                updateLevelText();

            }
        });

        start.setTitle("CLASSIC",UIControlState.Normal);

        start.setTitleColor(UIColor.fromRGBA(0 , 0.31, 0.34, 1), UIControlState.Normal);
        start.setTitleShadowColor(UIColor.fromWhiteAlpha(0.5, 1), UIControlState.Normal);

        start.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
            @Override
            public void onTouchUpInside (UIControl control, UIEvent event) {
                if (gameLauncher != null){
                    gameLauncher.setGameMode(getClassicGameMode());
                    gameLauncher.reset();
                    dismissViewController(true,null);

                }


            }
        });


        crazy.setTitle("NO LIMIT",UIControlState.Normal);
        crazy.setTitleColor(UIColor.fromRGBA(0 , 0.31, 0.34, 1), UIControlState.Normal);
        crazy.setTitleShadowColor(UIColor.fromWhiteAlpha(0.5, 1), UIControlState.Normal);

        crazy.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
            @Override
            public void onTouchUpInside (UIControl control, UIEvent event) {
                if (gameLauncher != null){
                    gameLauncher.setGameMode(Constants.GameMode.UNLIMITED);
                    dismissViewController(true,null);
                }


            }
        });

        updateLevelText();
    }

    @Override
    public void viewDidAppear(boolean animated) {
        super.viewDidAppear(animated);
//        NSUserDefaults defaults = NSUserDefaults.getStandardUserDefaults();
//        int start = defaults.getInt("START_GAME");
//        if (start > 5){
//            app.showAds(true);
//        }
//        start += 1;
//        defaults.put("START_GAME",start);
//        defaults.synchronize();
    }

    private void updateLevelText() {
        mode.setText(LEVEL_NAMES[levelIndex].toUpperCase());
    }

    private Constants.GameMode getClassicGameMode() {
        switch (levelIndex) {
            case 0:
                return Constants.GameMode.CLASSIC_SLOW;
            case 1:
                return Constants.GameMode.CLASSIC_MEDIUM;
            case 2:
                return Constants.GameMode.CLASSIC_FAST;
            case 3:
                return Constants.GameMode.CLASSIC_CRAZY;
        }
        return Constants.GameMode.CLASSIC_MEDIUM;
    }

    private void playsound(String name){

    }

    public void setGameLauncher(GDXGameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
    }

    public void setApp(IOSLauncher app) {
        this.app = app;
    }
}
