package com.tatteam.popthecamera.android;

import android.app.Application;


/**
 * Created by ThanhNH on 2/1/2015.
 */
public class ClientApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocalSharedPreferManager.getInstance().init(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
