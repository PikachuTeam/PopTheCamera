package com.tatteam.popthecamera.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


/**
 * Created by ThanhNH on 9/29/2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private AdView mAdView;
    private Button btnClassicalMode;
    private Button btnUnlimitedMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(this.getActivity(), R.layout.fragment_home, null);
        mAdView = (AdView) view.findViewById(R.id.adView);

        btnClassicalMode = (Button) view.findViewById(R.id.btn_classical_mode);
        btnUnlimitedMode = (Button) view.findViewById(R.id.btn_unlimited_mode);
        btnClassicalMode.setOnClickListener(this);
        btnUnlimitedMode.setOnClickListener(this);

        setupAds();


        mInterstitialAd = new InterstitialAd(this.getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.big_banner_ad_unit_id));
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        return view;
    }

    private void addGameFragment() {
        GameFragment fragment = new GameFragment();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.container, fragment);
        trans.addToBackStack(GameFragment.class.getSimpleName());
        trans.commit();
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
            addGameFragment();
        } else if (view == btnUnlimitedMode) {
            mInterstitialAd.show();
//            requestNewInterstitial();
        }
    }
    InterstitialAd mInterstitialAd;
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }
}
