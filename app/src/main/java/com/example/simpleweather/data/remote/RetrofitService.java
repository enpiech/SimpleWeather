package com.example.simpleweather.data.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static Retrofit sRetrofit = null;

    private static Retrofit getClient(String baseUrl) {
        if (sRetrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();

            sRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .client(client)
                    .build();
        }
        return sRetrofit;
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        return getClient(baseUrl).create(serviceClass);
    }
}
