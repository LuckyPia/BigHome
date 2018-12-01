package com.example.bighome.mvp.login;

import android.support.annotation.NonNull;
import android.util.Log;


import com.example.bighome.data.LoginData;
import com.example.bighome.data.LoginType;
import com.example.bighome.data.source.LoginDataRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter{

    @NonNull
    private LoginContract.View view;
    @NonNull
    private LoginDataRepository repository;

    private CompositeDisposable compositeDisposable;

    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LoginDataRepository loginDataRepository) {
        this.view = view;
        this.repository = loginDataRepository;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void login(String username, String password, @NonNull LoginType loginType) {
        getLoginData(username, password,loginType);
    }

    private void getLoginData(final String username, final String password, @NonNull final LoginType loginType){
        Disposable disposable=repository.getRemoteLoginData(username, password,loginType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginData>() {

                    @Override
                    public void onNext(LoginData value) {
                        if (!view.isActive()) {
                            return;
                        }
                        if (value.getErrorCode()==-1){
                            view.showLoginError(value.getErrorMsg());
                        }else {
                            //作者的api接口改了，现在返回的password是"*******"
                            if(!value.getData().getUsername().equals(username)||!value.getData().getPassword().equals(password)){
                                value.getData().setUsername(username);
                                value.getData().setPassword(password);
                            }
                            view.saveUserPreference(value.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.showNetworkError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void clearReadLaterData() {
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
