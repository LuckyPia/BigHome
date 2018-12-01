package com.example.bighome.data.source;


import com.example.bighome.data.BannerDetailData;

import java.util.List;

import io.reactivex.Observable;


public interface BannerDataSource {
    Observable<List<BannerDetailData>> getBanner();
}
