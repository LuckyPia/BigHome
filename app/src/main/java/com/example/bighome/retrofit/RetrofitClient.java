package com.example.bighome.retrofit;


import com.example.bighome.app.MyApp;
import com.example.bighome.retrofit.Cookies.CookieManger;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitClient {
    private RetrofitClient() {
    }

    private static class ClientHolder{

        private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieManger(MyApp.getContext()))
                .build();

        private static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.API_BASE)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static Retrofit getInstance(){
        return ClientHolder.retrofit;
    }
}
