package com.example.simpleweather.view;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.simpleweather.R;
import com.example.simpleweather.view_model.WeatherForecastViewModel;
import com.example.simpleweather.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends FragmentActivity {

    private ActivityMainBinding mBinding;

    private WeatherForecastViewModel mForecastViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        loadAds();
        bindView();
    }

    private void loadAds() {
        MobileAds.initialize(this, "ca-app-pub-5267136776838452/4515432762");
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("33BE2250B43518CCDA7DE426D04EE231")
                .build();
        adView.loadAd(adRequest);
    }

    private void bindView() {
        mForecastViewModel = ViewModelProviders.of(this).get(WeatherForecastViewModel.class);
        mBinding.setLifecycleOwner(this);

        setupRecycleView();
        setupSearchView();
    }

    private void setupRecycleView() {
        mBinding.setViewModel(mForecastViewModel);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragment_container, WeatherDetailFragment.newInstance());
        transaction.commit();
    }

    private void setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final String searchParam = query
                        .replaceAll("\\s+","")
                        .trim()
                        .toLowerCase();
                mForecastViewModel.requestForecastData(searchParam);
                mBinding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
