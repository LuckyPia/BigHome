package com.example.bighome.mvp.main.homepage;

import com.example.bighome.data.BannerDetailData;
import com.example.bighome.mvp.BasePresenter;
import com.example.bighome.mvp.BaseView;

import java.util.List;

public interface HomePageContract {
    interface Presenter extends BasePresenter {
        void getBanner();

    }

    interface View extends BaseView<Presenter> {

        void showBanner(List<BannerDetailData> list);

        void hideBanner();
    }
}
