package com.example.bighome.mvp.main.homepage;

import com.example.bighome.data.BannerDetailData;
import com.example.bighome.data.source.BannerDataRepository;
import com.example.bighome.mvp.main.homepage.HomePageContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HomePagePresenter implements HomePageContract.Presenter
{
    private BannerDataRepository bannerRepository;
    private CompositeDisposable compositeDisposable;
    private HomePageContract.View view;

    public HomePagePresenter(HomePageContract.View view,
                             BannerDataRepository bannerRepository){
        this.bannerRepository = bannerRepository;
        this.view = view;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getBanner() {
        Disposable disposable = bannerRepository.getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<BannerDetailData>>() {
                    @Override
                    public void onNext(List<BannerDetailData> value) {
                        view.showBanner(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideBanner();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
