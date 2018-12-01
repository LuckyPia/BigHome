package com.example.bighome.mvp.login;

import android.support.annotation.NonNull;

import com.example.bighome.data.LoginDetailData;
import com.example.bighome.data.LoginType;
import com.example.bighome.mvp.BasePresenter;
import com.example.bighome.mvp.BaseView;


public interface LoginContract {
    interface Presenter extends BasePresenter {
        void login(String username, String password, @NonNull LoginType loginType);

        void clearReadLaterData();

    }

    interface View extends BaseView<Presenter> {
        void showLoginError(String errorMsg);

        boolean isActive();

        void saveUserPreference(LoginDetailData loginDetailData);

        void showNetworkError();
    }
}
