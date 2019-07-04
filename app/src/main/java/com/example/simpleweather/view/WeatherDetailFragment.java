package com.example.simpleweather.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.simpleweather.databinding.FragmentWeatherDetailBinding;
import com.example.simpleweather.view_model.WeatherForecastViewModel;

public class WeatherDetailFragment extends Fragment {
    public WeatherDetailFragment() {
        // Required empty public constructor
    }

    public static WeatherDetailFragment newInstance() {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get viewmodel from main activity
        WeatherForecastViewModel viewModel = ViewModelProviders.of(getActivity()).get(WeatherForecastViewModel.class);
        // Get binding from layout
        FragmentWeatherDetailBinding binding = FragmentWeatherDetailBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(viewModel);

        // Bind data to layout when ever it change
//        viewModel.getSelected().observe(this, result -> Log.d("abc", result.getDtTxt()));

        return binding.getRoot();
    }
}
