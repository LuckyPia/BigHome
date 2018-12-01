package com.example.bighome.data.source.remote;


import android.support.annotation.NonNull;

import com.example.bighome.data.BannerData;
import com.example.bighome.data.BannerDetailData;
import com.example.bighome.data.source.BannerDataSource;
import com.example.bighome.retrofit.Api;
import com.example.bighome.retrofit.RetrofitClient;
import com.example.bighome.retrofit.RetrofitService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class BannerDataRemoteSource implements BannerDataSource {

    @NonNull
    private static BannerDataRemoteSource INSTANCE;

    private BannerDataRemoteSource() {

    }

    public static BannerDataRemoteSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BannerDataRemoteSource();
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<BannerDetailData>> getBanner() {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getBanner(Api.BANNER)
                .filter(new Predicate<BannerData>() {
                    @Override
                    public boolean test(BannerData bannerData) {
                        return bannerData.getErrorCode() != -1;
                    }
                })
                .flatMap(new Function<BannerData, ObservableSource<List<BannerDetailData>>>() {
                    @Override
                    public ObservableSource<List<BannerDetailData>> apply(BannerData bannerData) {
                        return Observable.fromIterable(bannerData.getData()).toList().toObservable();
                    }
                });
    }
}
