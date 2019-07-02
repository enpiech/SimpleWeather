package com.example.simpleweather;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.simpleweather.adapter.BaseBindingListAdapter;
import com.example.simpleweather.data.model.MainInfo;
import com.example.simpleweather.view_model.WeatherForecastViewModel;
import com.example.simpleweather.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends FragmentActivity {

    private ActivityMainBinding mBinding;

    WeatherForecastViewModel mForecastViewModel;

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
        // Setup Adapter
        final BaseBindingListAdapter<MainInfo> adapter = new BaseBindingListAdapter<>(this, R.layout.weather_item, MainInfo.DIFF_CALLBACK);
        mBinding.rvWeatherList.setAdapter(adapter);

        // FIXME use data binding instead of this
        mForecastViewModel.getAllList().observe(this, mainInfos -> {
            Log.d("abc", "reload");
            adapter.submitList(mainInfos);
            adapter.setOnClickListener(id -> mForecastViewModel.select(id));
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragment_container, WeatherDetailFragment.newInstance());
        transaction.commit();
    }

    private void setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mForecastViewModel.getWeatherResponse(query);
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
