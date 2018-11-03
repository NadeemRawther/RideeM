package com.nads.rideem;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class restaur extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    public static final String LATS ="it";
    public static final String LOG = "nh";
    public static final Double LA = 2.0;
    private ArrayList<objforRest> arrayList = new ArrayList<>();
    public static final Double LO = 10.2;
    private SectionsPagerAdapter mSectionsPagerAdapter;
  private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaur);
        Intent intent = getIntent();
        final double lats = intent.getDoubleExtra(LATS, LA);
        final double logs = intent.getDoubleExtra(LOG, LO);
        final Bundle bundle = new Bundle();
        bundle.putDouble(RestaurantFragment.LATS,lats);
        bundle.putDouble(RestaurantFragment.LOGS,logs);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2991702481825090/5069052914");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                showInterstitial();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),bundle);
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
            }


        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final Bundle fragmentBundle;


        public SectionsPagerAdapter(FragmentManager fm, Bundle data){

            super(fm);
            fragmentBundle = data;

        }


        @Override
        public Fragment getItem(int position) {
            switch (position){
            case 0:
                RestaurantFragment restaurantFragment = new RestaurantFragment();
                restaurantFragment.setArguments(this.fragmentBundle);
                return restaurantFragment;
            case 1:
                LodgingFragment lodgingFragment = new LodgingFragment();
                    lodgingFragment.setArguments(this.fragmentBundle);
                    return lodgingFragment;
            default:
                  return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.name_fragment);
                case 1:
                    return getString(R.string.name_fragment2);
            }
            return null;
        }
    }
}
