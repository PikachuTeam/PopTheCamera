package com.tatteam.popthecamera;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSErrorException;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.bindings.admob.*;
import org.robovm.apple.uikit.*;

import java.util.Arrays;
import java.util.List;

public class IOSLauncher extends IOSApplication.Delegate  implements GDXGameLauncher.OnGameListener{
    private static final Logger log = new Logger("AAA", Application.LOG_DEBUG);

    private MainScreenViewController menuViewController;
    private UIViewController rootViewController;
    private IOSApplication app;
    private GDXGameLauncher gameLauncher;

    private GADBannerView adview;
    private GADInterstitial interstitialAd;
    private boolean adsInitialized = false;


    private int lossGameCounter;
    private final String small_banner_ad_unit_id = "ca-app-pub-4338754459250860/8940075638";
    private final String big_banner_ad_unit_id ="ca-app-pub-4338754459250860/8783468432";

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape=false;
        config.orientationPortrait=true;
        gameLauncher = new GDXGameLauncher();

        gameLauncher.setGameMode(Constants.GameMode.CLASSIC_MEDIUM);

        app =  new IOSApplication(gameLauncher, config);


        return app;
    }




    @Override
    public void didBecomeActive (UIApplication application) {
        if (null == menuViewController){
            gameLauncher.setOnGameListener(this);
            menuViewController = new MainScreenViewController();
            menuViewController.setGameLauncher(gameLauncher);
            menuViewController.setApp(this);
            rootViewController = app.getUIWindow().getRootViewController();
            rootViewController.presentViewController(menuViewController,false,null);


        }

        setupAds();
    }

    @Override
    public void onLossGame(GDXGameLauncher gameLauncher, Constants.GameMode gameMode, int currentLevel, int score) {
        displayAdsIfNeeded(gameMode, currentLevel, score);
    }

    @Override
    public void onGameBackPressed(){
        rootViewController.presentViewController(menuViewController,false,null);
    };

    private void displayAdsIfNeeded(Constants.GameMode gameMode, int currentLevel, int score) {

        lossGameCounter++;
        if (gameMode == Constants.GameMode.UNLIMITED) {
            if (lossGameCounter % 5 == 0) {
                displayAds();
            }
        } else {
            if (gameMode == Constants.GameMode.CLASSIC_SLOW) {
                if (currentLevel >= 5 && lossGameCounter % 4 == 0) {
                    displayAds();
                }
            } else if (gameMode == Constants.GameMode.CLASSIC_MEDIUM) {
                if (currentLevel >= 3 && lossGameCounter % 6 == 0) {
                    displayAds();
                }
            } else if (gameMode == Constants.GameMode.CLASSIC_FAST) {
                if (lossGameCounter % 6 == 0) {
                    displayAds();
                }
            } else if (gameMode == Constants.GameMode.CLASSIC_CRAZY) {
                if (lossGameCounter % 8 == 0) {
                    displayAds();
                }
            }
        }
    }

    private void displayAds() {



        showFullAd();
    }

    private void showFullAd(){
        log.debug("Show banner Ad");
        if(interstitialAd.isReady()) {
            interstitialAd.present(app.getUIViewController());
        }

    }
    private void setupAds() {

        interstitialAd = new GADInterstitial();
        interstitialAd.setAdUnitID(big_banner_ad_unit_id);
        requestNewInterstitial();
        interstitialAd.setDelegate(new GADInterstitialDelegateAdapter(){
            @Override
            public void didFailToReceiveAd(GADInterstitial ad, GADRequestError error) {
                log.debug(error.description());
                requestNewInterstitial();
            }
        });
    }

    private void requestNewInterstitial() {
        final GADRequest request = GADRequest.create();
        List<String> testDevices = Arrays.asList(GADRequest.GAD_SIMULATOR_ID);
        request.setTestDevices(testDevices);
        interstitialAd.loadRequest(request);
    }




    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }









    public void initializeAds() {
        if (!adsInitialized) {


            adsInitialized = true;

            adview = new GADBannerView(GADAdSize.smartBannerPortrait());
            if (adview != null){
                adview.setAdUnitID(small_banner_ad_unit_id); //put your secret key here
                adview.setRootViewController(rootViewController);
                rootViewController.getView().addSubview(adview);

                final GADRequest request = GADRequest.create();
                List<String> testDevices = Arrays.asList(GADRequest.GAD_SIMULATOR_ID);
                request.setTestDevices(testDevices);
                adview.loadRequest(request);
                adview.setDelegate(new GADBannerViewDelegateAdapter() {
                    @Override
                    public void didReceiveAd(GADBannerView view) {
                        super.didReceiveAd(view);

                    }

                    @Override
                    public void didFailToReceiveAd(GADBannerView view,
                                                   GADRequestError error) {
                        super.didFailToReceiveAd(view, error);
                        log.debug(error.description());
                        final GADRequest request = GADRequest.create();
                        List<String> testDevices = Arrays.asList(GADRequest.GAD_SIMULATOR_ID);
                        request.setTestDevices(testDevices);
                        adview.loadRequest(request);

                    }
                });



            }



        }
    }


    public void showAds(boolean show) {
        log.debug("Show banner Ad");
        initializeAds();

        final CGSize screenSize = UIScreen.getMainScreen().getBounds().getSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        final CGSize adSize = adview.getBounds().getSize();
        double adWidth = adSize.getWidth();
        double adHeight = adSize.getHeight();



        float bannerWidth = (float) screenWidth;
        float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

        if(show) {
            adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, 0, bannerWidth, bannerHeight));
        } else {
            adview.setFrame(new CGRect(0, -bannerHeight, bannerWidth, bannerHeight));
        }
    }
/*
@Override
    public void showInterAd() {
        final GADInterstitial interstitial = new GADInterstitial(AD_UNIT_ID);
        interstitial.setDelegate(new GADInterstitialDelegateAdapter(){
            @Override
            public void didReceiveAd(GADInterstitial ad) {
                super.didReceiveAd(ad);
                interstitial.present(((IOSApplication) Gdx.app).getUIViewController());
            }

            @Override
            public void didFailToReceiveAd(GADInterstitial ad,
                    GADRequestError error) {
                Gdx.app.log("xxx", error.getCode() + "");
                super.didFailToReceiveAd(ad, error);
            }
        });

        GADRequest request = GADRequest.create();
        List<String> devices = new ArrayList<String>();
        devices.add(GADRequest.GAD_SIMULATOR_ID);

        request.setTestDevices(devices);
        interstitial.loadRequest(request);
    }

    @Override
    public void showBanner() {
         final GADBannerView mBannerView = new GADBannerView(GADAdSize.smartBannerPortrait());
            mBannerView.setAdUnitID("ca-app-pub-1743332321423234/2574098708");

            final UIViewController controller = ((IOSApplication) Gdx.app).getUIViewController();

            GADRequest request = GADRequest.create();
            List<String> devices = new ArrayList<String>();
            devices.add(GADRequest.GAD_SIMULATOR_ID);
            mBannerView.setRootViewController(controller);
            controller.getView().addSubview(mBannerView);

            mBannerView.loadRequest(request);
    }
}
 */

}