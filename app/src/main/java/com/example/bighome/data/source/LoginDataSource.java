package com.example.bighome.data.source;

import android.support.annotation.NonNull;

import com.example.bighome.data.LoginData;
import com.example.bighome.data.LoginDetailData;
import com.example.bighome.data.LoginType;

import io.reactivex.Observable;


public interface LoginDataSource {

    Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType);

    Observable<LoginDetailData> getLocalLoginData(@NonNull int userId);



}
